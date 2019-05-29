package org.processmining.filterd.configurations;

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
	
	List<Double> firstAndlastTimestamp;

	public FilterdTraceTimeframeConfig(XLog log, Filter filterType) {
		super(log, filterType);
		
		firstAndlastTimestamp = new ArrayList<>();
		firstAndlastTimestamp.add(Toolbox.getFirstAndLastTimes(log)[0]);
		firstAndlastTimestamp.add(Toolbox.getFirstAndLastTimes(log)[1]);
		
		parameters = new ArrayList<>();
		
		ParameterRangeFromRange<Double> timeframeParameter = 
				new ParameterRangeFromRange<Double>(
						"timeframe", 
						"Select timeframe", 
						firstAndlastTimestamp, 
						firstAndlastTimestamp);
		
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
