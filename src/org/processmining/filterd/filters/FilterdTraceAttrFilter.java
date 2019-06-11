package org.processmining.filterd.filters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.filterd.tools.Toolbox;

public class FilterdTraceAttrFilter extends Filter {
	
	public FilterdTraceAttrFilter() {
		
	}

	public XLog filter(XLog log, List<Parameter> parameters) {
						
		// clone input log, since ProM documentation says filters should not 
		// change input logs
		XLog clonedLog = (XLog) log.clone();
		
		
		ParameterOneFromSet attrTypeParam = (ParameterOneFromSet) parameters.get(0);
		String attrType = attrTypeParam.getChosen();
		
		ParameterMultipleFromSet attrValuesParam = (ParameterMultipleFromSet) parameters.get(2);
		List<String> attrValues = attrValuesParam.getChosen();
		
		ParameterOneFromSet selectionTypeParam = (ParameterOneFromSet) parameters.get(1);
		String selectionType = selectionTypeParam.getChosen();
		List<XTrace> toRemove = new ArrayList<>();
		
		for (XTrace trace : clonedLog) {
				XAttributeMap traceAttributes = trace.getAttributes();
				if (selectionType.equals("in") &&
						!Toolbox.satisfies(traceAttributes, attrType, attrValues)) {
					toRemove.add(trace);		
				}
				if (selectionType.equals("out") &&
						Toolbox.satisfies(traceAttributes, attrType, attrValues)) {
					toRemove.add(trace);
	
				}
		}
		clonedLog.removeAll(toRemove);
		return clonedLog;
	}
	
	

}
