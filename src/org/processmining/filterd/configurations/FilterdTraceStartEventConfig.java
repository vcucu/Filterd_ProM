package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.*;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.filterd.widgets.*;

public class FilterdTraceStartEventConfig extends FilterdAbstractReferencingConfig {


	public FilterdTraceStartEventConfig(XLog log, Filter filterType) {
		super(log, filterType);
		parameters = new ArrayList<Parameter>();
		List<XEventClassifier> complexClassifiers = Toolbox.computeComplexClassifiers(log);
		
		 // Get all the events attributes that are passed to the parameter 
		List<String> attrAndClassifiers = Toolbox.computeAttributes(log);
		//add the complex classifiers to the list of global attributes 
		attrAndClassifiers.addAll(Toolbox.getClassifiersName(complexClassifiers));
		
		// Create attribute parameter, creates reference is true
		
		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", 
				"Filter by", attrAndClassifiers.get(0), attrAndClassifiers, true);

		//Create selectionType parameter
		List<String> selectionTypeOptions = new ArrayList<>(Arrays.asList("Filter in", "Filter out"));
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType",
				"Selection type", selectionTypeOptions.get(0), selectionTypeOptions);	
		
		//initialize the concreteReference with a default value
		concreteReference = new FilterdTraceStartEventCategoricalConfig
				(log, filterType, attrAndClassifiers.get(0), complexClassifiers);	
		
		// Add all parameters to the list of parameters	
		parameters.add(attribute);
		parameters.add(selectionType);
		//parameters.addAll(concreteReference.getParameters());

	}
	
	
	@Override
	public FilterdAbstractConfig populate(AbstractFilterConfigPanelController abstractComponent) {
		FilterConfigPanelController component = (FilterConfigPanelController) abstractComponent;
		List<ParameterController> controllers = component.getControllers();
		for(ParameterController controller : controllers) {
			//all cases assume that the controller has a name corresponding to the parameter name
			if(controller instanceof ParameterOneFromSetExtendedController) {
				ParameterOneFromSetExtendedController casted = (ParameterOneFromSetExtendedController) controller;
				ParameterOneFromSet param = (ParameterOneFromSet) getParameter(controller.getName());
				param.setChosen(casted.getValue());
				System.out.println("I want a nested panel");
				concreteReference.populate(casted.getNestedConfigPanel());
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
		//check whether no params are empty if you populate with the component
		return true;
	};

	public AbstractFilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController("Trace Start Event Configuration",
				parameters, this);
	}

	/*
	 * The candidateLog is invalid if the event attributes list does not 
	 * contain the selected attribute.
	 */
	@Override
	public boolean checkValidity(XLog candidateLog) {
		if (parameters == null) {
			return true;
		}
		List<String> attrCandidateLog = new ArrayList<>();
		attrCandidateLog.addAll(Toolbox.computeAttributes(candidateLog));

		ParameterOneFromSet attribute = (ParameterOneFromSet) getParameter("attribute");
		String chosenAttr = attribute.getChosen();

		if (!attrCandidateLog.contains(chosenAttr)) {
			return false;
		}			
		return true;
	}


	@Override
	public FilterdAbstractConfig changeReference(
			ParameterOneFromSetExtendedController controller) {
		concreteReference = new FilterdTraceStartEventCategoricalConfig(
				log, filterType, controller.getValue(),
				Toolbox.computeComplexClassifiers(log));
				
		return concreteReference;
	}
}
