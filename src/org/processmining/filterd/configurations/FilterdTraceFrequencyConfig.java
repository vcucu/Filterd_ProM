package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.filters.Filter;
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

public class FilterdTraceFrequencyConfig extends FilterdAbstractConfig {
	
	private List<Double> logMinAndMaxSize;

	public FilterdTraceFrequencyConfig(XLog log, Filter filterType) {
		super(log, filterType);
		
		//initialize the configuration's parameters list
		parameters = new ArrayList<>();
		
		// Initialize the threshold type parameter and add it to the parameters 
		// list
		List<String> foOptions = new ArrayList<String>();
		
		foOptions.add("frequency");
		foOptions.add("occurrance");
		
		ParameterOneFromSet frequencyOccurranceParameter = 
				new ParameterOneFromSet(
						"FreqOcc", 
						"Threshold type", 
						"frequency", 
						foOptions);
		
		Map<XTrace, List<Integer>> variantsToTraceIndices = 
				Toolbox.getVariantsToTraceIndices(log);
		
		double minOccurrence = Double.MAX_VALUE;
		double maxOccurrence = -Double.MAX_VALUE;
		
		for (List<Integer> list : variantsToTraceIndices.values()) {
			
			if (list.size() < minOccurrence) {
				minOccurrence = list.size();
			}
			
			if (list.size() > maxOccurrence) {
				maxOccurrence = list.size();
			}
			
		}
		
		logMinAndMaxSize = new ArrayList<>();
		logMinAndMaxSize.add(minOccurrence);
		logMinAndMaxSize.add(maxOccurrence);
		
		// Initialize the threshold options parameter and add it to the 
		// parameters list
		List<Double> thrOptions = new ArrayList<>();
		
		//since the default option is "frequency", it goes from 1% to 100%
		thrOptions.add(0d);
		thrOptions.add(100d);
		
		ParameterRangeFromRange<Double> threshold = 
				new ParameterRangeFromRange<Double>(
				"threshold",
				"Threshold",
				thrOptions,
				thrOptions
				);
		
		
		
		// Initialize the filter mode options parameter and add it to the 
		// parameters list
		List<String> fModeOptions = new ArrayList<String>();
		
		fModeOptions.add("in");
		fModeOptions.add("out");
		
		ParameterOneFromSet filterInOut = new ParameterOneFromSet(
				"filterInOut",
				"Filter mode",
				"in",
				fModeOptions
				);
		
		parameters.add(frequencyOccurranceParameter);
		parameters.add(threshold);
		parameters.add(filterInOut);
				
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};

	public FilterConfigPanelController getConfigPanel() {
		// Return a new panel for this configuration with the relevant name and 
		// parameters.
		FilterConfigPanelController filterConfigPanel = new FilterConfigPanelController(
				"Filter Trace Frequency Configuration", 
				parameters, 
				this);
		for(ParameterController parameter : filterConfigPanel.getControllers()) {
			if (parameter.getName().equals("FreqOcc")) {
				ParameterOneFromSetController casted = (ParameterOneFromSetController) parameter;
				ComboBox<String> comboBox = casted.getComboBox();
				comboBox.valueProperty().addListener(new ChangeListener<String>() {
					@Override 
					public void changed(ObservableValue ov, String oldValue, String newValue) {
						
						for (ParameterController changingParameter : filterConfigPanel.getControllers()) {
							
							if (changingParameter.getName().equals("threshold")) {
								
								ParameterRangeFromRangeController<Double> castedChanging = 
										(ParameterRangeFromRangeController<Double>) changingParameter;
								
								if (newValue.equals("frequency")) {
									castedChanging.getSlider().setMin(0);
									castedChanging.getSlider().setLowValue(0);
									castedChanging.getSlider().setMax(100);
									castedChanging.getSlider().setHighValue(100);
								} else {
									castedChanging.getSlider().setMin(logMinAndMaxSize.get(0));
									castedChanging.getSlider().setLowValue(logMinAndMaxSize.get(0));
									castedChanging.getSlider().setMax(logMinAndMaxSize.get(1));
									castedChanging.getSlider().setHighValue(logMinAndMaxSize.get(1));
								}
								
							}
							
						}
						
			        }
				});
			}
		}
		return filterConfigPanel;
	}

	public boolean checkValidity(XLog log) {
		// If the threshold type is "occurrence" and the currently selected 
		// threshold is higher than the log's size, then the configuration is 
		// not valid for that log.
		if(parameters == null) {
			return true;
		}
		if (((ParameterOneFromSet) parameters.get(0))
				.getChosen()
				.contains("occ")) {
			if (((ParameterRangeFromRange<Double>) parameters.get(1))
				.getChosenPair()
				.get(1)
				.intValue() > log.size()) {
				return false;
			}
		}
		return true;
	}

}
