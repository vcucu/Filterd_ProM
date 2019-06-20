package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.classification.XEventAttributeClassifier;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.gui.NestedFilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.filterd.tools.Toolbox;

public class FilterdTraceStartEventCategoricalConfig extends FilterdAbstractReferenceableConfig {	
	
	XEventClasses xEventClasses;
	List<String> allValues = new ArrayList<>();
	String key;
	
	public FilterdTraceStartEventCategoricalConfig(XLog log, Filter filterType, String key, 
			List<XEventClassifier> classifiers) {
		super(log, filterType);
		this.key = key;
		parameters = new ArrayList<Parameter>();
		isAttribute = true;
		XLog startEventsLog = startEventsOnly();
		//classifiers array contains all complex classifiers that can be mapped to the log
		//the attribute is the selected thing from the dropdown regardless of whether it 
		// is a classifier or an attribute
		
		// check whether the selected string is an attribute or a classifier
		for (XEventClassifier c: classifiers) {
			if (c.name().equals(key)) {
				//if it is a classifier than create eventclasses object accordingly
				isAttribute = false; // the selected string is a complex classifier
				xEventClasses = new XEventClasses(c);
				xEventClasses = XEventClasses.deriveEventClasses(c, startEventsLog);				
		
				for (int i = 0; i <= xEventClasses.size() - 1; i++) {
					allValues.add(xEventClasses.getByIndex(i).toString());
				}
				break;
			}
		}
		
		if (isAttribute) {
			//if it is an attribute than create eventclasses object accordingly
			XEventAttributeClassifier attrClassifier = new XEventAttributeClassifier(
					"attrClassifier", key);
			
			xEventClasses = new XEventClasses(attrClassifier);
			xEventClasses = XEventClasses.deriveEventClasses(attrClassifier, startEventsLog);
			
			for (int i = 0; i <= xEventClasses.size() - 1; i++) {
				//uncomment to disallow filtering on empty values(non-null but just empty)
				//if (!xEventClasses.getByIndex(i).toString().equals("")) {
				allValues.add(xEventClasses.getByIndex(i).toString());
				//}
			}			
		}
		
		// Create desiredEvents parameter	
		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet(
				"desiredEvents", "Select start event values", new ArrayList<>(), allValues);
		
		// Should you keep empty traces
		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", 
				"Keep empty traces.", false);
		
		List<Integer> optionsPair = new ArrayList<>();
		optionsPair.add(0);
		optionsPair.add(100);
		ParameterValueFromRange<Integer> threshold = new ParameterValueFromRange<Integer>(
				"threshold", "Frequency threshold", 100, optionsPair, Integer.TYPE);
		
		parameters.add(desiredEvents);
		parameters.add(traceHandling);
		parameters.add(threshold);
		
		// none of the complex classifiers matched the selected values, therefore the 
		// selected string is a (possibly not global) attribute
		if (isAttribute) {
			// should you keep events which do not have the specified attribute
			ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", 
					"Keep events if attribute not specified.", false);
			parameters.add(eventHandling);
			for (XTrace trace : startEventsLog) {
				for (XEvent event : trace) {
					String value;
					if (event.getAttributes().containsKey(key)) {
						value = event.getAttributes().get(key).toString();
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
	
	private XLog startEventsOnly() {
		XLog filteredLog = Toolbox.initializeLog(log);
		XFactory factory = XFactoryRegistry.instance().currentDefault();
		for (XTrace trace: this.log) {
			XTrace filteredTrace = factory.createTrace(trace.getAttributes());
			filteredTrace.add(trace.get(0));
			filteredLog.add(filteredTrace);
		}
		return filteredLog;
	}
	
	
	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};

	public boolean checkValidity(XLog log) {
		return true;
	}
	
	public String getKey() {
		return key;
	}
	
	
	@Override
	public NestedFilterConfigPanelController getConfigPanel() {
		return ConfigurationToolbox.traceStartAndEndEventCategoricalConfigs(
				parameters, 
				this, 
				xEventClasses);
	}

}
