package org.processmining.filterd.configurations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.extension.XExtension;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.parameters.ParameterText;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.filterd.tools.EmptyLogException;
import org.processmining.filterd.widgets.ParameterController;
import org.processmining.filterd.widgets.ParameterMultipleFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetController;
import org.processmining.filterd.widgets.ParameterRangeFromRangeController;
import org.processmining.filterd.widgets.ParameterTextController;
import org.processmining.filterd.widgets.ParameterValueFromRangeController;
import org.processmining.filterd.widgets.ParameterYesNoController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;

public abstract class FilterdAbstractConfig {

	protected Filter filterType;
	protected XLog log;
	protected List<Parameter> parameters;
	protected boolean isValid;
	protected XEventClassifier classifier;
	protected List<XExtension> standardExtensions;
	protected boolean isAttribute; // checks whether selected string is attribute or complex classifier
	protected FilterConfigPanelController configPanel = null;

	public FilterdAbstractConfig(XLog log, Filter filterType) throws EmptyLogException {

		this.filterType = filterType;
		this.checkEmptyLog(log);
		this.setLog(log);

	}

	public XEventClassifier getClassifier() {
		return classifier;
	}

	public void setClassifier(XEventClassifier classifier) {
		this.classifier = classifier;
	}

	public Filter getFilterType() {
		return filterType;
	}

	public void setFilterType(Filter filterType) {
		this.filterType = filterType;
	}

	public XLog getLog() {
		return log;
	}

	public void checkEmptyLog(XLog candidateLog) throws EmptyLogException {
		try {
			candidateLog.get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new EmptyLogException("The used log cannot be empty");
		}
	}

