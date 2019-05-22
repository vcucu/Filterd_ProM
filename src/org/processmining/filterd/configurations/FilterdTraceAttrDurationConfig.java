package org.processmining.filterd.configurations;

import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterRangeFromRange;

public class FilterdTraceAttrDurationConfig extends FilterdAbstractConfig {
	
	public FilterdTraceAttrDurationConfig(XLog log, Filter filterType) {
		super(log, filterType);
		
		//initialize the threshold options list.
		List<Double> thrOptions = new ArrayList<Double>();
		
		// Frequency is a percentage, therefore minimum value is 0 and maximum
		// value is 100.
		thrOptions.add(0d);
		thrOptions.add(100d);
		
		// Create parameter for selecting the threshold and set the outermost
		// values as the default values.
		ParameterRangeFromRange<Double> parameterThreshold = 
				new ParameterRangeFromRange<Double>(
				"threshold", 
				"Select threshold for frequency", 
				thrOptions, 
				thrOptions
				);
		
		parameters.add(parameterThreshold);
	}

	public boolean checkValidity(XLog candidateLog) {
		// Always true since duration is a percentage.
		return true;
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		// Always true since duration is a percentage.
		return true;
	}

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController
				(
				"Duration Trace Attribute Configuration", 
				parameters
				);
	}

}
