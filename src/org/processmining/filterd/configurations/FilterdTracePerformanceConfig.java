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
	
	List<Integer> minAndMaxDuration;
	List<Integer> minAndMaxEvents;
	
	public FilterdTracePerformanceConfig(XLog log, Filter filterType) {
		super(log, filterType);
		
		parameters = new ArrayList<>();
		// Initialize members based on the log.
		minAndMaxDuration = Toolbox.getMinAnMaxDuration(log);
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
		ParameterRangeFromRange<Integer> valueParameter = 
				new ParameterRangeFromRange<Integer>(
						"threshold", 
						"Select the threshold", 
						minAndMaxDuration, 
						minAndMaxDuration,
						Integer.TYPE);
		
		// Add the created parameters.
		parameters.add(performanceOptionsParameter);
		parameters.add(valueParameter);
	}

	public boolean checkValidity(XLog candidateLog) {
		
		return true;
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		// TODO Auto-generated method stub
		return true;
	}

	public AbstractFilterConfigPanelController getConfigPanel() {
		FilterConfigPanelController filterConfigPanel = new FilterConfigPanelController(
				"Filter Trace Performance Configuration", 
				parameters, 
				this);
		for(ParameterController parameter : filterConfigPanel.getControllers()) {
			if (parameter.getName().equals("performanceOptions")) {
				ParameterOneFromSetController casted = (ParameterOneFromSetController) parameter;
				ComboBox<String> comboBox = casted.getComboBox();
				comboBox.valueProperty().addListener(new ChangeListener<String>() {
					@Override 
					public void changed(ObservableValue ov, String oldValue, String newValue) {
						
						for (ParameterController changingParameter : filterConfigPanel.getControllers()) {
							
							if (changingParameter.getName().equals("threshold")) {
								
								ParameterRangeFromRangeController<Integer> castedChanging = 
										(ParameterRangeFromRangeController<Integer>) changingParameter;
								
								if (newValue.equals("filter on duration")) {
									List<Integer> defaultValue = new ArrayList<>();
									defaultValue.add(minAndMaxDuration.get(0));
									defaultValue.add(minAndMaxDuration.get(1));
									List<Integer> minMaxPair = new ArrayList<>();
									minMaxPair.add(minAndMaxDuration.get(0));
									minMaxPair.add(minAndMaxDuration.get(1));
									castedChanging.setSliderConfig(defaultValue, minMaxPair);
									
									ParameterRangeFromRange<Integer> castedParameter = (ParameterRangeFromRange<Integer>) getParameter("threshold");
									castedParameter.setDefaultPair(defaultValue);
									castedParameter.setOptionsPair(minMaxPair);
								} else {
									castedChanging.setSliderConfig(minAndMaxEvents, minAndMaxEvents);
									
									ParameterRangeFromRange<Integer> castedParameter = (ParameterRangeFromRange<Integer>) getParameter("threshold");
									castedParameter.setDefaultPair(minAndMaxEvents);
									castedParameter.setOptionsPair(minAndMaxEvents);
								}
								
							}
							
						}
						
			        }
				});
			}
			if(parameter.getName().equals("threshold")) {
				ParameterRangeFromRangeController<Integer> casted = (ParameterRangeFromRangeController<Integer>) parameter;
				ParameterRangeFromRange<Integer> castedParameter = (ParameterRangeFromRange<Integer>) getParameter("threshold");
				casted.setSliderConfig(castedParameter.getChosenPair().size() == 0 ? 
						castedParameter.getDefaultPair() : 
						castedParameter.getChosenPair(), castedParameter.getOptionsPair());
			}
		}
		return filterConfigPanel;
	}

}
