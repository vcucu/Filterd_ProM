package org.processmining.filterd.gui;

import java.util.ArrayList;
import java.util.List;

import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterText;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.filterd.widgets.ParameterMultipleFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetController;
import org.processmining.filterd.widgets.ParameterTextController;
import org.processmining.filterd.widgets.ParameterValueFromRangeController;
import org.processmining.filterd.widgets.ParameterYesNoController;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class FilterConfigPanelController {
	
//	@FXML private VBox container;
	@FXML private VBox leftPanel;
	@FXML private VBox rightPanel;
	@FXML private Label title;
	private List<Node> nodes; // list of UI elements
	private boolean placeInLeftPane; 
	
	FilterConfigPanelController() {
		placeInLeftPane = true;
		nodes = new ArrayList<>();
	}
	
	public void setTitle(String title) {
		this.title.setText(title);
	}
	
	public void populateContainer(List<Parameter> parameters) {
		for(Parameter parameter : parameters) {
			VBox container;
			// pick whether to place in left or right side of the panel
			if(placeInLeftPane) {
				container = leftPanel;
			} else {
				container = rightPanel;
			}
			placeInLeftPane = !placeInLeftPane;
			// add UI node
			if(parameter instanceof ParameterYesNo) {
				ParameterYesNo casted = (ParameterYesNo) parameter;
				Node node = new ParameterYesNoController(casted.getNameDisplayed(), 
						 								 casted.getName(), 
						 								 casted.getDefaultChoice()).getContents();
				container.getChildren().add(node);
				nodes.add(node);
			} else if(parameter instanceof ParameterOneFromSet) {
				ParameterOneFromSet casted = (ParameterOneFromSet) parameter;
				Node node = new ParameterOneFromSetController(casted.getNameDisplayed(), 
															  casted.getName(), 
														  	  casted.getDefaultChoice(), 
														  	  casted.getOptions()).getContents();
				container.getChildren().add(node);
				nodes.add(node);
			} else if(parameter instanceof ParameterMultipleFromSet) {
				ParameterMultipleFromSet casted = (ParameterMultipleFromSet) parameter;
				Node node = new ParameterMultipleFromSetController(casted.getNameDisplayed(), 
																   casted.getName(), 
																   casted.getDefaultChoice(), 
																   casted.getOptions()).getContents();
				container.getChildren().add(node);
				nodes.add(node);
			} else if(parameter instanceof ParameterValueFromRange) {
				ParameterValueFromRange<Double> casted = (ParameterValueFromRange<Double>) parameter;
				Node node = new ParameterValueFromRangeController(casted.getNameDisplayed(), 
																  casted.getName(), 
																  casted.getDefaultChoice(), 
																  casted.getOptionsPair()).getContents();
				container.getChildren().add(node);
				nodes.add(node);
			} else if(parameter instanceof ParameterText) {
				ParameterText casted = (ParameterText) parameter;
				Node node = new ParameterTextController(casted.getNameDisplayed(), 
														casted.getName(), 
														casted.getDefaultChoice()).getContents();
				container.getChildren().add(node);
				nodes.add(node);
			} else {
				throw new IllegalArgumentException("Unsupporrted parameter type.");
			}
		}
	}
	
	public List<Node> getNodes() {
		return nodes;
	}
}
