package org.processmining.filterd.configurations;
import org.processmining.filterd.parameters.Parameter;
import java.util.ArrayList;

import javax.swing.JComponent;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;

public class FilterdTraceStartEventCategoricalConfig extends FilterdAbstractConfig {

	public FilterdTraceStartEventCategoricalConfig(XLog log, Filter filterType) {
		super(log, filterType);
		parameters = new ArrayList<Parameter>();
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

	public XLog filter() {
		// TODO Auto-generated method stub
		return null;
	}

}
