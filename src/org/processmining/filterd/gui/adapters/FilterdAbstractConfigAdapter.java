package org.processmining.filterd.gui.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.configurations.FilterdAbstractReferencingConfig;

public class FilterdAbstractConfigAdapter extends XmlAdapter<FilterdAbstractConfigAdapted, FilterdAbstractConfig> {

	public FilterdAbstractConfig unmarshal(FilterdAbstractConfigAdapted adaptedConfig) {
		
		return null;
	}

	public FilterdAbstractConfigAdapted marshal(FilterdAbstractConfig config) {
		FilterdAbstractConfigAdapted adaptedConfig;
		if (config.getClass().isAssignableFrom(FilterdAbstractReferencingConfig.class)) {
			adaptedConfig = new FilterdAbstractConfigReferencingAdapted();
			((FilterdAbstractConfigReferencingAdapted) adaptedConfig).setConcreteReference(
					((FilterdAbstractReferencingConfig) config).getConcreteReference());
		} else {
			adaptedConfig = new FilterdAbstractConfigAdapted();
		}
		adaptedConfig.setParameters(config.getParameters());
		return adaptedConfig;
	}

}
