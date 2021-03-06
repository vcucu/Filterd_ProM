package org.processmining.filterd.filters;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.tools.Toolbox;

public class FilterdTracePerformanceFilter extends Filter {
	
	/**
	 * This constructor is used for import/export 
	 */
	public FilterdTracePerformanceFilter() {
		
	}
	/**
	 * Method responsible for filtering the log according to the
	 * rules defined in the filter configuration
	 */
	public XLog filter(XLog log, List<Parameter> parameters) {
		
		// clone input log, since ProM documentation says filters should not 
		// change input logs
		XLog clonedLog = (XLog) log.clone();
		
		// retrieve the chosen option parameter from the list of parameters
		ParameterOneFromSet chosenOption = 
				(ParameterOneFromSet) parameters.get(0);
	
		// switch the filtering method according to the selected value
		// the double slider will have different endpoints and values
		// to display according to this selection
		switch (chosenOption.getChosen()) {
			case "filter on duration": {
				return filterDuration(
						clonedLog, 
						(ParameterRangeFromRange<Integer>) parameters.get(1));
			}
			case "filter on number of events": {
				return filterNumberOfEvents(
						clonedLog, 
						(ParameterRangeFromRange<Integer>) parameters.get(1));
			}
		}
		
		return clonedLog;
	}
	/**
	 * 
	 * @param clonedLog the log on which the filtering is performed
	 * @param threshold the threshold according to which
	 * traces are kept or discarded
	 * @return
	 */
	public XLog filterDuration(XLog clonedLog,
			ParameterRangeFromRange<Integer> threshold
			) {
		
		ArrayList<String> times = threshold.getTimes();

		int lowPos = threshold.getChosenPair().get(0);
		int highPos = threshold.getChosenPair().get(1);
	
		Set<XTrace> removeFromLog = new HashSet<>();
			
		for (XTrace trace : clonedLog) {
			
			// Use first and last event to calculate the total duration of
			// the trace.
			String firstEventTime = trace
					.get(0)
					.getAttributes()
					.get("time:timestamp")
					.toString();
			String lastEventTime = trace
					.get(trace.size() - 1)
					.getAttributes()
					.get("time:timestamp")
					.toString();
			
			// retrieve the end points according to the information
			// from the event log 
			LocalDateTime startTime = Toolbox.synchronizeGMT(firstEventTime);
			LocalDateTime endTime = Toolbox.synchronizeGMT(lastEventTime);
			
			// compute duration between 2 timestamps
			Duration traceDuration = Duration.between(startTime, endTime);
			long totalMillis = traceDuration.toMillis();
			
			// configurations for the time
			/*Calendar c = Calendar.getInstance(); 
			//Set time in milliseconds
			c.setTimeInMillis(totalMillis);
			int mYear = c.get(Calendar.YEAR) - 1970;
			int mMonth = c.get(Calendar.MONTH); 
			int mDay = c.get(Calendar.DAY_OF_MONTH) - 1;
			int hr = c.get(Calendar.HOUR);
			int min = c.get(Calendar.MINUTE);
			int sec = c.get(Calendar.SECOND);
			int millis = c.get(Calendar.MILLISECOND);*/
			int seconds = (int) (totalMillis/1000);
			
			int mYear = (int) Math.floor(seconds / 31536000);
			int mMonth = (int) Math.floor((seconds % 31536000) / 2628000);
			int mDay = (int) Math.floor(((seconds % 31536000) % 2628000)/ 86400); 
			int hr = (int) Math.floor(((seconds % 31536000) % 86400) / 3600);
			int min = (int) Math.floor((((seconds % 31536000) % 86400) % 3600) / 60);
			int sec = (((seconds % 31536000) % 86400) % 3600) % 60;
			int millis = (int) (totalMillis - seconds*1000);
			
			String string = "";
			
			// this is done for a nicer display of the time
			// for the user
			// the UI should convert selected time to an
			// understandable representation
			string += Toolbox.addToDuration(mYear, "year");
			string += Toolbox.addToDuration(mMonth, "month");
			string += Toolbox.addToDuration(mDay, "day");
			string += Toolbox.addToDuration(hr, "hour");
			string += Toolbox.addToDuration(min, "minute");
			string += Toolbox.addToDuration(sec, "second");
			string += Toolbox.addToDuration(millis, "millisecond");
			
			int tracePosition = times.indexOf(string);
			
			// check whether trace should be kept or discared
			// from the cloned log
			if (tracePosition < lowPos || tracePosition > highPos) {
				removeFromLog.add(trace);
			}
			
		}
		
		// remove the corresponding traces 
		// from the cloned log
		clonedLog.removeAll(removeFromLog);
		
		return clonedLog;
	}
	
	/**
	 * 
	 * @param clonedLog the log on which the filtering is performed
	 * @param threshold the threshold according to which
	 *  traces are kept or discarded
	 * @return the filtered log
	 */
	public XLog filterNumberOfEvents(XLog clonedLog,
			ParameterRangeFromRange<Integer> threshold) {
		
		// Threshold contains the thresholds in number of events.
		int lowThreshold = threshold.getChosenPair().get(0);
		int highThreshold = threshold.getChosenPair().get(1);
		
		Set<XTrace> removeFromLog = new HashSet<>();
		
		for (XTrace trace : clonedLog) {
			
			// A trace contains a list of events.
			// Check if the size of this list is within thresholds set by
			// the user.
			// Otherwise, remove it.
			if (trace.size() < lowThreshold || trace.size() > highThreshold) {
				removeFromLog.add(trace);
			}
			
		}
		
		clonedLog.removeAll(removeFromLog);
		
		return clonedLog;
	}

}
