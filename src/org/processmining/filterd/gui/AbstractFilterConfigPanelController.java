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
				parameter.getChosen());
		getNextContainer().getChildren().add(controller.getContents());
		controllers.add(controller);
	}
	
	public void addParameterOneFromSet(ParameterOneFromSet parameter) {
		ParameterController controller = new ParameterOneFromSetController(parameter.getNameDisplayed(), 
				parameter.getName(),
				parameter.getChosen(), 
				parameter.getOptions());
		getNextContainer().getChildren().add(controller.getContents());
		controllers.add(controller);
	}
	
	public void addParameterMultipleFromSet(ParameterMultipleFromSet parameter) {
		ParameterController controller = new ParameterMultipleFromSetController(parameter.getNameDisplayed(), 
				parameter.getName(), 
				parameter.getChosen(), 
				parameter.getOptions());
		getNextContainer().getChildren().add(controller.getContents());
		controllers.add(controller);
	}
	
	public <N extends Number> void addParameterValueFromRange(ParameterValueFromRange<N> parameter) {
		ParameterController controller = new ParameterValueFromRangeController<N>(parameter.getNameDisplayed(), 
				parameter.getName(), 
				parameter.getChosen(),
				parameter.getOptionsPair(),
				parameter.getGenericTypeClass());
		getNextContainer().getChildren().add(controller.getContents());
		controllers.add(controller);
	}
	
	public void addParameterText(ParameterText parameter) {
		ParameterController controller = new ParameterTextController(parameter.getNameDisplayed(), 
				parameter.getName(), 
				parameter.getChosen());
		getNextContainer().getChildren().add(controller.getContents());
		controllers.add(controller);
	}
	
	public <N extends Number> void addParameterRangeFromRange(ParameterRangeFromRange<N> parameter) {
		ParameterRangeFromRangeController<N> controller = new ParameterRangeFromRangeController<N>(parameter.getNameDisplayed(), 
				parameter.getName(), 
				parameter.getChosenPair(),
				parameter.getOptionsPair(),
				parameter.getGenericTypeClass());
		getNextContainer().getChildren().add(controller.getContents());
		if (parameter.getName().equals("time-range")) {
			controller.setTimes(parameter.getTimes());
			controller.setTimeframe();
		}
		controllers.add(controller);
	}
	
	public <N1 extends Number, N2 extends Number> void populateFromParameters(List<Parameter> parameters) {
		for(Parameter parameter : parameters) {
			if(parameter instanceof ParameterYesNo) {
				ParameterYesNo casted = (ParameterYesNo) parameter;
				addParameterYesNo(casted);
			} else if(parameter instanceof ParameterOneFromSet) {
				ParameterOneFromSet casted = (ParameterOneFromSet) parameter;
				addParameterOneFromSet(casted);
			} else if(parameter instanceof ParameterMultipleFromSet) {
				System.out.println("[!] Starting multiple from set");
				ParameterMultipleFromSet casted = (ParameterMultipleFromSet) parameter;
				addParameterMultipleFromSet(casted);
				System.out.println("[!] End multiple from set");
			} else if(parameter instanceof ParameterValueFromRange) {
				ParameterValueFromRange<N1> casted = (ParameterValueFromRange<N1>) parameter;
				addParameterValueFromRange(casted);
			} else if(parameter instanceof ParameterText) {
				ParameterText casted = (ParameterText) parameter;
				addParameterText(casted);
			} else if(parameter instanceof ParameterRangeFromRange) {
				ParameterRangeFromRange<N2> casted = (ParameterRangeFromRange<N2>) parameter;
				addParameterRangeFromRange(casted);
			} else {
				throw new IllegalArgumentException("Unsupporrted parameter type.");
			}
		}
	}
	
	public List<ParameterController> getControllers() {
		return controllers;
	}
	
	public VBox getRoot() {
		return root;
	}
}
