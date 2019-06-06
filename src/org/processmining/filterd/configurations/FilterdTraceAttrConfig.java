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

	Set<String> traceAttributes;

	public FilterdTraceAttrConfig(XLog log, Filter filterType) {
		super(log, filterType);

		parameters = new ArrayList<Parameter>();

		traceAttributes = new HashSet<>();

		for (XTrace trace : log) {
			traceAttributes.addAll(trace.getAttributes().keySet());
		}

		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", "Filter by",
				traceAttributes.iterator().next(), new ArrayList<String>(traceAttributes));

		ParameterOneFromSet filterInOut = new ParameterOneFromSet("filterInOut", "Filter mode", "in",
				Arrays.asList("in", "out"));

		Set<String> attributeValues = new HashSet<>();

		List<String> traceAttributesList = new ArrayList<>(traceAttributes);
		
		for (XTrace trace : log) {
			XAttributeMap traceAttrs = trace.getAttributes();
			if (traceAttrs.containsKey(traceAttributesList.get(0)))
				System.out.println(traceAttributesList.size());
			attributeValues.add(traceAttrs.get(traceAttributesList.get(0)).toString());
		}
		
		List<String> attributeValuesList = new ArrayList<String>(attributeValues);
		ParameterMultipleFromSet attrValues = new ParameterMultipleFromSet("attrValues", "Desired values:",
				Arrays.asList(attributeValuesList.get(0)), attributeValuesList);
		parameters.add(attribute);
		parameters.add(filterInOut);
		parameters.add(attrValues);
		
		configPanel = new FilterConfigPanelController("Trace Attribute Configuration",
				parameters, this);
		parameterListeners();
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		return true;
	}

	public void parameterListeners() {
		for (ParameterController parameter : configPanel.getControllers()) {
			if (parameter.getName().equals("attribute")) {
				ParameterOneFromSetController casted = (ParameterOneFromSetController) parameter;
				ComboBox<String> comboBox = casted.getComboBox();
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

								if (changingParameter.getName().equals("attrValues")) {

									ParameterMultipleFromSetController castedChanging = (ParameterMultipleFromSetController) changingParameter;
									Set<String> attributeValues = new HashSet<>();

									for (XTrace trace : Llog) {

										XAttributeMap traceAttrs = trace.getAttributes();
										if (traceAttrs.containsKey("customer"))
											System.out.println(traceAttrs.get("customer").toString());
										if (traceAttrs.containsKey(newValue))
											attributeValues.add(traceAttrs.get(newValue).toString());

									}
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
		if (parameters == null || candidateLog.equals(log))
			return true;
		Set<String> cTraceAttributes = new HashSet<>();
		for (XTrace trace : log) {
			cTraceAttributes.addAll(trace.getAttributes().keySet());
		}
		if (!cTraceAttributes.contains(((ParameterOneFromSet) parameters.get(0)).getChosen()))
			return false;
		return true;
	}

}
