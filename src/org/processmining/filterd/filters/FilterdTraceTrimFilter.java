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
		
		ArrayList<XTrace> tracesToRemove = new ArrayList<>();
		Map<XTrace, ArrayList<XEvent>> eventsToRemove =
				new HashMap<>();
		for (XTrace trace : log) {
			XEvent referenceEvent = null;
		
			boolean noEnd = true;
			for (XEvent event : trace) {
			// Find reference event.
			if (referenceEvent == null) {
			
				if (event.getAttributes().containsKey(attributeSelector.getChosen())) {
					
					String value = event.getAttributes().get(attributeSelector.getChosen()).toString();
					
					if (referenceParameter.getChosen().contains(value)) {
						referenceEvent = event;
					}
					
				}		
			}
			if (referenceEvent != null && referenceEvent != event) {
				switch (selectionType.getChosen()) {
				case "Trim longest": {
					
					int eventIndex = trace.indexOf(event);
					int endIndex = trace.size();
					boolean found = false;
					while (--endIndex >= eventIndex) {
						if (event.getAttributes().containsKey(attributeSelector.getChosen())) {
							String value = event.getAttributes().get(attributeSelector.getChosen()).toString();
							
							if (followerParameter.getChosen().contains(value)) {
								found = true;
								break;
							}
						}
					}
					if (!found) {
						tracesToRemove.add(trace);
					}
					else {
						
						ArrayList<XEvent> eventsToRemoveList = new ArrayList<>();
						for (XEvent itEvent : trace) {
							if (itEvent == referenceEvent) {
								break;
							}
							else {
								eventsToRemoveList.add(itEvent);
							}
						}
						while(++eventIndex < trace.size()) {
							eventsToRemoveList.add(trace.get(eventIndex));
						}
						eventsToRemove.put(trace, eventsToRemoveList);
						
					}
				}
					
				case "Trim first": {
					int eventIndex = trace.indexOf(event);
					boolean found = false;
					while (eventIndex < trace.size()) {
						if (event.getAttributes().containsKey(attributeSelector.getChosen())) {
							String value = event.getAttributes().get(attributeSelector.getChosen()).toString();
							
							if (followerParameter.getChosen().contains(value)) {
								found = true;
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
						for (XEvent itEvent : trace) {
							if (itEvent == referenceEvent) {
								break;
							}
							else {
								eventsToRemoveList.add(itEvent);
							}
						}
						while(++eventIndex < trace.size()) {
							eventsToRemoveList.add(trace.get(eventIndex));
						}
						eventsToRemove.put(trace, eventsToRemoveList);
					}
					
				}
				}
				break;
			}
			}
			if (referenceEvent == null || noEnd) {
				tracesToRemove.add(trace);
			}
		}
		clonedLog.removeAll(tracesToRemove);
		for (XTrace trace : eventsToRemove.keySet()) {
			clonedLog.get(clonedLog.indexOf(trace))
					 	.removeAll(eventsToRemove.get(trace));
		}
		
		return clonedLog;
	}

}
