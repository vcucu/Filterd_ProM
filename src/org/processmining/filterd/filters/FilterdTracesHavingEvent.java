package org.processmining.filterd.filters;

import java.util.List;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;

public class FilterdTracesHavingEvent extends Filter {

	@Override
	public XLog filter(XLog log, List<Parameter> parameters) {
		XLog clonedLog = (XLog) log.clone();
		
		ParameterOneFromSet attrTypeParam = (ParameterOneFromSet) parameters.get(0);
		String attrType = attrTypeParam.getChosen();
		
		ParameterMultipleFromSet attrValuesParam = (ParameterMultipleFromSet) parameters.get(1);
		List<String> attrValues = attrValuesParam.getChosen();
		
		for (XTrace trace : clonedLog) {
			boolean ok = true;
			for (XEvent event : trace) {
				XAttributeMap eventAttributes = event.getAttributes();
				
	}
		}
		return clonedLog;
}
}
