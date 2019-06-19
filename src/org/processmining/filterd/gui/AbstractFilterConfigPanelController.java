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

/**
 * Abstract class for all filter configuration panels. It contains basic methods
 * to create the UI, given a list of parameters. Since the UI is generated based
 * on the list of parameters, this procedure is completely automatic i.e. the
 * user does not have to program the configuration panel herself (although this
 * is possible as well).
 * 
 * @author Filip Davidovic
 */
public abstract class AbstractFilterConfigPanelController {

	protected List<ParameterController> controllers; // list of parameter controllers used to fetch their values while populating
	protected VBox root; // top-level box which contains all UI components in this configuration panel

	/**
	 * Abstract class which returns the container in which a UI component should
	 * be placed. This allows for exotic layouts which can be implemented per
	 * concrete class implementation.
	 * 
	 * @param parameter
	 *            parameter which is going to be placed in the container (used
	 *            to determine which containter to use)
	 * @return container in which the UI component should be added.
	 */
	abstract public VBox getNextContainer(Parameter parameter);

	/**
	 * Method to add a parameter yes no UI element.
	 * 
	 * @param parameter
	 *            parameter that the UI element should be based on.
	 */
	public void addParameterYesNo(ParameterYesNo parameter) {
		// create controller
		ParameterController controller = new ParameterYesNoController(parameter.getNameDisplayed(), parameter.getName(),
				parameter.getChosen());
		// add controller's contents to the next container
		getNextContainer(parameter).getChildren().add(controller.getContents());
		// add controller to the list of controllers
		controllers.add(controller);
	}

	/**
	 * Method to add a parameter one from set UI element.
	 * 
	 * @param parameter
	 *            parameter that the UI element should be based on.
	 */
	public void addParameterOneFromSet(ParameterOneFromSet parameter) {
		// create controller
		ParameterController controller = new ParameterOneFromSetController(parameter.getNameDisplayed(),
				parameter.getName(), parameter.getChosen(), parameter.getOptions());
		// add controller's contents to the next container
		getNextContainer(parameter).getChildren().add(controller.getContents());
		// add controller to the list of controllers
		controllers.add(controller);
	}

	/**
	 * Method to add a parameter multiple from set UI element.
	 * 
	 * @param parameter
	 *            parameter that the UI element should be based on.
	 */
	public void addParameterMultipleFromSet(ParameterMultipleFromSet parameter) {
		// create controller
		ParameterController controller = new ParameterMultipleFromSetController(parameter.getNameDisplayed(),
				parameter.getName(), parameter.getChosen(), parameter.getOptions());
		// add controller's contents to the next container
		getNextContainer(parameter).getChildren().add(controller.getContents());
		// add controller to the list of controllers
		controllers.add(controller);
	}

	/**
	 * Method to add a parameter value from range UI element.
	 * 
	 * @param <N>
	 *            type of the parameter (can be either Integer or Double)
	 * @param parameter
	 *            parameter that the UI element should be based on.
	 */
	public <N extends Number> void addParameterValueFromRange(ParameterValueFromRange<N> parameter) {
		// create controller
		ParameterController controller = new ParameterValueFromRangeController<N>(parameter.getNameDisplayed(),
				parameter.getName(), parameter.getChosen(), parameter.getOptionsPair(),
				parameter.getGenericTypeClass());
		// add controller's contents to the next container
		getNextContainer(parameter).getChildren().add(controller.getContents());
		// add controller to the list of controllers
		controllers.add(controller);
	}

	/**
	 * Method to add a parameter text UI element.
	 * 
	 * @param parameter
	 *            parameter that the UI element should be based on.
	 */
	public void addParameterText(ParameterText parameter) {
		// create controller
		ParameterController controller = new ParameterTextController(parameter.getNameDisplayed(), parameter.getName(),
				parameter.getChosen());
		// add controller's contents to the next container
		getNextContainer(parameter).getChildren().add(controller.getContents());
		// add controller to the list of controllers
		controllers.add(controller);
	}

	/**
	 * Method to add a parameter range from range UI element.
	 * 
	 * @param <N>
	 *            type of the parameter (can be either Integer or Double)
	 * @param parameter
	 *            parameter that the UI element should be based on.
	 */
	public <N extends Number> void addParameterRangeFromRange(ParameterRangeFromRange<N> parameter) {
		// create controller
		ParameterRangeFromRangeController<N> controller = new ParameterRangeFromRangeController<N>(
				parameter.getNameDisplayed(), parameter.getName(), parameter.getChosenPair(),
				parameter.getOptionsPair(), parameter.getGenericTypeClass());
		// add controller's contents to the next container
		getNextContainer(parameter).getChildren().add(controller.getContents());
		if (parameter.getName().equals("time-range")) {
			controller.setTimes(parameter.getTimes());
			controller.setTimeframe();
		}
		// add controller to the list of controllers
		controllers.add(controller);
	}

	/**
	 * Method used to generate parameter controllers from the list of
	 * parameters.
	 * 
	 * @param <N1>
	 *            type of the value from range parameter (can be either Integer
	 *            or Double)
	 * @param <N2>
	 *            type of the value from range parameter (can be either Integer
	 *            or Double)
	 * @param parameters
	 *            list of parameters from which the UI components should be
	 *            based on
	 */
	public <N1 extends Number, N2 extends Number> void populateFromParameters(List<Parameter> parameters) {
		// iterate through all parameters in the list and create their UI counterparts 
		for (Parameter parameter : parameters) {
			if (parameter instanceof ParameterYesNo) {
				// cast the parameter 
				ParameterYesNo casted = (ParameterYesNo) parameter;
				// create the UI component
				addParameterYesNo(casted);
			} else if (parameter instanceof ParameterOneFromSet) {
				// cast the parameter 
				ParameterOneFromSet casted = (ParameterOneFromSet) parameter;
				// create the UI component
				addParameterOneFromSet(casted);
			} else if (parameter instanceof ParameterMultipleFromSet) {
				// cast the parameter 
				ParameterMultipleFromSet casted = (ParameterMultipleFromSet) parameter;
				// create the UI component
				addParameterMultipleFromSet(casted);
			} else if (parameter instanceof ParameterValueFromRange) {
				// cast the parameter 
				ParameterValueFromRange<N1> casted = (ParameterValueFromRange<N1>) parameter;
				// create the UI component
				addParameterValueFromRange(casted);
			} else if (parameter instanceof ParameterText) {
				// cast the parameter 
				ParameterText casted = (ParameterText) parameter;
				// create the UI component
				addParameterText(casted);
			} else if (parameter instanceof ParameterRangeFromRange) {
				// cast the parameter 
				ParameterRangeFromRange<N2> casted = (ParameterRangeFromRange<N2>) parameter;
				// create the UI component
				addParameterRangeFromRange(casted);
			} else {
				// should never be reached. added as a safety check
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
