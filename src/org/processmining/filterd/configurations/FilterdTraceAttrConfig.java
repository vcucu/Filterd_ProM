package org.processmining.filterd.configurations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.model.XAttributeMap;
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

public class FilterdTraceAttrConfig extends FilterdAbstractConfig {

	// Set to hold all the trace attributes for the configuration panel.
	Set<String> traceKeys;

	public FilterdTraceAttrConfig(XLog log, Filter filterType) {
		super(log, filterType);

		// For configuration panel.
		parameters = new ArrayList<Parameter>();

		traceKeys = new HashSet<>();

		// Add all attributes of every trace.
		for (XTrace trace : log) {
			traceKeys.addAll(trace.getAttributes().keySet());
		}

		// Create the parameter to select which attribute to filter by.
		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", "Filter by",
				traceKeys.iterator().next(), new ArrayList<String>(traceKeys));

		// Create the parameter to select filtering in or out.
		ParameterOneFromSet filterInOut = new ParameterOneFromSet("filterInOut", "Filter mode", "in",
				Arrays.asList("in", "out"));

		Set<String> keyValues = new HashSet<>();

		// List of values for the currently selected attribute.
		List<String> traceKeysList = new ArrayList<>(traceKeys);
		
		// Add all value for the currently selected key to the attribute value
		// parameters.
		for (XTrace trace : log) {
			XAttributeMap traceAttrs = trace.getAttributes();
			keyValues.add(traceAttrs.get(traceKeysList.get(0)).toString());
		}
		
		// Create list of the set of all values the selected key can take on.
		List<String> keyValuesList = new ArrayList<String>(keyValues);
		
		// Create the parameter to display all the values of the currently
		// selected key.
		ParameterMultipleFromSet attrValues = new ParameterMultipleFromSet("attrValues", "Desired values:",
				Arrays.asList(keyValuesList.get(0)), keyValuesList);
		
		// Add parameters to the configuration.
		parameters.add(attribute);
		parameters.add(filterInOut);
		parameters.add(attrValues);
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		// Can always populate.
		return true;
	}
	
	@Override
	public AbstractFilterConfigPanelController getConfigPanel() {
		// If the configuration panel was not initialized yet.
		if (this.configPanel == null) {
			// Create it.
			this.configPanel = new FilterConfigPanelController("Trace Attribute Configuration",
					parameters, this);
			// Add the listeners.
			parameterListeners();
		}
		
		return configPanel;
	}

	public void parameterListeners() {
		for (ParameterController parameter : configPanel.getControllers()) {
			// The values parameter needs to change based on the chosen key in
			// the attribute parameter.
			if (parameter.getName().equals("attribute")) {
				// Get the attribute parameter and the corresponding combo box.
				ParameterOneFromSetController casted = (ParameterOneFromSetController) parameter;
				ComboBox<String> comboBox = casted.getComboBox();
				// Add a change listener.
				comboBox.valueProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue ov, String oldValue, String newValue) {
						final XLog Llog = log;
						if (Llog == null)
							System.out.println("log is null");
						List<Parameter> params = parameters;
						if (Llog != null) {
							System.out.println("log not null");
							for (ParameterController changingParameter : configPanel.getControllers()) {
								// Get the parameter that displays the
								// attributes.
								if (changingParameter.getName().equals("attrValues")) {
									
									// Cast it to the correct value.
									ParameterMultipleFromSetController castedChanging = (ParameterMultipleFromSetController) changingParameter;
									
									// Get the new values for the chosen key.
									Set<String> attributeValues = new HashSet<>();

									// Loop over all the values and add all.
									for (XTrace trace : Llog) {

										XAttributeMap traceAttrs = trace.getAttributes();
										if (traceAttrs.containsKey(newValue)) {
											attributeValues.add(traceAttrs.get(newValue).toString());
										}

									}
									
									// Change the parameter according to the
									// newly calculated values.
									List<String> attributeValuesList = new ArrayList<String>(attributeValues);
									((ParameterMultipleFromSet) params.get(2)).setOptions(attributeValuesList);
									((ParameterMultipleFromSet) params.get(2)).setChosen(attributeValuesList);
									((ParameterMultipleFromSet) params.get(2)).setDefaultChoice(attributeValuesList);
									castedChanging.changeOptions(attributeValuesList);
								}
							}
						}
					}
				});
			}
		}
	}

	public boolean checkValidity(XLog candidateLog) {
		// If the log is the same or no parameters have been set.
		if (parameters == null || candidateLog.equals(log)) {
			return true;
		}
		// Else check if the candidate log contains the key that is selected as
		// well as the value selected.
		Set<String> cTraceAttributes = new HashSet<>();
		for (XTrace trace : candidateLog) {
			cTraceAttributes.addAll(trace.getAttributes().keySet());
		}
		if (!cTraceAttributes.contains(((ParameterOneFromSet) parameters.get(0)).getChosen()))
			return false;
		return true;
	}

}
