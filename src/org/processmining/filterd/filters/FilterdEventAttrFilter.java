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
		
		// Get the attribute that was selected.
		ParameterOneFromSet attribute = (ParameterOneFromSet) this.getParameter(parameters, "attribute");
		// Set the string representation as the key.
		key = attribute.getChosen();

		// Used to get all the event attributes.
		XLogInfo logInfo = XLogInfoImpl.create(log);

		// Loop over all event attributes.
		for (XAttribute a : logInfo.getEventAttributeInfo().getAttributes()) {
			// If the event attributes contain the key.
			if (a.getKey().equals(key)) {
				// Check the type of the attribute.
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
		
		// Filter in or out.
		boolean choice = selectionType.getChosen().equals("Filter in");
		// Keep null or not.
		boolean keepNull = traceHandling.getChosen();
		// Keep empty or not.
		boolean keepEmpty = eventHandling.getChosen();

		// Create filtered log.
		filteredLog = Toolbox.initializeLog(log);
		XFactory factory = XFactoryRegistry.instance().currentDefault();

		// Loop over all traces in the original log.
		for (XTrace trace : log) {
			// Create new trace for the filtered log.
			XTrace filteredTrace = factory.createTrace(trace.getAttributes());
			// Loop over every event in the original log.
			for (XEvent event : trace) {
				// If the event does not contain the key
				if (!event.getAttributes().containsKey(key)) {
					if (keepEmpty) filteredTrace.add(event);
					continue;
				}
				// Get the value of the key.
				String value = event.getAttributes().get(key).toString();
				boolean add = !choice;

				// If the value is contained in the desired values.
				if(desiredValues.getChosen().contains(value)) add = choice;
				
				// Add this event to the filtered trace.
				if (add) filteredTrace.add(event);
			}
			// If the filtered trace is not empty or we keep empty traces.
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
		// Get the type of parameter
		ParameterOneFromSet parameterType = (ParameterOneFromSet) this.getParameter(parameters, "parameterType");
		// Get the desired values.
		ParameterMultipleFromSet desiredValues = (ParameterMultipleFromSet) this.getParameter(parameters, "desiredValues");
		// Get the range of numerical values.
		ParameterRangeFromRange<Double> range = (ParameterRangeFromRange<Double>) this.getParameter(parameters,"range");

		// Filter in or out.
		boolean choice = selectionType.getChosen().contains("Filter in");
		// Range from range or multiple from set.
		boolean selectionChoice = parameterType.getChosen().contains("interval");
		// Keep traces or not.
		boolean keepTraces = traceHandling.getChosen();
		// Keep events or not.
		boolean keepEvent = eventHandling.getChosen();
		// Get lower value of the range slider.
		double lower = range.getChosenPair().get(0);
		// Get upper value of the range slider.
		double upper = range.getChosenPair().get(1);

		// Create filtered log.
		filteredLog = Toolbox.initializeLog(log);
		XFactory factory = XFactoryRegistry.instance().currentDefault();

		// Loop over every trace in the original log.
		for (XTrace trace : log) {
			// Create the filtered trace with the correct attributes.
			XTrace filteredTrace = factory.createTrace(trace.getAttributes());

			// Loop over each event in the original trace.
			for (XEvent event : trace) {
				boolean add = !choice;

				/* check if event can be kept if it does not have this attribute */
				if (!event.getAttributes().containsKey(key)) {
					if (keepEvent) filteredTrace.add(event);
					continue;
				}

				// Get value from the key attribute as a double.
				double value = Double.parseDouble(event.getAttributes().get(key).toString());

				/* selection type: range from range */
				if (selectionChoice) {
					if (value >= lower && value <= upper) {
						add = choice;
					}
				} else {
					/* selection type: multiple from set */
					if (desiredValues.getChosen()
							.stream()
							.anyMatch(y -> Double.parseDouble(y) == value))
						add = choice;
				}

				if (add) {
					filteredTrace.add(event);
				}

			}

			// If the filtered trace is not empty or we keep empty traces.
			if (!filteredTrace.isEmpty() || keepTraces) filteredLog.add(filteredTrace);
		}
		return filteredLog;
	}

	public XLog filterTimestamp(XLog log, List<Parameter> parameters) {		

		// Get trace handling parameter.
		ParameterYesNo traceHandling = (ParameterYesNo) this.getParameter(parameters, "traceHandling"); 
		// Get event handling parameter.
		ParameterYesNo eventHandling = (ParameterYesNo) this.getParameter(parameters, "eventHandling");

		// Get selection type parameter.
		ParameterOneFromSet selectionType = (ParameterOneFromSet) this.getParameter(parameters, "selectionType");
		// Get range slider.
		ParameterRangeFromRange<Integer> range = (ParameterRangeFromRange<Integer>) this.getParameter(parameters,"time-range");

		// Filter in or out.
		boolean choice = selectionType.getChosen().contains("Filter in");
		// Keep empty traces or not.
		boolean keepNull = traceHandling.getChosen();
		// Keep empty events or not.
		boolean keepEmpty = eventHandling.getChosen();

		// Get times of the slider, special range from range parameter.
		ArrayList<String> times = range.getTimes();

		// Create the filtered log.
		filteredLog = Toolbox.initializeLog(log);
		XFactory factory = XFactoryRegistry.instance().currentDefault();

		// Get lower value from the range slider.
		int lower = range.getChosenPair().get(0);
		// Get upper value from the range slider.
		int upper = range.getChosenPair().get(1);

		// Loop over every trace in the original log.
		for (XTrace trace : log) {
			// Create the filtered trance.
			XTrace filteredTrace = factory.createTrace(trace.getAttributes());
			// Set key accordingly.
			key = "time:timestamp";

			// Loop over every event in the original trace.
			for (XEvent event : trace) {
				boolean add = !choice;
				// If the event contains the key selected by the user.
				if (!event.getAttributes().containsKey(key)) {
					if (keepEmpty) filteredTrace.add(event);
					continue;
				}

				// Get the time value.
				String time = event.getAttributes().get(key).toString();

				// Get the position in the index.
				int pos = times.indexOf(Toolbox.synchronizeGMT(time).toString());

				// Filter based on the lower and upper values set in the slider.
				if (pos >= lower && pos <= upper) {
					add = choice;
				}

				if (add) {
					filteredTrace.add(event);
				}
			}

			// If the filtered trace is not empty or we keep empty traces.
			if (!filteredTrace.isEmpty() || keepNull) {
				filteredLog.add(filteredTrace);
			}
		}

		return filteredLog;
	}

	/**
	 * Getter for the key.
	 * @return the key selected to filter on.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Setter for the key
	 * @param key Sets the key to this value.
	 */
	public void setKey(String key) {
		this.key = key;
	}

}
