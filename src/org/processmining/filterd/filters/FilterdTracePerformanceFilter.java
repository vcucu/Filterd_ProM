package org.processmining.filterd.filters;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.framework.plugin.PluginContext;

public class FilterdTracePerformanceFilter extends Filter {
	
	public FilterdTracePerformanceFilter() {
		
	}

	public XLog filter(
			PluginContext context, 
			XLog log, 
			List<Parameter> parameters) {
		
		// clone input log, since ProM documentation says filters should not 
		// change input logs
		XLog clonedLog = (XLog) log.clone();
		
		
		ParameterOneFromSet chosenOption = 
				(ParameterOneFromSet) parameters.get(0);
	
		switch (chosenOption.getChosen()) {
			case "filter on duration": {
				return filterDuration(
						clonedLog, 
						(ParameterRangeFromRange<Integer>) parameters.get(1));
			}
			case "filter on number of events": {
				return filterNumberOfEvents(
						clonedLog, 
						(ParameterRangeFromRange<Integer>) parameters.get(1));
			}
		}
		
		return clonedLog;
	}
	
	public XLog filterDuration(XLog clonedLog,
			ParameterRangeFromRange<Integer> threshold) {
		
		// Threshold contains the thresholds in milliseconds.
		double lowThreshold = threshold.getChosenPair().get(0);
		double highThreshold = threshold.getChosenPair().get(1);
		
		Set<XTrace> removeFromLog = new HashSet<>();
		
		
		for (XTrace trace : clonedLog) {
			
			// Use first and last event to calculate the total duration of
			// the trace.
			String firstEventTime = trace
					.get(0)
					.getAttributes()
					.get("time:timestamp")
					.toString();
			String lastEventTime = trace
					.get(trace.size() - 1)
					.getAttributes()
					.get("time:timestamp")
					.toString();
			
			LocalDateTime startTime = Toolbox.synchronizeGMT(firstEventTime);
			LocalDateTime endTime = Toolbox.synchronizeGMT(lastEventTime);
			
			Duration traceDuration = Duration.between(startTime, endTime);
			int totalMillis = (int) traceDuration.toMillis();
			
			if (totalMillis < lowThreshold || totalMillis > highThreshold) {
				removeFromLog.add(trace);
			}
			
		}
		
		clonedLog.removeAll(removeFromLog);
		
		return clonedLog;
	}
	
	public XLog filterNumberOfEvents(XLog clonedLog,
			ParameterRangeFromRange<Integer> threshold) {
		
		// Threshold contains the thresholds in number of events.
		int lowThreshold = threshold.getChosenPair().get(0);
		int highThreshold = threshold.getChosenPair().get(1);
		
		Set<XTrace> removeFromLog = new HashSet<>();
		
		for (XTrace trace : clonedLog) {
			
			// A trace contains a list of events.
			// Check if the size of this list is within thresholds set by
			// the user.
			// Otherwise, remove it.
			if (trace.size() < lowThreshold || trace.size() > highThreshold) {
				removeFromLog.add(trace);
			}
			
		}
		
		clonedLog.removeAll(removeFromLog);
		
		return clonedLog;
	}

}
