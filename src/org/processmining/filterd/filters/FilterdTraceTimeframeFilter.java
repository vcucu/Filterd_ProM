package org.processmining.filterd.filters;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.tools.Toolbox;

public class FilterdTraceTimeframeFilter extends Filter {
	
	public FilterdTraceTimeframeFilter() {
		
	}

	public XLog filter(XLog log, List<Parameter> parameters) {
		
		// clone input log, since ProM documentation says filters should not 
		// change input logs
		XLog clonedLog = (XLog) log.clone();
		
	ParameterRangeFromRange<Integer> timeframeParameter = 
		(ParameterRangeFromRange<Integer>) parameters.get(0);
		ParameterOneFromSet keepTracesParameter =
				(ParameterOneFromSet) parameters.get(1);
		
		ArrayList<String> times = timeframeParameter.getTimes();

		int lowPos = timeframeParameter.getChosenPair().get(0);
		int highPos = timeframeParameter.getChosenPair().get(1);
		
		Set<XTrace> tracesToRemove = new HashSet<>();
		Set<XEvent> eventsToRemove = new HashSet<>();
				
		for (XTrace trace : clonedLog) {
			
			// Use first and last event to get the time stamps of the trace.
			XEvent firstEvent = trace.get(0);
			XEvent lastEvent = trace.get(trace.size() - 1);
			
			LocalDateTime firstEventTimeStamp = Toolbox.synchronizeGMT(
					firstEvent
					.getAttributes()
					.get("time:timestamp")
					.toString());
			
			LocalDateTime finalEventTimeStamp = Toolbox.synchronizeGMT(
					lastEvent
					.getAttributes()
					.get("time:timestamp")
					.toString());
			
			int firstEventPos = times.indexOf(firstEventTimeStamp.toString());
			int finalEventPos = times.indexOf(finalEventTimeStamp.toString());
			
			
			/* Base the filtering on the parameter chosen:
			 * 
			 * - "Contained in time frame":
			 * Keep the traces that are contained in the time frame.
			 * - "Intersecting time frame":
			 * Keep the traces that intersect with the time frame.
			 * - "Started in time frame":
			 * Keep the traces that were started in the time frame.
			 * - "Completed in time frame":
			 * Keep the traces that were completed in the time frame.
			 * - "Trim to time frame":
			 * Trim all the traces such that all the events are contained in the
			 * time frame. Remove the traces that end up empty.
			 */
			switch (keepTracesParameter.getChosen()) {
				
				case "Contained in timeframe": {
					
					// If the trace is not contained, remove it.
					if (firstEventPos < lowPos
							|| finalEventPos > highPos) {
						tracesToRemove.add(trace);
					}
					
					break;
				}
				case "Intersecting timeframe": {
					
					// If the trace is not intersecting, remove it.
					if (!(firstEventPos <= highPos
							&& finalEventPos >= lowPos)) {
						tracesToRemove.add(trace);
					}
					
					break;
				}
				case "Started in timeframe": {
					
					// If the trace is not started in the time frame, remove it.
					if (firstEventPos < lowPos
							|| firstEventPos > highPos) {
						tracesToRemove.add(trace);
					}
					
					break;
				}
				case "Completed in timeframe": {
					
					// If the trace is not completed in the time frame, 
					// remove it.
					if (!(finalEventPos <= highPos
							&& finalEventPos >= highPos)) {
						tracesToRemove.add(trace);
					}
					
					break;
				}
				
			}
			
			
		}
		
		clonedLog.removeAll(tracesToRemove);
		
		
		return clonedLog;
	}

}
