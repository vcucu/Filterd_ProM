package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.filterd.tools.EmptyLogException;
import org.processmining.filterd.widgets.ParameterController;
import org.processmining.filterd.widgets.ParameterMultipleFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetController;
import org.processmining.filterd.widgets.ParameterValueFromRangeController;
import org.processmining.filterd.widgets.ParameterYesNoController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;

public class FilterdTraceFollowerConfig extends FilterdAbstractConfig {

	Set<String> eventKeys;

	public FilterdTraceFollowerConfig(XLog log, Filter filterType) throws EmptyLogException {
		super(log, filterType);
		this.log = log;

		// Initialize the configuration's parameters list.
		parameters = new ArrayList<>();

		// Do this by looping over every trace and collecting its attributes
		// and adding this to the set, except for time:timestamp
		eventKeys = new HashSet<>();
		for (XTrace trace : log) {

			for (XEvent event : trace) {

				for (String key : event.getAttributes().keySet()) {
					if (!key.equals("time:timestamp")) {
						eventKeys.add(key);
					}
				}

			}

		}

		// Convert the set into an array list because ParameterOneFromSet takes
		// a list as an argument.
		List<String> eventAttributesList = 
				new ArrayList<String>(eventKeys);

		// Create the parameter for selecting the attribute.
		ParameterOneFromSet attributeSelector = 
				new ParameterOneFromSet(
						"attrType", 
						"Select attribute", 
						eventAttributesList.get(0), 
						eventAttributesList);

		List<String> selectionTypeList = new ArrayList<String>();
		selectionTypeList.add("Directly followed");
		selectionTypeList.add("Never directly followed");
		selectionTypeList.add("Eventually followed");
		selectionTypeList.add("Never eventually followed");

		// Create the parameter for selecting the type.
		ParameterOneFromSet selectionType = new ParameterOneFromSet(
				"followType", 
				"Select follow type", 
				selectionTypeList.get(0), 
				selectionTypeList);

		Set<String> keyValues = new HashSet<>();

		for (XTrace trace : log) {

			for (XEvent event : trace) {

				XAttributeMap eventAttrs = event.getAttributes();
				if (eventAttrs.containsKey(eventAttributesList.get(0))) {
					keyValues.add(eventAttrs.get(eventAttributesList.get(0)).toString());
				}

			}

		}

		// To populate both the reference and follower event values with these
		// attribute values to start with. If the attribute is changed, so will
		// the values in both these parameters.
		List<String> attributeValuesList = new ArrayList<String>(keyValues);

		// Create parameter for reference event values.
		ParameterMultipleFromSet referenceParameter = 
				new ParameterMultipleFromSet(
						"firstattrValues",
						"Select reference values:",
						Arrays.asList(attributeValuesList.get(0)),
						attributeValuesList
						);

		// Create parameter for follower event values.
		ParameterMultipleFromSet followerParameter = 
				new ParameterMultipleFromSet(
						"endattrValues",
						"Select follower values:",
						Arrays.asList(attributeValuesList.get(0)),
						attributeValuesList
						);


		// Create parameter for value matching.
		ParameterYesNo valueMatchingParameter = new ParameterYesNo(
				"Value matching", 
				"Value matching", 
				false);

		List<String> sameOrDifferentList = new ArrayList<String>();
		sameOrDifferentList.add("The same value");
		sameOrDifferentList.add("Different values");

		// Create parameter for either same value or different value.
		ParameterOneFromSet sameOrDifferentParameter = new ParameterOneFromSet(
				"Same or Different value", 
				"Select same or different value", 
				sameOrDifferentList.get(0), 
				sameOrDifferentList);
		sameOrDifferentParameter.setDisappearable(true);

		// Create parameter for selecting the attribute whose value has to be
		// matched.
		ParameterOneFromSet valueMatchingAttributeParameter = 
				new ParameterOneFromSet(
						"Attribute for value matching", 
						"Select attribute", 
						eventAttributesList.get(0), 
						eventAttributesList);
		valueMatchingAttributeParameter.setDisappearable(true);

		// Create parameter for a time restriction.
		ParameterYesNo timeRestrictionParameter = new ParameterYesNo(
				"Time restrictions", 
				"Time restrictions", 
				false);

		List<String> shorterOrLongerList = new ArrayList<String>();
		shorterOrLongerList.add("Shorter");
		shorterOrLongerList.add("Longer");

		// Create parameter for selecting whether the time needs to be longer
		// or shorter than the time selected.
		ParameterOneFromSet shorterOrLongerParameter = new ParameterOneFromSet(
				"Shorter or longer", 
				"Select shorter or longer", 
				shorterOrLongerList.get(0), 
				shorterOrLongerList);
		shorterOrLongerParameter.setDisappearable(true);

		// Create parameter for selecting time duration.
		ParameterValueFromRange<Integer> timeDurationParameter = 
				new ParameterValueFromRange<Integer>(
						"duration", 
						"Select time duration", 
						1, 
						Arrays.asList(1, 999),
						Integer.TYPE);
		timeDurationParameter.setDisappearable(true);

		// Create parameter for selecting the time type.
		ParameterOneFromSet timeTypeParameter = 
				new ParameterOneFromSet(
						"timeType", 
						"Select time type", 
						"Millis", 
						Arrays.asList(
								"Millis",
								"Seconds",
								"Minutes",
								"Hours",
								"Days",
								"Weeks",
								"Years"));
		timeTypeParameter.setDisappearable(true);


		parameters.add(attributeSelector);
		parameters.add(selectionType);
		parameters.add(referenceParameter);
		parameters.add(followerParameter);
		parameters.add(timeRestrictionParameter);
		parameters.add(valueMatchingParameter);
		parameters.add(shorterOrLongerParameter);
		parameters.add(sameOrDifferentParameter);
		parameters.add(timeDurationParameter);
		parameters.add(valueMatchingAttributeParameter);
		parameters.add(timeTypeParameter);
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};

