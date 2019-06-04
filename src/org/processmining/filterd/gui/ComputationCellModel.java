package org.processmining.filterd.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
import javafx.concurrent.Task;
import javafx.util.Callback;

@XmlAccessorType(XmlAccessType.NONE) // Makes sure only explicitly named elements get added to the XML.
@XmlRootElement(name = "ComputationCellModel") // Needed by JAXB to generate an XML.
public class ComputationCellModel extends CellModel {

	private ProMCanceller canceller;
	private YLog inputLog;
	private List<YLog> inputLogs;
	private List<YLog> outputLogs;
	@XmlElement
	private ObservableList<FilterButtonModel> filters;
	
	/**
	 * Constructor for importing/exporting. This constructor needs to exist because JAXB needs a no-argument constructor for unmarshalling.
	 * Properties set here could be overwritten during loading.
	 */
	public ComputationCellModel() {
		filters = FXCollections.observableArrayList(new Callback<FilterButtonModel, Observable[]>() {
			@Override
			public Observable[] call(FilterButtonModel temp) {
				return new Observable[] { temp.nameProperty(), temp.selectedProperty() };
			}
		});
	}

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
	
	@Override
	public void setCellName(String cellName) {
		String oldState = this.cellName.getValue();
		this.cellName.setValue(cellName);
		property.firePropertyChange("setCellName", oldState, cellName);
		// change name of the output log (downstream cells may be using it) 
		if(this.outputLogs != null && this.outputLogs.size() > 0) {
			this.outputLogs.get(0).setName(cellName + " output log");			
		}
	}

	public ObservableList<FilterButtonModel> getFilters() {
		return filters;
	}

	public void addFilterModel(int index, FilterButtonModel model) {
		this.filters.add(index, model);
	}
	
	public void removeFilter(FilterButtonModel filter) {
		filters.remove(filter);
	}

	public void setInputLog(YLog log) {
		if(log == null) {
			throw new IllegalArgumentException("Log cannot be null!");
		}
		this.inputLog = log;
		// set the output to be the input (when the cell is computed, this will change)
		// this is needed so that downstream cells don't have null logs as their input
		if(log.get() != null) {
			this.outputLogs.get(0).setLog(log.get());
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
    
    public void compute(Task<Void> computeTask) {
    	XLog inputOutput = this.inputLog.get();
    	if(inputOutput == null) {
    		throw new IllegalStateException("Input log is null. "
    				+ "Cell has been requested to be computed, but its upstream cells have not been computed yet.");
    	}
    	for(FilterButtonModel filter : filters) {
    		if(computeTask.isCancelled()) {
    			break;
    		}
    		try {
    			filter.setInputLog(inputOutput);
    			filter.compute(); // no point in passing the task to the individual filter models (individual filters do not support canceling)
    			inputOutput = filter.getOutputLog();
    		} catch(Exception e) {
    			if(e instanceof EmptyLogException) {
    				
    			} else if(e instanceof InvalidConfigurationException) {
    				
    			} else {
    				
    			}
    		}
    	}
    	this.outputLogs.get(0).setLog(inputOutput);
    }
}
