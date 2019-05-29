package org.processmining.filterd.filters;

import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.tools.Toolbox;

public class FilterdTracesHavingEvent extends Filter {

	@Override
	public XLog filter(XLog log, List<Parameter> parameters) {
		XLog clonedLog = (XLog) log.clone();
		
		ParameterOneFromSet attrTypeParam = (ParameterOneFromSet) parameters.get(0);
		String attrType = attrTypeParam.getChosen();
		
		ParameterMultipleFromSet attrValuesParam = (ParameterMultipleFromSet) parameters.get(1);
		List<String> attrValues = attrValuesParam.getChosen();
		
		ParameterOneFromSet selectionTypeParam = (ParameterOneFromSet) parameters.get(2);
		String selectionType = selectionTypeParam.getChosen();
		List<XTrace> toRemove = new ArrayList<>();
		
		for (XTrace trace : clonedLog) {
			boolean remove = true;
			for (XEvent event : trace) {
				XAttributeMap eventAttributes = event.getAttributes();
				if (selectionType.equals("Forbidden") &&
						Toolbox.satisfies(eventAttributes, attrType, attrValues)) {
					toRemove.add(trace);
					break;			
				}
				if (selectionType.equals("Mandatory") &&
						Toolbox.satisfies(eventAttributes, attrType, attrValues)) {
					remove = false;
					break;
				}
	}
			if (selectionType.equals("Mandatory") && remove) {
				toRemove.add(trace);
			}
		}
		clonedLog.removeAll(toRemove);
		return clonedLog;
}
}
