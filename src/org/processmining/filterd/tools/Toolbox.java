package org.processmining.filterd.tools;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterValueFromRange;

public class Toolbox {

	private static int id = -1; // first ID will be 0

	/**
	 * Computes the list of complex classifiers for the current log.
	 * They are computed both as a list of strings as well as a list of XEventClassifiers.
	 * @param log the log to be interrogated
	 * @return the list of names of the complex classifiers
	 */
	public static List<XEventClassifier> computeComplexClassifiers(XLog log) {
		List<XEventClassifier> classifiers = new ArrayList<>();
		XLogInfo logInfo = XLogInfoImpl.create(log);
		Collection<XEventClassifier> compatibleClassifiers = logInfo.getEventClassifiers();
		for (XEventClassifier c : compatibleClassifiers) {
			String[] usedAttributes = c.getDefiningAttributeKeys();
			if (usedAttributes.length > 1) {
				classifiers.add(c);

			}
		}

		return classifiers;
	}
	
	
	/**
	 * Computes the list of all classifiers for the current log.
	 * They are computed both as a list of strings as well as a list of XEventClassifiers.
	 * @param log the log to be interrogated
	 * @return the list of names of the complex classifiers
	 */
	public static List<XEventClassifier> computeAllClassifiers(XLog log) {
		List<XEventClassifier> classifiers = new ArrayList<>();
		XLogInfo logInfo = XLogInfoImpl.create(log);
		Collection<XEventClassifier> compatibleClassifiers = logInfo.getEventClassifiers();
		for (XEventClassifier c : compatibleClassifiers) {
			if (!classifiers.contains(c)) {
				classifiers.add(c);
			}
		}

		return classifiers;
	}
	
	/**
	 * Returns the name of the classifier when passed a classifier object
	 * 
	 * @param classifiers
	 * @return the name of the @param classifier
	 */
	public static List<String> getClassifiersName(List<XEventClassifier> classifiers){
		List<String> names = new ArrayList<>();
		for (XEventClassifier c : classifiers) {
			names.add(c.name());
		}
		return names;
	}

	/**
	 * Computes the sizes of the event classes with the lowest and 
	 * highest absolute occurrence, respectively
	 * 
	 * @param log the log to be interrogated
	 * @param classifier based on which we generate the event classes
	 * @return the size of the event classes which contain the lowest 
	 * and highest number of events, respectively
	 */
	public static List<Integer> getMaxOccurrence(XLog log, XEventClassifier classifier) {
		List<Integer> minAndMax = new ArrayList<>();
		XLogInfo logInfo = XLogInfoImpl.create(log);
		XEventClasses eventClasses = logInfo.getEventClasses(classifier);
		TreeSet<Integer> eventSizes = new TreeSet<Integer>();

		for (XEventClass event : eventClasses.getClasses()) {
			eventSizes.add(event.size());
		}
		minAndMax.add(eventSizes.first()-1);
		minAndMax.add(eventSizes.last());
		return minAndMax;
	}

	/**
	 * Computes the corresponding XEventClassifier object when given 
	 * a log and the name of a classifier
	 * 
	 * @param log
	 * @param name the name of the classifier object
	 * @return XEventClassifier object with name being @param name
	 */

	public static XEventClassifier computeClassifier(XLog log, String name) {
		XLogInfo logInfo = XLogInfoImpl.create(log);
		Collection<XEventClassifier> compatibleClassifiers = logInfo.getEventClassifiers();
		for (XEventClassifier c : compatibleClassifiers) {
			String[] usedAttributes = c.getDefiningAttributeKeys();
			if (c.name().equals(name)) {
				return c;
			}
		}
		return null;
	}


	/**
	 * This method computes which event classes should be highlighted according 
	 * to the selected percentage
	 * @param threshold
	 * @param desiredEvents
	 * @param rate
	 */
	public static List<String> computeDesiredEventsFromThreshold
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

		int value;

