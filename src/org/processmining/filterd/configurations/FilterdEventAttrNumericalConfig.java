package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.gui.NestedFilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.filterd.widgets.ParameterMultipleFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetController;
import org.processmining.filterd.widgets.ParameterRangeFromRangeController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;

public class FilterdEventAttrNumericalConfig extends FilterdAbstractReferenceableConfig {
	
	// The key.
	String key;
	// Parameter for selecting the range.
	ParameterRangeFromRange<Double> range;
	// Desired attribute values.
	ParameterMultipleFromSet desiredValues;
	// Type ofparameter.
	ParameterOneFromSet parameterType;

	public FilterdEventAttrNumericalConfig(XLog log, Filter filterType, String key) {
		super(log, filterType);
		// Set the key.
		this.key = key;
		// Create list to hold the parameters.
		parameters = new ArrayList<Parameter>();
		// Create list to hold the default pair.
		ArrayList<Double> defaultPair = new ArrayList<>();
		// Create list to hold the options pair.
		ArrayList<Double> optionsPair = new ArrayList<>();

		// String for parameter.
		String defaultSelect = "Choose different values.";
		// Create list for selecting.
		ArrayList<String> selectList = new ArrayList<>();

		// Add values.
		selectList.add(defaultSelect);
		selectList.add("Choose from interval.");// filter in or filter out
		// Create parameter to selectt the type of parameter.
		parameterType = new ParameterOneFromSet("parameterType",
				"", defaultSelect, selectList);

		// String for parameter.
		String defaultOption = "Filter in";
		// Create list for options.
		ArrayList<String> optionList = new ArrayList<>();
		// Add values.
		optionList.add(defaultOption);
		optionList.add("Filter out");// filter in or filter out
		
		// Create parameter for selecting the filtering option.
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType",
				"Select option for filtering.", defaultOption, optionList);

