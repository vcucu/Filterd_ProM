package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
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

public class FilterdTraceStartEventConfig extends FilterdAbstractConfig implements Referenceable {
	
	FilterdAbstractConfig concreteReference;

	public FilterdTraceStartEventConfig(XLog log, Filter filterType) {
		super(log, filterType);
		parameters = new ArrayList<Parameter>();
		complexClassifiers = new ArrayList<>();
		
		 // Get global attributes that are passed to the parameter 
		List<String> globalAttrAndClassifiers = computeGlobalAttributes(log);
		//add the complex classifiers to the list of global attributes 
		globalAttrAndClassifiers.addAll(computeComplexClassifiers(log));
		
		// Create attribute parameter, creates reference is true
		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", 
				"Filter by", globalAttrAndClassifiers.get(0), globalAttrAndClassifiers, true);

		
		
		//Create selectionType parameter
		List<String> selectionTypeOptions = new ArrayList<>(Arrays.asList("Filter in", "Filter out"));
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType",
				"Selection type", selectionTypeOptions.get(0), selectionTypeOptions);	
		
		// Create the default concrete reference
		concreteReference = new FilterdTraceStartEventCategoricalConfig(log, filterType,
				globalAttrAndClassifiers.get(0), complexClassifiers);
		
		// Add all parameters to the list of parameters	
		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.addAll(concreteReference.getParameters());
	}
	
	
	@Override
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
		// TODO Auto-generated method stub
		return false;
	}

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController("Trace Start Event Configuration", parameters, this);
	}
	
	public FilterdAbstractConfig changeReference(ParameterOneFromSetExtendedController controller) {
		// ...
		return concreteReference;
	}
	
	/*
	 * The candidateLog is invalid if the global attributes list does not 
	 * contain the selected attribute.
	 */
	@Override
	public boolean checkValidity(XLog candidateLog) {
		List<String> globalAttrCandidateLog = new ArrayList<>();
		for (XAttribute attribute : candidateLog.getGlobalEventAttributes()) {
			globalAttrCandidateLog.add(attribute.getKey());
		}
		List<String> attrs = computeGlobalAttributes(candidateLog);
		String attr = attrs.get(0);
		if (!globalAttrCandidateLog.contains(attr)) {
			return false;
		}	
		return true;
	}

}
