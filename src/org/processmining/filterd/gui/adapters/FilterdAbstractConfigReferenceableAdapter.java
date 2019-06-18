package org.processmining.filterd.gui.adapters;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.configurations.FilterdAbstractReferenceableConfig;
import org.processmining.filterd.configurations.FilterdEventAttrNumericalConfig;
import org.processmining.filterd.configurations.FilterdTraceStartEventCategoricalConfig;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.tools.Toolbox;

public class FilterdAbstractConfigReferenceableAdapter
		extends FilterdAbstractConfigAdapter {

	@Override
	public FilterdAbstractConfig unmarshal(
			FilterdAbstractConfigAdapted adaptedConfig) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		FilterdAbstractConfig config;
		// get the input log and filter.
		XLog initialInput = FilterdAbstractConfigAdapter.getInitialInput();
		Filter filterType = (Filter) Class.forName(adaptedConfig.getfilterTypeName()).newInstance();
		
		Class<FilterdAbstractReferenceableConfig> configClass = (Class<FilterdAbstractReferenceableConfig>) Class.forName(adaptedConfig.getClassName());
		
		// construct the config
		if (FilterdAbstractConfigAttributeAdapted.class.isAssignableFrom(adaptedConfig.getClass())) {
			config = configClass.getDeclaredConstructor(XLog.class, Filter.class, String.class, List.class).newInstance(initialInput,
					filterType,
					((FilterdAbstractConfigAttributeAdapted) adaptedConfig).getAttribute(),
					Toolbox.computeComplexClassifiers(initialInput));
		} else if (FilterdAbstractConfigKeyAdapted.class.isAssignableFrom(adaptedConfig.getClass())) {
			config = configClass.getDeclaredConstructor(XLog.class, Filter.class, String.class).newInstance(initialInput,
					filterType,
					((FilterdAbstractConfigAttributeAdapted) adaptedConfig).getAttribute());
		} else {
			// adapter has not been configured for this class.
			throw new IllegalStateException("org.processmining.filterd.gui.adapters.FilterdAbstractConfigReferenceableAdapter.unmarshal(): Class not referenceable: " + adaptedConfig.getClass().getCanonicalName());
		}
		
		// set the parameters.
		config.setParameters(adaptedConfig.getParameters());
		return config;
	}

	@Override
	public FilterdAbstractConfigAdapted marshal(FilterdAbstractConfig config) {
		FilterdAbstractConfigAdapted adaptedConfig;
		
		if (FilterdTraceStartEventCategoricalConfig.class.isAssignableFrom(config.getClass())) {
			// FilterdTraceStartEventCategoricalConfig.
			adaptedConfig = new FilterdAbstractConfigAttributeAdapted();			
			((FilterdAbstractConfigAttributeAdapted) adaptedConfig).setAttribute(((FilterdTraceStartEventCategoricalConfig) config).getAttribute());
		} else if (FilterdEventAttrNumericalConfig.class.isAssignableFrom(config.getClass())) {
			// FilterdEventAttrNumericalConfig
			adaptedConfig =  new FilterdAbstractConfigKeyAdapted();
			((FilterdAbstractConfigKeyAdapted) adaptedConfig).setKey(((FilterdEventAttrNumericalConfig) config).getKey());
		} else {
			// adapter has not been configured for this class.
			throw new IllegalStateException("org.processmining.filterd.gui.adapters.FilterdAbstractConfigReferenceableAdapter.unmarshal(): Class not referenceable: " + config.getClass().getCanonicalName());
		}

		// set generic variables.
		adaptedConfig.setClassName(config.getClass().getName());
		adaptedConfig.setfilterTypeName(config.getFilterType().getClass().getName());
		adaptedConfig.setParameters(config.getParameters());

		return adaptedConfig;
	}
}
