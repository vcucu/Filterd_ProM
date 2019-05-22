package org.processmining.filterd.filters;
import java.util.List;

import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttributeMap;
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
	
	protected XLog initializeLog(XLog originalLog) {
		
		XFactory factory = XFactoryRegistry.instance().currentDefault();
		XLog filteredLog = factory.createLog((XAttributeMap) originalLog.getAttributes().clone());
		filteredLog.getClassifiers().addAll(originalLog.getClassifiers());
		filteredLog.getExtensions().addAll(originalLog.getExtensions());
		filteredLog.getGlobalTraceAttributes().addAll(originalLog.getGlobalTraceAttributes());
		filteredLog.getGlobalEventAttributes().addAll(originalLog.getGlobalEventAttributes());
		return filteredLog;
	}
	
	
	
}
