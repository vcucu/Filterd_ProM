package org.processmining.filterd.configurations;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.parameters.ParameterText;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.filterd.widgets.ParameterController;
import org.processmining.filterd.widgets.ParameterMultipleFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetExtendedController;
import org.processmining.filterd.widgets.ParameterRangeFromRangeController;
import org.processmining.filterd.widgets.ParameterTextController;
import org.processmining.filterd.widgets.ParameterValueFromRangeController;
import org.processmining.filterd.widgets.ParameterYesNoController;

public class FilterdTraceFollowerConfig extends FilterdAbstractConfig {

	public FilterdTraceFollowerConfig(XLog log, Filter filterType) {
		super(log, filterType);
		
		// Initialize the configuration's parameters list.
		parameters = new ArrayList<>();
		
		// Get all the attributes in the log
		Set<String> globalAttributes = new HashSet<String>();
		
		// Do this by looping over every trace and collecting its attributes
		// and adding this to the set.
		for (XTrace trace : log) {
			
			XAttributeMap attributesMap = trace.getAttributes();
			
			for (String attribute : attributesMap.keySet()) {
				globalAttributes.add(attribute);
			}
			
		}
		
		// Convert the set into an array list because ParameterOneFromSet takes
		// a list as an argument.
		List<String> globalAttributesList = 
				new ArrayList<String>(globalAttributes);
		
		// Create the parameter for selecting the attribute.
		ParameterOneFromSet attributeSelector = 
				new ParameterOneFromSet(
						"Attribute", 
						"Select attribute", 
						globalAttributesList.get(0), 
						globalAttributesList);
		
		List<String> selectionTypeList = new ArrayList<String>();
		selectionTypeList.add("Directly followed");
		selectionTypeList.add("Never directly followed");
		selectionTypeList.add("Eventually followed");
		selectionTypeList.add("Never eventually followed");
		
		// Create the parameter for selecting the type.
		ParameterOneFromSet selectionType = new ParameterOneFromSet(
								"Selection type", 
								"Select selection type", 
								selectionTypeList.get(0), 
								selectionTypeList);
		
		ParameterYesNo valueMatchingParameter = new ParameterYesNo(
				"Value matching", 
				"Value matching", 
				false);
		
		List<String> sameOrDifferentList = new ArrayList<String>();
		sameOrDifferentList.add("The same value");
		sameOrDifferentList.add("Different values");
		
		ParameterOneFromSet sameOrDifferentParameter = new ParameterOneFromSet(
				"Same or Different value", 
				"Select same or different value", 
				sameOrDifferentList.get(0), 
				sameOrDifferentList);
		
		ParameterOneFromSet valueMatchingAttributeParameter = 
				new ParameterOneFromSet(
				"Attribute for value matching", 
				"Select attribute", 
				globalAttributesList.get(0), 
				globalAttributesList);
		
		ParameterYesNo timeRestrictionParameter = new ParameterYesNo(
				"Time restrictions", 
				"Time restrictions", 
				false);
		
		List<String> shorterOrLongerList = new ArrayList<String>();
		shorterOrLongerList.add("Shorter");
		shorterOrLongerList.add("Longer");
		
		ParameterOneFromSet shorterOrLongerParameter = new ParameterOneFromSet(
				"Shorter or longer", 
				"Select shorter or longer", 
				shorterOrLongerList.get(0), 
				shorterOrLongerList);
		
		double shortestTime = 0;
		double longestTime = 0;
		
		
		for (XTrace trace : log) {
			
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
			if (duration > longestTime) {
				longestTime = duration;
			}
			
		}
		
		//initialize the threshold options list.
		List<Double> thrOptions = new ArrayList<Double>();
				
		// Frequency is a percentage, therefore minimum value is 0 and maximum
		// value is 100.
		thrOptions.add(shortestTime);
		thrOptions.add(longestTime);
				
		// Create parameter for selecting the threshold and set the outermost
		// values as the default values.
		ParameterRangeFromRange<Double> parameterThreshold = 
				new ParameterRangeFromRange<Double>(
				"Threshold", 
				"Select threshold for duration", 
				thrOptions, 
				thrOptions
				);
		
		
		
		
		parameters.add(attributeSelector);
		parameters.add(selectionType);
		parameters.add(valueMatchingParameter);
		parameters.add(sameOrDifferentParameter);
		parameters.add(valueMatchingAttributeParameter);
		parameters.add(timeRestrictionParameter);
		parameters.add(shorterOrLongerParameter);
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

	@Override
	public FilterdAbstractConfig populate(AbstractFilterConfigPanelController abstractComponent) {
		FilterConfigPanelController component = (FilterConfigPanelController) abstractComponent;
		List<ParameterController> controllers = component.getControllers();
		for(ParameterController controller : controllers) {
			//all cases assume that the controller has a name corresponding to the parameter name
			if(controller instanceof ParameterOneFromSetExtendedController) {
				ParameterOneFromSetExtendedController casted = (ParameterOneFromSetExtendedController) controller;
				//concreteReference.populate(casted.getNestedConfigPanel());
				//this method needs to be in every referencable class
				
			} else if(controller instanceof ParameterYesNoController) {
				ParameterYesNoController casted = (ParameterYesNoController) controller;
				ParameterYesNo param = (ParameterYesNo) getParameter(controller.getName());
				param.setChosen(casted.getValue());	
				
			} else if(controller instanceof ParameterOneFromSetController) {
				ParameterOneFromSetController casted = (ParameterOneFromSetController) controller;
				ParameterOneFromSet param = (ParameterOneFromSet) getParameter(controller.getName());
				param.setChosen(casted.getValue());	
				
			} else if(controller instanceof ParameterMultipleFromSetController) {
				ParameterMultipleFromSetController casted = (ParameterMultipleFromSetController) controller;
				ParameterMultipleFromSet param = (ParameterMultipleFromSet) getParameter(controller.getName());
				param.setChosen(casted.getValue());				
				
			} else if(controller instanceof ParameterValueFromRangeController) {
				ParameterValueFromRangeController casted = (ParameterValueFromRangeController) controller;
				ParameterValueFromRange param = (ParameterValueFromRange) getParameter(controller.getName());
				param.setChosen(casted.getValue());	
				
			} else if(controller instanceof ParameterTextController) {
				ParameterTextController casted = (ParameterTextController) controller;
				ParameterText param = (ParameterText) getParameter(controller.getName());
				param.setChosen(casted.getValue());	
				
			} else if(controller instanceof ParameterRangeFromRangeController) {
				ParameterRangeFromRangeController casted = (ParameterRangeFromRangeController) controller;
				ParameterRangeFromRange param = (ParameterRangeFromRange) getParameter(controller.getName());
				param.setChosenPair(casted.getValue());	
				
			} else {
				throw new IllegalArgumentException("Unsupporrted controller type.");
			}	

		}
		return this;
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		// Can always populate it.
		return true;
	}

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController(
				"Trace Follower Configuration", 
				parameters);
	}

	public boolean checkValidity(XLog log) {
		// TODO Auto-generated method stub
		return false;
	}
}
