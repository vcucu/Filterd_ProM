package org.processmining.filterd.filters;

import java.util.List;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.parameters.Parameter;;

public class FilterdTraceFollowerFilter extends Filter {

	public XLog filter(XLog log, List<Parameter> parameters) {
		// clone input log, since ProM documentation says filters should not 
		// change input logs
		XLog clonedLog = (XLog) log.clone();
		
		return clonedLog;
	}

}
