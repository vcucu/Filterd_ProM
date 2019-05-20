package timeframe;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.algorithms.Filter;
import org.processmining.filterd.parameters.FilterdParameters;
import org.processmining.framework.plugin.PluginContext;

public class TimeframeAlgorithm extends Filter {
	
	public XLog filter(PluginContext context, XLog log, FilterdParameters par) {
		TimeframeParameters parameters = (TimeframeParameters)par;
			XFactory factory = XFactoryRegistry.instance().currentDefault();
			XLog filteredLog = factory.createLog((XAttributeMap) log.getAttributes().clone());
			filteredLog.getClassifiers().addAll(log.getClassifiers());
			filteredLog.getExtensions().addAll(log.getExtensions());
			filteredLog.getGlobalTraceAttributes().addAll(log.getGlobalTraceAttributes());
			filteredLog.getGlobalEventAttributes().addAll(log.getGlobalEventAttributes());
			
			String lower = parameters.getLowerTime();
			String upper = parameters.getUpperTime();
			
			for (XTrace trace : log) {
				XTrace filteredTrace = factory.createTrace(trace.getAttributes());
				for (XEvent event : trace) {
					boolean add = false;
					for (String key : event.getAttributes().keySet()) {
						if (key.contains("time:timestamp")) {
							String time = stripGMT(event.getAttributes().get(key).toString());
							if (time.compareTo(lower) >= 0 && time.compareTo(upper) <= 0) {
								add = true;
							}
						}
					}
					
					if (add) {
						filteredTrace.add(event);
					}
					context.getProgress().inc();
				}
				
				if (!parameters.isRemoveEmptyTraces() || !filteredTrace.isEmpty()) {
					filteredLog.add(filteredTrace);
				}
			}
			
			XConceptExtension.instance().assignName(filteredLog, parameters.getName());
			context.getFutureResult(0).setLabel(parameters.getName());
			return filteredLog;
		}
	
	private String stripGMT(String time) {
		if (time.contains(".")) {
			return time.split("\\.")[0];
		} else {
			if (time.contains("+")) {
				return (time.split("\\+")[0]);
			} else if (time.contains("Z")) {
				return (time.split("Z")[0]);
			} else if (time.contains("-")) {
				return (time.split("\\-")[0]);
			}
		}
		return time;
	}
}
