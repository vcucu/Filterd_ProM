package org.processmining.filterd.gui.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.processmining.filterd.configurations.FilterdAbstractConfig;

public class FilterdAbstractConfigAdapter extends XmlAdapter<FilterdAbstractConfigAdapted, FilterdAbstractConfig> {

	public FilterdAbstractConfig unmarshal(FilterdAbstractConfigAdapted v) {
		
		return null;
	}

	public FilterdAbstractConfigAdapted marshal(FilterdAbstractConfig config) {
		FilterdAbstractConfigAdapted adaptedConfig = new FilterdAbstractConfigAdapted();
		adaptedConfig.setParameters(config.getParameters());
		return adaptedConfig;
	}

}
