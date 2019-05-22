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
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.parameters.ParameterText;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.filterd.widgets.ParameterController;
import org.processmining.filterd.widgets.ParameterMultipleFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetExtendedController;
import org.processmining.filterd.widgets.ParameterRangeFromRangeController;
import org.processmining.filterd.widgets.ParameterTextController;
import org.processmining.filterd.widgets.ParameterValueFromRangeController;
import org.processmining.filterd.widgets.ParameterYesNoController;

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
						"Attribute", 
						"Select attribute", 
						globalAttributesList.get(0), 
						globalAttributesList);
		
		// Create the array list for selecting the type of attribute the user
		// has selected.
		List<String> attributeTypeList = new ArrayList<String>();
		attributeTypeList.add("Categorical");		
		attributeTypeList.add("Numerical");
		attributeTypeList.add("Timeframe");
		attributeTypeList.add("Duration");
		attributeTypeList.add("Filter on events");
		
		// Create the parameter for selecting the type of attribute.
		ParameterOneFromSet attributeTypeSelector = 
				new ParameterOneFromSet(
						"Attribute type", 
						"Select attribute type", 
						attributeTypeList.get(0), 
						attributeTypeList);
		
		parameters.add(attributeSelector);
		parameters.add(attributeTypeSelector);
	}

	public FilterdAbstractConfig populate(AbstractFilterConfigPanelController abstractComponent) {
		FilterConfigPanelController component = (FilterConfigPanelController) abstractComponent;
		List<ParameterController> controllers = component.getControllers();
		for(ParameterController controller : controllers) {
			//all cases assume that the controller has a name corresponding to the parameter name
			if(controller instanceof ParameterOneFromSetExtendedController) {
				ParameterOneFromSetExtendedController casted = (ParameterOneFromSetExtendedController) controller;
				//concreteReference.populate(casted.getNestedConfigPanel());
				//this method needs to be in every referencable class
				
			} else if(controller instanceof ParameterYesNoController) {
				ParameterYesNoController casted = (ParameterYesNoController) controller;
				ParameterYesNo param = (ParameterYesNo) getParameter(controller.getName());
				param.setChosen(casted.getValue());	
				
			} else if(controller instanceof ParameterOneFromSetController) {
				ParameterOneFromSetController casted = (ParameterOneFromSetController) controller;
				ParameterOneFromSet param = (ParameterOneFromSet) getParameter(controller.getName());
				param.setChosen(casted.getValue());	
				
			} else if(controller instanceof ParameterMultipleFromSetController) {
				ParameterMultipleFromSetController casted = (ParameterMultipleFromSetController) controller;
				ParameterMultipleFromSet param = (ParameterMultipleFromSet) getParameter(controller.getName());
				param.setChosen(casted.getValue());				
				
			} else if(controller instanceof ParameterValueFromRangeController) {
				ParameterValueFromRangeController casted = (ParameterValueFromRangeController) controller;
				ParameterValueFromRange param = (ParameterValueFromRange) getParameter(controller.getName());
				param.setChosen(casted.getValue());	
				
			} else if(controller instanceof ParameterTextController) {
				ParameterTextController casted = (ParameterTextController) controller;
				ParameterText param = (ParameterText) getParameter(controller.getName());
				param.setChosen(casted.getValue());	
				
			} else if(controller instanceof ParameterRangeFromRangeController) {
				ParameterRangeFromRangeController casted = (ParameterRangeFromRangeController) controller;
				ParameterRangeFromRange param = (ParameterRangeFromRange) getParameter(controller.getName());
				param.setChosenPair(casted.getValue());	
				
			} else {
				throw new IllegalArgumentException("Unsupporrted controller type.");
			}	
			
			
		}
		return this;
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
						filterType, 
						((ParameterOneFromSet)(parameters.get(0))).getChosen());
				break;
			}
			// Numerical, so we create a new numerical configuration.
			case "Numerical": {
				concreteReference = new FilterdTraceAttrNumericalConfig(log, 
						filterType, 
						((ParameterOneFromSet)(parameters.get(0))).getChosen());
				break;
			}
			// Timeframe, so we create a new time frame configuration.
			case "Timeframe": {
				concreteReference = new FilterdTraceAttrTimeframeConfig(log, 
						filterType);
				break;
			}
			// Duration, so we create a new duration configuration.
			case "Duration": {
				concreteReference = new FilterdTraceAttrDurationConfig(log, 
						filterType);
				break;
			}
			// Filter on events, so we create a new filter on events
			// configuration.
			case "Filter on events": {
				concreteReference = new FilterdTraceAttrNumberOfEventsConfig(
						log, 
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