		if (rateChoice) {

			value = 0;
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

			value = eventSizes.last();
			
			while (value > selectedValueFromRange && eventSizes.size() > 0) {
				/*extract the class with the greatest size */
				int biggestEventClass = eventSizes.last();
				eventSizes.remove(biggestEventClass);
				if (eventSizes.size() != 0 ) {
					value = eventSizes.last();
				}
				/* mark all the event classes that have this size */
				for (XEventClass eventClass : eventClasses.getClasses()) {
					if (eventClass.size() == biggestEventClass && 
							!desirableEventClasses.contains(eventClass.toString())) {
						desirableEventClasses.add(eventClass.toString());
					}
				}
			}	

		}
		return desirableEventClasses;
	}



	/**
	 * Computes the list of global attributes of the log events
	 * @param log the log to be interrogated
	 * @return the list of names of the events global attributes
	 */
	public static List<String> computeGlobalAttributes(XLog log) {
		List<String> globalAttr = new ArrayList<>();
		if(log.getGlobalEventAttributes() != null) {
			for (XAttribute attribute : log.getGlobalEventAttributes()) {
				globalAttr.add(attribute.getKey());
			}
		}
		return globalAttr;
	}


	/**
	 * Computes the list of all attributes of all events in the log
	 * @param log the log to be interrogated
	 * @return the list of names of the events' attributes
	 */
	public static List<String> computeAttributes(XLog log) {
		XLogInfo logInfo = XLogInfoImpl.create(log);
		List<String> attributes = new ArrayList<>();
		Collection<String> attributes_collection =  logInfo.getEventAttributeInfo().getAttributeKeys();	
		attributes.addAll(attributes_collection);
		return attributes;
	}

	public static String getType(XAttribute attribute) {
		String type = attribute.getClass().getSimpleName();

		switch(type) {
			case "XAttributeLiteralImpl":
				return "Literal";
			case "XAttributeBooleanImpl":
				return "Boolean";
			case "XAttributeContinuousImpl":
				return "Continuous";
			case "XAttributeDiscreteImpl":
				return "Discrete";
			case "XAttributeIDImpl":
				return "ID";
			case "XAttributeTimestampImpl":
				return "Timestamp";
			default: return "Other";
		}
	}

	/* time format assumed to be YYYY-MM-DDThh:mm:ss.SSSZ */
	public static LocalDateTime synchronizeGMT(String time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
		// check if time has milliseconds, otherwise add it 
		if (!time.contains(".")) { 
			time = time.substring(0, 19) + ".000" + time.substring(19);
		}
		LocalDateTime date = LocalDateTime.parse(time.substring(0, 23), formatter);
		int offsetH;
		int offsetM;

		if (time.length() > 23) {
			boolean sign = false;
			if (time.charAt(23) == '+') sign = true;

			offsetH = Integer.parseInt(time.substring(24, 26));
			offsetM = Integer.parseInt(time.substring(27, 29));

			if (!sign) {
				return date.plusHours(offsetH).plusMinutes(offsetM);
			} else {
				return date.minusHours(offsetH).minusMinutes(offsetM);
			}
		}

		return date;
	}



	public static XLog initializeLog(XLog originalLog) {

		XFactory factory = XFactoryRegistry.instance().currentDefault();
		XLog filteredLog = factory.createLog((XAttributeMap) originalLog.getAttributes().clone());
		filteredLog.getClassifiers().addAll(originalLog.getClassifiers());
		filteredLog.getExtensions().addAll(originalLog.getExtensions());
		filteredLog.getGlobalTraceAttributes().addAll(originalLog.getGlobalTraceAttributes());
		filteredLog.getGlobalEventAttributes().addAll(originalLog.getGlobalEventAttributes());
		return filteredLog;
	}

	public static Map<XTrace, List<Integer>> getVariantsToTraceIndices(
			XLog log, XEventClassifier classifier
			) {


		// Need to get the defining attributes to do the variants.
		Set<String> classifyingAttributes = new HashSet<>();

		// Check variants and see if they occur within both thresholds.

		// Collect all variants:
		// Variants of traces are traces with the same sequence of events 
		// where to events are equal if they belong to the same classifier class.
		// Create mapping from variants to trace indices
		Map<XTrace, List<Integer>> variantsToTraceIndices = new HashMap<>();

		// Loop over every trace in the log
		for (int i = 0; i < log.size(); i++) {

			// Add first trace because we are certain that there is no
			// other variant in the hash map since it is empty.
			if (variantsToTraceIndices.isEmpty()) {

				// Add it to the mapping as a new entry.
				List<Integer> indicesList = new ArrayList<>();
				indicesList.add(i);

				variantsToTraceIndices.put(log.get(i), indicesList);

			} else {

				boolean newVariant = true;

				for (XTrace variant : variantsToTraceIndices.keySet()) {

					if (isSameVariant(
							log.get(i), 
							variant, 
							classifier
							)) {

						// Add the index of this trace to the mapping of 
						// the variant we found.
						variantsToTraceIndices.get(variant).add(i);

						newVariant = false;

						// stop looping over variants since we found one.
						break;
					}

				}

				// Add this trace because there are no variants pertaining
				// to this trace.
				if (newVariant) {

					// Add it to the mapping as a new entry.
					List<Integer> indicesList = new ArrayList<>();
					indicesList.add(i);

					variantsToTraceIndices.put(log.get(i), indicesList);

				}


			}


		}

		return variantsToTraceIndices;
	}

	public static boolean isSameVariant(
			XTrace firstTrace, 
			XTrace secondTrace,
			XEventClassifier classifier) {

		if (firstTrace.size() != secondTrace.size()) {
			return false;
		}


		for (int i = 0; i < firstTrace.size(); i++) {

			if (!(classifier.sameEventClass(firstTrace.get(i), secondTrace.get(i)))) {
				return false;
			}

		}


		return true;
	}

	public static boolean isSameEvent(
			XEvent firstEvent, 
			XEvent secondEvent,
			Set<String> attributes
			) {

		Set<String> firstKeys = firstEvent.getAttributes().keySet();
		Set<String> secondKeys = secondEvent.getAttributes().keySet();

		if (firstKeys.size() != secondKeys.size()) {
			return false;
		}

		for (String key : firstKeys) {

			if (!(secondKeys.contains(key))) {
				return false;
			}

			if (attributes.contains(key)) {

				String firstValue = firstEvent
						.getAttributes()
						.get(key)
						.toString();
				String secondValue = secondEvent
						.getAttributes()
						.get(key)
						.toString();

				if (!(firstValue.equals(secondValue))) {
					return false;
				}

			}
		}

		return true;
	}

	public static int getNextId() {
		id++;
		return id;
	}

	public static ArrayList<String> getDurations(XLog log) {
		
		ArrayList<Long> durations = new ArrayList<Long>();

		for (XTrace trace : log) {

			// Use first and last event to calculate the total duration of
			// the trace.
			LocalDateTime firstEventTime;
			LocalDateTime lastEventTime = null;
			
			int firstIndex = 0;
			
			while (!(trace.get(firstIndex).getAttributes().containsKey("time:timestamp")) && firstIndex < trace.size() - 2) {
				firstIndex++;
			}
			
			if (trace.get(firstIndex).getAttributes().containsKey("time:timestamp")) {
				firstEventTime = synchronizeGMT(
						trace
						.get(firstIndex)
						.getAttributes()
						.get("time:timestamp")
						.toString());
			} else {
				firstEventTime = LocalDateTime.MIN;
			}
			
			lastEventTime = computeLastEventTime(lastEventTime, firstIndex, trace, trace.size() - 1);
			
			if (firstEventTime != LocalDateTime.MIN && lastEventTime != LocalDateTime.MAX) {

				Duration traceDuration = Duration.between(firstEventTime, lastEventTime);
				long totalMillis = traceDuration.toMillis();
				
				durations.add(totalMillis);
			
			} else {
				durations.add(0l);
			}
			
			

		}
		
		Collections.sort(durations);

		return new ArrayList<String> (durations.stream().map(l -> {
			
			Calendar c = Calendar.getInstance(); 
			//Set time in milliseconds
			c.setTimeInMillis(l);
			int mYear = c.get(Calendar.YEAR) - 1970;
			int mMonth = c.get(Calendar.MONTH); 
			int mDay = c.get(Calendar.DAY_OF_MONTH) - 1;
			int hr = c.get(Calendar.HOUR);
			int min = c.get(Calendar.MINUTE);
			int sec = c.get(Calendar.SECOND);
			int millis = c.get(Calendar.MILLISECOND);
			
			String string = "";
			
			string += addToDuration(mYear, "year");
			string += addToDuration(mMonth, "month");
			string += addToDuration(mDay, "day");
			string += addToDuration(hr, "hour");
			string += addToDuration(min, "minute");
			string += addToDuration(sec, "second");
			string += addToDuration(millis, "millisecond");
			
			return string;
		}).collect(Collectors.toList()));
	}
	
	public static String addToDuration(int time, String type) {
		
		if (time == 0) {
			return "";
		} else if (time == 1) {
			return "1 " + type + " ";
		} else {
			return Integer.toString(time) + " " + type + "s ";
		}
		
	}

	public static List<Integer> getminAdnMaxEventSize(XLog log) {

		int minEventSize = Integer.MAX_VALUE;
		int maxEventSize = -Integer.MAX_VALUE;

		for (XTrace trace : log) {

			if (trace.size() < minEventSize) {
				minEventSize = trace.size();
			}

			if (trace.size() > maxEventSize) {
				maxEventSize = trace.size();
			}

		}

		return Arrays.asList(minEventSize, maxEventSize);
	}
	public static boolean satisfies(XAttributeMap attributes, String attribute_key,
			List<String> attribute_values) {
		//if the event does not have the desired attribute, return false
		if (!attributes.containsKey(attribute_key)) {
			return false;
		}
		XAttribute attr = attributes.get(attribute_key);
		// the only way to get the value consistently 
		// out of all the attribute subclasses
		String attr_value = attr.toString();

		//if one of the desired values matches the attribute value, return true
		//else return false
		for (String s : attribute_values) {
			if (attr_value.equals(s)) {
				return true;
			}
		}
		return false;
	}

	public static LocalDateTime[] getFirstAndLastTimes(XLog log) {

		LocalDateTime[] firstAndLast = new LocalDateTime[2];

		// Set initial values for comparison.
		LocalDateTime firstTimestamp = LocalDateTime.MAX;
		LocalDateTime finalTimestamp = LocalDateTime.MIN;

		// Loop over every trace to get the times of every trace.
		for (XTrace trace : log) {

			// First and last event is the start and finish time of this trace.
			// Use first and last event to calculate the total duration of
			// the trace.
			LocalDateTime firstEventTime;
			LocalDateTime lastEventTime = null;
			
			int firstIndex = 0;
			
			while (!(trace.get(firstIndex).getAttributes().containsKey("time:timestamp")) && firstIndex < trace.size() - 2) {
				firstIndex++;
			}
			
			if (trace.get(firstIndex).getAttributes().containsKey("time:timestamp")) {
				firstEventTime = synchronizeGMT(
						trace
						.get(firstIndex)
						.getAttributes()
						.get("time:timestamp")
						.toString());
			} else {
				firstEventTime = LocalDateTime.MAX;
			}
			
			lastEventTime = computeLastEventTime(lastEventTime, firstIndex, trace, firstIndex);
			
			if (firstEventTime != LocalDateTime.MAX && lastEventTime != LocalDateTime.MAX) {

				// Do comparisons to get the earliest time a trace is started.
				if (firstTimestamp.compareTo(firstEventTime) > 0) {
					firstTimestamp = firstEventTime;
				}
	
				// Do comparisons to get the latest time a trace is finished.
				if (finalTimestamp.compareTo(lastEventTime) < 0) {
					finalTimestamp = lastEventTime;
				}
			
			} else {
				finalTimestamp = firstTimestamp;
			}
		}

		firstAndLast[0] = firstTimestamp;
		firstAndLast[1] = finalTimestamp;


		return firstAndLast;
	}
	
	public static LocalDateTime computeLastEventTime(LocalDateTime lastEventTime, int firstIndex, XTrace trace, int index) {
		int lastIndex = trace.size() - 1;
		
		while (!(trace.get(firstIndex).getAttributes().containsKey("time:timestamp")) && lastIndex > firstIndex + 1) {
			lastIndex--;
		}
		
		if (trace.get(lastIndex).getAttributes().containsKey("time:timestamp")) {
			lastEventTime = synchronizeGMT(
					trace
					.get(index)
					.getAttributes()
					.get("time:timestamp")
					.toString());
		} else {
			lastEventTime = LocalDateTime.MAX;
		}
		
		return lastEventTime;
	}

}
