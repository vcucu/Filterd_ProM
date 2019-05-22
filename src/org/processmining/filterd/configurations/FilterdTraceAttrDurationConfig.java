package org.processmining.filterd.configurations;

import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterOneFromSet;

public class FilterdTraceAttrDurationConfig extends FilterdAbstractConfig {
	
	public FilterdTraceAttrDurationConfig(XLog log, Filter filterType) {
		super(log, filterType);
		
		// Create the array list for filtering on duration or filtering on
		// events.
		List<String> durationOrEventsList = new ArrayList<String>();
		durationOrEventsList.add("Filter on duration");		
		durationOrEventsList.add("Filter on number of events");
		
		// Create the parameter for filtering on duration or filtering on
		// events.
		ParameterOneFromSet keepTracesParameter = 
				new ParameterOneFromSet(
						"filtering option", 
						"Select trace filtering option", 
						durationOrEventsList.get(0), 
						durationOrEventsList);

		//initialize the threshold options list.
		List<Double> thrOptions = new ArrayList<Double>();
		
		
		
	}

	public boolean checkValidity(XLog candidateLog) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		// TODO Auto-generated method stub
		return false;
	}

	public FilterConfigPanelController getConfigPanel() {
		// TODO Auto-generated method stub
		return null;
	}

}
