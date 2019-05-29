package org.processmining.filterd.gui;

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

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

public class ComputationCellModel extends CellModel {

	private ProMCanceller canceller;
	private YLog inputLog;
	private List<YLog> inputLogs;
	private List<YLog> outputLogs;
	private ObservableList<FilterButtonModel> filters;

	public ComputationCellModel(UIPluginContext context, int index, ProMCanceller canceller, List<YLog> eventLogs) {
		super(context, index);
		this.canceller = canceller;
		this.inputLogs = eventLogs;
		this.outputLogs = new ArrayList<>();
		outputLogs.add(new YLog(Toolbox.getNextId(), getCellName() + " output log"));

		filters = FXCollections.observableArrayList(new Callback<FilterButtonModel, Observable[]>() {
			@Override
			public Observable[] call(FilterButtonModel temp) {
				return new Observable[] { temp.nameProperty(), temp.selectedProperty() };
			}
		});
	}

	public ObservableList<FilterButtonModel> getFilters() {
		return filters;
	}

	public void addFilterModel(int index, FilterButtonModel model) {
		this.filters.add(index, model);
		// set cell output to be the output of the last filter (the filter we just created)
		this.outputLogs.clear();
		YLog outputLog = model.getOutputLog();
//		outputLog.setName(getCellModel().getCellName() + " output log");
		this.outputLogs.add(outputLog);
	}
	
	public void removeFilter(FilterButtonModel filter) {
		filters.remove(filter);
	}

	public void setInputLog(YLog log) {
		if(log == null) {
			throw new IllegalArgumentException("Log cannot be null!");
		}
		this.inputLog = log;
		if(filters.size() > 0) { // if there are any filters, the first one should have the new log as input
			filters.get(0).setInputLog(log);
		}
	}
	
    public YLog getInputLog() {
    	return this.inputLog;
    }

	public void setInputLogs(List<YLog> eventLogs) {
		this.inputLogs = eventLogs;
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

	public void selectFilter(FilterButtonModel model) {
		for (FilterButtonModel filter : filters) {
			filter.setSelected(false);
		}
		model.setSelected(true);
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
		List<ViewType> logViewTypes = vm.getViewTypes(rm.getResourceForInstance(inputLog.get()));
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
				Visualizer.class, JComponent.class, context.getPluginContextType(), true, false, false, inputLog.get().getClass());
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
						System.out.println("[Visualizerd] Calling plug-in " + pluginName + " w/ canceller to visualize log");
						return context.tryToFindOrConstructFirstNamedObject(JComponent.class, pluginName, null, null,
								outputLogs.get(0).get(), new ProMCanceller() {
									public boolean isCancelled() {
										// TODO Auto-generated method stub
										return false;
									}});
					} else {
						// Let the user know which plug-in we're invoking. This name should match the name of the plug-in.
						System.out.println("[Visualizerd] Calling plug-in " + pluginName + " w/o canceller to visualize log");
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
    
    public void compute() {
    	System.out.println("Computation cell compute starting");
    	
    	XLog inputOutput = this.inputLog.get();
    	if(inputOutput == null) {
    		throw new IllegalStateException("Input log is null. "
    				+ "Cell has been requested to be computed, but its upstream cells have not been computed yet.");
    	}
    	for(FilterButtonModel filter : filters) {
    		try {
    			System.out.println("Starting with a filter");
    			filter.compute();
    			inputOutput = filter.getOutputLog().get();
    			System.out.println("Done with a filter");
    		} catch(InvalidConfigurationException e) {
    			FilterButtonModel model = e.getFilterButtonModel();
//    			FilterButtonController controller = filterControllers.get(model.getIndex());
    			// TODO: set controller as invalid
    			System.out.println("Invalid configuration");
    		} catch(EmptyLogException e) {
    			// TODO: handle this
    			System.out.println("Empty log");
    		} catch(Exception e) {
    			// if any other exception occurs, throw it
    			throw e;
    		}
    	}
    	System.out.println("Output log name is " + this.outputLogs.get(0).getName());
    	System.out.print("Output log size is ");
    	System.out.println(this.outputLogs.get(0).get().size());
    	System.out.println("Computation cell compute done.");
    	
    }
}
