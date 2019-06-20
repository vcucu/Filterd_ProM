package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.filterd.widgets.ParameterMultipleFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetExtendedController;
import org.python.google.common.collect.Sets;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;

/*
 * Class responsible for creating the configuration of the
 * Merge subsequent events filter
 */
public class FilterdModifMergeSubsequentConfig extends FilterdAbstractReferencingConfig {

	FilterConfigPanelController configPanel;

	public FilterdModifMergeSubsequentConfig(XLog log, Filter filterType) {
		super(log, filterType);
		parameters = new ArrayList<Parameter>();
		// retrieve needed information from the log
		// to be used within the parameters of this configuration
		List<XEventClassifier> classifiers = Toolbox.computeAllClassifiers(log);
		List<String> classifiersNames = Toolbox.getClassifiersName(classifiers);
		List<String> attributeNames = Toolbox.computeAttributes(log);

		// define the selection options for the comparison type parameter
		List<String> comparisonTypes = new ArrayList<>(Arrays.asList
				("Compare event class", 
						"Compare event timestamps", 
						"Compare event class & attributes"));
		// define selection options for the merge type parameter
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


		// Create merge type parameter
		ParameterOneFromSet mergeType = new ParameterOneFromSet
				("mergeType", 
						"Select merge type",
						mergeTypes.get(0),
						mergeTypes);
		// Create comparison type parameter
		
		ParameterOneFromSet comparisonType = new ParameterOneFromSet
				("comparisonType", 
						"Select how to compare events",
						comparisonTypes.get(0),
						comparisonTypes);
		//initialize the concreteReference with a default value
		concreteReference = new FilterdModifMergeSubsequentCategoricalConfig
				(log, filterType, classifiersNames.get(0), classifiers);



		//Create relevant attributes parameter
		// This is a parameter which can be hidden so we set disappearable to true
		ParameterMultipleFromSet relevantAttributes = new ParameterMultipleFromSet(
				"relevantAttributes",
				"Select the attributes that should coincide",
				new ArrayList<>(),
				attributeNames);
		// this is a parameter which appears and disappears
		// according to the value of another parameter,
		// therefore we need to set its disappearable
		// attribute to true
		relevantAttributes.setDisappearable(true);

		//Add all parameters to the list of parameters
		parameters.add(classifierParam);
		parameters.add(mergeType);
		parameters.add(comparisonType);
		parameters.add(relevantAttributes);


		this.configPanel = new FilterConfigPanelController("Merge Subsequent Events Configuration", parameters, this);
	}



	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};


	/*
	 * The candidate log is invalid if
	 * the selected relevant attributes and the log relevant attributes
	 * have an intersection that is null
	 */
	public boolean checkValidity(XLog candidateLog) {

		if( parameters == null || candidateLog.equals(log) )
			return true;
		if (concreteReference == null) 
			return true;
		ParameterMultipleFromSet relevantAttributes = (ParameterMultipleFromSet) this.getParameter("relevantAttributes");
		Set<String> chosenSet = new HashSet<>(relevantAttributes.getChosen());
		Set<String> candLogAttributeNames = new HashSet<>(Toolbox.computeAttributes(candidateLog));
		if (Sets.intersection(chosenSet, candLogAttributeNames).size() == 0 ) {
			return false;
		}

		return concreteReference.checkValidity(log);

	}
	
	/**
	 * Changes the content of the configuration according to the 
	 * selected attributes
	 */
	@Override
	public FilterdAbstractConfig changeReference(ParameterOneFromSetExtendedController controller) {
		for (Parameter param : concreteReference.getParameters()) {
			parameters.remove(param);
		}
		concreteReference = new FilterdModifMergeSubsequentCategoricalConfig
				(log, filterType, controller.getValue(), Toolbox.computeAllClassifiers(log));
		for (Parameter param : concreteReference.getParameters()) {
			parameters.add(param);
		}
		return concreteReference;
	}


	/*
	 * if the comparison type is "Compare event class & attributes" then the parameter
	 * relevantAttributes is displayed.
	 * Otherwise, keep it hidden.
	 */
	public AbstractFilterConfigPanelController getConfigPanel() {

		
		// retrieve the controller of the comparisonType parameter
		ParameterOneFromSetController comparisonTypeController =  (ParameterOneFromSetController)
				configPanel.getControllers().stream().
				filter(c->c.getName().equals("comparisonType")).
				findFirst().get();

		// retrieve the controller of the relevantAttributes parameter
		ParameterMultipleFromSetController relevantAttributesController = (ParameterMultipleFromSetController)
				configPanel.getControllers().stream().
				filter(c-> c.getName().equals("relevantAttributes")).
				findFirst().get();
		// check whether the relevantAttributes should be hidden or not,
		// by default
		if (!comparisonTypeController.getValue().equals("Compare event class & attributes")) {
			relevantAttributesController.getContents().setVisible(false);
			relevantAttributesController.getContents().setManaged(false);
		}

		/*
		 * when the parameter is not displayed, we need to make sure 
		 * that it does not occupy space in the UI,
		 * therefore we change setManaged() according to the
		 * visibility of the parameter
		 */
		ComboBox<String> comboBox = comparisonTypeController.getComboBox();
		comboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String oldValue, String newValue) {
				if (!comparisonTypeController.getValue().equals("Compare event class & attributes")) {
					relevantAttributesController.getContents().setVisible(false);
					relevantAttributesController.getContents().setManaged(false);	
				} else {
					relevantAttributesController.getContents().setVisible(true);	
					relevantAttributesController.getContents().setManaged(true);	
				}
			}
		});			
		return configPanel;
	}
}
