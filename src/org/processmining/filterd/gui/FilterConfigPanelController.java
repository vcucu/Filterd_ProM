package org.processmining.filterd.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.configurations.FilterdAbstractReferencingConfig;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.widgets.ParameterController;
import org.processmining.filterd.widgets.ParameterOneFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetExtendedController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class FilterConfigPanelController extends AbstractFilterConfigPanelController {
	
	@FXML private HBox mainPanel;
	@FXML private VBox rightPanel;
	@FXML private VBox othersPanel;
	@FXML private VBox slidersPanel;
	@FXML private VBox maybesPanel;
	@FXML private Label title;
	
	private FilterdAbstractConfig owner;
	
	public FilterConfigPanelController() {
		controllers = new ArrayList<>();
	}
	
	public FilterConfigPanelController(String title, List<Parameter> parameters, FilterdAbstractConfig owner) {
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
        if (owner instanceof FilterdAbstractReferencingConfig) {
        	// if the config is referencing, remove its duplicate parameters from the concrete reference before populating the config panel.
        	List<Parameter> referenceableParameters = ((FilterdAbstractReferencingConfig) owner).getConcreteReference().getParameters(); // get the parameters from the concrete reference
        	parameters.removeAll(referenceableParameters); // remove the parameters from the concrete reference from the referencing config.
        	populateFromParameters(parameters); // populate the config panel.
        	parameters.addAll(referenceableParameters); // restore the previously removed parameters.
        } else {
        	// if the config is not referencing, populate using all parameters.
        	populateFromParameters(parameters); // populate the config panel.
        }
        
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
					parameter.getChosen(), 
					parameter.getOptions(),
					casted);
		} else {
			controller = new ParameterOneFromSetController(parameter.getNameDisplayed(), 
					parameter.getName(),
					parameter.getChosen(), 
					parameter.getOptions());
		}
		getNextContainer(parameter).getChildren().add(controller.getContents());
		controllers.add(controller);
	}
	
	@Override
	public VBox getNextContainer(Parameter param) {
		if (param.getDisappearable()) {
			return maybesPanel;
		} else if (needsBigContainer(param)) {
			VBox newBox = new VBox();
			int index = mainPanel.getChildren().size() - 1;
			mainPanel.getChildren().add(index, newBox);
			return newBox;
		} else if (hasSlider(param)) {
			return slidersPanel;
		} else {
			return othersPanel;
		}
	}
	
	/**
	 * Returns whether the parameter needs a big container. 
	 * There are two types of parameters that require a big container:
	 * ParameterMultipleFromSet and OneFromSetExtended.
	 */
	private boolean needsBigContainer(Parameter param) {
		return (param instanceof ParameterMultipleFromSet ||
				(param instanceof ParameterOneFromSet &&
						((ParameterOneFromSet) param).getCreatesReference()));
	}
	
	/**
	 * Returns whether the parameter has a slider
	 */
	private boolean hasSlider(Parameter param) {
		return ((param instanceof ParameterRangeFromRange) ||
				(param instanceof ParameterValueFromRange));
	}

	public List<ParameterController> getControllers() {
		return controllers;
	}
	
	public VBox getRoot() {
		return root;
	}
}