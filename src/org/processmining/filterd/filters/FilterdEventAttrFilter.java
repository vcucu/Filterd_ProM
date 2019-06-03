package org.processmining.filterd.filters;
import java.util.ArrayList;
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

public class FilterdEventAttrFilter extends Filter {

	XLog filteredLog;
	String key;

	public FilterdEventAttrFilter() {}

	@Override
	public XLog filter(XLog log, List<Parameter> parameters) {
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
						return filterCategorical(log, parameters);
					case "Boolean":
						return filterCategorical(log, parameters);
					case "Continuous":
						return filterNumerical(log, parameters);
					case "Discrete":
						return filterNumerical(log, parameters);
					case "ID":
						return filterCategorical(log, parameters);
					case "Timestamp":
						return filterTimestamp(log, parameters);
					default: return filterCategorical(log, parameters);	
				}
			}
		}

		return null;
	}

	public XLog filterCategorical(XLog log, List<Parameter> parameters) {
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
			}
			if (!filteredTrace.isEmpty() || keepNull) {
				filteredLog.add(filteredTrace);
			}
		}

		return filteredLog;
	}

	public XLog filterNumerical(XLog log, List<Parameter> parameters) {
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
						add = choice;
					}
				} else {
					/* selection type: multiple from set */
					if (desiredValues.getChosen().contains(Integer.toString(value)))
						add = choice;
				}
				
				if (add) {
					filteredTrace.add(event);
				}
				
			}

			if (!filteredTrace.isEmpty() || keepTraces) filteredLog.add(filteredTrace);
		}
		return filteredLog;
	}

	public XLog filterTimestamp(XLog log, List<Parameter> parameters) {		

		ParameterYesNo traceHandling = (ParameterYesNo) this.getParameter(parameters, "traceHandling"); 
		ParameterYesNo eventHandling = (ParameterYesNo) this.getParameter(parameters, "eventHandling");

		ParameterOneFromSet selectionType = (ParameterOneFromSet) this.getParameter(parameters, "selectionType");
		ParameterRangeFromRange<Integer> range = (ParameterRangeFromRange<Integer>) this.getParameter(parameters,"time-range");

		boolean choice = selectionType.getChosen().contains("Filter in");
		boolean keepNull = traceHandling.getChosen();
		boolean keepEmpty = eventHandling.getChosen();
		
		ArrayList<String> times = range.getTimes();

		filteredLog = Toolbox.initializeLog(log);
		XFactory factory = XFactoryRegistry.instance().currentDefault();

		int lower = range.getChosenPair().get(0);
		int upper = range.getChosenPair().get(1);

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

				int pos = times.indexOf(Toolbox.synchronizeGMT(time).toString());

				if (pos >= lower && pos <= upper) {
					add = choice;
				}

				if (add) {
					filteredTrace.add(event);
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
