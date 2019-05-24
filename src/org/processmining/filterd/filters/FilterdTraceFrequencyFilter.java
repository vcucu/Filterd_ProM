package org.processmining.filterd.filters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.framework.plugin.PluginContext;

public class FilterdTraceFrequencyFilter extends Filter {
	
	public FilterdTraceFrequencyFilter() {
		
	}

	public XLog filter(PluginContext context, XLog log, List<Parameter> parameters) {
				
		//clone input log, since ProM documentation says filters should not change input logs
		XLog clonedLog = (XLog) log.clone();
		
		
		/*
		 * Map from XTrace -> List<String>.
		 * The trace to the the class identity of every event in that trace.
		 * Map from List<String> -> Integer.
		 * The class identity of every event in that trace to the occurrence of every trace.
		 */
		final Map<List<String>, Integer> traceOccurrenceMap = new HashMap<List<String>, Integer>();
		final Map<XTrace, List<String>> traceActivitiesMap = new HashMap<XTrace, List<String>>();
		
		// Loop over every trace in the log.
		for (XTrace trace : clonedLog) {
			
			// List to hold the class identity of every event in this trace.
			List<String> activities = new ArrayList<String>();
			
			// Loop over every event in this trace.
			for (XEvent event : trace) {
				activities.add(log.getClassifiers().get(0).getClassIdentity(event));
			}
			
			// Add it to the trace -> class identity mapping.
			traceActivitiesMap.put(trace, activities);
			
			// Count the occurrences of every trace
			// and add it to the class identity -> occurrences mapping.
			if (traceOccurrenceMap.keySet().contains(activities)) {
				traceOccurrenceMap.put(activities, traceOccurrenceMap.get(activities) + 1);
			} else {
				traceOccurrenceMap.put(activities, 1);
			}
			
		}
		
		// Get the occurrences of every trace variant.
		List<Integer> occurrences = new ArrayList<Integer>(traceOccurrenceMap.values());
		Collections.sort(occurrences);
		
		// Get parameters set by the user in the configuration panel.
		ParameterOneFromSet FreqOcc = (ParameterOneFromSet) parameters.get(0);
		ParameterRangeFromRange<Double> thresholdParameters = (ParameterRangeFromRange<Double>) parameters.get(1);
		ParameterOneFromSet filterInOut = (ParameterOneFromSet) parameters.get(2);

		int lowThreshold;
		int highThreshold;
		
		// Need to get the defining attributes to do the variants.
		Set<String> classifyingAttributes = new HashSet<>();
		
		// Get the classifiers from the log.
		List<XEventClassifier> classifiers = log.getClassifiers();
		
		// Loop over every classifier. 
		for (XEventClassifier classifier : classifiers) {
			
			// Extract every key and add it to the set.
			for (String key : classifier.getDefiningAttributeKeys()) {
				classifyingAttributes.add(key);
			}
			
		}
		
		// Create collection for traces inside and outside the thresholds set by 
		// the user.
		Collection<XTrace> tracesOutsideThreshold = new HashSet<XTrace>();
		Collection<XTrace> tracesInsideThreshold = new HashSet<XTrace>();
		
		// Check variants and see if they occur within both thresholds.
		
		// Collect all variants:
		// Variants of traces are traces with the same order of events and
		// values for every key except for the time stamp.
		// Create mapping from variants to trace indices
		Map<XTrace, List<Integer>> variantsToTraceIndices = new HashMap<>();
		
		// Loop over every trace in the log
		for (int i = 0; i < log.size(); i++) {
			
			// Add first trace because we are certain that there is no
			// other variant in the hash map since it is empty.
			if (variantsToTraceIndices.isEmpty()) {
				
				// Add it to the mapping as a new entry.
				List<Integer> indicesList = new ArrayList<>();
				indicesList.add(i);
				
				variantsToTraceIndices.put(log.get(i), indicesList);
				
			} else {
				
				boolean isDifferentVariant = true;
				
				for (XTrace variant : variantsToTraceIndices.keySet()) {
					
					if (isSameVariant(
							log.get(i), 
							variant, 
							classifyingAttributes
							)) {
						
						// Add the index of this trace to the mapping of 
						// the variant we found.
						variantsToTraceIndices.get(variant).add(i);
						
						isDifferentVariant = false;
						
						// stop looping over variants since we found one.
						break;
					}
					
				}
				
				// Add this trace because there are no variants pertaining
				// to this trace.
				if (isDifferentVariant) {
					
					// Add it to the mapping as a new entry.
					List<Integer> indicesList = new ArrayList<>();
					indicesList.add(i);
					
					variantsToTraceIndices.put(log.get(i), indicesList);
					
				}

				
			}
			
			
		}
		
		/// Check the threshold type 
		// If the user wants to filter based on occurrences.
		if (FreqOcc.getChosen().contains("occ")) {
			// The parameters are given as absolute values, 
			// thus we can simply retrieve them.
			lowThreshold = thresholdParameters.getOptionsPair().get(0).intValue();
			highThreshold = thresholdParameters.getOptionsPair().get(1).intValue();
			
			
			
			// Now we have the mapping from trace variants to all the indices
			// that have this variant.
			// Now we filter based on the number traces per variant and see if 
			// that number is contained within the threshold set by the user.
			
			// Loop over the trace variants to get the indices of the traces
			// with this variant.
			for (XTrace variant : variantsToTraceIndices.keySet()) {
				
				// Get list of trace indices.
				List<Integer> traceIndices = variantsToTraceIndices
						.get(variant);
				
				// Put it in the hash set of traces within the threshold.
				if (traceIndices.size() >= lowThreshold 
						&& traceIndices.size() <= highThreshold) {
					
					// Do this for every trace of this variant.
					for (Integer traceIndex : traceIndices) {
					
						tracesInsideThreshold.add(clonedLog.get(traceIndex));
					
					}
					
				}
				// Put it in the hash set of traces outside the threshold.
				else {
					
					// Do this for every trace of this variant.
					for (Integer traceIndex : traceIndices) {
						
						tracesOutsideThreshold.add(clonedLog.get(traceIndex));
					
					}
					
					
				}
				
				
			}
			
			
			
			
		}
		// Else the user wants to filter based on frequency.
		else {
			
			// Get the percentages the user wants
			// E.g. [40, 60] means the user wants the traces from 40% until 60% of the log
			// ordered by frequency.
			lowThreshold = (thresholdParameters.getOptionsPair().get(0).intValue() * clonedLog.size()) / 100;
			highThreshold = (thresholdParameters.getOptionsPair().get(0).intValue() * clonedLog.size()) / 100;
			
			/// Calculate which number of trace occurrence represents 
			/// with the percentage of the lower threshold.
			int sum = 0;
			int index = -1;
			while (sum < lowThreshold) {
				sum += occurrences.get(++index);
			}
			/*
			 * The low-frequency 'traces' (occurrences[0]...occurrences[index]) 
			 * counted so far make up for X% of the log, 
			 * where X = thresholdParameters.getOptionsPair().get(0).
			 */
			lowThreshold = (index == -1 ? 0 : occurrences.get(index)) + 1;
			/*
			 * If we take all traces that occur fewer than threshold times, 
			 * we cover at least X% of the log.
			 */
			
			if (lowThreshold == occurrences.get(occurrences.size() - 1) + 1) {
				/*
				 * We're about to remove all traces. That seems to be undesirable.
				 */
				lowThreshold--;
			}
			
			/// Calculate which number of trace occurrence represents 
			/// with the percentage of the higher threshold.
			sum = 0;
			index = -1;
			while (sum < highThreshold) {
				sum += occurrences.get(++index);
			}
			/*
			 * The 'traces' occurrences[index]...occurrences[occurrences.size()-1]
			 * cover more than X% if the log, 
			 * where X = thresholdParameters.getOptionsPair().get(1).
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
		}
		
		
		/*
		// Loop over every trace.
		for (XTrace trace : clonedLog) {
			// Trace is inside thresholds set by user.
			if (traceOccurrenceMap.get(traceActivitiesMap.get(trace)) < lowThreshold &&
					traceOccurrenceMap.get(traceActivitiesMap.get(trace)) > highThreshold) {
				tracesOutsideThreshold.add(trace);
			}
			// Trace is outside thresholds.
			else {
				tracesInsideThreshold.add(trace);
			}
		}
		*/
		
		
		/// Check the Filter mode (filter in/out)
		// If the users wants to filter in the traces
		// i.e. keep the traces inside the threshold.
		if (filterInOut.getChosen().contains("in")) {
			// Remove all traces outside the threshold.
			clonedLog.removeAll(tracesOutsideThreshold);
		}
		// Else the user wants to filter out the traces
		// i.e. keep the traces outside the threshold.
		else {
			// Remove all traces inside the threshold.
			clonedLog.removeAll(tracesInsideThreshold);
		}
		
		// Return the cloned log where the undesirable traces have been removed.
		return clonedLog;
	}
	
	private boolean isSameVariant(
			XTrace firstTrace, 
			XTrace secondTrace,
			Set<String> attributes
			) {
		
		if (firstTrace.size() != secondTrace.size()) {
			return false;
		}
		
		
		for (int i = 0; i < firstTrace.size(); i++) {
			
			if (!(isSameEvent(
					firstTrace.get(i), 
					secondTrace.get(i), 
					attributes)
					)) {
				return false;
			}
			
		}
		
		
		return true;
	}
	
	private boolean isSameEvent(
			XEvent firstEvent, 
			XEvent secondEvent,
			Set<String> attributes
			) {
		
		Set<String> firstKeys = firstEvent.getAttributes().keySet();
		Set<String> secondKeys = secondEvent.getAttributes().keySet();
		
		if (firstKeys.size() != secondKeys.size()) {
			return false;
		}
		
		for (String key : firstKeys) {
			
			if (!(secondKeys.contains(key))) {
				return false;
			}
			
			if (attributes.contains(key)) {
			
				String firstValue = firstEvent
						.getAttributes()
						.get(key)
						.toString();
				String secondValue = secondEvent
						.getAttributes()
						.get(key)
						.toString();
				
				if (!(firstValue.equals(secondValue))) {
					return false;
				}
				
			}
		}
		
		return true;
	}

}
