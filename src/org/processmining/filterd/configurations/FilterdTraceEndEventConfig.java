package org.processmining.filterd.configurations;
import java.util.ArrayList;
import org.processmining.filterd.parameters.*;
import org.processmining.filterd.widgets.*;
import java.util.List;
import java.util.Arrays;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.widgets.ParameterOneFromSetController;

public class FilterdTraceEndEventConfig extends FilterdAbstractConfig {
	
	FilterdAbstractConfig concreteReference;

	public FilterdTraceEndEventConfig(XLog log, Filter filterType) {
		super(log, filterType);
		parameters = new ArrayList<Parameter>();
		complexClassifiers = new ArrayList<>();
		
		 // Get all the events attributes that are passed to the parameter 
		List<String> attrAndClassifiers = this.computeAttributes(log);
		//add the complex classifiers to the list of global attributes 
		attrAndClassifiers.addAll(computeComplexClassifiers(log));
		
		// Create attribute parameter 
		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", 
				"Filter by", attrAndClassifiers.get(0), attrAndClassifiers, true);

		
		//Create selectionType parameter
		List<String> selectionTypeOptions = new ArrayList<>(Arrays.asList("Filter in", "Filter out"));
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType",
				"Selection type", "Filter in", selectionTypeOptions);	
		
		// Create the default concrete reference
		concreteReference = new FilterdTraceEndEventCategoricalConfig(log, filterType, 
				attrAndClassifiers.get(0), complexClassifiers);
		
		// Add all parameters to the list of parameters
		
		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.addAll(concreteReference.getParameters());
	}
	
	public List<String> computeGlobalAttributes(XLog log) {
		List<String> globalAttr = new ArrayList<>();
		for (XAttribute attribute : log.getGlobalEventAttributes()) {
			globalAttr.add(attribute.getKey());
		}
		return globalAttr;
	}
	
	public FilterdAbstractConfig populate(AbstractFilterConfigPanelController abstractComponent) {
		
		FilterConfigPanelController component = (FilterConfigPanelController) abstractComponent;
		List<ParameterController> controllers = component.getControllers();
		for(ParameterController controller : controllers) {
			//all cases assume that the controller has a name corresponding to the parameter name
			if(controller instanceof ParameterOneFromSetExtendedController) {
				ParameterOneFromSetExtendedController casted = (ParameterOneFromSetExtendedController) controller;
				concreteReference.populate(casted.getNestedConfigPanel());
				
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
		//check whether no params are empty if you populate with the component
		return true;
	};

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController("Trace End Event Configuration", parameters, this);
	}
	
	public FilterdAbstractConfig changeReference(ParameterOneFromSetController chosen) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean checkValidity(XLog candidateLog) {
		List<String> attrCandidateLog = new ArrayList<>();
		attrCandidateLog.addAll(computeAttributes(candidateLog));
		List<String> attrs = computeGlobalAttributes(candidateLog);
		// to be changed with the selected attribute
		String attr = attrs.get(0);
		if (!attrCandidateLog.contains(attr)) {
			return false;
		}	
		return true;
	}

}
