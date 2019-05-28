package org.processmining.filterd.filters;

import java.util.List;

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

public class FilterdEventRateFilter extends Filter {

	XLog filteredLog;
	
	
	public XLog filter(XLog log, List<Parameter> parameters) {
		
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
		
		/*
		 * TODO: remove the line if you do not want the desired  
		 * values generated from the threshold, but just extracted from the
		 * configuration panel
		 * Note: if the tests fail after this, manually add the desired event classes
		 * to the desiredEvents parameter.
		 */
		//desiredEvents.setChosen(Toolbox.computeDesiredEventsFromThreshold(threshold, rate, eventClasses));
		
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
	
}