	@Override
	public AbstractFilterConfigPanelController getConfigPanel() {
		if (this.configPanel == null) {
			this.configPanel = new FilterConfigPanelController(
					"Filter Traces follower filter", 
					parameters, 
					this);
			parameterListeners();
		}

		return configPanel;
	}

	public void parameterListeners() {

		/* if the time restriction box in unchecked, then hide the parameters */
		ParameterYesNoController timeControl = (ParameterYesNoController)
				configPanel.getControllers().stream()
				.filter(c -> c.getName().equals("Time restrictions"))
				.findFirst()
				.get();

		if(!timeControl.getValue()) {
			setTimeVisible(false);
		}

		/* if the value matching box in unchecked, then hide the parameters */
		ParameterYesNoController valueControl = (ParameterYesNoController)
				configPanel.getControllers().stream()
				.filter(c -> c.getName().equals("Value matching"))
				.findFirst()
				.get();

		if(!valueControl.getValue()) {
			setValueVisible(false);
		}

		/* add listener to the time control such that if the checkbox is checked,
		 * the parameter appear.
		 */
		timeControl.getCheckbox().selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override 
			public void changed(ObservableValue ov, Boolean oldValue, Boolean newValue) {
				setTimeVisible(newValue);
			}
		});
		
		/* add listener to the value control such that if the checkbox is checked,
		 * the parameter appear.
		 */
		valueControl.getCheckbox().selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override 
			public void changed(ObservableValue ov, Boolean oldValue, Boolean newValue) {
				setValueVisible(newValue);
			}
		});


		for(ParameterController parameter : configPanel.getControllers()) {
			if (parameter.getName().equals("attrType")) {
				ParameterOneFromSetController casted = (ParameterOneFromSetController) parameter;
				ComboBox<String> comboBox = casted.getComboBox();
				comboBox.valueProperty().addListener(new ChangeListener<String>() {
					@Override 
					public void changed(ObservableValue ov, String oldValue, String newValue) {
						final XLog Llog = log;
						List<Parameter> params = parameters;
						if (Llog != null) {
							for (ParameterController changingParameter : configPanel.getControllers()) {

								if (changingParameter.getName().equals("firstattrValues")) {

									ParameterMultipleFromSetController castedChanging = 
											(ParameterMultipleFromSetController) changingParameter;
									Set<String> attributeValues = new HashSet<>();

									for (XTrace trace : Llog) {

										for (XEvent event : trace) {

											XAttributeMap eventAttrs = event.getAttributes();
											if (eventAttrs.containsKey(newValue))
												attributeValues.add(eventAttrs.get(newValue).toString());
										}
									}
									List<String> attributeValuesList = new ArrayList<String>(attributeValues);
									((ParameterMultipleFromSet) params.get(2))
									.setOptions(attributeValuesList);
									((ParameterMultipleFromSet) params.get(2))
									.setChosen(attributeValuesList);
									((ParameterMultipleFromSet) params.get(2))
									.setDefaultChoice(attributeValuesList);
									castedChanging.changeOptions(attributeValuesList);

								}
								if (changingParameter.getName().equals("endattrValues")) {

									ParameterMultipleFromSetController castedChanging = 
											(ParameterMultipleFromSetController) changingParameter;
									Set<String> attributeValues = new HashSet<>();

									for (XTrace trace : Llog) {

										for (XEvent event : trace) {

											XAttributeMap eventAttrs = event.getAttributes();
											if (eventAttrs.containsKey(newValue))
												attributeValues.add(eventAttrs.get(newValue).toString());
										}
									}
									List<String> attributeValuesList = new ArrayList<String>(attributeValues);
									((ParameterMultipleFromSet) params.get(3))
									.setOptions(attributeValuesList);
									((ParameterMultipleFromSet) params.get(3))
									.setChosen(attributeValuesList);
									((ParameterMultipleFromSet) params.get(3))
									.setDefaultChoice(attributeValuesList);
									castedChanging.changeOptions(attributeValuesList);

								}
							}

						}
					}
				});

			}
		}
	}

	/* method for setting the time parameters controller visible or invisible
	 * based on whether the time restriction checkbox is checked or not
	 */
	private void setTimeVisible(Boolean visible) {
		// make parameter for selecting whether the time needs to be longer
		// or shorter than the time selected (in)visible
		ParameterOneFromSetController shorterOrLongerControl = 
				(ParameterOneFromSetController) configPanel
				.getControllers().stream()
				.filter(c -> c.getName().equals("Shorter or longer"))
				.findFirst()
				.get();
		shorterOrLongerControl.getContents().setVisible(visible);
		shorterOrLongerControl.getContents().setManaged(visible);

		// make parameter for selecting time duration (in)visible
		ParameterValueFromRangeController<Integer> timeDurationParameter = 
				(ParameterValueFromRangeController<Integer>) configPanel
				.getControllers().stream()
				.filter(c -> c.getName().equals("duration"))
				.findFirst()
				.get();
		timeDurationParameter.getContents().setVisible(visible);
		timeDurationParameter.getContents().setManaged(visible);
		

		// make parameter for selecting the time type (in)visible
		ParameterOneFromSetController timeTypeParameter = 
				(ParameterOneFromSetController) configPanel
				.getControllers().stream()
				.filter(c -> c.getName().equals("timeType"))
				.findFirst()
				.get();
		timeTypeParameter.getContents().setVisible(visible);
		timeTypeParameter.getContents().setManaged(visible);
	}
	
	/* method for setting the value parameters controller visible or invisible
	 * based on whether the value matching checkbox is checked or not
	 */
	private void setValueVisible(Boolean visible) {
		// make parameter (in)visible
		ParameterOneFromSetController sameOrDifferentControl = 
				(ParameterOneFromSetController) configPanel
				.getControllers().stream()
				.filter(c -> c.getName().equals("Same or Different value"))
				.findFirst()
				.get();
		sameOrDifferentControl.getContents().setVisible(visible);
		sameOrDifferentControl.getContents().setManaged(visible);

		// make parameter for selecting the attribute whose value has to be
		// matched (in)visible
		ParameterOneFromSetController valueMatchingControl = 
				(ParameterOneFromSetController) configPanel
				.getControllers().stream()
				.filter(c -> c.getName().equals("Attribute for value matching"))
				.findFirst()
				.get();
		valueMatchingControl.getContents().setVisible(visible);
		valueMatchingControl.getContents().setManaged(visible);
	}

	public boolean checkValidity(XLog candidateLog) {
		if( parameters == null || candidateLog.equals(log) )
			return true;
		return false;
	}
}