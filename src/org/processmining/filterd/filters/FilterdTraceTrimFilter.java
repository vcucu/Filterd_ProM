package org.processmining.filterd.filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;

public class FilterdTraceTrimFilter extends Filter {

	public XLog filter(XLog log, List<Parameter> parameters) {
		XLog clonedLog = (XLog) log.clone(); 		
		
		/// Get parameters from the configuration.
		
		// Get the attribute that was selected.
		ParameterOneFromSet attributeSelector = 
				(ParameterOneFromSet) parameters.get(0);
		// Get the type of selection.
		ParameterOneFromSet selectionType = 
				(ParameterOneFromSet) parameters.get(1);
		// Get the reference parameter values.
		ParameterMultipleFromSet referenceParameter = 
				(ParameterMultipleFromSet) parameters.get(2);
		// Get the follower parameter values.
		ParameterMultipleFromSet followerParameter = 
				(ParameterMultipleFromSet) parameters.get(3);
		
		// Create list of traces to remove.
		ArrayList<XTrace> tracesToRemove = new ArrayList<>();
		// Create a mapping from traces to the list of events.
		Map<XTrace, ArrayList<XEvent>> eventsToRemove =
				new HashMap<>();
		
		// Check every trace in the log.
		for (XTrace trace : clonedLog) {
			XEvent referenceEvent = null;
		
			boolean noEnd = true;
			int eventItIndex = 0;
			
			// Go through each event sequentially.
			while (eventItIndex < trace.size()) {

				// Get the event.
				XEvent event = trace.get(eventItIndex);
				
				// If we haven't found a reference event yet.
				if (referenceEvent == null) {
				
					// If this contains the attribute that was selected by the
					// user in the GUI.
					if (event.getAttributes().containsKey(attributeSelector.getChosen())) {
						
						String value = event.getAttributes().get(attributeSelector.getChosen()).toString();
						
						// And the values for the selected attribute.
						if (referenceParameter.getChosen().contains(value)) {
							// Reference event is found!
							referenceEvent = event;
						}
						
					}		
				}
				
				// If we have found the reference event.
				if (referenceEvent != null) {
					// Switch based on the selection type.
					switch (selectionType.getChosen()) {
						case "Trim longest": {
							
							// Get the index of the event.
							int eventIndex = trace.indexOf(event);
							// Get the index of the final event in the trace.
							int endIndex = trace.size();
							boolean found = false;
							// Searching for a follower event.
							while (--endIndex >= eventIndex) {
								XEvent currentEvent = trace.get(endIndex);
								if (currentEvent.getAttributes().containsKey(attributeSelector.getChosen())) {
									String value = currentEvent.getAttributes().get(attributeSelector.getChosen()).toString();
									
									if (followerParameter.getChosen().contains(value)) {
										// Follower event found!
										found = true;
										noEnd = false;
										break;
									}
								}
							}
							// Follower event not found.
							// Remove whole trace.
							if (!found) {
								tracesToRemove.add(trace);
							}
							// Follower event found
							// Remove all the events that are outside the
							// reference and the follower event.
							else {
								
								ArrayList<XEvent> eventsToRemoveList = new ArrayList<>();
								int eventIndex2 = 0;
								// Get all events before the follower event.
								while(eventIndex2 < eventItIndex) {
									eventsToRemoveList.add(trace.get(eventIndex2));
									eventIndex2++;
								}
								// Get all events after the reference event.
								while(++endIndex < trace.size()) {
									eventsToRemoveList.add(trace.get(endIndex));
								}
								eventsToRemove.put(trace, eventsToRemoveList);
								
							}
						}
							break;
						case "Trim first": {
							// Get the index of the event.
							int eventIndex = trace.indexOf(event);
							boolean found = false;
							// Go through the rest of the events of the trace
							// in order.
							while (eventIndex < trace.size()) {
								XEvent currentEvent = trace.get(eventIndex);
								if (currentEvent.getAttributes().containsKey(attributeSelector.getChosen())) {
									String value = currentEvent.getAttributes().get(attributeSelector.getChosen()).toString();
									
									if (followerParameter.getChosen().contains(value)) {
										// Follower event found!
										found = true;
										noEnd = false;
										break;
									}
								}
								eventIndex++;
							}
							// Follower event not found.
							// Remove whole trace.
							if (!found) {
								tracesToRemove.add(trace);
							}
							// Follower event found
							// Remove all the events that are outside the
							// reference and the follower event.
							else {
								ArrayList<XEvent> eventsToRemoveList = new ArrayList<>();
								int eventIndex2 = 0;
								// Get all events before the follower event.
								while(eventIndex2 < eventItIndex) {
									eventsToRemoveList.add(trace.get(eventIndex2));
									eventIndex2++;
								}
								// Get all events after the reference event.
								while(++eventIndex < trace.size()) {
									eventsToRemoveList.add(trace.get(eventIndex));
								}
								eventsToRemove.put(trace, eventsToRemoveList);
							}
							
						}
						break;
					}
					break;
				}
				eventItIndex++;
			}
			// If we weren't able to find a reference event or there is no 
			// follower event to be found.
			if (referenceEvent == null || noEnd) {
				tracesToRemove.add(trace);
			}
			
		}
		// Loop over all traces of which events have to be removed.
		for (XTrace trace : eventsToRemove.keySet()) {
		
			// Remove all the events in that trace.
			clonedLog.get(clonedLog.indexOf(trace))
					 	.removeAll(eventsToRemove.get(trace));
			
		}
		// Remove all traces that have to be removed.
		clonedLog.removeAll(tracesToRemove);

		return clonedLog;
	}

}
