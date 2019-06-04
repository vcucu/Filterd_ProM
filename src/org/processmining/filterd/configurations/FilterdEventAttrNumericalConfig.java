package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.Collections;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.filterd.tools.Toolbox;

public class FilterdEventAttrNumericalConfig extends FilterdAbstractReferenceableConfig {
	String key;

	public FilterdEventAttrNumericalConfig(XLog log, Filter filterType, String key) {
		super(log, filterType);
		this.key = key; 
		parameters = new ArrayList<Parameter>();
		ArrayList<Double> defaultPair = new ArrayList<>();
		ArrayList<Double> optionsPair = new ArrayList<>();

		String defaultSelect = "Choose different values.";
		ArrayList<String> selectList = new ArrayList<>();
		selectList.add(defaultSelect);
		selectList.add("Choose from interval.");// filter in or filter out
		ParameterOneFromSet parameterType = new ParameterOneFromSet("parameterType",
				"", defaultSelect, selectList);

		String defaultOption = "Filter in";
		ArrayList<String> optionList = new ArrayList<>();
		optionList.add(defaultOption);
		optionList.add("Filter out");// filter in or filter out
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType",
				"Select option for filtering.", defaultOption, optionList);

		// should you remove empty traces
		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", 
				"Keep empty traces.", true);

		// should you keep events which do not have the specified attribute
		ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", 
				"Keep events if attribute not specified.", false);

		ArrayList<String> values = new ArrayList<>();
		/*populate the array times with the numerical values of all events */
		for (XTrace trace: log) {
			for (XEvent event : trace) {
				if (!event.getAttributes().containsKey(key)) continue;
				String value = event.getAttributes().get(key).toString();
				if (!values.contains(value)) values.add(value);
			}
		}
		
		Collections.sort(values);
		ParameterMultipleFromSet desiredValues = new ParameterMultipleFromSet(
				"desiredValues", "Choose values:", values, values);
		
		/* populate the parameters */
		defaultPair.add(Double.parseDouble(values.get(0)));
		defaultPair.add(Double.parseDouble(values.get(values.size() - 1)));
		optionsPair.add(Double.parseDouble(values.get(0)));
		optionsPair.add(Double.parseDouble(values.get(values.size() - 1)));
		// slider values parameter
		ParameterRangeFromRange<Double> range = new ParameterRangeFromRange<>("range",
				"Select interval to choose from.", defaultPair, optionsPair, Double.TYPE);
		
		/* add the parameters */
		parameters.add(selectionType);
		parameters.add(parameterType);
		parameters.add(desiredValues);
		parameters.add(range);
		parameters.add(traceHandling);
		parameters.add(eventHandling);
		
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};


	public boolean checkValidity(XLog log) {
		return Toolbox.computeAttributes(log).contains(key);
	}
}
