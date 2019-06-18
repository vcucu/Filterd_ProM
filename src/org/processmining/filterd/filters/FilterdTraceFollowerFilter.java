package org.processmining.filterd.filters;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
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
	ParameterOneFromSet attributeSelector;
	ParameterMultipleFromSet referenceParameter;
	ParameterMultipleFromSet followerParameter;
	ParameterYesNo timeRestrictionParameter;
	ParameterOneFromSet shorterOrLongerParameter;
	ParameterValueFromRange<Integer> timeDurationParameter;
	ParameterOneFromSet timeTypeParameter;
	ParameterYesNo valueMatchingParameter;
	ParameterOneFromSet sameOrDifferentParameter;
	ParameterOneFromSet valueMatchingAttributeParameter;

	public XLog filter(XLog log, List<Parameter> parameters) {
		// clone input log, since ProM documentation says filters should not 
		// change input logs
		XLog clonedLog = (XLog) log.clone(); 

		Set<XTrace> tracesToRemove = new HashSet<>();

		// Basic follower parameters.
		attributeSelector = 
				(ParameterOneFromSet) parameters.get(0); 
		ParameterOneFromSet selectionType = 
				(ParameterOneFromSet) parameters.get(1);
		referenceParameter = 
				(ParameterMultipleFromSet) parameters.get(2);
		followerParameter = 
				(ParameterMultipleFromSet) parameters.get(3);

		// Time duration parameters.
		timeRestrictionParameter = 
				(ParameterYesNo) parameters.get(4);
		shorterOrLongerParameter = 
				(ParameterOneFromSet) parameters.get(6);
		timeDurationParameter = 
				(ParameterValueFromRange<Integer>) parameters.get(8);
		timeTypeParameter = 
				(ParameterOneFromSet) parameters.get(10);

		// Value matching parameters.
		valueMatchingParameter = 
				(ParameterYesNo) parameters.get(5);
		sameOrDifferentParameter = 
				(ParameterOneFromSet) parameters.get(7);
		valueMatchingAttributeParameter = 
				(ParameterOneFromSet) parameters.get(9);


		/*
		 * Method for filtering traces based on the distance between
		 * reference and follower event.
		 * 
		 * If distance > 0, then both referencing and follower exist and 
		 * the referencing is for sure eventually followed.
		 * If distance == 1, then the referencing is directly followed.
		 * If distance <= 0, then the referencing is never eventually followed. 
		 * If distance == traces.size() then the referencing event does not exist.
		 * 
		 */
		for (XTrace trace : clonedLog) {
			int follower = getFollower(trace);

			
			/* Uncomment if traces which do not have a suitable referencing 
			 * event must be discarded in the 'never' cases.
			if (follower == -trace.size()) {
				tracesToRemove.add(trace);
				break;
			} */

			switch(selectionType.getChosen()) {
				case "Directly followed":
					if (follower != 1) tracesToRemove.add(trace);
					break;
				case "Never directly followed":
					if (follower == 1) tracesToRemove.add(trace);
					break;
				case "Eventually followed": 
					if (follower < 1) tracesToRemove.add(trace);
					break;
				case "Never eventually followed":
					if (follower > 0) tracesToRemove.add(trace);
					break;
			}
		}

		clonedLog.removeAll(tracesToRemove);

		return clonedLog;
	}

	/* This method return the distance between the follower event
	 * and the referencing event.  
	 */
	public int getFollower(XTrace trace) {
		/* if there is no suitable referencing event, then the difference
		 * will be -trace.size();
		 */
		int ref = trace.size(); // reference event index
		int foll = 0; // follower event index

		String key = attributeSelector.getChosen(); // attribute key
		Boolean reference = true; // still looking for reference event
		XEvent referenceEvent = null; // event object

		for (int i = 0; i < trace.size(); i++) {
			XEvent event = trace.get(i);

			// check whether the event has the chosen attribute
			if (event.getAttributes().containsKey(key)) {
				// Get value for the chosen key.
				String value = event.getAttributes().get(key).toString();

				// If looking for the reference event, check if its value is in the reference parameter.
				if (reference && referenceParameter.getChosen().contains(value)) {
					reference = false; // reference event found
					ref = i;
					referenceEvent = trace.get(ref);
				} 
				// If looking for the follower event, check if its value is in the follower parameter.
				else if (!reference && followerParameter.getChosen().contains(value)) {
					// Check whether the user imposes time restriction.
					if (timeRestrictionParameter.getChosen()) {
						// If the time restriction does not hold for these events,
						// keep looking.
						if (!timeRestrictionHolds(
								referenceEvent, 
								event, 
								shorterOrLongerParameter.getChosen().equals("Shorter"), 
								timeDurationParameter.getChosen(), 
								timeTypeParameter.getChosen())) {
							continue;
						}
					}
					// If value matching is imposed on the reference event.
					if (valueMatchingParameter.getChosen()) {
						if (!valueMatchingHolds(
								referenceEvent, 
								event, 
								sameOrDifferentParameter.getChosen().equals("The same value"), 
								valueMatchingAttributeParameter.getChosen())) {
							continue;
						}
					} 

					/* the follower has been found */
					foll = i;
					break;
				}
			}
		}

		return (foll - ref); 
	}


	public boolean timeRestrictionHolds(
			XEvent referenceEvent, 
			XEvent followerEvent, 
			boolean shouldBeShorter, 
			int duration, 
			String durationType) {

		// Get timestamps of both events.
		LocalDateTime referenceTime = Toolbox.synchronizeGMT(
				referenceEvent.getAttributes().get("time:timestamp").toString());
		LocalDateTime followerTime = Toolbox.synchronizeGMT(
				followerEvent.getAttributes().get("time:timestamp").toString());

		// Get the duration in between the events.
		Duration durationBetween = Duration.between(referenceTime, followerTime);

		// Build the duration set for the threshold.
		Duration treshold = null;

		switch (durationType) {
			case "Millis": {

				// In milliseconds.
				treshold = Duration.of(duration, ChronoUnit.MILLIS);

				break;
			}
			case "Seconds": {

				// In seconds.
				treshold = Duration.of(duration, ChronoUnit.SECONDS);

				break;
			}
			case "Minutes": {

				// In minutes.
				treshold = Duration.of(duration, ChronoUnit.MINUTES);

				break;
			}
			case "Hours": {

				// In hours.
				treshold = Duration.of(duration, ChronoUnit.HOURS);

				break;
			}
			case "Days": {

				// In days.
				Period period = Period.ofDays(duration);
				treshold = Duration.ofDays((period.getDays()));

				break;
			}
			case "Weeks": {

				// In weeks.
				Period period = Period.ofDays(duration * 7);
				treshold = Duration.ofDays((period.getDays()));

				break;
			}
			case "Years": {

				// In years.
				Period period = Period.ofDays((int)(duration * 365.2425));
				treshold = Duration.ofDays((period.getDays()));

				break;
			}
		}

		// Switch based on whether the time between the events should be shorter
		// or longer than the selected duration.
		if (shouldBeShorter) {
			return (durationBetween.compareTo(treshold) < 0);
		} else {
			return (durationBetween.compareTo(treshold) > 0);
		}
	}

	private boolean valueMatchingHolds(
			XEvent referenceEvent, 
			XEvent followerEvent, 
			boolean shouldBeSame, 
			String attributeToCompare) {

		// Check if both events contain the attribute to compare.
		if (referenceEvent.getAttributes().keySet().contains(attributeToCompare)
				&& followerEvent.getAttributes().keySet().contains(attributeToCompare)) {

			// Get both values of the reference and the follower event of the
			// attribute, the key.
			String referenceValue = referenceEvent
					.getAttributes()
					.get(attributeToCompare)
					.toString();
			String followerValue = followerEvent
					.getAttributes()
					.get(attributeToCompare)
					.toString();

			// Switch based on whether the values should be equal to each other
			// or not.
			if (shouldBeSame) {

				return referenceValue.equals(followerValue);

			} else {

				return !(referenceValue.equals(followerValue));

			}

		}

		return false;
	}

}
