package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterValueFromRange;

public class FilterdEventRateConfig extends FilterdAbstractConfig {
	
	Collection<XEventClass> eventClasses;

	public FilterdEventRateConfig(XLog log, Filter filterType) {
		super(log, filterType);
		List<Parameter> parameters = new ArrayList<>();
		
		//Create the rate parameter
		List<String> rateOptions = new ArrayList<>(Arrays.asList("Frequency","Occurence"));
		ParameterOneFromSet rate = new ParameterOneFromSet("rate", "Choose rate type", rateOptions.get(0), rateOptions);
		
		//Create the threshold parameter
		List<Integer> optionsPair = new ArrayList<>(Arrays.asList(0, 100));
		ParameterValueFromRange<Integer> threshold = new ParameterValueFromRange<>("threshold",
				"Select threshold for frequency/occurence", 100, optionsPair);
		
		//Create the selectionType parameter
		List<String> selectionTypeOptions = new ArrayList<>(Arrays.asList("Filter in", "Filter out"));
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "Selection type",
				selectionTypeOptions.get(0), selectionTypeOptions);
		
		// classify the events according to their name and lifecycle
		this.classifier = XLogInfoImpl.STANDARD_CLASSIFIER;
		
		//create list of desired events 
		List<String> allValuesDesiredEvents = new ArrayList<>();

		//create the event classes according to the classifier
		XLogInfo logInfo = XLogInfoImpl.create(log, classifier);
		eventClasses = logInfo.getEventClasses().getClasses();
		//retrieve the name of the eventClass to be displayed in the ParameterMultipleFromSet desiredEvents
		for (XEventClass eventClass : eventClasses) {
			allValuesDesiredEvents.add(eventClass.toString());		
		}
		
		//Create the desired events parameter
		// by default it sets selected all eventClasses, since the threshold is 100
		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet("desiredEvents",
				"Selected classes according to the threshold", allValuesDesiredEvents, allValuesDesiredEvents);
		
		// Add all parameters to the list of parameters	
		parameters.add(rate);
		parameters.add(threshold);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController("Event Rate Configuration", parameters, this);
	}
	// I did not find any case where a different input log would cause invalidity
	public boolean checkValidity(XLog candidateLog) {	
		return true;
	}
	
	
	/**
	 * This method computes which event classes should be highlighted according to the selected percentage
	 * @param threshold
	 * @param desiredEvents
	 */
	public void stateChanged(ParameterValueFromRange<Integer> threshold, ParameterMultipleFromSet desiredEvents) {
		int percentage = threshold.getChosen();
		int size = 0;
		//sort eventClasses according to their size, from smallest to biggest
		TreeSet<Integer> eventSizes = new TreeSet<Integer>();
		for (XEventClass event : eventClasses) {
			size += event.size();
			eventSizes.add(event.size());
		}
		int aux_threshold = size * percentage/100;
		int value = 0;
		List<String> desirableEventClasses = new ArrayList<>();
		while (value < aux_threshold) {
			//extract the class with the greatest value
			int biggestEventClass = eventSizes.last();
			eventSizes.remove(biggestEventClass);
			
			// mark all the event classes that have this size
			for (XEventClass eventClass : eventClasses) {
				if (eventClass.size() == biggestEventClass) {
					value += biggestEventClass;
					// this event class should be highlighted
					desirableEventClasses.add(eventClass.toString());
				}
			}
		}
		desiredEvents.setChosen(desirableEventClasses);
	}

}
