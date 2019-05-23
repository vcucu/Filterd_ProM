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

public class FilterdEventAttrDateConfig extends FilterdAbstractConfig{

	private ArrayList<String> times; 
	private ArrayList<String> defaultPair;
	private ArrayList<String> optionsPair;
	String defaultOption;
	ArrayList<String> optionList;
	ParameterRangeFromRange<String> range;

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
				String value = event.getAttributes().get("time:timestamp").toString();
				LocalDateTime time = synchronizeGMT(value);
				times.add(time.toString());
			}
		}

		/* sort the timestamps in ascending order */
		Collections.sort(times);

		defaultPair.add(new String(times.get(0)));
		defaultPair.add(new String(times.get(times.size()-1)));
		optionsPair.add(new String(times.get(0)));
		optionsPair.add(new String(times.get(times.size()-1)));

		// slider values parameter
		range = new ParameterRangeFromRange<>("range",
				"Select timeframe", defaultPair, optionsPair);

		// should you remove empty traces
		ParameterYesNo nullHandling = new ParameterYesNo("nullHandling", 
				"Remove empty traces.", true);

		// should you keep events which do not have the specified attribute
		ParameterYesNo emptyHandling = new ParameterYesNo("emptyHandling", 
				"Keep events if attribute not specified.", false);

		// filter in or filter out
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType",
				"Select option for filtering", defaultOption, optionList);

		parameters.add(nullHandling);
		parameters.add(emptyHandling);
		parameters.add(selectionType);
		parameters.add(range);

	}

	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController("Date Event Attribute Configuration", parameters, this);
	}

	public boolean checkValidity(XLog log) {
		ArrayList<LocalDateTime> times = new ArrayList<>();
		LocalDateTime lower = synchronizeGMT(range.getChosenPair().get(0));
		LocalDateTime upper = synchronizeGMT(range.getChosenPair().get(1));

		for (XTrace trace : log) {
			for (XEvent event : trace) {
				String key = "time:timestamp";
				LocalDateTime time = synchronizeGMT(event.getAttributes().get(key).toString());
				times.add(time);
			}
		}

		Collections.sort(times);

		/* an old date configuration is valid iff the old bounds are in the new log
		 * and there exists time:timestamp attributes */
		return times.size() != 0 &&  lower.isAfter(times.get(0)) && upper.isBefore(times.get(times.size()-1));
	}

	public XLog filter() {
		// TODO Auto-generated method stub
		return null;
	}


	/* time format assumed to be YYYY-MM-DDThh:mm:ss.SSSZ */
	private LocalDateTime synchronizeGMT(String time) {
		LocalDateTime date = LocalDateTime.parse(time.substring(0, 23));
		int offsetH;
		int offsetM;
		
		if (time.length() > 23) {
			boolean sign = false;
			if (time.charAt(23) == '+') sign = true;
			
			offsetH = Integer.parseInt(time.substring(24, 26));
			offsetM = Integer.parseInt(time.substring(27, 29));
			
			if (sign) {
				return date.plusHours(offsetH).plusMinutes(offsetM);
			} else {
				return date.minusHours(offsetH).minusMinutes(offsetM);
			}
		}

		return date;
	}
}
