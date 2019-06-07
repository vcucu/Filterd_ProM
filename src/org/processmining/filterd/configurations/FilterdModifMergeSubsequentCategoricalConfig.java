package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.gui.NestedFilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.tools.Toolbox;
import org.python.google.common.collect.Sets;

public class FilterdModifMergeSubsequentCategoricalConfig extends FilterdAbstractReferenceableConfig {

	XEventClasses xEventClasses;
	List<String> allValues = new ArrayList<>();
	XEventClassifier key;
	
	public FilterdModifMergeSubsequentCategoricalConfig(XLog log, Filter filterType, String classifier, List<XEventClassifier> classifiers) {
		super(log, filterType);
		parameters = new ArrayList<>();
		
		for (XEventClassifier c : classifiers) {
			if (c.name().equals(classifier)) {
				this.key = c;
				xEventClasses = new XEventClasses(c);
				xEventClasses = XEventClasses.deriveEventClasses(c, log);
				
				for (XEventClass eventClass : xEventClasses.getClasses()) {
					if (!allValues.contains(eventClass.toString())) {
						allValues.add(eventClass.toString());
					}
				}
				break;
			}
		}
		
		// Create desiredEvents parameters
		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet
				("desiredEvents", "Select events", allValues, allValues);
		parameters.add(desiredEvents);
		

	}

	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};

	public boolean checkValidity(XLog candidateLog) {
		if( parameters == null || candidateLog.equals(log) )
			return true;
		/*
		 get the classifiers of the candidate log 
		 if the selected classifier of the previous log is a classifier of the 
		 candidate log, then we look at the selected event classes
		 if the intersection between the event classes of the log and the candidate log
		 is not null then the candidate log is valid and we only want to filter 
		 on the event classes that are present in the candidate log, hence we
		 set the chosen of the desiredValues Parameter to be this intersection
		 */
		
		List<XEventClassifier> candidateLogClassifiers = Toolbox.computeAllClassifiers(candidateLog);
		if (!candidateLogClassifiers.contains(key)) {
			return false;
		} else {
			Set<String> candLogDesiredEvents = new HashSet<>();
			XEventClasses candLogEventClasses = XEventClasses.deriveEventClasses(key, candidateLog);
			
				for (XEventClass eventClass : candLogEventClasses.getClasses()) {
						candLogDesiredEvents.add(eventClass.toString());
					
				}
			List<String> desiredEvents = ((ParameterMultipleFromSet)
					this.getParameter("desiredEvents")).getChosen();
			Set<String> desiredEventsSet = new HashSet<>(desiredEvents);
			if(Sets.intersection(candLogDesiredEvents, desiredEventsSet).size() == 0) {
				return false;
			} else {
				// make sure to only filter on event values that exist in the candidateLog
				Set<String> intersection = Sets.intersection(candLogDesiredEvents, desiredEventsSet);
				((ParameterMultipleFromSet)
						this.getParameter("desiredEvents")).setChosen(new ArrayList<>(intersection));
			}
			
		}
		
		return true;
	}
	
	@Override
	public NestedFilterConfigPanelController getConfigPanel() {
		NestedFilterConfigPanelController nestedPanel = new NestedFilterConfigPanelController(parameters);
		return nestedPanel;
	}


}
