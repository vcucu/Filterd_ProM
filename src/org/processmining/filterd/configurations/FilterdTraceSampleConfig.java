package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterValueFromRange;

public class FilterdTraceSampleConfig extends FilterdAbstractConfig {

	public FilterdTraceSampleConfig(XLog log, Filter filterType) {
		super(log, filterType);
		
		//initialize the configuration's parameters list
		parameters = new ArrayList<>();
		
		//initialize the sample size larger to have options from 0 to the log's size
		List<Integer> optionsPair = new ArrayList<>();
		optionsPair.add(0);
		optionsPair.add(log.size());
		
		//initialize the sample size parameter and add it to the parameters list
		ParameterValueFromRange<Integer> valueFromRangeParam = 
				new ParameterValueFromRange<Integer>(
			"threshold", 
			"Sample size", 
			0, 
			optionsPair,
			Integer.class	
		);
		
		parameters.add(valueFromRangeParam);
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};

	public FilterConfigPanelController getConfigPanel() {
		//return a new panel for this configuration with the relevant name and parameters
		return new FilterConfigPanelController(
				"Filter Trace Sample Configuration", parameters, this);
	}


	@SuppressWarnings("unchecked")
	public boolean checkValidity(XLog log) {
		//the log is valid for this configuration if its size is bigger than the sample size
		return log.size() >= ((ParameterValueFromRange<Integer>)
				(parameters.get(0))).getChosen();
	}

}
