package org.processmining.filterd.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.configurations.FilterdAbstractReferencingConfig;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.widgets.ParameterController;
import org.processmining.filterd.widgets.ParameterOneFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetExtendedController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class FilterConfigPanelController extends AbstractFilterConfigPanelController {
	
	@FXML private VBox leftPanel;
	@FXML private VBox rightPanel;
	@FXML private Label title;
	private boolean placeInLeftPane;
	private FilterdAbstractConfig owner;
	
	public FilterConfigPanelController(String title, List<Parameter> parameters, FilterdAbstractConfig owner) {
		placeInLeftPane = true;
		controllers = new ArrayList<>();
		this.owner = owner;
		// load UI
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/gui/fxml/FilterConfigPanel.fxml"));
		loader.setController(this);
        try {
            root = (VBox) loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // populate UI
        this.title.setText(title);
        populateFromParameters(parameters);
	}
	
	@Override
	public void addParameterOneFromSet(ParameterOneFromSet parameter) {
		ParameterController controller;
		if(parameter.getCreatesReference()) {
			if(!(owner instanceof FilterdAbstractReferencingConfig)) {
				throw new IllegalStateException("Filter configuration is not Referencing, but there is a ParameterOneFromSet that creates a reference.");
			}
			FilterdAbstractReferencingConfig casted = (FilterdAbstractReferencingConfig) owner;
			controller = new ParameterOneFromSetExtendedController(parameter.getNameDisplayed(), 
					parameter.getName(),
					parameter.getChosen() == null ? parameter.getDefaultChoice() : parameter.getChosen(), 
					parameter.getOptions(),
					casted);
		} else {
			controller = new ParameterOneFromSetController(parameter.getNameDisplayed(), 
					parameter.getName(),
					parameter.getChosen() == null ? parameter.getDefaultChoice() : parameter.getChosen(), 
					parameter.getOptions());
		}
		getNextContainer().getChildren().add(controller.getContents());
		controllers.add(controller);
	}
	
	@Override
	protected VBox getNextContainer() {
		VBox container;
		// pick whether to place in left or right side of the panel
		if(placeInLeftPane) {
			container = leftPanel;
		} else {
			container = rightPanel;
		}
		placeInLeftPane = !placeInLeftPane; // change for the next time
		return container;
	}
	
	public List<ParameterController> getControllers() {
		return controllers;
	}
	
	public VBox getRoot() {
		return root;
	}
}