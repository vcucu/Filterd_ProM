package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.parameters.ParameterValueFromRange;

public class FilterdTraceSampleConfig extends FilterdAbstractConfig {

	public FilterdTraceSampleConfig(XLog log, Filter filterType) {
		super(log, filterType);
		parameters = new ArrayList<>();
		
		List<Integer> optionsPair = new ArrayList<>();
		optionsPair.add(0);
		optionsPair.add(log.size());
		
		ParameterValueFromRange param = new ParameterValueFromRange<>("threshold", "Sample size", new Integer(0), optionsPair);
		
		parameters.add();
		// TODO Auto-generated constructor stub
	}

	public FilterdAbstractConfig populate(JComponent component) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean canPopulate(JComponent component) {
		// TODO Auto-generated method stub
		return false;
	}

	public JComponent getConfigPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean checkValidity(XLog log) {
		// TODO Auto-generated method stub
		return false;
	}

}
