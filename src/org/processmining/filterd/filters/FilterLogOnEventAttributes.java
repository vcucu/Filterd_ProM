package org.processmining.filterd.filters;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.configurations.OLD_FilterdEventAttributesParameters;
import org.processmining.framework.plugin.PluginContext;

public class FilterLogOnEventAttributes extends Filter {

	public XLog filter(PluginContext context, XLog log, FilterdAbstractConfig par) {
		OLD_FilterdEventAttributesParameters parameters = (OLD_FilterdEventAttributesParameters)par;
		XFactory factory = XFactoryRegistry.instance().currentDefault();
		XLog filteredLog = factory.createLog((XAttributeMap) log.getAttributes().clone());
		filteredLog.getClassifiers().addAll(log.getClassifiers());
		filteredLog.getExtensions().addAll(log.getExtensions());
		filteredLog.getGlobalTraceAttributes().addAll(log.getGlobalTraceAttributes());
		filteredLog.getGlobalEventAttributes().addAll(log.getGlobalEventAttributes());
		for (XTrace trace : log) {
			XTrace filteredTrace = factory.createTrace(trace.getAttributes());
			for (XEvent event : trace) {
				boolean add = true;
				if(event.getAttributes().keySet().containsAll(parameters.getGlobalAttributes())) {
					for (String key: event.getAttributes().keySet()) {
						String value = event.getAttributes().get(key).toString();
						if (!parameters.getLogMap().get(key).contains(value)) {
							add = false;
							continue;
						}
					}
					if (add) {
						filteredTrace.add(event);
					}
				}
				context.getProgress().inc();
			}
			if (!parameters.isRemoveEmptyTraces()||!filteredTrace.isEmpty()) {
				filteredLog.add(filteredTrace);
			}
		}
		XConceptExtension.instance().assignName(filteredLog, parameters.getName());
		context.getFutureResult(0).setLabel(parameters.getName());
		return filteredLog;
	}

}
