package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterYesNo;

public class FilterdEventAttrCategoricalConfig extends FilterdAbstractReferenceableConfig{

	List<String> optionList;
	List<String> values;
	List<String> defaultValues;
	
	
	public FilterdEventAttrCategoricalConfig(XLog log, Filter filterType, XAttribute attrib) {
		super(log, filterType);
		parameters = new ArrayList<>();
		optionList = new ArrayList<>();
		
		optionList.add("Filter in");
		optionList.add("Filter out");
		
		
		// should you remove empty traces
		ParameterYesNo nullHandling = new ParameterYesNo("nullHandling", 
				"Keep empty traces", true);
		// should you keep events which do not have the specified attribute
		ParameterYesNo emptyHandling = new ParameterYesNo("emptyHandling", 
				"Keep events without value", false);
		// filter in or filter out
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType",
				"Select option for filtering", optionList.get(0), optionList);
		
		values = new ArrayList<>();
		for (XTrace trace: log) {
			for (XEvent event : trace) {
				String value = event.getAttributes().get(attrib.getKey()).toString();
				if(!values.contains(value)) {
					values.add(value);						
				}
			}
		}
		defaultValues = new ArrayList<>();
		defaultValues.add(values.get(0));
		
		ParameterMultipleFromSet desiredValues = new ParameterMultipleFromSet(
				"desiredValues", "Choose values:", defaultValues, values);
		
		parameters.add(nullHandling);
		parameters.add(emptyHandling);
		parameters.add(selectionType);
		parameters.add(desiredValues);
	}

	public boolean checkValidity(XLog log) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};

	public XLog filter() {
		// TODO Auto-generated method stub
		return null;
	}

}
