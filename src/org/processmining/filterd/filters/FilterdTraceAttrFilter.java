package org.processmining.filterd.filters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.framework.plugin.PluginContext;

public class FilterdTraceAttrFilter extends Filter {

	public XLog filter(PluginContext context, 
			XLog log, 
			List<Parameter> parameters) {
						
		// clone input log, since ProM documentation says filters should not 
		// change input logs
		XLog clonedLog = (XLog) log.clone();
		
		
		/*
		 * 1st parameter: Select attribute to filter on.
		 * 2nd parameter: Select type of attribute.
		 * 
		 * Consecutive parameters are based on the attribute type.
		 * 
		 * - Categorical:
		 * 	3rd parameter, Null handling:
		 * 	Remove the trace if an event does not have this attribute.
		 * 	If Null handling == true
		 * 		Then Remove the trace.
		 * 	Else
		 * 		Then Continue.
		 * 	4th parameter, selection type:
		 * 	Choice out of 2 options:
		 * 	- "Mandatory": Mandatory to have the selection the user made for the
		 * 	chosen attribute.
		 * 	- "Forbidden": Forbidden to have the selection the user made for the
		 * 	chosen attribute.
		 * 	5th parameter, attribute:
		 * 	What attribute the user has selected to filter on.
		 * 	6th parameter, desired values:
		 * 	The values the selected attribute can take.
		 * 
		 * - Numerical:
		 * 	3rd parameter, range:
		 * 	The range selected by the user.
		 * 	4th parameter, selection type:
		 * 	Choice out of 2 options:
		 * 	- "Mandatory": Mandatory to have the selection the user made for the
		 * 	chosen attribute.
		 * 	- "Forbidden": Forbidden to have the selection the user made for the
		 * 	chosen attribute.
		 * 	5th parameter, attribute
		 * 	What attribute the user has selected to filter on.
		 * 
		 * - Time frame:
		 * 	3rd parameter, keep traces options:
		 * 	How the traces are to be filtered with the threshold set.
		 * 	4th parameter, threshold:
		 * 	The time frame selected by the user.
		 * 
		 * - NumberOfEvents:
		 * 	3rd parameter, threshold:
		 * 	The threshold set by the user.
		 * 
		 * - Duration:
		 * 	3rd parameter, threshold:
		 * 	The threshold set by the user.
		 * 
		 */
		
		ParameterOneFromSet attrType = (ParameterOneFromSet) parameters.get(1);
		String attrValue = attrType.getChosen();
		
		switch (attrValue) {
			case "Categorical": {
				clonedLog = filterCategorical(clonedLog, 
						(ParameterYesNo) parameters.get(2), 
						(ParameterOneFromSet) parameters.get(3), 
						(ParameterOneFromSet) parameters.get(4), 
						(ParameterMultipleFromSet) parameters.get(5));
				break;
			}
			case "Numerical": {
				clonedLog = filterNumerical(clonedLog, 
						(ParameterRangeFromRange<Double>) parameters.get(2), 
						(ParameterOneFromSet) parameters.get(3), 
						(ParameterOneFromSet) parameters.get(4));
				break;
			}
			case "Timeframe": {
				clonedLog = filterTimeframe(clonedLog, 
						(ParameterOneFromSet) parameters.get(2), 
						(ParameterRangeFromRange<Double>) parameters.get(3));
				break;
			}
			case "Duration": {
				clonedLog = filterDuration(clonedLog, 
						(ParameterRangeFromRange<Double>) parameters.get(2));
				break;
			}
			case "Number of events": {
				clonedLog = filterDuration(clonedLog, 
						(ParameterRangeFromRange<Double>) parameters.get(2));
				break;
			}
		}
		
		return clonedLog;
	}
	
