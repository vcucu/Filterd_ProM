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
	
	List<LocalDateTime> firstAndlastTimestamp;
	private ArrayList<String> times; 
	private ArrayList<Integer> defaultPair;
	private ArrayList<Integer> optionsPair;
	private FilterConfigPanelController configPanel;

	ArrayList<String> optionList;
	ParameterRangeFromRange<Integer> range;
	String key = "time:timestamp";

	public FilterdTraceTimeframeConfig(XLog log, Filter filterType) {
		super(log, filterType);
		
		// Get first and last time stamp using the toolbox.
		firstAndlastTimestamp = new ArrayList<>();
		firstAndlastTimestamp.add(Toolbox.getFirstAndLastTimes(log)[0]);
		firstAndlastTimestamp.add(Toolbox.getFirstAndLastTimes(log)[1]);
		
		
		times = new ArrayList<>();
		defaultPair = new ArrayList<>();
		optionsPair = new ArrayList<>();
		parameters = new ArrayList<>();
		
		boolean added = false;
		
		/*populate the array times with the ordered date&time of all events */
		for (XTrace trace: log) {
			for (XEvent event : trace) {
				/* timestamp format YYYY-MM-DDTHH:MM:SS.ssssGMT with GMT = {Z, + , -} */
				if (!event.getAttributes().containsKey(key)) continue;
				String value = event.getAttributes().get(key).toString();
				LocalDateTime time = Toolbox.synchronizeGMT(value);
				times.add(time.toString());
				added = true;
			}
		}
		
		if (!added) {
			times.add("0");
		}
		
		Collections.sort(times);
		defaultPair.add(0);
		defaultPair.add(times.size()-1);
		optionsPair.add(0);
		optionsPair.add(times.size()-1);
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
								"Completed in timeframe",
								"Trim to timeframe"));
		
		parameters.add(range);
		parameters.add(keepTracesParameter);
		configPanel = new FilterConfigPanelController(
				"Filter Trace Timeframe Configuration", parameters, this);
	}

	public boolean checkValidity(XLog candidateLog) {
		if( parameters == null || candidateLog.equals(log) )
			return true;
		return false;
	}

	public boolean canPopulate(FilterConfigPanelController component) {	
		return true;
	}

	public AbstractFilterConfigPanelController getConfigPanel() {
		return configPanel;
	}
	
	

}
