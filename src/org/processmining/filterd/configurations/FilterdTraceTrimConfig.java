package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;

public class FilterdTraceTrimConfig extends FilterdAbstractConfig {
	// Set to hold all the event attributes.
	Set<String> eventKeys;

	public FilterdTraceTrimConfig(XLog log, Filter filterType) {
		super(log, filterType);
		// Initialize the configuration's parameters list.
		parameters = new ArrayList<>();

		// Do this by looping over every trace and collecting its attributes
		// and adding this to the set, except for time:timestamp
		eventKeys = new HashSet<>();
		// Loop over every trace in the log.
		for (XTrace trace : log) {

			// Loop over every event in the trace.
			for (XEvent event : trace) {

				// Get the keys of the attributes of the event.
				for (String key : event.getAttributes().keySet()) {
					// Add all of them except timestamp.
					if (!key.equals("time:timestamp")) {
						eventKeys.add(key);
					}
				}

			}

		}
		// Convert the set into an array list because ParameterOneFromSet takes
		// a list as an argument.
		List<String> eventAttributesList = 
				new ArrayList<String>(eventKeys);

		// Create the parameter for selecting the attribute.
		ParameterOneFromSet attributeSelector = 
				new ParameterOneFromSet(
						"attrType", 
						"Select attribute", 
						eventAttributesList.get(0), 
						eventAttributesList);

		// Create list for selecting trimming options.
		List<String> selectionTypeList = new ArrayList<String>();
		// Trimming longest.
		selectionTypeList.add("Trim longest");
		// Trimming first.
		selectionTypeList.add("Trim first");

		// Create the parameter for selecting the type.
		ParameterOneFromSet selectionType = new ParameterOneFromSet(
				"followType", 
				"Select trim type", 
				selectionTypeList.get(0), 
				selectionTypeList);

		// Create set to hold attribute vales.
		Set<String> attributeValues = new HashSet<>();

		// Loop over every trace in the log.
		for (XTrace trace : log) {

			// Loop over every event in the trace.
			for (XEvent event : trace) {

				// Get event attributes.
				XAttributeMap eventAttrs = event.getAttributes();
				if (eventAttrs.containsKey(eventAttributesList.get(0))) {
					// Add the value to the list.
					attributeValues.add(eventAttrs.get(eventAttributesList.get(0)).toString());
				}

			}

		}

		// To populate both the reference and follower event values with these
		// attribute values to start with. If the attribute is changed, so will
		// the values in both these parameters.
		List<String> attributeValuesList = new ArrayList<String>(attributeValues);

		// Create parameter for reference event values.
		ParameterMultipleFromSet firstEvents = 
				new ParameterMultipleFromSet(
						"firstattrValues",
						"Select start event values:",
						Arrays.asList(attributeValuesList.get(0)),
						attributeValuesList
						);

		// Create parameter for follower event values.
		ParameterMultipleFromSet endEvents = 
				new ParameterMultipleFromSet(
						"endattrValues",
						"Select end event values:",
						Arrays.asList(attributeValuesList.get(0)),
						attributeValuesList
						);

		// Add all parameters to the parameters list.
		parameters = Arrays.asList(attributeSelector, selectionType,
				firstEvents, endEvents); 
	}

	@Override
	/**
	 * Getter for the configuration panel.
	 */
	public AbstractFilterConfigPanelController getConfigPanel() {
		if(this.configPanel == null) {
			// Create a new configuration panel.
			this.configPanel = new FilterConfigPanelController("Trace Trim Configuration", 
					parameters, this);
			// And add the correct listeners.
			parameterListeners();
		}
		return configPanel;
	}


	/**
	 * Checks if the configuration can populate the parameters.
	 * 
	 * @param component The component that populates the parameters.
	 */
	public boolean canPopulate(FilterConfigPanelController component) {
		return true;
	}

	/**
	 * Check if the parameters are still valid on the candidate log.
	 * 
	 * @param candidateLog the log to check.
	 */
	public boolean checkValidity(XLog candidateLog) {
		// If the parameters are null or the log is equal to the log that was
		// already selected.
		if (parameters == null || candidateLog.equals(log))
			return true;
		// Check if the new log contains the same event attribute as the one
		// selected in the parameters.
		Set<String> cEventAttributes = new HashSet<>();
		// Loop over every trace in the candidate log.
		for (XTrace trace : candidateLog) {

			// Loop over every event in the trace.
			for (XEvent event : trace) {

				cEventAttributes.addAll(event.getAttributes().keySet());

			}		
		}
		// If the candidate log does not contain the same event attribute.
		if (!cEventAttributes.contains(((ParameterOneFromSet) parameters.get(0))
				.getChosen()))
			return false;
		return true;	
	}

}
