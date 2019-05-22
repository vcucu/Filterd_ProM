package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.widgets.ParameterOneFromSetController;

public class FilterdTraceAttrConfig extends FilterdAbstractConfig {

	FilterdAbstractConfig concreteReference; 
	
	public FilterdTraceAttrConfig(XLog log, Filter filterType) {
		super(log, filterType);
		
		// Initialize the configuration's parameters list.
		parameters = new ArrayList<>();
		
		// Get all the attributes in the log
		Set<String> globalAttributes = new HashSet<String>();
		
		// Do this by looping over every trace and collecting its attributes
		// and adding this to the set.
		for (XTrace trace : log) {
			
			XAttributeMap attributesMap = trace.getAttributes();
			
			for (String attribute : attributesMap.keySet()) {
				globalAttributes.add(attribute);
			}
			
		}
		
		// Convert the set into an array list because ParameterOneFromSet takes
		// a list as an argument.
		List<String> globalAttributesList = 
				new ArrayList<String>(globalAttributes);
		
		// Create the parameter for selecting the attribute.
		ParameterOneFromSet attributeSelector = 
				new ParameterOneFromSet(
						"attribute", 
						"Select attribute", 
						globalAttributesList.get(0), 
						globalAttributesList);
		
		// Create the array list for selecting the type of attribute the user
		// has selected.
		List<String> attributeTypeList = new ArrayList<String>();
		attributeTypeList.add("Categorical");		
		attributeTypeList.add("Numerical");
		attributeTypeList.add("Timeframe");
		attributeTypeList.add("Performance");
		
		// Create the parameter for selecting the type of attribute.
		ParameterOneFromSet attributeTypeSelector = 
				new ParameterOneFromSet(
						"attribute type", 
						"Select attribute type", 
						attributeTypeList.get(0), 
						attributeTypeList);
		
		parameters.add(attributeSelector);
		parameters.add(attributeTypeSelector);
	}

	public FilterdAbstractConfig populate(AbstractFilterConfigPanelController component) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		// Impossible to check so we have to rely on the user himself to 
		// populate it with the correct parameters.
		return true;
	}

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController(
				"Trace Attribute Configuration", 
				parameters
				);
	}
	
	public FilterdAbstractConfig changeReference(
			ParameterOneFromSetController chosen) {

		// Switch on the chosen type of attribute
		switch (chosen.getValue()) {
			// Categorical, so we create a new categorical configuration.
			case "Categorical": {
				concreteReference = new FilterdTraceAttrCategoricalConfig(log, 
						filterType);
				break;
			}
			// Numerical, so we create a new numerical configuration.
			case "Numerical": {
				concreteReference = new FilterdTraceAttrNumericalConfig(log, 
						filterType);
				break;
			}
			// Timeframe, so we create a new time frame configuration.
			case "Timeframe": {
				concreteReference = new FilterdTraceAttrTimeframeConfig(log, 
						filterType);
				break;
			}
			// Performance, so we create a new performance configuration.
			case "Performance": {
				concreteReference = new FilterdTraceAttrPerformanceConfig(log, 
						filterType);
				break;
			}
		}
		
		// Return this reference.
		return this;
	}

	public boolean checkValidity(XLog log) {
		// Impossible since we can not figure out the type.
		return false;
	}

}
