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
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.widgets.ParameterController;
import org.processmining.filterd.widgets.ParameterMultipleFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;

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
		// If the configuration panel has not been initialized yet.
		if (this.configPanel == null) {
			// Create it.
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
	 * Method for adding all the listeners to the configuration.
	 */
	public void parameterListeners() {
		// Loop over all the controllers.
		for(ParameterController parameter : configPanel.getControllers()) {
			// Want to add a listener to the parameter that holds the selected
			// attribute.
			if (parameter.getName().equals("attrType")) {
				// Get the parameter.
				ParameterOneFromSetController casted = (ParameterOneFromSetController) parameter;
				// Get the corresponding combo box.
				ComboBox<String> comboBox = casted.getComboBox();
				// Add a listener to this combo box based on the selected value.
				comboBox.valueProperty().addListener(new ChangeListener<String>() {
					@Override 
					public void changed(ObservableValue ov, String oldValue, String newValue) {
						final XLog Llog = log;
						List<Parameter> params = parameters;
						// If log is not null.
						if (Llog != null) {
							// Loop over all the controllers.
							for (ParameterController changingParameter : configPanel.getControllers()) {

								// Get the parameter that we want to change
								// based on the value of the combo box.
								if (changingParameter.getName().equals("firstattrValues")) {

									// Get the changing parameter.
									ParameterMultipleFromSetController castedChanging = 
											(ParameterMultipleFromSetController) changingParameter;
									// Create a set of attribute values.
									Set<String> attributeValues = new HashSet<>();

									// Loop over every trace in the Llog.
									for (XTrace trace : Llog) {

										// Loop over every trace in the event.
										for (XEvent event : trace) {

											// Get the attributes of the event.
											XAttributeMap eventAttrs = event.getAttributes();
											// If the key is present
											if (eventAttrs.containsKey(newValue))
												// Add all values to the list.
												attributeValues.add(eventAttrs.get(newValue).toString());
										}
									}
									// Set an attribute values list.
									List<String> attributeValuesList = new ArrayList<String>(attributeValues);
									// Change the parameter based on these
									// parameters.
									// change options.
									((ParameterMultipleFromSet) params.get(2))
									.setOptions(attributeValuesList);
									// Change chosen values.
									((ParameterMultipleFromSet) params.get(2))
									.setChosen(attributeValuesList);
									// Change default values.
									((ParameterMultipleFromSet) params.get(2))
									.setDefaultChoice(attributeValuesList);
									// Change options.
									castedChanging.changeOptions(attributeValuesList);

								}
								// Get another parameter that we want to change
								// based on the value of the combo box.
								if (changingParameter.getName().equals("endattrValues")) {

									// Get the parameter.
									ParameterMultipleFromSetController castedChanging = 
											(ParameterMultipleFromSetController) changingParameter;
									// Set to hold all attribute values.
									Set<String> attributeValues = new HashSet<>();

									// Loop over every trace in the Llog.
									for (XTrace trace : Llog) {

										// Loop over every event in the trace.
										for (XEvent event : trace) {

											// Get all the attributes of the
											// events.
											XAttributeMap eventAttrs = event.getAttributes();
											// If it contains the key.
											if (eventAttrs.containsKey(newValue))
												attributeValues.add(eventAttrs.get(newValue).toString());
										}
									}
									// Create list from the set.
									List<String> attributeValuesList = new ArrayList<String>(attributeValues);
									// Change the values of the parameter
									// accordingly.
									((ParameterMultipleFromSet) params.get(3))
									.setOptions(attributeValuesList);
									((ParameterMultipleFromSet) params.get(3))
									.setChosen(attributeValuesList);
									((ParameterMultipleFromSet) params.get(3))
									.setDefaultChoice(attributeValuesList);
									castedChanging.changeOptions(attributeValuesList);

								}
							}

						}
					}
				});

			}
		}
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
