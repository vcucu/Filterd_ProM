package org.processmining.filterd.configurations;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.tools.Toolbox;

public class FilterdTraceTimeframeConfig extends FilterdAbstractConfig {
	
	List<LocalDateTime> firstAndlastTimestamp;

	public FilterdTraceTimeframeConfig(XLog log, Filter filterType) {
		super(log, filterType);
		
		// Get first and last time stamp using the toolbox.
		firstAndlastTimestamp = new ArrayList<>();
		firstAndlastTimestamp.add(Toolbox.getFirstAndLastTimes(log)[0]);
		firstAndlastTimestamp.add(Toolbox.getFirstAndLastTimes(log)[1]);
		
		parameters = new ArrayList<>();
		
		// Create time frame parameter, the user can select here what time
		// frame he wants.
		ParameterRangeFromRange<Double> timeframeParameter = 
				new ParameterRangeFromRange<Double>(
						"timeframe", 
						"Select timeframe", 
						Arrays.asList(
								0d,
								(double) Duration.between(
										firstAndlastTimestamp.get(0), 
										firstAndlastTimestamp.get(1))
								.toMillis()), 
						Arrays.asList(
								0d,
								(double) Duration.between(
										firstAndlastTimestamp.get(0), 
										firstAndlastTimestamp.get(1))
								.toMillis()));
		
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
		
		parameters.add(timeframeParameter);
		parameters.add(keepTracesParameter);
	}

	public boolean checkValidity(XLog candidateLog) {
		
		return false;
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		
		
		return false;
	}

	public AbstractFilterConfigPanelController getConfigPanel() {
		
		
		
		return null;
	}
	
	

}
