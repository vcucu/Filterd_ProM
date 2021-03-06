package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterValueFromRange;

public class FilterdTraceSampleConfig extends FilterdAbstractConfig {

	public FilterdTraceSampleConfig(XLog log, Filter filterType) {
		super(log, filterType);

		// Initialize the configuration's parameters list
		parameters = new ArrayList<>();

		// Initialize the sample size larger to have options from 
		// 0 to the log's size
		List<Integer> optionsPair = new ArrayList<>();
		optionsPair.add(0);
		optionsPair.add(log.size());

		// Initialize the sample size parameter 
		// and add it to the parameters list
		ParameterValueFromRange<Integer> valueFromRangeParam = 
				new ParameterValueFromRange<Integer>(
						"threshold", 
						"Sample size", 
						0, 
						optionsPair,
						Integer.TYPE	
						);

		parameters.add(valueFromRangeParam);
	}

	@Override
	public AbstractFilterConfigPanelController getConfigPanel() {
		if(this.configPanel == null) {
			this.configPanel = new FilterConfigPanelController(
					"Filter Trace Sample Configuration", parameters, this);
		}

		return configPanel;
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		//Check whether no params are empty if you populate with the component
		return true;
	};

	@SuppressWarnings("unchecked")
	public boolean checkValidity(XLog log) {
		// The log is valid for this configuration 
		// if its size is bigger than the sample size
		if(parameters == null) {
			return true;
		}
		return log.size() >= ((ParameterValueFromRange<Integer>)
				(parameters.get(0))).getChosen();
	}

}
