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
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;

public class FilterdTraceAttrTimeframeConfig extends FilterdAbstractReferenceableConfig {

	public FilterdTraceAttrTimeframeConfig(XLog log, Filter filterType) {
		super(log, filterType);
		
		// Initialize the configuration's parameters list.
		parameters = new ArrayList<>();
		

		// Create the array list for selecting the method with which the traces
		// are to be based on the time frame.
		List<String> keepTracesList = new ArrayList<String>();
		keepTracesList.add("Contained in timeframe");		
		keepTracesList.add("Intersecting timeframe");
		keepTracesList.add("Started in timeframe");
		keepTracesList.add("Completed in timeframe");
		keepTracesList.add("Trim to timeframe");
		
		// Create the parameter for selecting the method with which the traces
		// are to be based on the time frame.
		ParameterOneFromSet keepTracesParameter = 
				new ParameterOneFromSet(
						"keep traces", 
						"Select trace options based on time frame", 
						keepTracesList.get(0), 
						keepTracesList);
		
		//initialize the threshold options list.
		List<Double> thrOptions = new ArrayList<Double>();
		
		// Calculate the first and last times of the log.
		double firstAndLast[] = getFirstAndLastTimes(log);
		
		// Add the outermost values to the list
		thrOptions.add(firstAndLast[0]);
		thrOptions.add(firstAndLast[1]);
		
		// Create parameter for selecting the threshold and set the outermost
		// values as the default values.
		ParameterRangeFromRange<Double> parameterThreshold = 
				new ParameterRangeFromRange<Double>(
				"Threshold", 
				"Select threshold for timeframe", 
				thrOptions, 
				thrOptions
				);
		
		// Add both parameters to the list.
		parameters.add(keepTracesParameter);
		parameters.add(parameterThreshold);		
	}
	
	private double[] getFirstAndLastTimes(XLog log) {
		
		double[] firstAndLast = new double[2];
		
		// Use first and last event to get the time stamps of the trace.
		XEvent firstEvent = null; 
		XEvent lastEvent = null;
		
		// Set initial values for comparison.
		long firstTimeStampMillis = Long.MAX_VALUE;
		long secondTimeStampMillis = Long.MIN_VALUE;
		
		// Loop over every trace to get the times of every trace.
		for (XTrace trace : log) {
			
			// First and last event is the start and finish time of this trace.
			firstEvent = trace.get(0);
			lastEvent = trace.get(trace.size());
			
			// Get time in milliseconds for easier comparisons.
			long tempFirstTimeStampMillis = getTimeStamp(firstEvent).getTime();
			long tempSecondTimeStampMillis = getTimeStamp(lastEvent).getTime();
			
			// Do comparisons to get the earliest time a trace is started.
			if (tempFirstTimeStampMillis < firstTimeStampMillis) {
				firstTimeStampMillis = tempFirstTimeStampMillis;
			}
			
			// Do comparisons to get the latest time a trace is finished.
			if (tempSecondTimeStampMillis > secondTimeStampMillis) {
				secondTimeStampMillis = tempSecondTimeStampMillis;
			}
			
		}
		
		// Set the found values in the array.
		firstAndLast[0] = firstTimeStampMillis;
		firstAndLast[1] = secondTimeStampMillis;
			
				
		return firstAndLast;
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

	public boolean checkValidity(XLog log) {
		
		// Check if the set thresholds are still within the times of the chosen
		// log.
		
		// Get the first and last time of the chosen log.
		double firstAndLast[] = getFirstAndLastTimes(log);
		
		// Get the threshold parameter.
		ParameterRangeFromRange<Double> setThreshold = 
				(ParameterRangeFromRange<Double>) parameters.get(1);
		
		List<Double> chosenValues = setThreshold.getChosenPair();
		
		// Check if the values chosen are within the times of the chosen log.
		if (chosenValues.get(0) >= firstAndLast[0]
				&& chosenValues.get(1) <= firstAndLast[1]) {
			return true;
		} else {
			return false;
		}
	}

}
