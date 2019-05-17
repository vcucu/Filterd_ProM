package org.processmining.filterd.gui;

import java.util.List;

import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterText;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.filterd.widgets.ParameterMultipleFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetController;
import org.processmining.filterd.widgets.ParameterValueFromRangeController;
import org.processmining.filterd.widgets.ParameterYesNoController;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class FilterConfigPanelController {
	
	@FXML private VBox container;
	@FXML private Label title;
	private List<Node> components;
	
	FilterConfigPanelController() {
		
	}
	
	public void setTitle(String title) {
		this.title.setText(title);
	}
	
	public void populateContainer(List<Parameter> parameters) {
		for(Parameter parameter : parameters) {
			if(parameter instanceof ParameterYesNo) {
				ParameterYesNo casted = (ParameterYesNo) parameter;
				container.getChildren().add(new ParameterYesNoController(casted.getNameDisplayed(), 
																		 casted.getName(), 
																		 casted.getDefaultChoice()).getContents());
			} else if(parameter instanceof ParameterOneFromSet) {
				ParameterOneFromSet casted = (ParameterOneFromSet) parameter;
				container.getChildren().add(new ParameterOneFromSetController(casted.getNameDisplayed(), 
																			  casted.getName(), 
																		  	  casted.getDefaultChoice(), 
																		  	  casted.getOptions()).getContents());
			} else if(parameter instanceof ParameterMultipleFromSet) {
				ParameterMultipleFromSet casted = (ParameterMultipleFromSet) parameter;
				container.getChildren().add(new ParameterMultipleFromSetController(casted.getNameDisplayed(), 
																				   casted.getName(), 
																				   casted.getDefaultChoice(), 
																				   casted.getOptions()).getContents());
			} else if(parameter instanceof ParameterValueFromRange) {
				ParameterValueFromRange<Double> casted = (ParameterValueFromRange<Double>) parameter;
				container.getChildren().add(new ParameterValueFromRangeController(casted.getNameDisplayed(), 
																		casted.getName(), 
																		casted.getDefaultChoice(), 
																		casted.getOptionsPair()).getContents());
			} else if(parameter instanceof ParameterText) {
				
			} else {
				throw new IllegalArgumentException("Unsupporrted parameter type.");
			}
		}
	}
}
