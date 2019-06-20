package org.processmining.filterd.configurations;

import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.gui.NestedFilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.filterd.widgets.ParameterController;
import org.processmining.filterd.widgets.ParameterMultipleFromSetController;
import org.processmining.filterd.widgets.ParameterValueFromRangeController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;

public class ConfigurationToolbox {
	
	/**
	 * Method that both classes use for getting their configuration panel.
	 * 
	 * @param parameters
	 * @param config
	 * @param xEventClasses
	 * @return The configuration panel
	 */
	public static NestedFilterConfigPanelController traceStartAndEndEventCategoricalConfigs(
			List<Parameter> parameters,
			FilterdAbstractConfig config,
			XEventClasses xEventClasses) {
		
		NestedFilterConfigPanelController nestedPanel = new NestedFilterConfigPanelController(parameters);
		for( ParameterController controller : nestedPanel.getControllers()) {
			//find threshold parameter controller and add listener to it
			if (controller.getName().equals("threshold")) {
				ParameterValueFromRangeController casted = (ParameterValueFromRangeController) controller;
				Slider slider = casted.getSlider();
				slider.valueProperty().addListener(new ChangeListener<Number>() {

					private ParameterValueFromRange<Integer> threshold;

					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue,
							Number newValue) {
						//find the controller for desired events so that it can change
						for (ParameterController changingController : nestedPanel.getControllers()) {
							if (changingController.getName().equals("desiredEvents")) {
								 ParameterMultipleFromSetController castedChangingController = 
											(ParameterMultipleFromSetController) changingController;
								 //select events corresponding to the slider selection 
								 //using the toolbox function
								 
								 //the parameter is only used because thats the interface in toolbox
								 //but nice solution would also be making it string & number, cos this is kinda workaround
								 ParameterOneFromSet rate = new ParameterOneFromSet(
										 "frequency","frequency","frequency",null );
								 rate.setChosen("Frequency");
								 threshold = new ParameterValueFromRange<Integer>(
											"Frequency threshold", "threshold", 100, null, Integer.TYPE);
								 threshold.setChosen(newValue.intValue());
								 List<String> selection = new ArrayList<String>();

								 // Get correct selection.
								 selection = Toolbox.computeDesiredEventsFromThreshold(threshold, rate, xEventClasses);
								 // Set selected value.
								 castedChangingController.setSelected(selection);
								 
								 // Get changing parameter.
								 ParameterMultipleFromSet castedChangingParameter = (ParameterMultipleFromSet) config.getParameter("desiredEvents");
								 // Set the chosen value in the changing 
								 // parameter.
								 castedChangingParameter.setChosen(selection);
								 
								
								 
							}	
							
						}
						
					}

				});
			
			}
		}

		return nestedPanel;
		
	}
	
	/*
	 * The candidateLog is invalid if the event attributes list does not 
	 * contain the selected attribute 
	 * The candidateLog is invalid if the complex classifiers list does not
	 * contain the selected complex classifier
	 */
	public static boolean traceStartAndEndEventValidity(
			List<Parameter> parameters,
			FilterdAbstractConfig config,
			XLog candidateLog) {
		
		// If parameters is null,
		// just return true.
		if (parameters == null) {
			return true;
		}
		
		// Initialize attribute list of candidate log.
		List<String> attrCandidateLog = new ArrayList<>();
		// Initialize classifiers for candidate log.
		List<XEventClassifier> complexClassifiers = new ArrayList<>();
		
		// Compute both lists.
		attrCandidateLog.addAll(Toolbox.computeAttributes(candidateLog));
		complexClassifiers.addAll(Toolbox.computeComplexClassifiers(candidateLog));
		
		// Loop over all event classifiers.
		for (XEventClassifier c : complexClassifiers) {
			attrCandidateLog.add(c.toString());
		}
		
		// Get correct parameter.
		ParameterOneFromSet attribute = (ParameterOneFromSet) config.getParameter("attribute");
		// Get string representation.
		String chosenAttr = attribute.getChosen();
		
		// If it doesn't contain the right attributes.
		if (!attrCandidateLog.contains(chosenAttr)) {
			return false;
		}
		
		return true;
	}

}
