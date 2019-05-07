package org.processmining.filterd.algorithms;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.parameters.FilterdParameters;
import org.processmining.framework.plugin.PluginContext;

public abstract class Filter {

	public abstract XLog filter(PluginContext context, XLog log, FilterdParameters parameters);
}
