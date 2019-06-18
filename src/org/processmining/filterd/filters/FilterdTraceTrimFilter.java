package org.processmining.filterd.filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;

public class FilterdTraceTrimFilter extends Filter {

	public XLog filter(XLog log, List<Parameter> parameters) {
		XLog clonedLog = (XLog) log.clone(); 		
		
		
		ParameterOneFromSet attributeSelector = 
				(ParameterOneFromSet) parameters.get(0); 
		ParameterOneFromSet selectionType = 
				(ParameterOneFromSet) parameters.get(1);
		ParameterMultipleFromSet referenceParameter = 
				(ParameterMultipleFromSet) parameters.get(2);
		ParameterMultipleFromSet followerParameter = 
				(ParameterMultipleFromSet) parameters.get(3);
		
		System.out.println(clonedLog.get(0).size());
		System.out.println(attributeSelector.getChosen());
		System.out.println(selectionType.getChosen());
		System.out.println(referenceParameter.getChosen());
		System.out.println(followerParameter.getChosen());
		
		ArrayList<XTrace> tracesToRemove = new ArrayList<>();
		Map<XTrace, ArrayList<XEvent>> eventsToRemove =
				new HashMap<>();
		for (XTrace trace : clonedLog) {
			XEvent referenceEvent = null;
		
			boolean noEnd = true;
			int eventItIndex = 0;
			while (eventItIndex < trace.size()) {
//			for (XEvent event : trace) {
			XEvent event = trace.get(eventItIndex);
			if (referenceEvent == null) {
			
				if (event.getAttributes().containsKey(attributeSelector.getChosen())) {
					
					String value = event.getAttributes().get(attributeSelector.getChosen()).toString();
					
					if (referenceParameter.getChosen().contains(value)) {
						referenceEvent = event;
					}
					
				}		
			}
			if (referenceEvent != null) {
				switch (selectionType.getChosen()) {
				case "Trim longest": {
					
					int eventIndex = trace.indexOf(event);
					int endIndex = trace.size();
					boolean found = false;
					while (--endIndex >= eventIndex) {
						XEvent currentEvent = trace.get(endIndex);
						if (currentEvent.getAttributes().containsKey(attributeSelector.getChosen())) {
							String value = currentEvent.getAttributes().get(attributeSelector.getChosen()).toString();
							
							if (followerParameter.getChosen().contains(value)) {
								found = true;
								noEnd = false;
								break;
							}
						}
					}
					if (!found) {
						tracesToRemove.add(trace);
					}
					else {
						
						ArrayList<XEvent> eventsToRemoveList = new ArrayList<>();
						int eventIndex2 = 0;
						while(eventIndex2 < eventItIndex) {
							eventsToRemoveList.add(trace.get(eventIndex2));
							eventIndex2++;
						}
						while(++endIndex < trace.size()) {
							eventsToRemoveList.add(trace.get(endIndex));
						}
						eventsToRemove.put(trace, eventsToRemoveList);
						
					}
				}
					break;
				case "Trim first": {
					int eventIndex = trace.indexOf(event);
					boolean found = false;
					while (eventIndex < trace.size()) {
						XEvent currentEvent = trace.get(eventIndex);
						if (currentEvent.getAttributes().containsKey(attributeSelector.getChosen())) {
							String value = currentEvent.getAttributes().get(attributeSelector.getChosen()).toString();
							
							if (followerParameter.getChosen().contains(value)) {
								found = true;
								noEnd = false;
								break;
							}
						}
						eventIndex++;
					}
					if (!found) {
						tracesToRemove.add(trace);
					}
					else {
						ArrayList<XEvent> eventsToRemoveList = new ArrayList<>();
						int eventIndex2 = 0;
						System.out.println("eventIndex2 = " + eventIndex2);
						while(eventIndex2 < eventItIndex) {
							eventsToRemoveList.add(trace.get(eventIndex2));
							eventIndex2++;
						}
						System.out.println("eventIndex2 = " + eventIndex2);
						System.out.println("eventIndex = " + eventIndex);
						while(++eventIndex < trace.size()) {
							eventsToRemoveList.add(trace.get(eventIndex));
						}
						System.out.println("eventIndex = " + eventIndex);
						eventsToRemove.put(trace, eventsToRemoveList);
						System.out.println(eventsToRemove.get(trace).size());
					}
					
				}
				break;
				}
				break;
			}
			eventItIndex++;
			}
			if (referenceEvent == null || noEnd) {
				tracesToRemove.add(trace);
			}
			
		}
		for (XTrace trace : eventsToRemove.keySet()) {
		
			clonedLog.get(clonedLog.indexOf(trace))
					 	.removeAll(eventsToRemove.get(trace));
			
		}
		clonedLog.removeAll(tracesToRemove);
		
		System.out.println(clonedLog.get(0).size());
		return clonedLog;
	}

}
