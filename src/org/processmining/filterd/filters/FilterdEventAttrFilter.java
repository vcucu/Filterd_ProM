package org.processmining.filterd.filters;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.framework.plugin.PluginContext;

public class FilterdEventAttrFilter extends Filter {
	
	@Override
	public XLog filter(PluginContext context, XLog log, List<Parameter> parameters) {
		// TODO Auto-generated method stub this method should just contain a switch for the following 4 methods
		//that are invisible still :}
		return null;
	}

	public XLog filterTimestamp(PluginContext context, XLog log, List<Parameter> parameters) {

		ParameterRangeFromRange<String> range = (ParameterRangeFromRange<String>) this
				.getParameter(parameters,"range");

		XLog filteredLog = this.initializeLog(log);
		XFactory factory = XFactoryRegistry.instance().currentDefault();
		
		String lower = new String(range.getChosenPair().get(0));
		String upper = new String (range.getChosenPair().get(1));

		for (XTrace trace : log) {
			XTrace filteredTrace = factory.createTrace(trace.getAttributes());
			
			for (XEvent event : trace) {
				boolean add = false;
				Date date = addTimezone(event.getAttributes().get("time:timestamp").toString());
				String time = new String(date.toString());
				if (time.compareTo(lower) >= 0 && time.compareTo(upper) <= 0) {
					add = true;
				}

				if (add) {
					filteredTrace.add(event);
				}
				context.getProgress().inc();
			}
			//add !parameters.isRemoveEmptyTraces() || once its added to 
			//FilterdEventAttrDateConfig
			if (!filteredTrace.isEmpty()) {
				filteredLog.add(filteredTrace);
			}
		}

		return filteredLog;
	}

	private Date addTimezone (String time) {
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
