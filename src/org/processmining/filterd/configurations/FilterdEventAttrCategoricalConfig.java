package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.filterd.tools.Toolbox;

public class FilterdEventAttrCategoricalConfig extends FilterdAbstractReferenceableConfig{

	// List to hold the options.
	List<String> optionList;
	// List to hold the values
	List<String> values;
	// List to hold the default values.
	List<String> defaultValues;
	// parameter to select values.
	ParameterMultipleFromSet desiredValues;
	
	// The key.
	String key;
	
	
	public FilterdEventAttrCategoricalConfig(XLog log, Filter filterType, String key) {
		super(log, filterType);
		// Set the key.
		this.key = key;
		// Initialize
		parameters = new ArrayList<>();
		optionList = new ArrayList<>();
		
		// filter in or filter out options.
		optionList.add("Filter in");
		optionList.add("Filter out");
		
		
		// should you remove empty traces
		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", 
				"Keep empty traces", true);
		// should you keep events which do not have the specified attribute
		ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", 
				"Keep events without value", false);
		// filter in or filter out
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType",
				"Select option for filtering", optionList.get(0), optionList);
		
		// Initialize values member.
		values = new ArrayList<>();
		// Loop over all traces in the log.
		for (XTrace trace: log) {
			// Loop over all events in the trace.
			for (XEvent event : trace) {
				if (!event.getAttributes().containsKey(key)) continue;
				String value = event.getAttributes().get(key).toString();
				// Add all values to the list.
				if(!values.contains(value)) {
					values.add(value);						
				}
			}
		}
		// Create default values from values list.
		// list of 1 value, the first of the values list.
		defaultValues = new ArrayList<>();
		defaultValues.add(values.get(0));
		
		// Create parameter with all the values calculated.
		desiredValues = new ParameterMultipleFromSet(
				"desiredValues", "Choose values:", defaultValues, values);

		// Add all parameters created to the parameters list.
		parameters.add(selectionType);
		parameters.add(desiredValues);
		parameters.add(traceHandling);
		parameters.add(eventHandling);
	}

	/**
	 * Check if the parameters are still valid on the candidate log.
	 * 
	 * @param candidateLog the log to check.
	 */
	public boolean checkValidity(XLog log) {
        		
		if(key == null) return true;
		if(!Toolbox.computeAttributes(log).contains(key)) return false;
		
		// Loop over all traces in the log.
		for (XTrace trace : log) {
			// Loop over all events in the trace.
			for (XEvent event : trace) {
				if(!event.getAttributes().containsKey(key)) continue;
				// If the new log has the same key and value that is selcted.
				if(desiredValues.getChosen().contains(event.getAttributes().get(key).toString())) return true;
			}
		}
		
		return false;	
	}
	
	/**
	 * Checks if the configuration can populate the parameters.
	 * 
	 * @param component The component that populates the parameters.
	 */
	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component.
		return true;
	}
	
	/**
	 * Getter for the key.
	 */
	public String getKey() {
		return key;
	}
}
