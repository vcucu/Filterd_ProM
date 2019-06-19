package org.processmining.filterd.filters;

import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.tools.Toolbox;

public class FilterdTraceAttrFilter extends Filter {
	
	public FilterdTraceAttrFilter() {
		
	}

	public XLog filter(XLog log, List<Parameter> parameters) {
						
		// clone input log, since ProM documentation says filters should not 
		// change input logs
		XLog clonedLog = (XLog) log.clone();
		
		// Get the attribute key that was chosen.
		ParameterOneFromSet attrTypeParam = (ParameterOneFromSet) parameters.get(0);
		// Get the string representation.
		String attrType = attrTypeParam.getChosen();
		
		// Get the values of the attribute key that was chosen.
		ParameterMultipleFromSet attrValuesParam = (ParameterMultipleFromSet) parameters.get(2);
		// Get the list of chosen values.
		List<String> attrValues = attrValuesParam.getChosen();
		
		// Get the selection type.
		ParameterOneFromSet selectionTypeParam = (ParameterOneFromSet) parameters.get(1);
		// Get the string representation.
		String selectionType = selectionTypeParam.getChosen();
		
		// Create list of traces to remove.
		List<XTrace> toRemove = new ArrayList<>();
		
		// Loop over all traces in the cloned log.
		for (XTrace trace : clonedLog) {
				XAttributeMap traceAttributes = trace.getAttributes();
				// If the trace does not contain these attributes.
				if (selectionType.equals("in") &&
						!Toolbox.satisfies(traceAttributes, attrType, attrValues)) {
					// Remove the trace from the log.
					toRemove.add(trace);		
				}
				// If the trace does contain these attributes.
				if (selectionType.equals("out") &&
						Toolbox.satisfies(traceAttributes, attrType, attrValues)) {
					// Remove the trace from the log.
					toRemove.add(trace);
	
				}
		}
		clonedLog.removeAll(toRemove);
		return clonedLog;
	}
	
	

}
