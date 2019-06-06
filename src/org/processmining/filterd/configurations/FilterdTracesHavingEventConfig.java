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
	Set<String> eventAttributes;
	public FilterdTracesHavingEventConfig(XLog log, Filter filterType) throws EmptyLogException {
		super(log, filterType);
		this.log = log;
		eventAttributes = new HashSet<>();
		for (XTrace trace : log) {

			for (XEvent event : trace) {

				eventAttributes.addAll(event.getAttributes().keySet());

			}

		}
		List<String> attributesList = new ArrayList<String>(eventAttributes);
		ParameterOneFromSet attrType = new ParameterOneFromSet("attrType",
				"Attribute type:",
				attributesList.get(0),
				attributesList
				);

		Set<String> attributeValues = new HashSet<>();

		for (XTrace trace : log) {

			for (XEvent event : trace) {

				XAttributeMap eventAttrs = event.getAttributes();
				if (eventAttrs.containsKey(attributesList.get(0))) 
					attributeValues.add(eventAttrs.get(attributesList.get(0)).toString());
			}
		}
		List<String> attributeValuesList = new ArrayList<String>(attributeValues);
		ParameterMultipleFromSet attrValues = new ParameterMultipleFromSet(
				"attrValues",
				"Desired values:",
				Arrays.asList(attributeValuesList.get(0)),
				attributeValuesList
				);

		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType",
				"Selection type:",
				"Mandatory",
				Arrays.asList("Mandatory", "Forbidden")
				);

		this.parameters = Arrays.asList(attrType, attrValues, selectionType);
		this.configPanel = new FilterConfigPanelController(
				"Filter Traces Having Event Configuration", 
				parameters, 
				this);
		parameterListeners();
	}

	@Override
	public boolean checkValidity(XLog candidateLog) {
		if (parameters == null || candidateLog.equals(log))
			return true;
		Set<String> cEventAttributes = new HashSet<>();
		for (XTrace trace : log) {

			for (XEvent event : trace) {

				eventAttributes.addAll(event.getAttributes().keySet());

			}		
		}
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

	public void parameterListeners() {
		for(ParameterController parameter : configPanel.getControllers()) {
			if (parameter.getName().equals("attrType")) {
				ParameterOneFromSetController casted = (ParameterOneFromSetController) parameter;
				ComboBox<String> comboBox = casted.getComboBox();
				comboBox.valueProperty().addListener(new ChangeListener<String>() {
					@Override 
					public void changed(ObservableValue ov, String oldValue, String newValue) {
						final XLog Llog = log;
						List<Parameter> params = parameters;
						if (Llog != null) {
							for (ParameterController changingParameter : configPanel.getControllers()) {

								if (changingParameter.getName().equals("attrValues")) {

									ParameterMultipleFromSetController castedChanging = 
											(ParameterMultipleFromSetController) changingParameter;
									Set<String> attributeValues = new HashSet<>();

									for (XTrace trace : Llog) {

										for (XEvent event : trace) {

											XAttributeMap eventAttrs = event.getAttributes();
											if (eventAttrs.containsKey(newValue))
												attributeValues.add(eventAttrs.get(newValue).toString());
										}
									}
									List<String> attributeValuesList = new ArrayList<String>(attributeValues);
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
