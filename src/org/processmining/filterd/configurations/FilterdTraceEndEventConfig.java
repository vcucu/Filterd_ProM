package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deckfour.xes.classification.XEventClassifier;
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
import org.processmining.filterd.tools.Toolbox;
import org.processmining.filterd.widgets.ParameterController;
import org.processmining.filterd.widgets.ParameterMultipleFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetExtendedController;
import org.processmining.filterd.widgets.ParameterRangeFromRangeController;
import org.processmining.filterd.widgets.ParameterTextController;
import org.processmining.filterd.widgets.ParameterValueFromRangeController;
import org.processmining.filterd.widgets.ParameterYesNoController;

public class FilterdTraceEndEventConfig extends FilterdAbstractReferencingConfig {

	public FilterdTraceEndEventConfig(XLog log, Filter filterType) {
		super(log, filterType);
		parameters = new ArrayList<Parameter>();
		List<XEventClassifier>complexClassifiers = Toolbox.computeComplexClassifiers(log);
		
		 // Get all the events attributes that are passed to the parameter 
		List<String> attrAndClassifiers = Toolbox.computeAttributes(log);
		//add the complex classifiers to the list of global attributes 
		attrAndClassifiers.addAll(Toolbox.getClassifiersName(complexClassifiers));
		
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
		//parameters.addAll(concreteReference.getParameters());
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
	public FilterdAbstractConfig changeReference(ParameterOneFromSetExtendedController controller) {
		concreteReference = new FilterdTraceEndEventCategoricalConfig(
				log, filterType, controller.getValue(),Toolbox.computeComplexClassifiers(log));
				
		return concreteReference;
	}


}