	/**
	 * Setter for the {@log} attribute. Invokes {@checkValidity(log)}. If it
	 * returns true, it sets the log to the corresponding value. If it returns
	 * false, it sets log to null and throws exception.
	 * 
	 * @param log
	 *            the log to be set
	 * @throws InputMismatchException
	 */
	public void setLog(XLog candidateLog) {

		if (this.checkValidity(candidateLog)) {
			this.log = candidateLog;
			isValid = true;
		} else {
			// raise error
			isValid = false;
		}

	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public boolean isValid() {
		return isValid;
	}

	/**
	 * Get the parameter object according to its name.
	 * 
	 * @param whichParameter
	 *            the name of the parameter whose type is to be returned
	 * @return the parameter object or null if no parameter with
	 *         {@whichParameter} name does not exist.
	 */
	public Parameter getParameter(String whichParameter) {

		for (Parameter parameter : parameters) {
			if (parameter.getName().equals(whichParameter)) {
				return parameter;
			}
		}
		return null;
	}

	/**
	 * Checks whether the imported log complies with the filter configuration(s)
	 * 
	 * @param log
	 *            the imported log in the cell
	 * @return true if the log is valid, false otherwise
	 */
	public abstract boolean checkValidity(XLog candidateLog);

	/**
	 * Populates the parameters with information from the configuration panel.
	 * 
	 * @return concrete configuration of the configuration panel
	 */

	public FilterdAbstractConfig populate(AbstractFilterConfigPanelController abstractComponent) {

		FilterConfigPanelController component = (FilterConfigPanelController) abstractComponent;
		List<ParameterController> controllers = component.getControllers();
		for (ParameterController controller : controllers) {
			//all cases assume that the controller has a name corresponding to the parameter name
			if (controller instanceof ParameterYesNoController) {
				ParameterYesNoController casted = (ParameterYesNoController) controller;
				ParameterYesNo param = (ParameterYesNo) getParameter(controller.getName());
				param.setChosen(casted.getValue());

			} else if (controller instanceof ParameterOneFromSetController) {
				ParameterOneFromSetController casted = (ParameterOneFromSetController) controller;
				ParameterOneFromSet param = (ParameterOneFromSet) getParameter(controller.getName());
				param.setChosen(casted.getValue());

			} else if (controller instanceof ParameterMultipleFromSetController) {
				ParameterMultipleFromSetController casted = (ParameterMultipleFromSetController) controller;
				ParameterMultipleFromSet param = (ParameterMultipleFromSet) getParameter(controller.getName());
				param.setChosen(casted.getValue());

			} else if (controller instanceof ParameterValueFromRangeController) {
				ParameterValueFromRangeController casted = (ParameterValueFromRangeController) controller;
				ParameterValueFromRange param = (ParameterValueFromRange) getParameter(controller.getName());
				param.setChosen(casted.getValue());

			} else if (controller instanceof ParameterTextController) {
				ParameterTextController casted = (ParameterTextController) controller;
				ParameterText param = (ParameterText) getParameter(controller.getName());
				param.setChosen(casted.getValue());

			} else if (controller instanceof ParameterRangeFromRangeController) {
				ParameterRangeFromRangeController casted = (ParameterRangeFromRangeController) controller;
				ParameterRangeFromRange param = (ParameterRangeFromRange) getParameter(controller.getName());
				param.setChosenPair(casted.getValue());

			} else {
				throw new IllegalArgumentException("Unsupporrted controller type.");
			}

		}
		return this;
	}

	/**
	 * Checks whether all components from the configuration panel have a mapping
	 * to all parameters of the concrete configuration.
	 */
	public abstract boolean canPopulate(FilterConfigPanelController component);

	/**
	 * Returns the configuration panel which is used by the
	 * {@populate(component)} and {@canPopulate(component)}.
	 * 
	 * @return the concrete configuration panel
	 */
	public abstract AbstractFilterConfigPanelController getConfigPanel();

	/**
	 * Invokes the {@filter(PluginContext context, XLog log, List<Parameter>
	 * parameters)} method of the concrete {@filterType}
	 * 
	 * @param context
	 *            the PluginContext
	 * @return the filtered log
	 */
	public XLog filter() {
		return filterType.filter(log, parameters);
	}

	/**
	 * Method for adding all the listeners to the configuration.
	 */
	public void parameterListeners() {
		// Loop over all the controllers.
		for (ParameterController parameter : configPanel.getControllers()) {
			// Want to add a listener to the parameter that holds the selected
			// attribute.
			if (parameter.getName().equals("attrType")) {
				// Get the parameter.
				ParameterOneFromSetController casted = (ParameterOneFromSetController) parameter;
				// Get the corresponding combo box.
				ComboBox<String> comboBox = casted.getComboBox();
				// Add a listener to this combo box based on the selected value.
				comboBox.valueProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue ov, String oldValue, String newValue) {
						final XLog Llog = log;
						List<Parameter> params = parameters;
						// If log is not null.
						if (Llog != null) {
							// Loop over all the controllers.
							for (ParameterController changingParameter : configPanel.getControllers()) {

								// Get the parameter that we want to change
								// based on the value of the combo box.
								if (changingParameter.getName().equals("firstattrValues")) {

									// Get the changing parameter.
									ParameterMultipleFromSetController castedChanging = (ParameterMultipleFromSetController) changingParameter;
									// Create a set of attribute values.
									Set<String> attributeValues = new HashSet<>();

									// Loop over every trace in the Llog.
									for (XTrace trace : Llog) {

										// Loop over every trace in the event.
										for (XEvent event : trace) {

											// Get the attributes of the event.
											XAttributeMap eventAttrs = event.getAttributes();
											// If the key is present
											if (eventAttrs.containsKey(newValue))
												// Add all values to the list.
												attributeValues.add(eventAttrs.get(newValue).toString());
										}
									}
									// Set an attribute values list.
									List<String> attributeValuesList = new ArrayList<String>(attributeValues);
									// Change the parameter based on these
									// parameters.
									// change options.
									((ParameterMultipleFromSet) params.get(2)).setOptions(attributeValuesList);
									// Change chosen values.
									((ParameterMultipleFromSet) params.get(2)).setChosen(attributeValuesList);
									// Change default values.
									((ParameterMultipleFromSet) params.get(2)).setDefaultChoice(attributeValuesList);
									// Change options.
									castedChanging.changeOptions(attributeValuesList);

								}
								// Get another parameter that we want to change
								// based on the value of the combo box.
								if (changingParameter.getName().equals("endattrValues")) {

									// Get the parameter.
									ParameterMultipleFromSetController castedChanging = (ParameterMultipleFromSetController) changingParameter;
									// Set to hold all attribute values.
									Set<String> attributeValues = new HashSet<>();

									// Loop over every trace in the Llog.
									for (XTrace trace : Llog) {

										// Loop over every event in the trace.
										for (XEvent event : trace) {

											// Get all the attributes of the
											// events.
											XAttributeMap eventAttrs = event.getAttributes();
											// If it contains the key.
											if (eventAttrs.containsKey(newValue))
												attributeValues.add(eventAttrs.get(newValue).toString());
										}
									}
									// Create list from the set.
									List<String> attributeValuesList = new ArrayList<String>(attributeValues);
									// Change the values of the parameter
									// accordingly.
									((ParameterMultipleFromSet) params.get(3)).setOptions(attributeValuesList);
									((ParameterMultipleFromSet) params.get(3)).setChosen(attributeValuesList);
									((ParameterMultipleFromSet) params.get(3)).setDefaultChoice(attributeValuesList);
									castedChanging.changeOptions(attributeValuesList);

								}
							}

						}
					}
				});

			}
		}
	}
}
