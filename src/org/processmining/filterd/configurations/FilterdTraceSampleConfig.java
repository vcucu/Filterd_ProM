package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.widgets.ParameterValueFromRangeController;

import javafx.scene.Node;

public class FilterdTraceSampleConfig extends FilterdAbstractConfig {

	public FilterdTraceSampleConfig(XLog log, Filter filterType) {
		super(log, filterType);
		parameters = new ArrayList<>();
		
		List<Double> optionsPair = new ArrayList<>();
		optionsPair.add(0d);
		optionsPair.add((double)log.size());
		
		ParameterValueFromRange<Double> valueFromRangeParam = new ParameterValueFromRange<>(
			"threshold", 
			"Sample size", 
			0d, 
			optionsPair
		);
		
		parameters.add(valueFromRangeParam);
	}

	public FilterdAbstractConfig populate(FilterConfigPanelController component) {
		ParameterValueFromRangeController controller = 
				(ParameterValueFromRangeController) component.getControllers().get(0);
	
		((ParameterValueFromRange<Double>)this.parameters.get(0))
		.setChosen(controller.getValue());
		
		return this;
	}

	public boolean canPopulate(FilterConfigPanelController component) {
	
		return true;
	}

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController("Filter trace sample configuration dialog", parameters);
		
	}


	@SuppressWarnings("unchecked")
	public boolean checkValidity(XLog log) {
		return log.size() < ((ParameterValueFromRange<Integer>)(parameters.get(0))).getChosen();
	}

}
