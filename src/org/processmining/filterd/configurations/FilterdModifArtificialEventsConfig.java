package org.processmining.filterd.configurations;
import org.processmining.filterd.parameters.Parameter;
import java.util.ArrayList;
import javax.swing.JComponent;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.FilterConfigPanelController;

public class FilterdModifArtificialEventsConfig extends FilterdAbstractConfig {

	public FilterdModifArtificialEventsConfig(XLog log, Filter filterType) {
		super(log, filterType);
		// TODO Auto-generated constructor stub
	}

	public FilterdAbstractConfig populate(FilterConfigPanelController component) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		// TODO Auto-generated method stub
		return false;
	}

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController("Artificial Events Configuration", parameters);
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