	public XLog filterCategorical(XLog clonedLog, 
			ParameterYesNo nullHandling,
			ParameterOneFromSet selectionType, 
			ParameterOneFromSet attribute,
			ParameterMultipleFromSet desiredValues) {
		
		//for each trace in the log, first assume that the trace should not
		//be removed
		for(XTrace trace : clonedLog) {
			boolean ok = true;
			
			//for each event in the trace check if its attribute has one of the
			//chosen values
			for(XEvent event : trace) {
				XAttributeMap eventAttributes = event.getAttributes();
				//if the attribute value is null and we don't want to handle
				//null values, just move on to the next event
				
				if (!nullHandling.getChosen() && 
						eventAttributes.get(attribute.getChosen()) == null) {
					continue;
				}
				else {
					
					
					if (selectionType.getChosen().equals("Mandatory")) {
						
						/*
						 *if the attribute value of one event
						 *is not one of the selected values,
						 *then the whole trace should be removed 
						 *and we don't care
						 *about the rest of its events
						 *
						 */
					if (!satisfies(eventAttributes, attribute.getChosen(),
							desiredValues.getChosen())) {
						ok = false;
						break;
						}
					}
					else {
						
						/*
						 *if the attribute value of one event
						 *is one of the selected values,
						 *then the whole trace should be removed 
						 *and we don't care
						 *about the rest of its events
						 *
						 */
					
						if (satisfies(eventAttributes, attribute.getChosen(),
								desiredValues.getChosen())) {
							ok = false;
							break;
						}
					}
				}
			//remove the trace if it's not okay
			if (!ok) {
				clonedLog.remove(trace);
				}
			}
		}
		return clonedLog;
		
	}
	
	public boolean satisfies(XAttributeMap attributes, String attribute_key,
			List<String> attribute_values) {
		//if the event does not have the desired attribute, return false
		if (!attributes.containsKey(attribute_key)) {
			return false;
		}
		XAttribute attr = attributes.get(attribute_key);
		// the only way to get the value consistently 
		// out of all the attribute subclasses
		String attr_value = attr.toString();
		
		//if one of the desired values matches the attribute value, return true
		//else return false
		for (String s : attribute_values) {
			if (attr_value.equals(s)) {
				return true;
			}
		}
		return false;
	}
	
	public XLog filterNumerical(XLog clonedLog, 
			ParameterRangeFromRange<Double> range,
			ParameterOneFromSet selectionType, 
			ParameterOneFromSet attribute) {
			
			//for each trace in the log, first assume that the trace should not
			//be removed
			for (XTrace trace : clonedLog) {
				boolean ok = true;
				
				//for each event in the trace check if their attribute values are  
				//in the range
				for (XEvent event : trace) {
					XAttributeMap eventAttributes = event.getAttributes();

					/*
					 * if the value of the event is outside the range and
					 * it is mandatory for it to be inside the range,
					 * make ok false
					 */
					if (selectionType.getChosen().equals("Mandatory")) {
						if (!(Long.parseLong(eventAttributes.get(attribute.getChosen())
								.toString()) 
								> range.getChosenPair().get(0) &&
						Long.parseLong(eventAttributes.get(attribute.getChosen())
								.toString()) 
								< range.getChosenPair().get(1))){
							ok = false;
							break;
						}

					}
					/*
					 * if the value of the event is inside the range and
					 * it is forbidden for it to be inside the range,
					 * make ok false
					 */
					else {
						if (Long.parseLong(eventAttributes.get(attribute.getChosen())
								.toString()) 
								> range.getChosenPair().get(0) &&
						Long.parseLong(eventAttributes.get(attribute.getChosen())
								.toString()) 
								< range.getChosenPair().get(1)){
							ok = false;
							break;
						}
					}
					
				}
				//remove the trace if it's not okay
				if (!ok) {
					clonedLog.remove(trace);
				}
			}
			return clonedLog;
			
	}
	
