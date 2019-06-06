package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.filterd.widgets.ParameterOneFromSetExtendedController;

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
	public AbstractFilterConfigPanelController getConfigPanel() {
		if (this.configPanel == null) {
			this.configPanel = new FilterConfigPanelController("Trace Start Event Configuration",
					parameters, this);
		}
		
		return configPanel;
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};

	/*
	 * The candidateLog is invalid if the event attributes list does not 
	 * contain the selected attribute 
	 * The candidateLog is invalid if the complex classifiers list does not
	 * contain the selected complex classifier
	 */
	@Override
	public boolean checkValidity(XLog candidateLog) {
		if (parameters == null) {
			return true;
		}
		List<String> attrCandidateLog = new ArrayList<>();
		List<XEventClassifier> complexClassifiers = new ArrayList<>();
		
		attrCandidateLog.addAll(Toolbox.computeAttributes(candidateLog));
		complexClassifiers.addAll(Toolbox.computeComplexClassifiers(candidateLog));
		for (XEventClassifier c : complexClassifiers) {
			attrCandidateLog.add(c.toString());
		}
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
