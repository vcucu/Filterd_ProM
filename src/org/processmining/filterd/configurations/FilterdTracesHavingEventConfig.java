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
import org.processmining.filterd.tools.EmptyLogException;
import org.processmining.filterd.widgets.ParameterController;
import org.processmining.filterd.widgets.ParameterMultipleFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;

public class FilterdTracesHavingEventConfig extends FilterdAbstractConfig {
	
	// Set to hold attributes for the event.
	Set<String> eventKeys;
	
	public FilterdTracesHavingEventConfig(XLog log, Filter filterType) throws EmptyLogException {
		super(log, filterType);
		// Set the log.
		this.log = log;
		// Create new set for event attributes.
		eventKeys = new HashSet<>();
		
		// Loop over every trace in the log.
		for (XTrace trace : log) {

			// Loop over every event in the trace.
			for (XEvent event : trace) {

				// Add all the keys of the event attributes.
				eventKeys.addAll(event.getAttributes().keySet());

			}

		}
		// Create list out of set.
		List<String> attributesList = new ArrayList<String>(eventKeys);
		// Create parameter for the user to select which attribute he wants to
		// filter with.
		ParameterOneFromSet attrType = new ParameterOneFromSet("attrType",
				"Attribute type:",
				attributesList.get(0),
				attributesList
				);

		// Create set for all the values of an attribute.
		Set<String> attributeValues = new HashSet<>();

		// Loop over all traces in the log.
		for (XTrace trace : log) {

			// Loop over all events in the trace.
			for (XEvent event : trace) {

				// Get all the attributes of this event.
				XAttributeMap eventAttrs = event.getAttributes();
				// If it contains the key.
				if (eventAttrs.containsKey(attributesList.get(0))) 
					// Add all values to the set.
					attributeValues.add(eventAttrs.get(attributesList.get(0)).toString());
			}
		}
		
		// Create a list out of the set.
		List<String> attributeValuesList = new ArrayList<String>(attributeValues);
		// Create a parameter for the user to select which values of the 
		// selected attribute he wants to use.
		ParameterMultipleFromSet attrValues = new ParameterMultipleFromSet(
				"attrValues",
				"Desired values:",
				Arrays.asList(attributeValuesList.get(0)),
				attributeValuesList
				);

		// Create parameter for the selection type, either mandatory to have or
		// forbidden to have.
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType",
				"Selection type:",
				"Mandatory",
				Arrays.asList("Mandatory", "Forbidden")
				);

		// Add all parameters to the list.
		this.parameters = Arrays.asList(attrType, attrValues, selectionType);
	}

	@Override
	/**
	 * Getter for the configuration panel.
	 */
	public AbstractFilterConfigPanelController getConfigPanel() {
		// If the configuration panel has not been initialized yet.
		if (this.configPanel == null) {
			// Create it.
			this.configPanel = new FilterConfigPanelController(
					"Filter Traces Having Event Configuration", 
					parameters, 
					this);
			// Add the listeners to the configuration panel.
			parameterListeners();
		}
		
		return configPanel;
	}
	
	@Override
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
		// Loop over all traces in the log.
		for (XTrace trace : log) {

			// Loop over all events in the trace.
			for (XEvent event : trace) {

				// Add all keys of the event to the list.
				cEventAttributes.addAll(event.getAttributes().keySet());

			}		
		}
		// If the candidate log does not contain the same event attribute.
		if (!cEventAttributes.contains(((ParameterOneFromSet) parameters.get(0))
				.getChosen()))
			return false;
		return true;
	}

	@Override
	public boolean canPopulate(FilterConfigPanelController component) {
		// TODO Auto-generated method stub
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
								if (changingParameter.getName().equals("attrValues")) {

									// Get the parameter.
									ParameterMultipleFromSetController castedChanging = 
											(ParameterMultipleFromSetController) changingParameter;
									// Create set to hold the values of the attribute.
									Set<String> attributeValues = new HashSet<>();

									// Loop over all traces in the Llog.
									for (XTrace trace : Llog) {

										// Loop over all the events in the trace.
										for (XEvent event : trace) {

											// Add all event values to that set.
											XAttributeMap eventAttrs = event.getAttributes();
											if (eventAttrs.containsKey(newValue))
												attributeValues.add(eventAttrs.get(newValue).toString());
										}
									}
									// Create a list out of the set.
									List<String> attributeValuesList = new ArrayList<String>(attributeValues);
									// Change the parameter that we want to 
									// change based on the new values
									// calculated.
									((ParameterMultipleFromSet) params.get(1))
									.setOptions(attributeValuesList);
									((ParameterMultipleFromSet) params.get(1))
									.setChosen(attributeValuesList);
									((ParameterMultipleFromSet) params.get(1))
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

}
