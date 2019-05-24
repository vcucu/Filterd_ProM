package org.processmining.filterd.filters;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.framework.plugin.PluginContext;

public abstract class Filter {

	public abstract XLog filter(PluginContext context, XLog log, List<Parameter> parameters);
	
	public Parameter getParameter(List<Parameter> parameters, String whichParameter) {
			
		for(Parameter parameter :parameters ) {
			if (parameter.getName().equals(whichParameter)) {
				return parameter;
			}
		}
		return null;
	}
}
