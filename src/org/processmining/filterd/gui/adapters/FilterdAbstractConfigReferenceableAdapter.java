package org.processmining.filterd.gui.adapters;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.configurations.FilterdTraceStartEventCategoricalConfig;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.tools.Toolbox;

public class FilterdAbstractConfigReferenceableAdapter
		extends FilterdAbstractConfigAdapter {

	@Override
	public FilterdAbstractConfig unmarshal(
			FilterdAbstractConfigAdapted adaptedConfig) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		FilterdAbstractConfig config;
		// get the input log and filter
		XLog initialInput = FilterdAbstractConfigAdapter.getInitialInput();
		Filter filterType = (Filter) Class.forName(adaptedConfig.getfilterTypeName()).newInstance();
		// construct the config
		
		if (FilterdTraceStartEventCategoricalConfigAdapted.class.isAssignableFrom(adaptedConfig.getClass())) {
			config = new FilterdTraceStartEventCategoricalConfig(
					initialInput, filterType, ((FilterdTraceStartEventCategoricalConfigAdapted) adaptedConfig).getAttribute(),
					Toolbox.computeComplexClassifiers(initialInput));
		} else {
			throw new IllegalStateException("org.processmining.filterd.gui.adapters.FilterdAbstractConfigReferenceableAdapter.unmarshal(): Class not referenceable.");
		}
		
		// set the parameters
		config.setParameters(adaptedConfig.getParameters());
		return config;

	}

	@Override
	public FilterdAbstractConfigAdapted marshal(FilterdAbstractConfig config) {
		FilterdAbstractConfigAdapted adaptedConfig;
		
		if (FilterdTraceStartEventCategoricalConfig.class.isAssignableFrom(config.getClass())) {
			// FilterdTraceStartEventCategoricalConfig
			adaptedConfig= new FilterdTraceStartEventCategoricalConfigAdapted();			
			((FilterdTraceStartEventCategoricalConfigAdapted) adaptedConfig).setAttribute(((FilterdTraceStartEventCategoricalConfig) config).getAttribute());
		} else {
			throw new IllegalStateException("org.processmining.filterd.gui.adapters.FilterdAbstractConfigReferenceableAdapter.marshal(): Class not referenceable.");
		}

		// set generic variables.
		adaptedConfig.setClassName(config.getClass().getName());
		adaptedConfig.setfilterTypeName(config.getFilterType().getClass().getName());
		adaptedConfig.setParameters(config.getParameters());

		return adaptedConfig;

	}

}
