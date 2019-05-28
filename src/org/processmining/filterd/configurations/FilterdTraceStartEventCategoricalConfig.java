package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;

public class FilterdTraceStartEventCategoricalConfig extends FilterdAbstractReferenceableConfig {	
	
	public FilterdTraceStartEventCategoricalConfig(XLog log, Filter filterType, String attribute, 
			List<XEventClassifier> classifiers) {
		super(log, filterType);
		parameters = new ArrayList<Parameter>();
		isAttribute = true;
		List<String> allValues = new ArrayList<>();
		
		// check whether the selected string is an attribute or a classifier
		for (XEventClassifier c: classifiers) {
			if (c.name().equals(attribute)) {
				isAttribute = false; // the selected string is a complex classifier
				XLogInfo logInfo = XLogInfoImpl.create(log);
				List<XEventClass> eventClasses = new ArrayList<>(logInfo.getEventClasses(c).getClasses());
				for (XEventClass eventClass : eventClasses) {
					allValues.add(eventClass.toString());
				}
				break;
			}
		}
		
		
		// Create desiredEvents parameter	
		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet("desiredEvents",
				"Select start values", allValues, allValues);
		
		// Should you keep empty traces
		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", 
				"Keep empty traces.", false);
		
		List<Integer> optionsPair = new ArrayList<>();
		optionsPair.add(0);
		optionsPair.add(100);
		ParameterValueFromRange<Integer> threshold = new ParameterValueFromRange<Integer>(
				"Frequency threshold", "threshold", 100, optionsPair, Integer.TYPE);
		
		parameters.add(desiredEvents);
		parameters.add(traceHandling);
		parameters.add(threshold);
		
		// none of the complex classifiers matched the selected values, therefore the 
		// selected string is a global attribute
		if (isAttribute) {
			// should you keep events which do not have the specified attribute
			ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", 
					"Keep events if attribute not specified.", false);
			parameters.add(eventHandling);
			for (XTrace trace : log) {
				for (XEvent event : trace) {
					String value;
					if (event.getAttributes().containsKey(attribute)) {
						value = event.getAttributes().get(attribute).toString();
					} else {
						continue;
					}
					if (!allValues.contains(value)) {
						allValues.add(value);
					}
				}
			}
		}
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};

	public boolean checkValidity(XLog log) {
		return true;
	}

}
