package org.processmining.filterd.filters;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;;

public class FilterdTraceFollowerFilter extends Filter {

	public XLog filter(XLog log, List<Parameter> parameters) {
		// clone input log, since ProM documentation says filters should not 
		// change input logs
		XLog clonedLog = (XLog) log.clone(); 
		
		Set<XTrace> tracesToRemove = new HashSet<>();
		
		// Basic follower parameters.
		ParameterOneFromSet attributeSelector = 
				(ParameterOneFromSet) parameters.get(0); 
		ParameterOneFromSet selectionType = 
				(ParameterOneFromSet) parameters.get(1);
		ParameterMultipleFromSet referenceParameter = 
				(ParameterMultipleFromSet) parameters.get(2);
		ParameterMultipleFromSet followerParameter = 
				(ParameterMultipleFromSet) parameters.get(3);
		
		// Time duration parameters.
		ParameterYesNo timeRestrictionParameter = 
				(ParameterYesNo) parameters.get(4);
		ParameterOneFromSet shorterOrLongerParameter = 
				(ParameterOneFromSet) parameters.get(6);
		ParameterValueFromRange<Integer> timeDurationParameter = 
				(ParameterValueFromRange<Integer>) parameters.get(8);
		ParameterOneFromSet timeTypeParameter = 
				(ParameterOneFromSet) parameters.get(10);
		
		// Value matching parameters.
		ParameterYesNo valueMatchingParameter = 
				(ParameterYesNo) parameters.get(5);
		ParameterOneFromSet sameOrDifferentParameter = 
				(ParameterOneFromSet) parameters.get(7);
		ParameterOneFromSet valueMatchingAttributeParameter = 
				(ParameterOneFromSet) parameters.get(9);
		
		
		for (XTrace trace : clonedLog) {
			
			boolean removeFromLog = true;
			// Only used when the user has selected "never eventually followed".
			boolean foundEventualEvent = false;
			
			XEvent referenceEvent = null;
			
			for (XEvent event : trace) {
				
				if (!removeFromLog) {
					break;
				}
				
				// Find reference event.
				if (referenceEvent == null) {
				
					if (event.getAttributes().containsKey(attributeSelector.getChosen())) {
						
						String value = event.getAttributes().get(attributeSelector.getChosen()).toString();
						
						if (referenceParameter.getChosen().contains(value)) {
							referenceEvent = event;
						}
						
					}
				
				}
				
				// Find follower event.
				if (referenceEvent != null && referenceEvent != event) {
					
					switch (selectionType.getChosen()) {
						
						case "Directly followed": {
							
							if (trace.indexOf(referenceEvent) == trace.indexOf(event) - 1) {
							
								String value = event.getAttributes().get(attributeSelector.getChosen()).toString();
								
								if (followerParameter.getChosen().contains(value)) {
									removeFromLog = false;
								}
								
							}
							
							break;
						}
						case "Never directly followed": {
							
							if (trace.indexOf(referenceEvent) == trace.indexOf(event) - 1) {
								
								String value = event.getAttributes().get(attributeSelector.getChosen()).toString();
								
								if (!followerParameter.getChosen().contains(value)) {
									removeFromLog = false;
								}
								
							}
							
							break;
						}
						case "Eventually followed": {
							
							String value = event.getAttributes().get(attributeSelector.getChosen()).toString();
							
							if (followerParameter.getChosen().contains(value)) {
								removeFromLog = false;
							}
							
							break;
						}
						case "Never eventually followed": {
							
							String value = event.getAttributes().get(attributeSelector.getChosen()).toString();
							
							if (followerParameter.getChosen().contains(value)) {
								foundEventualEvent = true;
							}
							
							
							break;
						}
						
					}
					
				}

			}
			
			if (!foundEventualEvent && selectionType.getChosen().equals("Never eventually followed")) {
				removeFromLog = false;
			}
			
			if (removeFromLog) {
				tracesToRemove.add(trace);
			}
			
		}
		
		clonedLog.removeAll(tracesToRemove);
		
		return clonedLog;
	}
}
