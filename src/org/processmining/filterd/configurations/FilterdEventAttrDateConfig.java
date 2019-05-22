package org.processmining.filterd.configurations;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

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
				String time = event.getAttributes().get("time:timestamp").toString();
				Date date = addTimezone(time);
				times.add(date.toString());
			}
		}

		/* sort the timestamps in ascending order */
		Collections.sort(times);
		
		defaultPair.add(new String(times.get(0)));
		defaultPair.add(new String(times.get(times.size()-1)));
		optionsPair.add(new String(times.get(0)));
		optionsPair.add(new String(times.get(times.size()-1)));

		//create slider values parameter
		range = new ParameterRangeFromRange<>("range",
				"Select timeframe", defaultPair, optionsPair);


		//Create nullHandling parameter
		ParameterYesNo nullHandling = new ParameterYesNo("nullHandling", 
				"Remove if no value provided", true);
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType",
				"Select option for trimming",defaultOption,optionList);

		parameters.add(nullHandling);
		parameters.add(selectionType);
		parameters.add(range);

	}

	public boolean canPopulate(FilterConfigPanelController component) {
		// TODO Auto-generated method stub
		return false;
	}

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController("Date Event Attribute Configuration", parameters);
	}

	public boolean checkValidity(XLog log) {
		
		ArrayList<String> times = new ArrayList<>();
		
		for (XTrace trace : log) {
			for (XEvent event : trace) {
				for (String key : event.getAttributes().keySet()) {
					if (key.contains("time:timestamp")) {
						Date date = addTimezone(event.getAttributes().get(key).toString());
						String time = new String(date.toString());
						times.add(time);
					}
				}	
			}
		}
		
		Collections.sort(times);
		
		//if XLog has no time:timestamp attributes
		if(times.size()==0) {
			return false;
		}
		
		if(range.getChosenPair().get(0).compareTo(times.get(0)) < 0
				|| range.getChosenPair().get(1).compareTo(times.get(times.size()-1)) > 0) {
			return false;
		}
		
		return true;
	}

	public XLog filter() {
		// TODO Auto-generated method stub
		return null;
	}
	

	private Date addTimezone (String time) {
		// Set time format for the time stamp
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd-HH:mm:ss");

		Date date = null;

		// Time is in GMT
		if (time.contains("Z")) {
			time.replace("Z", "");

			try {
				date = dateFormat.parse(time);	
			} catch (ParseException e) {
				// Print the trace so we know what went wrong.
				e.printStackTrace();
			}
		}
		// Time is relative to GMT
		else {

			// Represents the last 5 characters e.g. "02:00".
			String lastFiveCharacters = time.substring(time.length() - 5, 
					time.length());
			System.out.println(lastFiveCharacters);

			// Set time format for the hours relative to GMT.
			SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm");
			Date hourDate = null;

			// Parse the hours into a Date.
			try {
				hourDate = hourFormat.parse(lastFiveCharacters);	
			} catch (ParseException e) {
				// Print the trace so we know what went wrong.
				e.printStackTrace();
			}

			// Replace the T-separator with a colon.
			time = time.replace("T", "-");

			// Get whether it was later or earlier relative to GMT.
			char stringSign = time.charAt(time.length() - 6);
			System.out.println(stringSign);
			
			// Remove The relative time as we already have it separated.
			time = time.substring(0, time.length() - 6);

			// Parse the time stamp into a Date.
			try {
				date = dateFormat.parse(time);	
			} catch (ParseException e) {
				// Print the trace so we know what went wrong.
				e.printStackTrace();
			}

			// Change the relative time to GMT
			if (stringSign == '+') {
				date.setTime(date.getTime() - hourDate.getTime());
			} else {
				date.setTime(date.getTime() + hourDate.getTime());
			}
		}

		return date;
	}


}
