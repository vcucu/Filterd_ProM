package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.widgets.ParameterValueFromRangeController;

import javafx.scene.Node;

public class FilterdTraceSampleConfig extends FilterdAbstractConfig {

	public FilterdTraceSampleConfig(XLog log, Filter filterType) {
		super(log, filterType);
		
		//initialize the configuration's parameters list
		parameters = new ArrayList<>();
		
		//initialize the sample size larger to have options from 0 to the log's size
		List<Double> optionsPair = new ArrayList<>();
		optionsPair.add(0d);
		optionsPair.add((double)log.size());
		
		//initialize the sample size parameter and add it to the parameters list
		ParameterValueFromRange<Double> valueFromRangeParam = new ParameterValueFromRange<>(
			"threshold", 
			"Sample size", 
			0d, 
			optionsPair
		);
		
		parameters.add(valueFromRangeParam);
	}

	public FilterdAbstractConfig populate(AbstractFilterConfigPanelController component) {
		
		//update the sample size parameter with the value chosen in the 
		//panel's slider, using the panel's parameter controller 
		//THIS COMMENT BLOCK CAN BE REMOVED IN THE NEXT COMMIT WHEN REPLACED BY METHOD IN THE ABSTRACT CLASS
	/*	ParameterValueFromRangeController controller = 
				(ParameterValueFromRangeController) component.getControllers().get(0);
	
		((ParameterValueFromRange<Double>)this.parameters.get(0))
		.setChosen(controller.getValue());*/
		
		return this;
	}

	public boolean canPopulate(FilterConfigPanelController component) {
	
		return true;
	}

	public FilterConfigPanelController getConfigPanel() {
		//return a new panel for this configuration with the relevant name and parameters
		return new FilterConfigPanelController("Filter Trace Sample Configuration", parameters);
		
	}


	@SuppressWarnings("unchecked")
	public boolean checkValidity(XLog log) {
		//the log is valid for this configuration if its size is bigger than the sample size
		return log.size() > ((ParameterValueFromRange<Integer>)(parameters.get(0))).getChosen();
	}

}
