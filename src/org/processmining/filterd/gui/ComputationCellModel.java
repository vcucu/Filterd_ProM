package org.processmining.filterd.gui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.deckfour.uitopia.api.model.ViewType;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.contexts.uitopia.hub.ProMResourceManager;
import org.processmining.contexts.uitopia.hub.ProMViewManager;
import org.processmining.filterd.models.YLog;
import org.processmining.filterd.plugins.FilterdVisualizer;
import org.processmining.filterd.tools.EmptyLogException;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.framework.plugin.PluginParameterBinding;
import org.processmining.framework.plugin.ProMCanceller;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.impl.PluginManagerImpl;
import org.processmining.framework.util.Pair;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ComputationCellModel extends CellModel {

	private ProMCanceller canceller;
	private YLog inputLog;
	private List<YLog> inputLogs;
	private List<YLog> outputLogs;
	private ObservableList<FilterButtonModel> filters;
	private SimpleBooleanProperty isComputing;
	private Task<Void> computeTask;

	/**
	 * Constructor for importing/exporting. This constructor needs to exist
	 * because JAXB needs a no-argument constructor for unmarshalling.
	 * Properties set here could be overwritten during loading.
	 */
	public ComputationCellModel() {
		filters = FXCollections.observableArrayList(new Callback<FilterButtonModel, Observable[]>() {
			@Override
			public Observable[] call(FilterButtonModel temp) {
				return new Observable[] { temp.nameProperty(), temp.selectedProperty() };
			}
		});
		this.isComputing = new SimpleBooleanProperty(false);
	}

	public ComputationCellModel(UIPluginContext context, int index, ProMCanceller canceller, List<YLog> eventLogs) {
		super(context, index);
		this.canceller = canceller;
		this.inputLogs = eventLogs;
		this.outputLogs = new ArrayList<>();
		outputLogs.add(new YLog(Toolbox.getNextId(), getCellName() + " output log", index));

		filters = FXCollections.observableArrayList(new Callback<FilterButtonModel, Observable[]>() {
			@Override
			public Observable[] call(FilterButtonModel temp) {
				return new Observable[] { temp.nameProperty(), temp.selectedProperty() };
			}
		});
		this.isComputing = new SimpleBooleanProperty(false);
	}

	@Override
	public void setCellName(String cellName) {
		String oldState = this.cellName.getValue();
		this.cellName.setValue(cellName);
		property.firePropertyChange("setCellName", oldState, cellName);
		// change name of the output log (downstream cells may be using it) 
		if (this.outputLogs != null && this.outputLogs.size() > 0) {
			this.outputLogs.get(0).setName(cellName + " output log");
		}
	}

	public ObservableList<FilterButtonModel> getFilters() {
		return filters;
	}

	public void addFilterModel(int index, FilterButtonModel model) {
		this.filters.add(index, model);
	}

	/**
	 * <<<<<<< Updated upstream Adds all FilterButtonModels in a collection to
	 * this model. Ignores models if it is null or empty.
	 * 
	 * @param models
	 *            collection of FilterButtonModels. ======= Adds all
	 *            FilterButtonModels in a collection to this model.
	 * 
	 * @param models
	 *            collection of FilterButtonModels. >>>>>>> Stashed changes
	 */
	public void addFilterModels(List<FilterButtonModel> models) {
		if (models != null && !models.isEmpty()) {
			this.filters.addAll(models);
		}
	}

	public void removeFilter(FilterButtonModel filter) {
		filters.remove(filter);
	}

	public void setInputLog(YLog log) {
		if (log == null) {
			throw new IllegalArgumentException("Log cannot be null!");
		}
		this.inputLog = log;
		// set the output to be the input (when the cell is computed, this will change)
		// this is needed so that downstream cells don't have null logs as their input
		if (log.get() != null) {
			this.outputLogs.get(0).setLog(log.get());
		}
	}

	public YLog getInputLog() {
		return this.inputLog;
	}

	public void setInputLogs(List<YLog> eventLogs) {
		//System.out.println("Setting log for computation cell "+ this.getIndex());					
		if (eventLogs == null) {
			System.out.println("the input logs are null");
		}
		List<YLog> oldState = this.getInputLogs();
		this.inputLogs = eventLogs;
		//change the items in combobox that are displayed after list in model changes 
		property.firePropertyChange("setInputLogs", oldState, eventLogs);
	}

	public List<YLog> getInputLogs() {
		return inputLogs;
	}

	public void setOutputLogs(List<YLog> outputLogs) {
		this.outputLogs = outputLogs;
	}

	public List<YLog> getOutputLogs() {
		return outputLogs;
	}

	public boolean isComputing() {
		return this.isComputing.get();
	}

	public BooleanProperty isComputingProperty() {
		return this.isComputing;
	}

	public void selectFilter(FilterButtonModel model) {
		for (FilterButtonModel filter : filters) {
			filter.setSelected(false);
		}
		model.setSelected(true);
	}

	/**
	 * Sets the canceller of this Model. Used when loading a notebook from File.
	 * 
	 * @param canceller
	 */
	public void setCanceller(ProMCanceller canceller) {
		this.canceller = canceller;
	}

	// Get visualizer names
	// LET OP! Log must be set first.
	public List<ViewType> getVisualizers() {
		List<ViewType> visualizers = new ArrayList<ViewType>();
		visualizers.add(Utilities.dummyViewType);
		UIPluginContext context = getContext();
		// Get the necessary managers
		ProMViewManager vm = ProMViewManager.initialize(context.getGlobalContext()); // Get current view manager
		ProMResourceManager rm = ProMResourceManager.initialize(context.getGlobalContext()); // Get current resource manager
		// Get the possible visualizers for the input event log.
		List<ViewType> logViewTypes = vm.getViewTypes(rm.getResourceForInstance(this.inputLogs.get(0).get()));
		// Add all visualizer (except this one).
		for (ViewType type : logViewTypes) {
			if (!type.getTypeName().equals(FilterdVisualizer.NAME)) {
				//Add the name of this view type.
				visualizers.add(type);
			}
		}
		return visualizers;
	}

	public JComponent getVisualization(ViewType type) {
		UIPluginContext context = getContext();
		// Get all log visualizers.
		Set<Pair<Integer, PluginParameterBinding>> logVisualizers = PluginManagerImpl.getInstance().find(
				Visualizer.class, JComponent.class, context.getPluginContextType(), true, false, false,
				inputLog.get().getClass());
		logVisualizers.addAll(PluginManagerImpl.getInstance().find(Visualizer.class, JComponent.class,
				context.getPluginContextType(), true, false, false, inputLog.get().getClass(), canceller.getClass()));
		// Check all log visualizers for the visualizer name.
		for (Pair<Integer, PluginParameterBinding> logVisualizer : logVisualizers) {
			// Get the visualizer name.
			String visualizerName = logVisualizer.getSecond().getPlugin().getAnnotation(Visualizer.class).name();
			if (visualizerName.equals(UITopiaVariant.USEPLUGIN)) {
				// Visualizer name is the plug-in name.
				visualizerName = logVisualizer.getSecond().getPlugin().getAnnotation(Plugin.class).name();
			}
			// Remove @XYZ<Space> from the beginning.
			if (visualizerName.startsWith("@") && visualizerName.contains(" ")) {
				visualizerName = visualizerName.substring(visualizerName.indexOf(" ") + 1);
			}
			// Check whether name matches.
			if (visualizerName.equals(type.getTypeName())) {
				// Name matches, invoke this log visualizer and return the correct result.
				try {
					// Get plug-in name.
					String pluginName = logVisualizer.getSecond().getPlugin().getAnnotation(Plugin.class).name();
					// Invoke the plug-in, depending on whether or not it takes a canceller.
					List<Class<?>> parameterTypes = logVisualizer.getSecond().getPlugin()
							.getParameterTypes(logVisualizer.getSecond().getMethodIndex());
					if (parameterTypes.size() == 2 && parameterTypes.get(1) == ProMCanceller.class) {
						// Let the user know which plug-in we're invoking. This name should match the name of the plug-in.
						System.out.println(
								"[Visualizerd] Calling plug-in " + pluginName + " w/ canceller to visualize log");
						return context.tryToFindOrConstructFirstNamedObject(JComponent.class, pluginName, null, null,
								outputLogs.get(0).get(), new ProMCanceller() {
									public boolean isCancelled() {
										// TODO Auto-generated method stub
										return false;
									}
								});
					} else {
						// Let the user know which plug-in we're invoking. This name should match the name of the plug-in.
						System.out.println(
								"[Visualizerd] Calling plug-in " + pluginName + " w/o canceller to visualize log");
						return context.tryToFindOrConstructFirstNamedObject(JComponent.class, pluginName, null, null,
								outputLogs.get(0).get());
					}
				} catch (Exception ex) {
					// Message if visualizer fails for whatever reason.
					return new JLabel("Visualizer " + type.getTypeName() + " failed: " + ex.getMessage());
				}
			}
		}
		// If the visualizer could not be found, show some text.
		return new JLabel("Visualizer " + type.getTypeName() + " could not be found.");
	}

	public static void handleError(Exception e) {
		// This method is invoked on the JavaFX thread
		Alert alert = new Alert(AlertType.ERROR);
		if (e instanceof EmptyLogException) {
			//create pop up to warn user in case of empty xlog
			alert.setTitle("Filter preset error");
			alert.setHeaderText("Xlog is empty");
			alert.setContentText("The input Xlog or intermediate Xlog is empty");
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);// make sure window always at front when open
			alert.showAndWait();
			//user chose No or closed the dialog don't export
		} else if (e instanceof InvalidConfigurationException) {
			//create pop up to warn user in case of impossible configuration
			alert.setTitle("Invalid configuration error");
			alert.setHeaderText("Invalid configuration");
			alert.setContentText("The configuration that you have selected for the highlighted filter is"
					+ " not valid with respect to its upstream filters. Please change the configuration "
					+ "accordingly before trying to recompute.");
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);// make sure window always at front when open
			alert.showAndWait();
		} else {
			alert.setTitle("Error");
			alert.setHeaderText("Runtime exception");
			alert.setContentText("The highlighted filter has caused a runtime exception to be thrown.");

			// Create expandable Exception.
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String exceptionText = sw.toString();

			Label label = new Label("The exception stacktrace was:");

			TextArea textArea = new TextArea(exceptionText);
			textArea.setEditable(false);
			textArea.setWrapText(true);

			textArea.setMaxWidth(Double.MAX_VALUE);
			textArea.setMaxHeight(Double.MAX_VALUE);
			//fill in all available space
			GridPane.setVgrow(textArea, Priority.ALWAYS);
			GridPane.setHgrow(textArea, Priority.ALWAYS);

			GridPane expContent = new GridPane();
			expContent.setMaxWidth(Double.MAX_VALUE);
			expContent.add(label, 0, 0);
			expContent.add(textArea, 0, 1);

			// Set expandable Exception into the dialog pane.
			alert.getDialogPane().setExpandableContent(expContent);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);// make sure window always at front when open
			alert.showAndWait();
		}

    }

	public void compute() {
		// computation is executed in a task which is passed to a thread
		this.computeTask = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				computeWithTask(this);
				return null;
			}
		};
		// javafx tasks do not report exceptions to the console (this is a feature)
		// add a change listener to print the exception (needed for debugging)
		this.computeTask.exceptionProperty().addListener(new ChangeListener<Throwable>() {

			public void changed(ObservableValue<? extends Throwable> observable, Throwable oldValue,
					Throwable newValue) {
				if (newValue != null) {
					Exception exception = (Exception) newValue;
					exception.printStackTrace();
				}
			}
		});
		// create and start a thread
		Thread thread = new Thread(this.computeTask);
		thread.start();
	}

	/**
	 * Compute the cell by computing each of its individual filters in the
	 * appropriate order
	 * 
	 * @param computeTask
	 *            javafx task which is used to check whether the user cancelled
	 *            the compute task
	 */
	public void computeWithTask(Task<Void> computeTask) {
		this.computeTask = computeTask;
		this.isComputing.set(true);
		XLog inputOutput = this.inputLog.get(); // variable that stores the output of the previous filter (i.e. input for the next one)
		if (inputOutput == null) {
			// this state can only be reached if the cells are not computed in order (i.e. this cell uses the output of another cell which is yet to be computed)
			throw new IllegalStateException("Input log is null. "
					+ "Cell has been requested to be computed, but its upstream cells have not been computed yet.");
		}
		// go through all filters in order
		for (FilterButtonModel filter : filters) {
			// check if the computation was cancelled by the user (if so, break out of the loop)
			if (computeTask.isCancelled()) {
				break;
			}
			// compute method may throw two expected exceptions:
			// - EmptyLogException  the of the previously computed filter is empty (i.e. it has 0 traces)
			// - InvalidConfigurationExcpetion  the selected configuration is not valid w.r.t. the output of the previous filter  
			try {
				filter.setInputLog(inputOutput);
				filter.compute(); // no point in passing the task to the individual filter models (individual filters do not support canceling)
				inputOutput = filter.getOutputLog(); // if this line is reached, the filter did not throw an error -> fetch the filter output
			} catch (Exception e) {
				computeTask.cancel(); // stop any further computation in the notebook
				filter.isValidProperty().set(false); // make this filter button invalid (filter button controllers will handle this property change)
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						handleError(e);
					}
				});
			}
		}
		this.isComputing.set(false);
		this.outputLogs.get(0).setLog(inputOutput); // set the output of this cell to be the output of the last filter
	}

	public void cancelCompute() {
		if (this.computeTask != null) {
			this.computeTask.cancel();
		}
		this.isComputing.set(false);
	}
}
