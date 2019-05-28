package org.processmining.filterd.tools;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	public static List<String> getClassifiersName(List<XEventClassifier> classifiers){
		List<String> names = new ArrayList<>();
		for (XEventClassifier c : classifiers) {
			names.add(c.name());
		}
		return names;
	}

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
		LocalDateTime date = LocalDateTime.parse(time.substring(0, 23));
		int offsetH;
		int offsetM;
		
		if (time.length() > 23) {
			boolean sign = false;
			if (time.charAt(23) == '+') sign = true;
			
			offsetH = Integer.parseInt(time.substring(24, 26));
			offsetM = Integer.parseInt(time.substring(27, 29));
			
			if (sign) {
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
			XLog log
			) {
		
		
		// Need to get the defining attributes to do the variants.
		Set<String> classifyingAttributes = new HashSet<>();
		
		// Get the classifiers from the log.
		List<XEventClassifier> classifiers = log.getClassifiers();
		
		// Loop over every classifier. 
		for (XEventClassifier classifier : classifiers) {
			
			// Extract every key and add it to the set.
			for (String key : classifier.getDefiningAttributeKeys()) {
				classifyingAttributes.add(key);
			}
			
		}
		
		// Check variants and see if they occur within both thresholds.
		
		// Collect all variants:
		// Variants of traces are traces with the same order of events and
		// values for every key except for the time stamp.
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
				
				boolean isDifferentVariant = true;
				
				for (XTrace variant : variantsToTraceIndices.keySet()) {
					
					if (isSameVariant(
							log.get(i), 
							variant, 
							classifyingAttributes
							)) {
						
						// Add the index of this trace to the mapping of 
						// the variant we found.
						variantsToTraceIndices.get(variant).add(i);
						
						isDifferentVariant = false;
						
						// stop looping over variants since we found one.
						break;
					}
					
				}
				
				// Add this trace because there are no variants pertaining
				// to this trace.
				if (isDifferentVariant) {
					
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
			Set<String> attributes
			) {
		
		if (firstTrace.size() != secondTrace.size()) {
			return false;
		}
		
		
		for (int i = 0; i < firstTrace.size(); i++) {
			
			if (!(isSameEvent(
					firstTrace.get(i), 
					secondTrace.get(i), 
					attributes)
					)) {
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
	
public static List<Double> getMinAnMaxDuration(XLog log) {
		
		double minDuration = Double.MAX_VALUE;
		double maxDuration = -Double.MAX_VALUE;
		
		for (XTrace trace : log) {
			
			// Use first and last event to calculate the total duration of
			// the trace.
			String firstEventTime = trace
					.get(0)
					.getAttributes()
					.get("time:timestamp")
					.toString();
			String lastEventTime = trace
					.get(trace.size())
					.getAttributes()
					.get("time:timestamp")
					.toString();
			
			LocalDateTime startTime = synchronizeGMT(firstEventTime);
			LocalDateTime endTime = synchronizeGMT(lastEventTime);
			
			Duration traceDuration = Duration.between(startTime, endTime);
			double totalMillis = (double) traceDuration.toMillis();
			
			if (totalMillis < minDuration) {
				minDuration = totalMillis;
			}
			
			if (totalMillis > maxDuration) {
				maxDuration = totalMillis;
			}
			
		}
		
		return Arrays.asList(minDuration, maxDuration);
	}
	
	public static List<Double> getminAdnMaxEventSize(XLog log) {
		
		double minEventSize = Double.MAX_VALUE;
		double maxEventSize = -Double.MAX_VALUE;
		
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

}
