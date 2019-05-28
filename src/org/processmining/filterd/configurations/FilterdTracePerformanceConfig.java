package org.processmining.filterd.configurations;

import java.util.Arrays;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.tools.Toolbox;

public class FilterdTracePerformanceConfig extends FilterdAbstractConfig {
	
	List<Double> minAndMaxDuration;
	List<Double> minAndMaxEvents;
	
	public FilterdTracePerformanceConfig(XLog log, Filter filterType) {
		super(log, filterType);
		
		// Initialize members based on the log.
		minAndMaxDuration = Toolbox.getMinAnMaxDuration(log);
		minAndMaxEvents = Toolbox.getminAdnMaxEventSize(log);
		
		// Create performance options parameter and set the option to duration
		// as default.
		ParameterOneFromSet performanceOptionsParameter = 
				new ParameterOneFromSet(
						"performanceOptions", 
						"Select performance option", 
						"filter on duration", 
						Arrays.asList(
								"filter on duration", 
								"filter on number of events"));

		// Use duration as default because this is also set in the performance
		// options parameter.
		ParameterRangeFromRange<Double> valueParameter = 
				new ParameterRangeFromRange<Double>(
						"threshold", 
						"Select the threshold", 
						minAndMaxDuration, 
						minAndMaxDuration);
		
		// Add the created parameters.
		parameters.add(performanceOptionsParameter);
		parameters.add(valueParameter);
	}

	public boolean checkValidity(XLog candidateLog) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		// TODO Auto-generated method stub
		return false;
	}

	public AbstractFilterConfigPanelController getConfigPanel() {
		// TODO Auto-generated method stub
		return null;
	}

}
