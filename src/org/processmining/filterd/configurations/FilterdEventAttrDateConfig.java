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
				if (!event.getAttributes().containsKey("time:timestamp")) continue;
				String value = event.getAttributes().get("time:timestamp").toString();
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
		range = new ParameterRangeFromRange<>("range",
				"Select timeframe", defaultPair, optionsPair, Integer.TYPE);

		// should you remove empty traces
		ParameterYesNo traceHandling = new ParameterYesNo("eventHandling", 
				"Keep empty traces.", true);

		// should you keep events which do not have the specified attribute
		ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", 
				"Keep events without value", false);

		// filter in or filter out
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType",
				"Select option for filtering", defaultOption, optionList);

		parameters.add(traceHandling);
		parameters.add(eventHandling);
		parameters.add(selectionType);
		parameters.add(range);

	}

	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};


	public boolean checkValidity(XLog log) {
		ArrayList<LocalDateTime> newTimes = new ArrayList<>();
		ArrayList<Integer> pair = new ArrayList<>();
		
		try {
			pair.addAll(range.getChosenPair());
		} catch(Exception e) {
			return true;
		}
		
		LocalDateTime lower = Toolbox.synchronizeGMT(times.get(pair.get(0)));
		LocalDateTime upper = Toolbox.synchronizeGMT(times.get(pair.get(1)));

		for (XTrace trace : log) {
			for (XEvent event : trace) {
				String key = "time:timestamp";
				LocalDateTime time = Toolbox.synchronizeGMT(event.getAttributes().get(key).toString());
				newTimes.add(time);
			}
		}

		Collections.sort(newTimes);

		/* an old date configuration is valid iff the old bounds are in the new log
		 * and there exists time:timestamp attributes */
		return times.size() != 0 &&  lower.isAfter(newTimes.get(0)) && upper.isBefore(newTimes.get(newTimes.size()-1));
	}
}
