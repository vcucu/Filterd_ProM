package org.processmining.filterd.filters;
import java.util.List;

import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.framework.plugin.PluginContext;

public class FilterdEventAttrFilter extends Filter {

	@Override
	public XLog filter(PluginContext context, XLog log, List<Parameter> parameters) {
		// TODO Auto-generated method stub this method should just contain a switch for the following 4 methods
		//that are invisible still :}
		return null;
	}
	
	public XLog filterTimestamp(PluginContext context, XLog log, List<Parameter> parameters) {
		ParameterRangeFromRange<String> range = (ParameterRangeFromRange<String>) this
				.getParameter(parameters,"range");
		
		
		XFactory factory = XFactoryRegistry.instance().currentDefault();
		XLog filteredLog = factory.createLog((XAttributeMap) log.getAttributes().clone());
		filteredLog.getClassifiers().addAll(log.getClassifiers());
		filteredLog.getExtensions().addAll(log.getExtensions());
		filteredLog.getGlobalTraceAttributes().addAll(log.getGlobalTraceAttributes());
		filteredLog.getGlobalEventAttributes().addAll(log.getGlobalEventAttributes());
		
        String lower = new String(range.getChosenPair().get(0));
		String upper = new String (range.getChosenPair().get(1));
		
		for (XTrace trace : log) {
			XTrace filteredTrace = factory.createTrace(trace.getAttributes());
			for (XEvent event : trace) {
				boolean add = false;
				for (String key : event.getAttributes().keySet()) {
					if (key.contains("time:timestamp")) {
						String time = event.getAttributes().get(key).toString();
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
			//add !parameters.isRemoveEmptyTraces() || once its added to 
			//FilterdEventAttrDateConfig
			if (!filteredTrace.isEmpty()) {
				filteredLog.add(filteredTrace);
			}
		}
		
		return filteredLog;
	}

}