	public XLog filterTimeframe(XLog clonedLog,
			ParameterOneFromSet keepTracesOptions,
			ParameterRangeFromRange<Double> threshold) {
		
		// Threshold contains the thresholds in milliseconds.
		double lowThreshold = threshold.getChosenPair().get(0);
		double highThreshold = threshold.getChosenPair().get(1);
		
		
		for (XTrace trace : clonedLog) {
			
			// Use first and last event to get the time stamps of the trace.
			XEvent firstEvent = trace.get(0);
			XEvent lastEvent = trace.get(trace.size());
			
			long firstTimeStampMillis = getTimeStamp(firstEvent).getTime();
			long secondTimeStampMillis = getTimeStamp(lastEvent).getTime();
			
			/* Base the filtering on the parameter chosen:
			 * 
			 * - "Contained in timeframe":
			 * Keep the traces that are contained in the time frame.
			 * - "Intersecting timeframe":
			 * Keep the traces that intersect with the time frame.
			 * - "Started in timeframe":
			 * Keep the traces that were started in the time frame.
			 * - "Completed in timeframe":
			 * Keep the traces that were completed in the time frame.
			 * - "Trim to timeframe":
			 * Trim all the traces such that all the events are contained in the
			 * time frame. Remove the traces that end up empty.
			 */
			switch (keepTracesOptions.getChosen()) {
				
				case "Contained in timeframe": {
					
					// If the trace is not contained, remove it.
					if (firstTimeStampMillis < lowThreshold
							|| secondTimeStampMillis > highThreshold) {
						clonedLog.remove(trace);
					}
					
					break;
				}
				case "Intersecting timeframe": {
					
					// If the trace is not intersecting, remove it.
					if (!(firstTimeStampMillis <= highThreshold
							&& secondTimeStampMillis >= lowThreshold)) {
						clonedLog.remove(trace);
					}
					
					break;
				}
				case "Started in timeframe": {
					
					// If the trace is not started in the time frame, remove it.
					if (firstTimeStampMillis < lowThreshold
							|| firstTimeStampMillis > highThreshold) {
						clonedLog.remove(trace);
					}
					
					break;
				}
				case "Completed in timeframe": {
					
					// If the trace is not completed in the time frame, 
					// remove it.
					if (!(secondTimeStampMillis <= highThreshold
							&& secondTimeStampMillis >= highThreshold)) {
						clonedLog.remove(trace);
					}
					
					break;
				}
				case "Trim to timeframe": {
					
					// Check the time stamp of every event
					for (XEvent event : trace) {
						
						long timeStamp = getTimeStamp(event).getTime();
						
						// If the event is not contained in the time frame,
						// remove it.
						if (!(timeStamp >= firstTimeStampMillis
								&& timeStamp <= secondTimeStampMillis)) {
							
							trace.remove(event);
							
						}
						
					}
					
					// If the trace ended up empty, remove it.
					if (trace.isEmpty()) {
						clonedLog.remove(trace);
					}

					break;
				}
				
			}
			
			
		}
		
		
		
		return clonedLog;
	}
	
	public XLog filterDuration(XLog clonedLog,
			ParameterRangeFromRange<Double> threshold) {
		
		// Threshold contains the thresholds in milliseconds.
		double lowThreshold = threshold.getChosenPair().get(0);
		double highThreshold = threshold.getChosenPair().get(1);
		
		
		for (XTrace trace : clonedLog) {
			
			// Use first and last event to calculate the total duration of
			// the trace.
			XEvent firstEvent = trace.get(0);
			XEvent lastEvent = trace.get(trace.size());
			
			Date firstTimeStamp = getTimeStamp(firstEvent);
			Date secondTimeStamp = getTimeStamp(lastEvent);
			
			// Duration = final time stamp - first time stamp.
			long duration = secondTimeStamp.getTime()
					- firstTimeStamp.getTime();
			
			// See if if is within thresholds set by the user.
			// Otherwise, remove it.
			if (duration < lowThreshold || duration > highThreshold) {
				clonedLog.remove(trace);
			}
			
		}
		
		return clonedLog;
	}
	
	public XLog filterNumberOfEvents(XLog clonedLog,
			ParameterRangeFromRange<Double> threshold) {

		// Threshold contains the thresholds in number of events.
		double lowThreshold = threshold.getChosenPair().get(0);
		double highThreshold = threshold.getChosenPair().get(1);
		
		for (XTrace trace : clonedLog) {
			
			// A trace contains a list of events.
			// Check if the size of this list is within thresholds set by
			// the user.
			// Otherwise, remove it.
			if (trace.size() < lowThreshold 
					|| trace.size() > highThreshold) {
				clonedLog.remove(trace);
			}
			
		}
		
		return clonedLog;
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

}
