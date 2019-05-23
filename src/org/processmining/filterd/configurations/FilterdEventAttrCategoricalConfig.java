package org.processmining.filterd.configurations;
import org.processmining.filterd.parameters.Parameter;
import java.util.ArrayList;
import javax.swing.JComponent;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;

public class FilterdEventAttrCategoricalConfig extends FilterdAbstractConfig{

	public FilterdEventAttrCategoricalConfig(XLog log, Filter filterType) {
		super(log, filterType);
		// TODO Auto-generated constructor stub
	}

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController("Categorical Event Attribute Configuration", parameters, this);
	}

	public boolean checkValidity(XLog log) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};

	public XLog filter() {
		// TODO Auto-generated method stub
		return null;
	}

}
