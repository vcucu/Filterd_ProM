package org.processmining.filterd.filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.framework.plugin.PluginContext;

public class FilterdTraceFrequencyFilter extends Filter {

	public XLog filter(PluginContext context, XLog log, List<Parameter> parameters) {
		//initialize the log that will be output
		XLog filteredLog = this.initializeLog(log);
				
		//clone input log, since ProM documentation says filters should not change input logs
		XLog clonedLog = (XLog) log.clone();
		
		final Map<List<String>, Integer> traceOccurrenceMap = new HashMap<List<String>, Integer>();
		final Map<XTrace, List<String>> traceActivitiesMap = new HashMap<XTrace, List<String>>();
		
		for (XTrace trace : clonedLog) {
			List<String> activities = new ArrayList<String>();
			for (XEvent event : trace) {
				//activities.add(parameters.getClassifier().getClassIdentity(event));
			}
			traceActivitiesMap.put(trace, activities);
			if (traceOccurrenceMap.keySet().contains(activities)) {
				traceOccurrenceMap.put(activities, traceOccurrenceMap.get(activities) + 1);
			} else {
				traceOccurrenceMap.put(activities, 1);
			}
		}
		
		return null;
	}

}
