package org.processmining.filterd.filters;
import java.util.List;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.framework.plugin.PluginContext;

public class FilterdTraceStartEventFilter extends Filter {

	public XLog filter(PluginContext context, XLog log, List<Parameter> parameters) {
		
		//attribute to be filtered on
		ParameterOneFromSet attribute = (ParameterOneFromSet)this
				.getParameter(parameters, "attribute");
		//filter in/ filter out
		ParameterOneFromSet selectionType = (ParameterOneFromSet)this
				.getParameter(parameters, "selectionType");
		//which values for the attribute were selected
		ParameterMultipleFromSet desiredEvents = (ParameterMultipleFromSet)this
				.getParameter(parameters, "desiredEvents");
		//whether to remove if no value provided
		ParameterYesNo nullHandling = (ParameterYesNo)this
				.getParameter(parameters, "nullHandling");
		
		
		//initialize the log that will be output
		XLog filteredLog = this.initializeLog(log);
		
		for (XTrace trace : log) {
			//do not query on empty traces
			if (!trace.isEmpty()) {
				//retrieve the first event
				XEvent first =  trace.get(0);
				//retrieve the first value
				String value = first.getAttributes().get(attribute.getChosen()).toString();
				
			}
			
		}
		
		return filteredLog;
	}

}
