package org.processmining.filterd.filters;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.filterd.tools.Toolbox;;

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
		
		
		/*
		 * Method for filtering traces based on the follower filter:
		 * 1: Find the reference event. This is the first event in the trace
		 * that has the value for the key selected by the user.
		 * 2: Switch over the cases based on which selection type was chosen:
		 * - "Directly followed"
		 * - "Never directly followed"
		 * - "Eventually followed"
		 * - "Never eventually followed"
		 * And check the events after the reference event that hold for this
		 * selection.
		 * 2: Check the events in the trace to find a reference event. The 
		 * reference event needs to have the values for the key that was 
		 * selected for the reference event to have. Also, if any restrictions 
		 * were imposed on the reference event such as time or value matching, 
		 * these must also hold.
		 */
		for (XTrace trace : clonedLog) {
			
			// Boolean that is changed when a follower event is found that holds
			// even with the restrictions.
			boolean removeFromLog = true;
			// Only used when the user has selected "never eventually followed".
			boolean foundEventualEvent = false;
			
			XEvent referenceEvent = null;
			
			for (XEvent event : trace) {
				
				// We already found a reference event, move to the next trace.
				if (!removeFromLog) {
					break;
				}
				
				// Find reference event.
				if (referenceEvent == null) {
				
					// If key exists in event,
					if (event.getAttributes().containsKey(attributeSelector.getChosen())) {
						// Get value for the chosen key.
						String value = event.getAttributes().get(attributeSelector.getChosen()).toString();
						
						// Check if it is present in the reference parameter.
						if (referenceParameter.getChosen().contains(value)) {
							// Reference event found!
							referenceEvent = event;
						}
						
					}
				
				}
				
				// Find follower event.
				if (referenceEvent != null && referenceEvent != event) {
					
					// Switch based on the selection types mentioned above.
					switch (selectionType.getChosen()) {
						
						case "Directly followed": {
							
							// If this is the event directly following the reference event
							if (trace.indexOf(referenceEvent) == trace.indexOf(event) - 1) {
							
								// Get the value for the selected attribute key.
								String value = event.getAttributes().get(attributeSelector.getChosen()).toString();
								
								// Check if it is present in the follower parameter. 
								if (followerParameter.getChosen().contains(value)) {
									// If time restriction is imposed on the reference event.
									if (timeRestrictionParameter.getChosen()) {
										if (timeRestrictionHolds(
												referenceEvent, 
												event, 
												shorterOrLongerParameter.getChosen().equals("Shorter"), 
												timeDurationParameter.getChosen(), 
												timeTypeParameter.getChosen())) {
											// If value matching is imposed on the reference event.
											if (valueMatchingParameter.getChosen()) {
												if (valueMatchingHolds(
														referenceEvent, 
														event, 
														sameOrDifferentParameter.getChosen().equals("The same value"), 
														valueMatchingAttributeParameter.getChosen())) {
													// Time and value matching restriction imposed.
													// Follower event found!
													removeFromLog = false;
												}
											} else {
												// Time restriction imposed.
												// Follower event found!
												removeFromLog = false;
											}
										}
									}
									// If value matching is imposed on the reference event.
									else if (valueMatchingParameter.getChosen()) {
										if (valueMatchingHolds(
												referenceEvent, 
												event, 
												sameOrDifferentParameter.getChosen().equals("The same value"), 
												valueMatchingAttributeParameter.getChosen())) {
											// Value matching restriction imposed.
											// Follower event found!
											removeFromLog = false;
										}
									} else {
										// No time or value matching restrictions imposed.
										// Follower event found!
										removeFromLog = false;
									}
								}
								
							}
							
							break;
						}
						case "Never directly followed": {
							
							// If this is the event directly following the reference event.
							if (trace.indexOf(referenceEvent) == trace.indexOf(event) - 1) {
								
								// Get the value for the selected attribute key.
								String value = event.getAttributes().get(attributeSelector.getChosen()).toString();
								
								// Check if it is not present in the follower parameter.
								if (!followerParameter.getChosen().contains(value)) {
									// If time restriction is imposed on the reference event.
									if (timeRestrictionParameter.getChosen()) {
										if (timeRestrictionHolds(
												referenceEvent, 
												event, 
												shorterOrLongerParameter.getChosen().equals("Shorter"), 
												timeDurationParameter.getChosen(), 
												timeTypeParameter.getChosen())) {
											// If value matching is imposed on the reference event.
											if (valueMatchingParameter.getChosen()) {
												if (valueMatchingHolds(
														referenceEvent, 
														event, 
														sameOrDifferentParameter.getChosen().equals("The same value"), 
														valueMatchingAttributeParameter.getChosen())) {
													// Time and value matching restriction imposed.
													// Follower event found!
													removeFromLog = false;
												}
											} else {
												// Time restriction imposed.
												// Follower event found!
												removeFromLog = false;
											}
										}
									}
									// If value matching is imposed on the reference event.
									else if (valueMatchingParameter.getChosen()) {
										if (valueMatchingHolds(
												referenceEvent, 
												event, 
												sameOrDifferentParameter.getChosen().equals("The same value"), 
												valueMatchingAttributeParameter.getChosen())) {
											// Value matching restriction imposed.
											// Follower event found!
											removeFromLog = false;
										}
									} else {
										// No time or value matching restrictions imposed.
										// Follower event found!
										removeFromLog = false;
									}
								}
								
							}
							
							break;
						}
						case "Eventually followed": {
							
							String value = event.getAttributes().get(attributeSelector.getChosen()).toString();
							
							if (followerParameter.getChosen().contains(value)) {
								if (timeRestrictionParameter.getChosen()) {
									if (timeRestrictionHolds(
											referenceEvent, 
											event, 
											shorterOrLongerParameter.getChosen().equals("Shorter"), 
											timeDurationParameter.getChosen(), 
											timeTypeParameter.getChosen())) {
										if (valueMatchingParameter.getChosen()) {
											if (valueMatchingHolds(
													referenceEvent, 
													event, 
													sameOrDifferentParameter.getChosen().equals("The same value"), 
													valueMatchingAttributeParameter.getChosen())) {
												removeFromLog = false;
											}
										} else {
											removeFromLog = false;
										}
									}
								} else if (valueMatchingParameter.getChosen()) {
									if (valueMatchingHolds(
											referenceEvent, 
											event, 
											sameOrDifferentParameter.getChosen().equals("The same value"), 
											valueMatchingAttributeParameter.getChosen())) {
										removeFromLog = false;
									}
								} else {
									removeFromLog = false;
								}
							}
							
							break;
						}
						case "Never eventually followed": {
							
							String value = event.getAttributes().get(attributeSelector.getChosen()).toString();
							
							if (followerParameter.getChosen().contains(value)) {
								if (timeRestrictionParameter.getChosen()) {
									if (timeRestrictionHolds(
											referenceEvent, 
											event, 
											shorterOrLongerParameter.getChosen().equals("Shorter"), 
											timeDurationParameter.getChosen(), 
											timeTypeParameter.getChosen())) {
										if (valueMatchingParameter.getChosen()) {
											if (valueMatchingHolds(
													referenceEvent, 
													event, 
													sameOrDifferentParameter.getChosen().equals("The same value"), 
													valueMatchingAttributeParameter.getChosen())) {
												foundEventualEvent = true;
											}
										} else {
											foundEventualEvent = true;
										}
									}
								} else if (valueMatchingParameter.getChosen()) {
									if (valueMatchingHolds(
											referenceEvent, 
											event, 
											sameOrDifferentParameter.getChosen().equals("The same value"), 
											valueMatchingAttributeParameter.getChosen())) {
										removeFromLog = false;
									}
								} else {
									foundEventualEvent = true;
								}
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
	
	
	public boolean timeRestrictionHolds(
			XEvent referenceEvent, 
			XEvent followerEvent, 
			boolean shouldBeShorter, 
			int duration, 
			String durationType) {
		
		
		LocalDateTime referenceTime = Toolbox.synchronizeGMT(
				referenceEvent.getAttributes().get("time:timestamp").toString());
		LocalDateTime followerTime = Toolbox.synchronizeGMT(
				followerEvent.getAttributes().get("time:timestamp").toString());
		
		Duration durationBetween = Duration.between(referenceTime, followerTime);
		
		Duration durationThreshold;
		
		switch (durationType) {
			case "Millis": {
				
				durationThreshold = Duration.of(duration, ChronoUnit.MILLIS);
				
				break;
			}
			case "Seconds": {
				
				durationThreshold = Duration.of(duration, ChronoUnit.SECONDS);
				
				break;
			}
			case "Minutes": {
				
				durationThreshold = Duration.of(duration, ChronoUnit.MINUTES);
				
				break;
			}
			case "Hours": {
				
				durationThreshold = Duration.of(duration, ChronoUnit.HOURS);
				
				break;
			}
			case "Days": {
				
				durationThreshold = Duration.of(duration, ChronoUnit.DAYS);
				
				break;
			}
			case "Weeks": {
				
				durationThreshold = Duration.of(duration, ChronoUnit.WEEKS);
				
				break;
			}
			case "Years": {
				
				durationThreshold = Duration.of(duration, ChronoUnit.MILLIS);
				
				break;
			}
			default: {
				return false;
			}
		}
		
		
		if (shouldBeShorter) {
			return (durationBetween.compareTo(durationThreshold) == -1);
		} else {
			return (durationBetween.compareTo(durationThreshold) == 1);
		}
	}
	
	private boolean valueMatchingHolds(
			XEvent referenceEvent, 
			XEvent followerEvent, 
			boolean shouldBeSame, 
			String attributeToCompare) {
		
		
		if (referenceEvent.getAttributes().keySet().contains(attributeToCompare)
				&& followerEvent.getAttributes().keySet().contains(attributeToCompare)) {
			
			String referenceValue = referenceEvent
					.getAttributes()
					.get(attributeToCompare)
					.toString();
			String followerValue = followerEvent
					.getAttributes()
					.get(attributeToCompare)
					.toString();
			
			if (shouldBeSame) {
				
				return referenceValue.equals(followerValue);
				
			} else {
				
				return !(referenceValue.equals(followerValue));
				
			}
			
		}
		
		return false;
	}
	
}
