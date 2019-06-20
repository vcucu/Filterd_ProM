package org.processmining.filterd.configurations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.tools.Toolbox;

public class FilterdTraceTimeframeConfig extends FilterdAbstractConfig {
	
	// List to set the bounds in the slider.
	List<LocalDateTime> firstAndlastTimestamp;
	// times for the special range slider.
	private ArrayList<String> times; 
	// Default pair for special range slider.
	private ArrayList<Integer> defaultPair;
	// Options pair for special range slider.
	private ArrayList<Integer> optionsPair;
	// Configuration panel to hold all parameters.
	private FilterConfigPanelController configPanel;

	// Special range slider.
	ParameterRangeFromRange<Integer> range;
	// Hardcode key.
	String key = "time:timestamp";

	public FilterdTraceTimeframeConfig(XLog log, Filter filterType) {
		super(log, filterType);
		
		// Get first and last time stamp using the toolbox.
		firstAndlastTimestamp = new ArrayList<>();
		firstAndlastTimestamp.add(Toolbox.getFirstAndLastTimes(log)[0]);
		firstAndlastTimestamp.add(Toolbox.getFirstAndLastTimes(log)[1]);
		
		// Initialize all the member variables.
		times = new ArrayList<>();
		defaultPair = new ArrayList<>();
		optionsPair = new ArrayList<>();
		parameters = new ArrayList<>();
		
		// Edge case.
		boolean added = false;
		
		/*populate the array times with the ordered date&time of all events */
		for (XTrace trace: log) {
			for (XEvent event : trace) {
				/* timestamp format YYYY-MM-DDTHH:MM:SS.ssssGMT with GMT = {Z, + , -} */
				if (!event.getAttributes().containsKey(key)) continue;
				String value = event.getAttributes().get(key).toString();
				// Get the time specified in the key-value pair with key = 
				// "time:timestamp".
				LocalDateTime time = Toolbox.synchronizeGMT(value);
				times.add(time.toString());
				added = true;
			}
		}
		
		// If log does not have any time stamps.
		if (!added) {
			times.add("0");
		}
		
		// Sort the times.
		Collections.sort(times);
		// Set the default pair to the outer bounds.
		defaultPair.add(0);
		defaultPair.add(times.size()-1);
		// Set the options pair to the outer bounds.
		optionsPair.add(0);
		optionsPair.add(times.size()-1);
		// Create special range from range slider.
		range = new ParameterRangeFromRange<>("time-range",
				"Select timeframe", defaultPair, 
				optionsPair, Integer.TYPE);
		range.setTimes(times);
		// Create the keep traces parameter, the user can select here how the
		// traces should be kept based on the time frame.
		ParameterOneFromSet keepTracesParameter = 
				new ParameterOneFromSet(
						"keep traces", 
						"Select how to keep traces", 
						"Contained in timeframe", 
						Arrays.asList(
								"Contained in timeframe",
								"Intersecting timeframe",
								"Started in timeframe",
								"Completed in timeframe"));
		// Add all created parameters to the created list.
		parameters.add(range);
		parameters.add(keepTracesParameter);
	}

	/**
	 * Check if the parameters are still valid on the candidate log.
	 * 
	 * @param candidateLog the log to check.
	 */
	public boolean checkValidity(XLog candidateLog) {
		// If the parameters have not been set yet or it is the same log as the
		// one that is already selected.
		if( parameters == null || candidateLog.equals(log) )
			return true;
		return false;
	}

	/**
	 * Checks if the configuration can populate the parameters.
	 * 
	 * @param component The component that populates the parameters.
	 */
	public boolean canPopulate(FilterConfigPanelController component) {	
		return true;
	}

	/**
	 * Getter for the configuration panel.
	 */
	public AbstractFilterConfigPanelController getConfigPanel() {
		// If the configuration panel was not initialized yet.
		if (this.configPanel == null) {
			// Create it.
			this.configPanel = new FilterConfigPanelController(
					"Filter Trace Timeframe Configuration", parameters, this);
		}
		return configPanel;
	}
	
	

}
