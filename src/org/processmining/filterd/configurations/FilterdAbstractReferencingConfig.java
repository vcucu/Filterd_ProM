package org.processmining.filterd.configurations;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.widgets.ParameterOneFromSetExtendedController;

public abstract class FilterdAbstractReferencingConfig extends FilterdAbstractConfig {

	
	FilterdAbstractReferenceableConfig concreteReference;
	
	public FilterdAbstractReferencingConfig(XLog log, Filter filterType) {
		super(log, filterType);
	}

	@Override
	public abstract boolean checkValidity(XLog candidateLog);

	@Override
	public abstract boolean canPopulate(FilterConfigPanelController component) ;

	@Override
	public abstract AbstractFilterConfigPanelController getConfigPanel() ;

	public FilterdAbstractConfig changeReference(ParameterOneFromSetExtendedController controller) {
		concreteReference = new FilterdTraceStartEventCategoricalConfig(log, filterType,
				controller.getValue(), complexClassifiers);
		return concreteReference;
	}
	
	public FilterdAbstractReferenceableConfig getConcreteReference() {
		return concreteReference;
	}
}
