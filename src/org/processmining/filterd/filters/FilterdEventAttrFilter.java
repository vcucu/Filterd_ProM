package org.processmining.filterd.filters;
import java.time.LocalDateTime;
import java.util.List;

import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.framework.plugin.PluginContext;

public class FilterdEventAttrFilter extends Filter {

	XLog filteredLog;
	Toolbox toolbox = Toolbox.getInstance();

	public FilterdEventAttrFilter() {}

	@Override
	public XLog filter(PluginContext context, XLog log, List<Parameter> parameters) {
		// TODO Auto-generated method stub this method should just contain a switch for the following 4 methods
		//that are invisible still :}
		ParameterOneFromSet attribute = (ParameterOneFromSet) this.getParameter(parameters, "attribute");
		String key = attribute.getChosen();
		
		XLogInfo logInfo = XLogInfoImpl.create(log);
		
		for (XAttribute a : logInfo.getEventAttributeInfo().getAttributes()) {
			if (a.equals(key)) {
				switch(toolbox.getType(a)) {
					case "Literal":
						return filterCategorical(context, log, parameters);
					case "Boolean":
						return filterCategorical(context, log, parameters);
					case "Continuous":
						return filterNumerical(context, log, parameters);
					case "Discrete":
						return filterNumerical(context, log, parameters);
					case "ID":
						return filterCategorical(context, log, parameters);
					case "Timestamp":
						return filterTimestamp(context, log, parameters);
					default: return filterCategorical(context, log, parameters);	
				}
			}
		}

		return null;
	}

	public XLog filterCategorical(PluginContext context, XLog log, List<Parameter> parameters) {return null;}

	public XLog filterNumerical(PluginContext context, XLog log, List<Parameter> parameters) {return null;}

	public XLog filterTimestamp(PluginContext context, XLog log, List<Parameter> parameters) {		
		ParameterYesNo nullHandling = new ParameterYesNo("nullHandling", 
				"Remove if no value provided", true);

		ParameterYesNo emptyHandling = new ParameterYesNo("emptyHandling", 
				"Remove if no value provided", true);

		ParameterOneFromSet selectionType = (ParameterOneFromSet) this
				.getParameter(parameters, "selectionType");

		ParameterRangeFromRange<String> range = (ParameterRangeFromRange<String>) this
				.getParameter(parameters,"range");

		boolean choice = selectionType.getChosen().contains("Filter in");
		boolean keepNull = nullHandling.getChosen();
		boolean keepEmpty = emptyHandling.getChosen();

		filteredLog = this.initializeLog(log);
		XFactory factory = XFactoryRegistry.instance().currentDefault();

		LocalDateTime lower = toolbox.synchronizeGMT(range.getChosenPair().get(0));
		LocalDateTime upper = toolbox.synchronizeGMT(range.getChosenPair().get(1));

		for (XTrace trace : log) {
			XTrace filteredTrace = factory.createTrace(trace.getAttributes());
			String key = "time:timestamp";

			for (XEvent event : trace) {
				boolean add = !choice;
				if (!event.getAttributes().containsKey(key)) {
					if (keepEmpty) filteredTrace.add(event);
					continue;
				}

				String time = event.getAttributes().get(key).toString();

				// check if time has miliseconds, otherwise add it 
				if (!time.contains(".")) time = time.substring(0, 19) + ".000" + time.substring(19);

				LocalDateTime date = toolbox.synchronizeGMT(time);

				if (date.isAfter(lower) && date.isBefore(upper)) {
					add = choice;
				}

				if (add) {
					filteredTrace.add(event);
				}

				if (context != null) {
					context.getProgress().inc();
				}
			}

			if (!filteredTrace.isEmpty() || keepNull) {
				filteredLog.add(filteredTrace);
			}
		}

		return filteredLog;
	}
}
