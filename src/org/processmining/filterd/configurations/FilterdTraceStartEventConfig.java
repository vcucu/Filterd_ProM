package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.widgets.ParameterOneFromSetExtendedController;

public class FilterdTraceStartEventConfig extends FilterdAbstractConfig implements Referenceable {
	
	FilterdAbstractConfig concreteReference;

	public FilterdTraceStartEventConfig(XLog log, Filter filterType) {
		super(log, filterType);
		parameters = new ArrayList<Parameter>();
		complexClassifiers = new ArrayList<>();
		
		 // Get all the events attributes that are passed to the parameter 
		List<String> attrAndClassifiers = computeAttributes(log);
		//add the complex classifiers to the list of global attributes 
		attrAndClassifiers.addAll(computeComplexClassifiers(log));
		
		// Create attribute parameter, creates reference is true
		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", 
				"Filter by", attrAndClassifiers.get(0), attrAndClassifiers, true);

		
		
		//Create selectionType parameter
		List<String> selectionTypeOptions = new ArrayList<>(Arrays.asList("Filter in", "Filter out"));
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType",
				"Selection type", selectionTypeOptions.get(0), selectionTypeOptions);	
		
		
		
		// Add all parameters to the list of parameters	
		parameters.add(attribute);
		parameters.add(selectionType);
		//parameters.addAll(concreteReference.getParameters());
	}
	
	

	public FilterdAbstractConfig populate(AbstractFilterConfigPanelController component) {
		return this;
		/*ParameterOneFromSetController attrController = 
				(ParameterOneFromSetController) component.getControllers().get(0);
		
		ParameterOneFromSetController selectionController = 
				(ParameterOneFromSetController) component.getControllers().get(1);
			
		//update the parameters with the values from the parameter controllers
		((ParameterOneFromSet)this.parameters.get(0))
		.setChosen(attrController.getValue());
		
		((ParameterOneFromSet)this.parameters.get(1))
		.setChosen(selectionController.getValue());*/
		
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		// TODO Auto-generated method stub
		return false;
	}

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController("Trace Start Event Configuration", parameters);
	}
	
	/*
	 * The candidateLog is invalid if the event attributes list does not 
	 * contain the selected attribute.
	 */
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



	public FilterdAbstractConfig changeReference(ParameterOneFromSetExtendedController controller) {
		return new FilterdTraceStartEventCategoricalConfig(log, filterType, controller.getValue(), complexClassifiers);	 
	}
}
