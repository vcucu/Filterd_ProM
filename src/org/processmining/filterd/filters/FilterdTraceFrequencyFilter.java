package org.processmining.filterd.filters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
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
				activities.add(log.getClassifiers().get(0).getClassIdentity(event));
			}
			traceActivitiesMap.put(trace, activities);
			if (traceOccurrenceMap.keySet().contains(activities)) {
				traceOccurrenceMap.put(activities, traceOccurrenceMap.get(activities) + 1);
			} else {
				traceOccurrenceMap.put(activities, 1);
			}
		}
		List<Integer> occurrences = new ArrayList<Integer>(traceOccurrenceMap.values());
		Collections.sort(occurrences);
		
		ParameterOneFromSet FreqOcc = (ParameterOneFromSet) parameters.get(0);
		ParameterRangeFromRange<Integer> thresholdParameters = (ParameterRangeFromRange<Integer>) parameters.get(1);
		ParameterOneFromSet filterInOut = (ParameterOneFromSet) parameters.get(2);
		
		
		Collection<XTrace> tracesOutsideThreshold = new HashSet<XTrace>();
		Collection<XTrace> tracesInsideThreshold = new HashSet<XTrace>();

		int lowThreshold;
		int highThreshold;
		if (FreqOcc.getChosen().contains("occ")) {
			lowThreshold = thresholdParameters.getOptionsPair().get(0);
			highThreshold = thresholdParameters.getOptionsPair().get(1);
		}
		else {
			lowThreshold = (thresholdParameters.getOptionsPair().get(0) * clonedLog.size()) / 100;
			highThreshold = (thresholdParameters.getOptionsPair().get(0) * clonedLog.size()) / 100;
			int sum = 0;
			int index = -1;
			while (sum < highThreshold) {
				sum += occurrences.get(++index);
			}
			/*
			 * The 'traces' occurrences[index]...occurrences[occurrences.size()-1]
			 * cover more than X% if the log, where X = parameters.getThreshold().
			 */
			highThreshold = occurrences.get(index);
			/*
			 * If we take all traces that occur at least as many times as threshold
			 * times, we cover at least X% of the log.
			 */
			if (highThreshold == occurrences.get(occurrences.size() - 1) + 1) {
				/*
				 * We're about to remove all traces. That seems to be undesirable.
				 */
				highThreshold--;
			}
			sum = 0;
			index = -1;
			while (sum < lowThreshold) {
				sum += occurrences.get(++index);
			}
			/*
			 * The low-frequency 'traces' (occurrences[0]...occurrences[index]) counted so far make up for X% of the log, 
			 * where X = parameters.getThreshold().
			 */
			lowThreshold = (index == -1 ? 0 : occurrences.get(index)) + 1;
			/*
			 * If we take all traces that occur fewer than threshold times, we cover at least X% of the log.
			 */
			
			if (lowThreshold == occurrences.get(occurrences.size() - 1) + 1) {
				/*
				 * We're about to remove all traces. That seems to be undesirable.
				 */
				lowThreshold--;
			}
		}
		for (XTrace trace : clonedLog) {
			/*
			 * Trace is within thresholds set by user. Add to hashset.
			 */
			if (traceOccurrenceMap.get(traceActivitiesMap.get(trace)) < lowThreshold &&
					traceOccurrenceMap.get(traceActivitiesMap.get(trace)) > highThreshold) {
				tracesOutsideThreshold.add(trace);
			} else {
				tracesInsideThreshold.add(trace);
			}
		}
		
		if (filterInOut.getChosen().contains("in")) {
			clonedLog.removeAll(tracesOutsideThreshold);
		}
		else {
			clonedLog.removeAll(tracesInsideThreshold);
		}
		/*
		 * tracesToRemove holds at least X% of the log.
		 */
		return clonedLog;
	}

}
