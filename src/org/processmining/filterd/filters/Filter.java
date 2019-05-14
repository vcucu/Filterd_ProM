package org.processmining.filterd.filters;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.framework.plugin.PluginContext;

public abstract class Filter {

	public abstract XLog filter(PluginContext context, XLog log, FilterdAbstractConfig parameters);
}
