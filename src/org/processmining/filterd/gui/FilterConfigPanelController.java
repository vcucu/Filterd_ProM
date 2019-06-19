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
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Default configuration panel for filters. Uses all the methods from its
 * abstract parent class, but has the addition of special parameter one from set
 * extended.
 * 
 * @author Filip Davidovic
 */
public class FilterConfigPanelController extends AbstractFilterConfigPanelController {

	@FXML
	private HBox mainPanel; // main panel to place components
	@FXML
	private VBox rightPanel; // right panel to place components
	@FXML
	private VBox othersPanel; // panel to place other components
	@FXML
	private VBox slidersPanel; // panel to place slider components (parameter value from range and range from range)
	@FXML
	private VBox maybesPanel; // panel to add disappearable components
	@FXML
	private Label title; // title of the filter configuration panel

	private FilterdAbstractConfig owner;

	/**
	 * Constructor used in the tests. It should not be used in actual code!
	 */
	public FilterConfigPanelController() {
		controllers = new ArrayList<>();
	}

	/**
	 * Default constructor which should be used in actual code.
	 * 
	 * @param title
	 *            title for the configuration panel
	 * @param parameters
	 *            list of parameters that the configuration panel should contain
	 * @param owner
	 *            filter configuration which this filter configuration panel is
	 *            associated with
	 */
	public FilterConfigPanelController(String title, List<Parameter> parameters, FilterdAbstractConfig owner) {
		controllers = new ArrayList<>(); // list of controllers is initially empty (populated later)
		this.owner = owner;
		// load UI
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/org/processmining/filterd/gui/fxml/FilterConfigPanel.fxml"));
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
			List<Parameter> referenceableParameters = ((FilterdAbstractReferencingConfig) owner).getConcreteReference()
					.getParameters(); // get the parameters from the concrete reference
			parameters.removeAll(referenceableParameters); // remove the parameters from the concrete reference from the referencing config.
			populateFromParameters(parameters); // populate the config panel.
			parameters.addAll(referenceableParameters); // restore the previously removed parameters.
		} else {
			// if the config is not referencing, populate using all parameters.
			populateFromParameters(parameters); // populate the config panel.
		}

	}

	/**
	 * Override this method to accommodate for parameter one from set extended.
	 */
	@Override
	public void addParameterOneFromSet(ParameterOneFromSet parameter) {
		ParameterController controller;
		if (parameter.getCreatesReference()) {
			// if the parameter is creating a reference we should create a one from set extended parameter
			if (!(owner instanceof FilterdAbstractReferencingConfig)) {
				throw new IllegalStateException(
						"Filter configuration is not Referencing, but there is a ParameterOneFromSet that creates a reference.");
			}
			FilterdAbstractReferencingConfig casted = (FilterdAbstractReferencingConfig) owner;
			controller = new ParameterOneFromSetExtendedController(parameter.getNameDisplayed(), parameter.getName(),
					parameter.getChosen(), parameter.getOptions(), casted);
		} else {
			// if the parameter is not creating a reference we should create a regular one from set parameter
			controller = new ParameterOneFromSetController(parameter.getNameDisplayed(), parameter.getName(),
					parameter.getChosen(), parameter.getOptions());
		}
		// add the controller's contents to the appropriate container
		getNextContainer(parameter).getChildren().add(controller.getContents());
		// add controller to the list of controllers
		controllers.add(controller);
	}

	@Override
	public VBox getNextContainer(Parameter param) {
		if (param.getDisappearable()) {
			// if the parameter can disappear then return the maybes panel
			return maybesPanel;
		} else if (needsBigContainer(param)) {
			// if the parameter needs a big container, add new container to the main panel and return it
			VBox newBox = new VBox();
			int index = mainPanel.getChildren().size() - 1;
			mainPanel.getChildren().add(index, newBox);
			return newBox;
		} else if (hasSlider(param)) {
			// if the parameter has a slider then return the sliders panel
			return slidersPanel;
		} else {
			// otherwise return the others panel
			return othersPanel;
		}
	}

	/**
	 * Returns whether the parameter needs a big container. There are two types
	 * of parameters that require a big container: ParameterMultipleFromSet and
	 * OneFromSetExtended.
	 */
	private boolean needsBigContainer(Parameter param) {
		return (param instanceof ParameterMultipleFromSet
				|| (param instanceof ParameterOneFromSet && ((ParameterOneFromSet) param).getCreatesReference()));
	}

	/**
	 * Returns whether the parameter has a slider
	 */
	private boolean hasSlider(Parameter param) {
		return ((param instanceof ParameterRangeFromRange) || (param instanceof ParameterValueFromRange));
	}

	public List<ParameterController> getControllers() {
		return controllers;
	}

	public VBox getRoot() {
		return root;
	}
}