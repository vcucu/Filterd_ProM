package org.processmining.filterd.configurations;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.filterd.tools.Toolbox;

public class FilterdEventAttrDateConfig extends FilterdAbstractReferenceableConfig{

	private ArrayList<String> times; 
	private ArrayList<Integer> defaultPair;
	private ArrayList<Integer> optionsPair;
	String defaultOption;
	ArrayList<String> optionList;
	ParameterRangeFromRange<Integer> range;
	String key = "time:timestamp";

	public FilterdEventAttrDateConfig(XLog log, Filter filterType) {
		super(log, filterType);
		parameters = new ArrayList<Parameter>();

		times = new ArrayList<>();
		defaultPair = new ArrayList<>();
		optionsPair = new ArrayList<>();
		defaultOption = new String("Filter in");
		optionList = new ArrayList<>();
		optionList.add(defaultOption);
		optionList.add("Filter out");

		/*populate the array times with the ordered date&time of all events */
		for (XTrace trace: log) {
			for (XEvent event : trace) {
				/* timestamp format YYYY-MM-DDTHH:MM:SS.ssssGMT with GMT = {Z, + , -} */
				if (!event.getAttributes().containsKey(key)) continue;
				String value = event.getAttributes().get(key).toString();
				LocalDateTime time = Toolbox.synchronizeGMT(value);
				times.add(time.toString());
			}
		}

		/* sort the timestamps in ascending order */
		Collections.sort(times);

		defaultPair.add(0);
		defaultPair.add(times.size()-1);
		optionsPair.add(0);
		optionsPair.add(times.size()-1);

		// slider values parameter
		range = new ParameterRangeFromRange<>("time-range",
				"Select timeframe", defaultPair, optionsPair, Integer.TYPE);
		range.setTimes(times);

		// should you remove empty traces
		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", 
				"Keep empty traces.", true);

		// should you keep events which do not have the specified attribute
		ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", 
				"Keep events without value", false);

		// filter in or filter out
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType",
				"Select option for filtering", defaultOption, optionList);

		parameters.add(selectionType);
		parameters.add(range);
		parameters.add(traceHandling);
		parameters.add(eventHandling);

	}

	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};


	public boolean checkValidity(XLog log) {
		ArrayList<Integer> pair = new ArrayList<>();
		
		/* check if the parameters are populated */
		try {
			pair.addAll(range.getChosenPair());
		} catch(Exception e) {
			return true;
		}
		
		/* get the chosen timestamps */
		String lower = times.get(pair.get(0));
		String upper = times.get(pair.get(1));
		Boolean hasTime = false;

		/* check if each event of the log is in those timebounds */
		for (XTrace trace : log) {
			for (XEvent event : trace) {
				if (!event.getAttributes().containsKey(key)) continue;
				hasTime = true;
				String time = Toolbox.synchronizeGMT(event.getAttributes().get(key).toString()).toString();
				if (lower.compareTo(time) <= 0 && upper.compareTo(time) >= 0) return true;
			}
		}

		/* an old date configuration is valid iff the old bounds are in the new log
		 * and there exists time:timestamp attributes */
		return hasTime;
	}
}
