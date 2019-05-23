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
import org.processmining.framework.plugin.PluginContext;

public class FilterdEventRateFilter extends Filter {

	XLog filteredLog;
	
	public XLog filter(PluginContext context, XLog log, List<Parameter> parameters) {
		// filter in/ filter out
		ParameterOneFromSet selectionType = (ParameterOneFromSet)this
				.getParameter(parameters, "selectionType");
		//which values correspond to the selected threshold
		ParameterMultipleFromSet desiredEvents = (ParameterMultipleFromSet)this
				.getParameter(parameters, "desiredEvents");
		
		//initialize the log that will be output
		filteredLog = this.initializeLog(log);
		XFactory factory = XFactoryRegistry.instance().currentDefault();
		//create the event classes according to the classifier
		// THE standard classifier IS HARDCODED because I could not pass the classifier 
		//from the configuration to the filter method.
		XLogInfo logInfo = XLogInfoImpl.create(log, XLogInfoImpl.STANDARD_CLASSIFIER);
		XEventClasses eventClasses = logInfo.getEventClasses();	
		
		if (selectionType.getChosen().equals("Filter in")) {  //selectionType is "Filter in"
			for (XTrace trace : log) {
				// create a new trace which stores events to be kept from the initial trace
				XTrace filteredTrace = factory.createTrace(trace.getAttributes());
				for (XEvent event : trace) {
					XEventClass c = eventClasses.getClassOf(event);
					// if the class of this event is in the list of selected classes
					// then keep it
					if(desiredEvents.getChosen().contains(c.toString())) {
						filteredTrace.add(event);
					}
				}
				//if the filtered trace is not empty, we add it to the filtered log
				if (!filteredTrace.isEmpty()) {
					filteredLog.add(filteredTrace);
				}		
			}
		} else { //selectionType is "Filter out"
			for (XTrace trace : log) {
				// create a new trace which stores events to be kept from the initial trace
				XTrace filteredTrace = factory.createTrace(trace.getAttributes());
				for (XEvent event : trace) {
					XEventClass c = eventClasses.getClassOf(event);
					// if the class of this event is NOT in the list of selected classes
					// then keep it
					if(!desiredEvents.getChosen().contains(c.toString())) {
						filteredTrace.add(event);
					}
				}
				//if the filtered trace is not empty, we add it to the filtered log
				if (!filteredTrace.isEmpty()) {
					filteredLog.add(filteredTrace);
				}		
			}
		}							
		return filteredLog;
	}

}
