package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.gui.NestedFilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;

public class FilterdModifMergeSubsequentCategoricalConfig extends FilterdAbstractReferenceableConfig {

	XEventClasses xEventClasses;
	List<String> allValues = new ArrayList<>();
	
	public FilterdModifMergeSubsequentCategoricalConfig(XLog log, Filter filterType, String classifier, List<XEventClassifier> classifiers) {
		super(log, filterType);
		parameters = new ArrayList<>();
		
		for (XEventClassifier c : classifiers) {
			if (c.name().equals(classifier)) {
				xEventClasses = new XEventClasses(c);
				xEventClasses = XEventClasses.deriveEventClasses(c, log);
				
				for (XEventClass eventClass : xEventClasses.getClasses()) {
					allValues.add(eventClass.toString());
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

	public boolean checkValidity(XLog log) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public NestedFilterConfigPanelController getConfigPanel() {
		NestedFilterConfigPanelController nestedPanel = new NestedFilterConfigPanelController(parameters);
		return nestedPanel;
	}


}
