package org.processmining.filterd.configurations;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;

public class FilterdTraceAttrTimeframeConfig extends FilterdAbstractConfig {

	public FilterdTraceAttrTimeframeConfig(XLog log, Filter filterType) {
		super(log, filterType);
		
		// Initialize the configuration's parameters list.
		parameters = new ArrayList<>();
		

		// Create the array list for selecting the type of attribute the user
		// has selected.
		List<String> keepTracesList = new ArrayList<String>();
		keepTracesList.add("Contained in timeframe");		
		keepTracesList.add("Intersecting timeframe");
		keepTracesList.add("Started in timeframe");
		keepTracesList.add("Completed in timeframe");
		keepTracesList.add("Trim to timeframe");
		
		// Create the parameter for selecting the type of attribute.
		ParameterOneFromSet attributeTypeSelector = 
				new ParameterOneFromSet(
						"keep traces", 
						"Select trace options based on time frame", 
						keepTracesList.get(0), 
						keepTracesList);
		
		//initialize the threshold options parameter and add it to the parameters list
		List<Double> thrOptions = new ArrayList<Double>();
		
		// Use first and last event to get the time stamps of the trace.
		XEvent firstEvent = null; 
		XEvent lastEvent = null;
		
		long firstTimeStampMillis = Long.MAX_VALUE;
		long secondTimeStampMillis = Long.MIN_VALUE;
		
		for (XTrace trace : log) {
			
			firstEvent = trace.get(0);
			lastEvent = trace.get(trace.size());
			
			long tempFirstTimeStampMillis = getTimeStamp(firstEvent).getTime();
			long tempSecondTimeStampMillis = getTimeStamp(lastEvent).getTime();
			
			if (tempFirstTimeStampMillis < firstTimeStampMillis) {
				firstTimeStampMillis = tempFirstTimeStampMillis;
			}
			
			if (tempSecondTimeStampMillis > secondTimeStampMillis) {
				secondTimeStampMillis = tempSecondTimeStampMillis;
			}
			
		}
		
		//since the default option is "frequency", it goes from 1% to 100%
		thrOptions.add((double) firstTimeStampMillis);
		thrOptions.add((double) secondTimeStampMillis);
		
		ParameterRangeFromRange<Double> parameterThreshold = 
				new ParameterRangeFromRange<Double>(
				"threshold", 
				"Select threshold for timeframe", 
				thrOptions, 
				thrOptions
				);
		
		parameters.add(attributeTypeSelector);
		parameters.add(parameterThreshold);		
	}
	
	private Date getTimeStamp(XEvent event) {
		
		// Initialize time stamp.
		String timeStamp = "";
		
		// Get the time stamp of this event.
		if (event.getAttributes().get("time:timestamp") != null) {
			timeStamp = event.getAttributes().get("time:timestamp").toString();
		}

		/*
		 * Time stamps are in this format in ProM:
		 * "yyyy-MM-ddTHH:mm:ss.SSSZ"
		 * yyyy: Corresponds to the year e.g. ("1900", "2019").
		 * MM: Corresponds to the month e.g. ("01", "02", "12").
		 * dd: Corresponds to the day e.g. ("01", "10", "30").
		 *
		 * HH: Corresponds to the hours e.g. ("00", "01", "23").
		 * mm: Corresponds to the minutes e.g. ("00, "01", "59").
		 * ss: Corresponds to the seconds e.g. ("00, "01", "59").
		 * SSS: Corresponds to the milliseconds e.g. ("000, "001", "999").
		 * Z: Corresponds to the time zone:
		 * 	if "Z" 
		 * 		Z: GMT
		 * 	else
		 * 		Z: Corresponds to the time zone relative to GMT e.g. 
		 * 		   ("+01:30", "-02:00")  
		 */
		
		return fixTimeZone(timeStamp);
	}
	
	private Date fixTimeZone(String time) {
		
		// Set time format for the time stamp
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd-HH:mm:ss.SSS");
		
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
					time.length() - 1);
			
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
			time.replace("T", "-");
			
			// Get whether it was later or earlier relative to GMT.
			char stringSign = time.charAt(time.length() - 3);
			
			// Remove The relative time as we already have it separated.
			time = time.substring(0, time.length() - 7);

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

	public boolean canPopulate(FilterConfigPanelController component) {
		// Can always populate since outer options are the first and final time
		// of the log
		return true;
	}

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController
				(
				"Timeframe Trace Attribute Configuration", 
				parameters
				);
	}

	public boolean checkValidity(XLog log) {
		// TODO Auto-generated method stub
		return false;
	}

	public XLog filter() {
		// TODO Auto-generated method stub
		return null;
	}

}
