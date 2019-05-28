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
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.framework.plugin.PluginContext;

public class FilterdEventAttrFilter extends Filter {

	XLog filteredLog;
	String key;

	public FilterdEventAttrFilter() {}

	@Override
	public XLog filter(PluginContext context, XLog log, List<Parameter> parameters) {
		// TODO Auto-generated method stub this method should just contain a switch for the following 4 methods
		//that are invisible still :}
		
		System.out.println("i apply");

		ParameterOneFromSet attribute = (ParameterOneFromSet) this.getParameter(parameters, "attribute");
		key = attribute.getChosen();

		XLogInfo logInfo = XLogInfoImpl.create(log);

		for (XAttribute a : logInfo.getEventAttributeInfo().getAttributes()) {
			if (a.getKey().equals(key)) {
				switch(Toolbox.getType(a)) {
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

	public XLog filterCategorical(PluginContext context, XLog log, List<Parameter> parameters) {
		// should you remove empty traces
		ParameterYesNo traceHandling = (ParameterYesNo) this
				.getParameter(parameters, "traceHandling");
		// should you keep events which do not have the specified attribute
		ParameterYesNo eventHandling = (ParameterYesNo) this
				.getParameter(parameters, "eventHandling");
		// filter in or filter out
		ParameterOneFromSet selectionType = (ParameterOneFromSet) this
				.getParameter(parameters, "selectionType");
		ParameterMultipleFromSet desiredValues = (ParameterMultipleFromSet) this
				.getParameter(parameters, "desiredValues");

		boolean choice = selectionType.getChosen().equals("Filter in");
		boolean keepNull = traceHandling.getChosen();
		boolean keepEmpty = eventHandling.getChosen();

		filteredLog = Toolbox.initializeLog(log);
		XFactory factory = XFactoryRegistry.instance().currentDefault();

		for (XTrace trace: log) {
			XTrace filteredTrace = factory.createTrace(trace.getAttributes());
			for (XEvent event : trace) {
				if (!event.getAttributes().containsKey(key)) {
					if (keepEmpty) filteredTrace.add(event);
					continue;
				}
				String value = event.getAttributes().get(key).toString();
				boolean add = !choice;

				if(desiredValues.getChosen().contains(value)) add = choice;
			
				if (add) filteredTrace.add(event);

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

	public XLog filterNumerical(PluginContext context, XLog log, List<Parameter> parameters) {
		// should you remove empty traces
		ParameterYesNo traceHandling = (ParameterYesNo) this.getParameter(parameters, "traceHandling");
		// should you keep events which do not have the specified attribute
		ParameterYesNo eventHandling = (ParameterYesNo) this.getParameter(parameters, "eventHandling");
		// filter in or filter out
		ParameterOneFromSet selectionType = (ParameterOneFromSet) this.getParameter(parameters, "selectionType");
		ParameterOneFromSet parameterType = (ParameterOneFromSet) this.getParameter(parameters, "parameterType");
		ParameterMultipleFromSet desiredValues = (ParameterMultipleFromSet) this.getParameter(parameters, "desiredValues");
		ParameterRangeFromRange<Integer> range = (ParameterRangeFromRange<Integer>) this.getParameter(parameters,"range");

		boolean choice = selectionType.getChosen().contains("Filter in");
		boolean selectionChoice = parameterType.getChosen().contains("interval");
		boolean keepTraces = traceHandling.getChosen();
		boolean keepEvent = eventHandling.getChosen();
		int lower = range.getChosenPair().get(0);
		int upper = range.getChosenPair().get(1);

		filteredLog = Toolbox.initializeLog(log);
		XFactory factory = XFactoryRegistry.instance().currentDefault();

		for (XTrace trace : log) {
			XTrace filteredTrace = factory.createTrace(trace.getAttributes());

			for (XEvent event : trace) {
				boolean add = !choice;
				
				/* check if event can be kept if it does not have this attribute */
				if (!event.getAttributes().containsKey(key)) {
					if (keepEvent) filteredTrace.add(event);
					continue;
				}

				int value = Integer.parseInt(event.getAttributes().get(key).toString());

				/* selection type: range from range */
				if (selectionChoice) {
					if (value >= lower && value <= upper) {
						System.out.println("event with " + value);
						add = choice;
					}
				} else {
					/* selection type: multiple from set */
					if (desiredValues.getChosen().contains(Integer.toString(value))) add = choice;
				}

				if (add) filteredTrace.add(event);
				if (context != null) context.getProgress().inc();
			}

			if (!filteredTrace.isEmpty() || keepTraces) filteredLog.add(filteredTrace);
		}

		return filteredLog;
	}

	public XLog filterTimestamp(PluginContext context, XLog log, List<Parameter> parameters) {		

		ParameterYesNo traceHandling = (ParameterYesNo) this.getParameter(parameters, "traceHandling"); 
		ParameterYesNo eventHandling = (ParameterYesNo) this.getParameter(parameters, "eventHandling");

		ParameterOneFromSet selectionType = (ParameterOneFromSet) this.getParameter(parameters, "selectionType");
		ParameterRangeFromRange<String> range = (ParameterRangeFromRange<String>) this.getParameter(parameters,"range");

		boolean choice = selectionType.getChosen().contains("Filter in");
		boolean keepNull = traceHandling.getChosen();
		boolean keepEmpty = eventHandling.getChosen();


		filteredLog = Toolbox.initializeLog(log);
		XFactory factory = XFactoryRegistry.instance().currentDefault();

		LocalDateTime lower = Toolbox.synchronizeGMT(range.getChosenPair().get(0));
		LocalDateTime upper = Toolbox.synchronizeGMT(range.getChosenPair().get(1));

		for (XTrace trace : log) {
			XTrace filteredTrace = factory.createTrace(trace.getAttributes());
			key = "time:timestamp";

			for (XEvent event : trace) {
				boolean add = !choice;
				if (!event.getAttributes().containsKey(key)) {
					if (keepEmpty) filteredTrace.add(event);
					continue;
				}

				String time = event.getAttributes().get(key).toString();

				LocalDateTime date = Toolbox.synchronizeGMT(time);

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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
