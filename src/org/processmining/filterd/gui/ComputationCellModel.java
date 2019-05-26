package org.processmining.filterd.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.deckfour.uitopia.api.model.Author;
import org.deckfour.uitopia.api.model.Resource;
import org.deckfour.uitopia.api.model.ResourceType;
import org.deckfour.uitopia.api.model.View;
import org.deckfour.uitopia.api.model.ViewType;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.contexts.uitopia.hub.ProMResourceManager;
import org.processmining.contexts.uitopia.hub.ProMViewManager;
import org.processmining.filterd.models.YLog;
import org.processmining.filterd.plugins.FilterdVisualizer;
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
	private XLog log;
	private List<YLog> inputLogs;
	private List<YLog> outputLogs;
	private ObservableList<FilterButtonModel> filters;
	private ArrayList<FilterButtonController> filterControllers;


	public ComputationCellModel(UIPluginContext context, ProMCanceller canceller, List<YLog> eventLogs) {
			super(context);
			this.canceller = canceller;
			this.inputLogs = eventLogs;
			this.outputLogs = new ArrayList<>();
			
			filterControllers = new ArrayList<>();

			filters = FXCollections.observableArrayList(//);
					new Callback<FilterButtonModel, Observable[]>() {
						@Override
						public Observable[] call(FilterButtonModel temp) {
							return new Observable[] {
									temp.nameProperty(),
									temp.selectedProperty()
							};
						}
					});
	}

	public ObservableList<FilterButtonModel> getFilters() {
		return filters;
	}

	public void addFilterModel(int index, FilterButtonModel model) {
		this.filters.add(index, model);
	}
	
	public void removeFilterModel(FilterButtonModel filter) {
		filters.remove(filter);
	}
	
	public ArrayList<FilterButtonController> getFilterControllers() {
		return filterControllers;
	}

	public void addFilterController(int index, FilterButtonController controller) {
		this.filterControllers.add(index, controller);
	}
	
	public void removeFilterController(FilterButtonController controller) {
		this.filterControllers.remove(controller);
	}

	public void setXLog(XLog log) {
		if(log == null) {
			throw new IllegalArgumentException("Log cannot be null!");
		}
		this.log = log;
	}
	
    public XLog getInputLog() {
    	return log;
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
		List<ViewType> logViewTypes = vm.getViewTypes(rm.getResourceForInstance(log));
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
				Visualizer.class, JComponent.class, context.getPluginContextType(), true, false, false, log.getClass());
		logVisualizers.addAll(PluginManagerImpl.getInstance().find(Visualizer.class, JComponent.class,
				context.getPluginContextType(), true, false, false, log.getClass(), canceller.getClass()));
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
								log, new ProMCanceller() {
									public boolean isCancelled() {
										// TODO Auto-generated method stub
										return false;
									}});
					} else {
						// Let the user know which plug-in we're invoking. This name should match the name of the plug-in.
						System.out.println("[Visualizerd] Calling plug-in " + pluginName + " w/o canceller to visualize log");
						return context.tryToFindOrConstructFirstNamedObject(JComponent.class, pluginName, null, null,
								log);
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

}
