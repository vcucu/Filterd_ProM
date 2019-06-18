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
	String key;
	ParameterRangeFromRange<Double> range;
	ParameterMultipleFromSet desiredValues;
	ParameterOneFromSet parameterType;

	public FilterdEventAttrNumericalConfig(XLog log, Filter filterType, String key) {
		super(log, filterType);
		this.key = key; 
		parameters = new ArrayList<Parameter>();
		ArrayList<Double> defaultPair = new ArrayList<>();
		ArrayList<Double> optionsPair = new ArrayList<>();

		String defaultSelect = "Choose different values.";
		ArrayList<String> selectList = new ArrayList<>();
		selectList.add(defaultSelect);
		selectList.add("Choose from interval.");// filter in or filter out
		parameterType = new ParameterOneFromSet("parameterType",
				"", defaultSelect, selectList);

		String defaultOption = "Filter in";
		ArrayList<String> optionList = new ArrayList<>();
		optionList.add(defaultOption);
		optionList.add("Filter out");// filter in or filter out
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
		for (XTrace trace: log) {
			for (XEvent event : trace) {
				if (!event.getAttributes().containsKey(key)) continue;
				Double value = Double.parseDouble(event.getAttributes().get(key).toString());
				if (!values.contains(value)) values.add(value);
			}
		}

		Collections.sort(values);
		
		List<String> stringValues = values.stream().map(x -> x.toString())
		        .collect(Collectors.toList());
		
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
	public NestedFilterConfigPanelController getConfigPanel() {
		NestedFilterConfigPanelController nestedPanel =  new NestedFilterConfigPanelController(parameters);

		ParameterOneFromSetController parameterControl = (ParameterOneFromSetController)
				nestedPanel.getControllers().stream()
				.filter(c -> c.getName().equals("parameterType"))
				.findFirst()
				.get();
		ComboBox<String> comboBox = parameterControl.getComboBox();
		
		if (!comboBox.getValue().contains("interval")) {
		ParameterRangeFromRangeController<Double> rangeControl;
		 rangeControl = 
				(ParameterRangeFromRangeController<Double>)
				nestedPanel.getControllers().stream()
				.filter(c -> c.getName().equals("range"))
				.findFirst()
				.get();
		 rangeControl.getContents().setVisible(false);
		 rangeControl.getContents().setManaged(false);
		}

		comboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override 
			public void changed(ObservableValue ov, String oldValue, String newValue) {
				ParameterMultipleFromSetController desiredControl = (ParameterMultipleFromSetController)
						nestedPanel.getControllers().stream()
						.filter(c -> c.getName().equals("desiredValues"))
						.findFirst()
						.get();
				
				ParameterRangeFromRangeController<Double> rangeControl;
				 rangeControl = 
						(ParameterRangeFromRangeController<Double>)
						nestedPanel.getControllers().stream()
						.filter(c -> c.getName().equals("range"))
						.findFirst()
						.get();

				if (!newValue.contains("interval") && parameters.contains(range)) {
					desiredControl.getContents().setVisible(true);
					desiredControl.getContents().setManaged(true);
					rangeControl.getContents().setVisible(false);
					rangeControl.getContents().setManaged(false);
				} else if (newValue.contains("interval") && parameters.contains(desiredValues)) {
					desiredControl.getContents().setVisible(false);
					desiredControl.getContents().setManaged(false);
					rangeControl.getContents().setVisible(true);
					rangeControl.getContents().setManaged(true);
				}
			}
		});
		return nestedPanel;
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};
	
	public String getKey() {
		return key;
	}


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
