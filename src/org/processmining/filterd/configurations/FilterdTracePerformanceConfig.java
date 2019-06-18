package org.processmining.filterd.configurations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.filterd.widgets.ParameterController;
import org.processmining.filterd.widgets.ParameterOneFromSetController;
import org.processmining.filterd.widgets.ParameterRangeFromRangeController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;

public class FilterdTracePerformanceConfig extends FilterdAbstractConfig {

	List<Integer> minAndMaxEvents;
	ArrayList<String> durations;

	public FilterdTracePerformanceConfig(XLog log, Filter filterType) {
		super(log, filterType);

		parameters = new ArrayList<>();
		durations = Toolbox.getDurations(log);
		
		// Initialize members based on the log.
		minAndMaxEvents = Toolbox.getminAdnMaxEventSize(log);

		// Create performance options parameter and set the option to duration
		// as default.
		ParameterOneFromSet performanceOptionsParameter = 
				new ParameterOneFromSet(
						"performanceOptions", 
						"Select performance option", 
						"filter on duration", 
						Arrays.asList(
								"filter on duration", 
								"filter on number of events"));
				
		// Use duration as default because this is also set in the performance
		// options parameter.
		ParameterRangeFromRange<Integer> thresholdParameter = 
				new ParameterRangeFromRange<Integer>(
						"time-range", 
						"Select test threshold", 
						Arrays.asList(0, durations.size() - 1), 
						Arrays.asList(0, durations.size() - 1),
						Integer.TYPE);
		thresholdParameter.setTimes(durations);

		// Add the created parameters.
		parameters.add(performanceOptionsParameter);
		parameters.add(thresholdParameter);
	}

	public boolean checkValidity(XLog candidateLog) {

		return true;
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public AbstractFilterConfigPanelController getConfigPanel() {
		if (this.configPanel == null) {
			this.configPanel = new FilterConfigPanelController(
					"Filter Trace Performance Configuration", 
					parameters, 
					this);
			parameterListeners();
		}
		
		return configPanel;
	}

	public void parameterListeners() {
		
		for(ParameterController parameter : configPanel.getControllers()) {
			if (parameter.getName().equals("performanceOptions")) {
				ParameterOneFromSetController casted = (ParameterOneFromSetController) parameter;
				ComboBox<String> comboBox = casted.getComboBox();
				comboBox.valueProperty().addListener(new ChangeListener<String>() {
					@Override 
					public void changed(ObservableValue ov, String oldValue, String newValue) {

						for (ParameterController changingParameter : configPanel.getControllers()) {

							if (changingParameter.getName().equals("time-range")) {

								ParameterRangeFromRangeController<Integer> castedChanging = 
										(ParameterRangeFromRangeController<Integer>) changingParameter;

								if (newValue.equals("filter on duration")) {
									castedChanging.setTimeframe(true);
									castedChanging.setTimes(durations);
									castedChanging.setSliderConfig(Arrays.asList(0, durations.size() - 1), Arrays.asList(0, durations.size() - 1));
									castedChanging.setTimeframe();
								} else {
									castedChanging.setTimeframe(false);
									castedChanging.setSliderConfig(minAndMaxEvents, minAndMaxEvents);
								}

							}

						}

					}
				});
			}
			/*
			if(parameter.getName().equals("threshold")) {
				ParameterRangeFromRangeController<Integer> casted = (ParameterRangeFromRangeController<Integer>) parameter;
				ParameterRangeFromRange<Integer> castedParameter = (ParameterRangeFromRange<Integer>) getParameter("threshold");
				casted.setSliderConfig(castedParameter.getChosenPair().size() == 0 ? 
						castedParameter.getDefaultPair() : 
							castedParameter.getChosenPair(), castedParameter.getOptionsPair());
			}
			*/
		}
		
	}

}
