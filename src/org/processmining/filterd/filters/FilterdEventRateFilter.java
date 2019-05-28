package org.processmining.filterd.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.framework.plugin.PluginContext;

public class FilterdEventRateFilter extends Filter {

	XLog filteredLog;
	
	
	public XLog filter(PluginContext context, XLog log, List<Parameter> parameters) {
		
		/*create the event classes according to the classifier */	
		XLogInfo logInfo = XLogInfoImpl.create(log, XLogInfoImpl.STANDARD_CLASSIFIER);
		XEventClasses eventClasses = logInfo.getEventClasses();	
		
		
		// frequency or occurrence
		ParameterOneFromSet rate = (ParameterOneFromSet) this
				.getParameter(parameters, "rate");
		// filter in/ filter out
		ParameterOneFromSet selectionType = (ParameterOneFromSet)this
				.getParameter(parameters, "selectionType");
		//which values correspond to the selected threshold
		ParameterMultipleFromSet desiredEvents = (ParameterMultipleFromSet)this
				.getParameter(parameters, "desiredEvents");
		//selected threshold
		ParameterValueFromRange<Integer> threshold = (ParameterValueFromRange<Integer>)this
				.getParameter(parameters,"threshold");
		
		desiredEvents.setChosen(this.computeDesiredEventsFromThreshold(threshold, rate, eventClasses));
		
		//initialize the log that will be output
		filteredLog = Toolbox.initializeLog(log);
		XFactory factory = XFactoryRegistry.instance().currentDefault();
		
			
		boolean choice = selectionType.getChosen().equals("Filter in");
		List<String> chosenClasses = desiredEvents.getChosen();		
		
		for (XTrace trace: log) {
			/*create a new trace which stores events to be kept from the initial trace*/
			XTrace filteredTrace = factory.createTrace(trace.getAttributes());
			
			for (XEvent event: trace) {
				boolean add = !choice;
				XEventClass c = eventClasses.getClassOf(event);
				
				
				/* check if the class of this event is in the list of selected classes */
				if (chosenClasses.contains(c.toString())) {
					add = choice;
				}
				
				if (add) {
					filteredTrace.add(event);
				}
			}
			
			if (!filteredTrace.isEmpty()) {
				filteredLog.add(filteredTrace);
			}	
		}
		
		return filteredLog;
		
		
	}
	
	/**
	 * This method computes which event classes should be highlighted according 
	 * to the selected percentage
	 * @param threshold
	 * @param desiredEvents
	 * @param rate
	 */
	public List<String> computeDesiredEventsFromThreshold
	(ParameterValueFromRange<Integer> threshold, ParameterOneFromSet rate,
			XEventClasses eventClasses) {
		
		boolean rateChoice = rate.getChosen().equals("Frequency");
		List<String> desirableEventClasses = new ArrayList<>();
		
		
		int selectedValueFromRange = threshold.getChosen();
		int size = 0;
		/*sort eventClasses according to their size, from smallest to biggest*/
		TreeSet<Integer> eventSizes = new TreeSet<Integer>();
		
		for (XEventClass event : eventClasses.getClasses()) {
			size += event.size();
			eventSizes.add(event.size());
		}
		
		int value = 0;
		
		if (rateChoice) {
			
			int aux_threshold = size * selectedValueFromRange/100;
			
			while (value < aux_threshold) {
				/* extract the class with the greatest value */
				int biggestEventClass = eventSizes.last();
				eventSizes.remove(biggestEventClass);
				
				/* mark all the event classes that have this size */
				for (XEventClass eventClass : eventClasses.getClasses()) {
					if (eventClass.size() == biggestEventClass) {
						value += biggestEventClass;
						desirableEventClasses.add(eventClass.toString());
					}
				}
			}
			
		} else {
				
			while (value < selectedValueFromRange) {
				/*extract the class with the greatest size */
				int biggestEventClass = eventSizes.last();
				eventSizes.remove(biggestEventClass);
				
				/* mark all the event classes that have this size */
				for (XEventClass eventClass : eventClasses.getClasses()) {
					if (eventClass.size() == biggestEventClass) {
						value += biggestEventClass;
						desirableEventClasses.add(eventClass.toString());
					}
				}
			}		
			
		}
		
		return desirableEventClasses;
	}

}
