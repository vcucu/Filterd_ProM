package org.processmining.filterd.tools;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

public class Toolbox {

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

}
