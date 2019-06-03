package org.processmining.filterd.configurations;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.*;
import org.processmining.filterd.widgets.*;

import java.util.List;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;

public class FilterdModifMergeSubsequentConfig extends FilterdAbstractReferencingConfig {
	
	public FilterdModifMergeSubsequentConfig(XLog log, Filter filterType) {
		super(log, filterType);
		// TODO Auto-generated constructor stub
	}



	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController("Merge Subsequent Events Configuration", parameters, this);
	}
	

	public boolean checkValidity(XLog log) {
		// TODO Auto-generated method stub
		return false;
	}

	public XLog filter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FilterdAbstractConfig changeReference(ParameterOneFromSetExtendedController controller) {
		// TODO Auto-generated method stub
		return null;
	}



}
