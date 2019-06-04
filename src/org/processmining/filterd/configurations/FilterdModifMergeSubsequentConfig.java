package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.filterd.widgets.ParameterOneFromSetExtendedController;

public class FilterdModifMergeSubsequentConfig extends FilterdAbstractReferencingConfig {
	
	public FilterdModifMergeSubsequentConfig(XLog log, Filter filterType) {
		super(log, filterType);
		parameters = new ArrayList<Parameter>();
		List<XEventClassifier> classifiers = Toolbox.computeAllClassifiers(log);
		List<String> classifiersNames = Toolbox.getClassifiersName(classifiers);
		List<String> comparisonTypes = new ArrayList<>(Arrays.asList
				("Compare event class", 
				"Compare event timestamps", 
				"Compare event class & attributes"));
		List<String> mergeTypes = new ArrayList<>(Arrays.asList
				("Merge taking first event", 
				"Merge taking last event", 
				"Merge taking first as 'start' and last as 'complete' life-cycle transitions"));
		
		//Create the classifier parameter
		ParameterOneFromSet classifierParam = new ParameterOneFromSet
				("classifier", 
				"Select classifier",
				classifiersNames.get(0),
				classifiersNames, true);
		
		//initialize the concreteReference with a default value
		concreteReference = new FilterdModifMergeSubsequentCategoricalConfig
				(log, filterType, classifiersNames.get(0), classifiers);
		
		// Create comparison type parameter
		ParameterOneFromSet comparisonType = new ParameterOneFromSet
				("comparisonType", 
				"Select how to compare events",
				comparisonTypes.get(0),
				comparisonTypes);
		
		// Create merge type parameter
		ParameterOneFromSet mergeType = new ParameterOneFromSet
				("mergeType", 
				"Select merge type",
				mergeTypes.get(0),
				mergeTypes);
		
		//Add all parameters to the list of parameters
		parameters.add(classifierParam);
		parameters.add(comparisonType);
		parameters.add(mergeType);
	}



	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController("Merge Subsequent Events Configuration", parameters, this);
	}
	

	public boolean checkValidity(XLog log) {
		if (parameters == null) {
			return true;
		}
		return true;
	}

	@Override
	public FilterdAbstractConfig changeReference(ParameterOneFromSetExtendedController controller) {
		concreteReference = new FilterdModifMergeSubsequentCategoricalConfig
				(log, filterType, controller.getValue(), Toolbox.computeAllClassifiers(log));
		return concreteReference;
	}



}
