package org.processmining.filterd.gui;

import java.util.List;

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
import org.processmining.filterd.widgets.ParameterRangeFromRangeController;
import org.processmining.filterd.widgets.ParameterTextController;
import org.processmining.filterd.widgets.ParameterValueFromRangeController;
import org.processmining.filterd.widgets.ParameterYesNoController;

import javafx.scene.layout.VBox;

public abstract class AbstractFilterConfigPanelController {
	
	protected List<ParameterController> controllers;
	protected VBox root; // top-level box which contains all components in this config. panel
	
	abstract protected VBox getNextContainer();
	
	public void addParameterYesNo(ParameterYesNo parameter) {
		ParameterController controller = new ParameterYesNoController(parameter.getNameDisplayed(), 
				parameter.getName(), 
				parameter.getDefaultChoice());
		getNextContainer().getChildren().add(controller.getContents());
		controllers.add(controller);
	}
	
	public void addParameterOneFromSet(ParameterOneFromSet parameter) {
		ParameterController controller = new ParameterOneFromSetController(parameter.getNameDisplayed(), 
				parameter.getName(),
				parameter.getDefaultChoice(), 
				parameter.getOptions());
		getNextContainer().getChildren().add(controller.getContents());
		controllers.add(controller);
	}
	
	public void addParameterMultipleFromSet(ParameterMultipleFromSet parameter) {
		ParameterController controller = new ParameterMultipleFromSetController(parameter.getNameDisplayed(), 
				parameter.getName(), 
				parameter.getDefaultChoice(), 
				parameter.getOptions());
		getNextContainer().getChildren().add(controller.getContents());
		controllers.add(controller);
	}
	
	public void addParameterValueFromRange(ParameterValueFromRange<Double> parameter) {
		ParameterController controller = new ParameterValueFromRangeController(parameter.getNameDisplayed(), 
				parameter.getName(), 
				parameter.getDefaultChoice(), 
				parameter.getOptionsPair());
		getNextContainer().getChildren().add(controller.getContents());
		controllers.add(controller);
	}
	
	public void addParameterText(ParameterText parameter) {
		ParameterController controller = new ParameterTextController(parameter.getNameDisplayed(), 
				parameter.getName(), 
				parameter.getDefaultChoice());
		getNextContainer().getChildren().add(controller.getContents());
		controllers.add(controller);
	}
	
	public void addParameterRangeFromRange(ParameterRangeFromRange<Double> parameter) {
		ParameterController controller = new ParameterRangeFromRangeController(parameter.getNameDisplayed(), 
				parameter.getName(), 
				parameter.getDefaultPair(),
				parameter.getOptionsPair());
		getNextContainer().getChildren().add(controller.getContents());
		controllers.add(controller);
	}
	
	protected void populateFromParameters(List<Parameter> parameters) {
		for(Parameter parameter : parameters) {
			if(parameter instanceof ParameterYesNo) {
				ParameterYesNo casted = (ParameterYesNo) parameter;
				addParameterYesNo(casted);
			} else if(parameter instanceof ParameterOneFromSet) {
				ParameterOneFromSet casted = (ParameterOneFromSet) parameter;
				addParameterOneFromSet(casted);
			} else if(parameter instanceof ParameterMultipleFromSet) {
				ParameterMultipleFromSet casted = (ParameterMultipleFromSet) parameter;
				addParameterMultipleFromSet(casted);
			} else if(parameter instanceof ParameterValueFromRange) {
				ParameterValueFromRange<Double> casted = (ParameterValueFromRange<Double>) parameter;
				addParameterValueFromRange(casted);
			} else if(parameter instanceof ParameterText) {
				ParameterText casted = (ParameterText) parameter;
				addParameterText(casted);
			} else if(parameter instanceof ParameterRangeFromRange) {
				ParameterRangeFromRange<Double> casted = (ParameterRangeFromRange<Double>) parameter;
				addParameterRangeFromRange(casted);
			} else {
				throw new IllegalArgumentException("Unsupporrted parameter type.");
			}
		}
	}
	
	public VBox getRoot() {
		return root;
	}
}
