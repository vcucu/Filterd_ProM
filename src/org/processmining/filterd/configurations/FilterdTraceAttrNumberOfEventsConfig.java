package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterRangeFromRange;

public class FilterdTraceAttrNumberOfEventsConfig extends FilterdAbstractConfig {

	public FilterdTraceAttrNumberOfEventsConfig(XLog log, Filter filterType) {
		super(log, filterType);


		//initialize the threshold options list.
		List<Double> thrOptions = new ArrayList<Double>();
		
		int smallestAndLargest[] = getSmallestAndLargestSize(log);
		
		thrOptions.add((double) smallestAndLargest[0]);
		thrOptions.add((double) smallestAndLargest[1]);
		
		// Create parameter for selecting the threshold and set the outermost
		// values as the default values.
		ParameterRangeFromRange<Double> parameterThreshold = 
				new ParameterRangeFromRange<Double>(
				"Threshold", 
				"Select threshold for frequency", 
				thrOptions, 
				thrOptions
				);
		
		parameters.add(parameterThreshold);
	}
	
	private int[] getSmallestAndLargestSize(XLog log) {
		
		// Initialize array for holding smallest and largest trace size.
		int smallestAndLargest[] = new int[2];
		
		// Set initial values for comparison.
		int smallestSize = Integer.MAX_VALUE;
		int largestSize = Integer.MIN_VALUE;
	
		// Loop over every trace to get the size of every trace.
		for (XTrace trace : log) {
			
			int traceSize = trace.size();
			
			// Do comparisons to get the earliest time a trace is started.
			if (traceSize < smallestSize) {
				smallestSize = traceSize;
			}
			
			// Do comparisons to get the latest time a trace is finished.
			if (traceSize > largestSize) {
				largestSize = traceSize;
			}
		}
		
		// Set the found values in the array.
		smallestAndLargest[0] = smallestSize;
		smallestAndLargest[1] = largestSize;
		
		return smallestAndLargest;
	}


	public boolean canPopulate(FilterConfigPanelController component) {
		// Can always populate since the outermost values are still applicable.
		return true;
	}

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController(
				"Filter on events Trace Attribute Configuration", 
				parameters);
	}

	public boolean checkValidity(XLog log) {
		
		// Check if the set thresholds are still within the times of the chosen
		// log.
		
		// Get the first and last time of the chosen log.
		int smallestAndLargest[] = getSmallestAndLargestSize(log);
		
		// Get the threshold parameter.
		ParameterRangeFromRange<Double> setThreshold = 
				(ParameterRangeFromRange<Double>) parameters.get(0);
		
		List<Double> chosenValues = setThreshold.getChosenPair();
		
		
		// Check if the values chosen are within the times of the chosen log.
		if (chosenValues.get(0) >= smallestAndLargest[0]
				&& chosenValues.get(1) <= smallestAndLargest[1]) {
			return true;
		} else {
			return false;
		}
	}

}
