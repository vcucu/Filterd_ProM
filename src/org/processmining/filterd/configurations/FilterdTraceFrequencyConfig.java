package org.processmining.filterd.configurations;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.widgets.ParameterOneFromSetController;
import org.processmining.filterd.widgets.ParameterRangeFromRangeController;
import org.processmining.filterd.widgets.ParameterValueFromRangeController;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;

public class FilterdTraceFrequencyConfig extends FilterdAbstractConfig {

	public FilterdTraceFrequencyConfig(XLog log, Filter filterType) {
		super(log, filterType);
		
		//initialize the configuration's parameters list
		parameters = new ArrayList<>();
		
		//initialize the threshold type parameter and add it to the parameters list
		List<String> foOptions = new ArrayList<String>();
		
		foOptions.add("frequency");
		foOptions.add("occurrance");
		
		ParameterOneFromSet FreqOcc = new ParameterOneFromSet(
			"FreqOcc", 
			"Threshold type", 
			"frequency", 
			foOptions
		);
		
		parameters.add(FreqOcc);
		
		//initialize the threshold options parameter and add it to the parameters list
		List<Double> thrOptions = new ArrayList<Double>();
		
		//since the default option is "frequency", it goes from 1% to 100%
		thrOptions.add(1d);
		thrOptions.add(100d);
		
		ParameterRangeFromRange<Double> threshold = new ParameterRangeFromRange<Double>(
				"threshold",
				"Threshold",
				thrOptions,
				thrOptions
				);
		
		parameters.add(threshold);
		
		//initialize the filter mode options parameter and add it to the parameters list
		List<String> fModeOptions = new ArrayList<String>();
		
		fModeOptions.add("in");
		fModeOptions.add("out");
		
		ParameterOneFromSet filterInOut = new ParameterOneFromSet(
				"filterInOut",
				"Filter mode",
				"in",
				fModeOptions
				);
		
		parameters.add(filterInOut);
				
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};

	public FilterConfigPanelController getConfigPanel() {
		//return a new panel for this configuration with the relevant name and parameters
		return new FilterConfigPanelController("Filter Trace Frequency Configuration", parameters, this);
	}

	public boolean checkValidity(XLog log) {
		//if the threshold type is "occurrence" and the currently selected threshold is higher than  
		//the log's size, then the configuration is not valid for that log
		if(((ParameterOneFromSet)parameters.get(0)).getChosen().contains("occ") && 
				 ((ParameterRangeFromRange<Double>)parameters.get(1)).getChosenPair().get(1).intValue() > log.size()) {
				return false;
		}
		else {
			return true;
		}
	}

}
