package org.processmining.filterd.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterText;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.filterd.widgets.ParameterController;
import org.processmining.filterd.widgets.ParameterMultipleFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetController;
import org.processmining.filterd.widgets.ParameterTextController;
import org.processmining.filterd.widgets.ParameterValueFromRangeController;
import org.processmining.filterd.widgets.ParameterYesNoController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class FilterConfigPanelController {
	
	@FXML private VBox leftPanel;
	@FXML private VBox rightPanel;
	@FXML private Label title;
	private List<ParameterController> controllers;
	private boolean placeInLeftPane;
	private VBox contents; // top-level box which contains all components in this config. panel  
	
	FilterConfigPanelController(String title, List<Parameter> parameters) {
		placeInLeftPane = true;
		controllers = new ArrayList<>();
		// load UI
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/gui/fxml/FilterConfigPanel.fxml"));
		loader.setController(this);
        try {
            contents = (VBox) loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // populate UI
        this.title.setText(title);
        populateFromParameters(parameters);
	}
	
	private void populateFromParameters(List<Parameter> parameters) {
		for(Parameter parameter : parameters) {
			VBox container;
			// pick whether to place in left or right side of the panel
			if(placeInLeftPane) {
				container = leftPanel;
			} else {
				container = rightPanel;
			}
			placeInLeftPane = !placeInLeftPane; // change for the next time
			// add UI node
			if(parameter instanceof ParameterYesNo) {
				ParameterYesNo casted = (ParameterYesNo) parameter;
				ParameterController controller = new ParameterYesNoController(casted.getNameDisplayed(), 
						 								 casted.getName(), 
						 								 casted.getDefaultChoice());
				container.getChildren().add(controller.getContents());
				controllers.add(controller);
			} else if(parameter instanceof ParameterOneFromSet) {
				ParameterOneFromSet casted = (ParameterOneFromSet) parameter;
				ParameterController controller = new ParameterOneFromSetController(casted.getNameDisplayed(), 
															  casted.getName(), 
														  	  casted.getDefaultChoice(), 
														  	  casted.getOptions());
				container.getChildren().add(controller.getContents());
				controllers.add(controller);
			} else if(parameter instanceof ParameterMultipleFromSet) {
				ParameterMultipleFromSet casted = (ParameterMultipleFromSet) parameter;
				ParameterController controller = new ParameterMultipleFromSetController(casted.getNameDisplayed(), 
																   casted.getName(), 
																   casted.getDefaultChoice(), 
																   casted.getOptions());
				container.getChildren().add(controller.getContents());
				controllers.add(controller);
			} else if(parameter instanceof ParameterValueFromRange) {
				ParameterValueFromRange<Double> casted = (ParameterValueFromRange<Double>) parameter;
				ParameterController controller = new ParameterValueFromRangeController(casted.getNameDisplayed(), 
																  casted.getName(), 
																  casted.getDefaultChoice(), 
																  casted.getOptionsPair());
				container.getChildren().add(controller.getContents());
				controllers.add(controller);
			} else if(parameter instanceof ParameterText) {
				ParameterText casted = (ParameterText) parameter;
				ParameterController controller = new ParameterTextController(casted.getNameDisplayed(), 
														casted.getName(), 
														casted.getDefaultChoice());
				container.getChildren().add(controller.getContents());
				controllers.add(controller);
			} else {
				throw new IllegalArgumentException("Unsupporrted parameter type.");
			}
		}
	}
	
	public List<ParameterController> getControllers() {
		return controllers;
	}
	
	public VBox getContents() {
		return contents;
	}
}
