package org.processmining.filterd.configurations;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.FilterConfigPanelController;

public class FilterdTracePCLConfig extends FilterdAbstractConfig {

	public FilterdTracePCLConfig(XLog log, Filter filterType) {
		super(log, filterType);
		// TODO Auto-generated constructor stub
	}


	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController("Trace PCL Configuration", parameters, this);
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