		// should you remove empty traces
		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", 
				"Keep empty traces.", true);

		// should you keep events which do not have the specified attribute
		ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", 
				"Keep events if attribute not specified.", false);

		ArrayList<Double> values = new ArrayList<>();
		/*populate the array times with the numerical values of all events */
		// Loop over all traces in the log.
		for (XTrace trace: log) {
			// Loop over all events in the trace.
			for (XEvent event : trace) {
				if (!event.getAttributes().containsKey(key)) continue;
				Double value = Double.parseDouble(event.getAttributes().get(key).toString());
				// Add all values to the list.
				if (!values.contains(value)) values.add(value);
			}
		}

		// Sort the values.
		Collections.sort(values);
		
		// Get the values in string format for the parameter.
		List<String> stringValues = values.stream().map(x -> x.toString())
		        .collect(Collectors.toList());
		
		// Create the parameter for selecting the desired numerical values.
		desiredValues = new ParameterMultipleFromSet(
				"desiredValues", "Choose values:", stringValues, stringValues);
		desiredValues.setDisappearable(true);

		/* populate the parameters */
		defaultPair.add(values.get(0));
		defaultPair.add(values.get(values.size() - 1));
		optionsPair.add(values.get(0));
		optionsPair.add(values.get(values.size() - 1));
		// slider values parameter
		range = new ParameterRangeFromRange<Double>("range",
				"Select interval to choose from.", defaultPair, optionsPair, Double.TYPE);
		range.setDisappearable(true);

		/* add the parameters */
		parameters.add(selectionType);
		parameters.add(parameterType);
		parameters.add(desiredValues);
		parameters.add(range);
		parameters.add(traceHandling);
		parameters.add(eventHandling);
	}

	@Override
	/**
	 * Getter for the configuration panel.
	 */
	public NestedFilterConfigPanelController getConfigPanel() {
		// Create new nested panel.
		NestedFilterConfigPanelController nestedPanel =  new NestedFilterConfigPanelController(parameters);

		// Get the parameter "parameterType".
		ParameterOneFromSetController parameterControl = (ParameterOneFromSetController)
				nestedPanel.getControllers().stream()
				.filter(c -> c.getName().equals("parameterType"))
				.findFirst()
				.get();
		// Get this parameter's corresponding combo box.
		ComboBox<String> comboBox = parameterControl.getComboBox();
		
		// If this combo box's value does not contain "interval"
		// We make certain parameters invisible.
		if (!comboBox.getValue().contains("interval")) {
			// RangeControl parameter can be set to invsible.
			ParameterRangeFromRangeController<Double> rangeControl;
			 rangeControl = 
					(ParameterRangeFromRangeController<Double>)
					nestedPanel.getControllers().stream()
					.filter(c -> c.getName().equals("range"))
					.findFirst()
					.get();
			 // Set to invisible.
			 rangeControl.getContents().setVisible(false);
			 // Set to unmanaged.
			 rangeControl.getContents().setManaged(false);
		}

		// Add listener to the combo box.
		comboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override 
			public void changed(ObservableValue ov, String oldValue, String newValue) {
				// Get the parameter that holds the desired values.
				ParameterMultipleFromSetController desiredControl = (ParameterMultipleFromSetController)
						nestedPanel.getControllers().stream()
						.filter(c -> c.getName().equals("desiredValues"))
						.findFirst()
						.get();
				// Get the parameter that holds the ranges.
				ParameterRangeFromRangeController<Double> rangeControl;
				 rangeControl = 
						(ParameterRangeFromRangeController<Double>)
						nestedPanel.getControllers().stream()
						.filter(c -> c.getName().equals("range"))
						.findFirst()
						.get();

				// If we don't want an interval and there is a range. 
			    // We need to change visibility.
				if (!newValue.contains("interval") && parameters.contains(range)) {
					// Set the desired values parameter to visible and managed.
					desiredControl.getContents().setVisible(true);
					desiredControl.getContents().setManaged(true);
					// Set the range parameter to invisible and unmanaged.
					rangeControl.getContents().setVisible(false);
					rangeControl.getContents().setManaged(false);
				}
				// Else if it we want an interval and the parameters contain
				// desired values.
				// We need to change visibility.
				else if (newValue.contains("interval") && parameters.contains(desiredValues)) {
					// Set the desired values parameter to invisible and 
					// unmanaged.
					desiredControl.getContents().setVisible(false);
					desiredControl.getContents().setManaged(false);
					// Set the range parameter to visisble and managed.
					rangeControl.getContents().setVisible(true);
					rangeControl.getContents().setManaged(true);
				}
			}
		});
		return nestedPanel;
	}

	/**
	 * Checks if the configuration can populate the parameters.
	 * 
	 * @param component The component that populates the parameters.
	 */
	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};
	
	/**
	 * Getter for the key.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Check if the parameters are still valid on the candidate log.
	 * 
	 * @param candidateLog the log to check.
	 */
	public boolean checkValidity(XLog log) {
		if (key == null) return true;
		
		if (!Toolbox.computeAttributes(log).contains(key)) {
			return false;
		}
		
		String parameter;
		
		/* check if the parameters are populated */
		try {
			parameter = parameterType.getChosen();
		} catch(Exception e) {
			return true;
		}
		
		/* if using the interval configuration.. */
		if (parameter.contains("interval")) {
			ArrayList<Double> pair = new ArrayList<>();
			
			/* check if the parameters are populated */
			try {
				pair.addAll(range.getChosenPair());
			} catch(Exception e) {
				return true;
			}
			
			/* get the chosen values */
			Double lower = pair.get(0);
			Double upper = pair.get(1);

			/* check if at least one of the events is in those bounds */
			for (XTrace trace : log) {
				for (XEvent event : trace) {
					if (!event.getAttributes().containsKey(key)) continue;
					Double value = Double.parseDouble(event.getAttributes().get(key).toString());
					if (lower <= value  && upper >= value) return true;
				}
			}
		} else {
			List<Double> values = new ArrayList<>();
			
			try {
				values = desiredValues.getChosen()
						.stream().map(x -> Double.parseDouble(x))
				        .collect(Collectors.toList());
			} catch(Exception e) {
				return true;
			}
			
			/* check if at least one of the events has one of the values */
			for (XTrace trace : log) {
				for (XEvent event : trace) {
					if (!event.getAttributes().containsKey(key)) continue;
					Double value = Double.parseDouble(event.getAttributes().get(key).toString());
					if (values.contains(value)) return true;
				}
			}
		}
	
		return false;
	}
}
