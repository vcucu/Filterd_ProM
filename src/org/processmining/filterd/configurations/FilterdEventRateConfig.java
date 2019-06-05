package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.filterd.widgets.ParameterController;
import org.processmining.filterd.widgets.ParameterMultipleFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetController;
import org.processmining.filterd.widgets.ParameterValueFromRangeController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;

public class FilterdEventRateConfig extends FilterdAbstractConfig {
	
	XEventClasses eventClasses;
	List<Integer> minAndMaxFrequency;
	List<Integer> minAndMaxOccurrence;

	public FilterdEventRateConfig(XLog log, Filter filterType) {
		super(log, filterType);
		
		// classify the events according to their name and lifecycle
		this.classifier = XLogInfoImpl.STANDARD_CLASSIFIER;
		
		parameters = new ArrayList<>();	
		minAndMaxOccurrence =  Toolbox.getMaxOccurrence(log, classifier);
		minAndMaxFrequency = new ArrayList<>(Arrays.asList(0, 100));
		
		//Create the rate parameter
		List<String> rateOptions = new ArrayList<>(Arrays.asList("Frequency","Occurrence"));
		ParameterOneFromSet rate = new ParameterOneFromSet("rate", "Choose rate type", rateOptions.get(0), rateOptions);
		
		//Create the threshold parameter
		ParameterValueFromRange<Integer> threshold = new ParameterValueFromRange<>("threshold",
				"Select threshold for frequency/occurence", 100, minAndMaxFrequency, Integer.TYPE);
		
		//Create the selectionType parameter
		List<String> selectionTypeOptions = new ArrayList<>(Arrays.asList("Filter in", "Filter out"));
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "Selection type",
				selectionTypeOptions.get(0), selectionTypeOptions);
		
		
		
		//create list of desired events 
		List<String> allValuesDesiredEvents = new ArrayList<>();

		//create the event classes according to the classifier
		XLogInfo logInfo = XLogInfoImpl.create(log, classifier);
		eventClasses = logInfo.getEventClasses();
		//retrieve the name of the eventClass to be displayed in the ParameterMultipleFromSet desiredEvents
		for (XEventClass eventClass : eventClasses.getClasses()) {
			allValuesDesiredEvents.add(eventClass.toString());		
		}
		
		//Create the desired events parameter
		// by default it sets selected all eventClasses, since the threshold is 100
		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet("desiredEvents",
				"Selected classes according to the threshold", allValuesDesiredEvents, allValuesDesiredEvents);
		
		// Add all parameters to the list of parameters	
		parameters.add(rate);
		parameters.add(threshold);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		return true;
	}

	public FilterConfigPanelController getConfigPanel() {
		FilterConfigPanelController filterConfigPanel = new FilterConfigPanelController("Filter Event Rate Configuration",
				parameters, this);
		for (ParameterController parameter: filterConfigPanel.getControllers()) {
			if (parameter.getName().contentEquals("rate")) {
				ParameterOneFromSetController casted = (ParameterOneFromSetController) parameter;
				ComboBox<String> comboBox = casted.getComboBox();
				comboBox.valueProperty().addListener(new ChangeListener<String>() {
					
					private ParameterValueFromRange<Integer> threshold;
					@Override 
					public void changed(ObservableValue ov, String oldValue, String newValue) {
						
						for (ParameterController changingParameter : filterConfigPanel.getControllers()) {
							
							if (changingParameter.getName().equals("threshold")) {
								
								ParameterValueFromRangeController<Integer> castedChanging = 
										(ParameterValueFromRangeController<Integer>) changingParameter;
								
								if (newValue.equals("Occurrence")) {
									int defaultValue = minAndMaxOccurrence.get(0);
									List<Integer> minMaxPair = new ArrayList<>();
									minMaxPair.add(minAndMaxOccurrence.get(0));
									minMaxPair.add(minAndMaxOccurrence.get(1));
									castedChanging.setSliderConfig(defaultValue, minMaxPair);
									
									ParameterValueFromRange<Integer> castedParameter = (ParameterValueFromRange<Integer>) getParameter("threshold");
									castedParameter.setDefaultChoice(defaultValue);
									castedParameter.setOptionsPair(minMaxPair);
								} else {
									castedChanging.setSliderConfig(minAndMaxFrequency.get(1), minAndMaxFrequency);
									
									ParameterValueFromRange<Integer> castedParameter = (ParameterValueFromRange<Integer>) getParameter("threshold");
									castedParameter.setDefaultChoice(minAndMaxFrequency.get(1));
									castedParameter.setOptionsPair(minAndMaxFrequency);
								}
								
							}
							
						}
						
			        }
				});
			}
			
			if (parameter.getName().equals("threshold")) {
				ParameterValueFromRangeController casted = (ParameterValueFromRangeController) parameter;
				Slider slider = casted.getSlider();
				slider.valueProperty().addListener(new ChangeListener<Number>() {

					private ParameterValueFromRange<Integer> threshold;

					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue,
							Number newValue) {
						//find the controller for desired events so that it can change
						for (ParameterController changingController : filterConfigPanel.getControllers()) {
							if (changingController.getName().equals("desiredEvents")) {
								 ParameterMultipleFromSetController castedChangingController = 
											(ParameterMultipleFromSetController) changingController;
								 //select events corresponding to the slider selection 
								 //using the toolbox function
								 
								 //the parameter is only used because thats the interface in toolbox
								 //but nice solution would also be making it string & number, cos this is kinda workaround
								 ParameterOneFromSet rate = new ParameterOneFromSet(
										 "Frequency","Frequency","Frequency",null );
								 threshold = new ParameterValueFromRange<Integer>(
											"Frequency threshold", "threshold", 100, null, Integer.TYPE);
								 threshold.setChosen(newValue.intValue());
								 List<String> selection = new ArrayList<String>();

								 
								 selection = Toolbox.computeDesiredEventsFromThreshold(threshold, rate, eventClasses);
								 castedChangingController.setSelected(selection);
								 
								 ParameterMultipleFromSet castedChangingParameter = (ParameterMultipleFromSet) getParameter("desiredEvents");
								 castedChangingParameter.setChosen(selection);
								 
								
								 
							}	
							
						}
						
					}

				});
			
			}
		}
		
		
		
		return filterConfigPanel;
	}
	// I did not find any case where a different input log would cause invalidity
	public boolean checkValidity(XLog candidateLog) {	
		
		if (parameters == null) {
			return true;
		}
		return true;
	}

}
