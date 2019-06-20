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
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.tools.Toolbox;
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

	@Override
	public AbstractFilterConfigPanelController getConfigPanel() {
		if(this.configPanel == null) {
			this.configPanel = new FilterConfigPanelController("Filter Event Rate Configuration",
					parameters, this);
			parameterListeners();
		}
		return configPanel;
	}

	public void parameterListeners() {


		/*
		 * retrieve and cast all needed parameter controllers
		 */
		ParameterOneFromSetController rateController =  (ParameterOneFromSetController)
				configPanel.getControllers().stream().
				filter(c->c.getName().equals("rate")).
				findFirst().get();

		ParameterMultipleFromSetController desiredEventsController =  (ParameterMultipleFromSetController)
				configPanel.getControllers().stream().
				filter(c->c.getName().equals("desiredEvents")).
				findFirst().get();

		ParameterValueFromRangeController thresholdController = (ParameterValueFromRangeController)
				configPanel.getControllers().stream().
				filter(c->c.getName().equals("threshold")).
				findFirst().get();

		// retrieve and cast all needed parameters
		ParameterOneFromSet rateParameter = (ParameterOneFromSet) getParameter("rate");
		ParameterMultipleFromSet desiredEventsParameter = (ParameterMultipleFromSet) getParameter("desiredEvents");
		ParameterValueFromRange<Integer> thresholdParameter =  (ParameterValueFromRange<Integer>) getParameter("threshold");


		/*
		 * listener for the selection of rate drop-down
		 * the selected values modifies the end points of the slider,
		 * the units in the slider, as well as the selected events 
		 * from the desiredEvents parameter
		 */
		ComboBox<String> comboBox = rateController.getComboBox();
		comboBox.valueProperty().addListener(new ChangeListener<String>() {

			ParameterValueFromRange<Integer> thresholdParameter;
			ParameterMultipleFromSet desiredEventsParameter;
			ParameterOneFromSet rateParameter;
			@Override 
			public void changed(ObservableValue ov, String oldValue, String newValue) {

				//cast the needed parameters
				thresholdParameter = (ParameterValueFromRange<Integer>) getParameter("threshold");
				rateParameter = (ParameterOneFromSet) getParameter("rate");
				desiredEventsParameter = (ParameterMultipleFromSet) getParameter("desiredEvents");
				List<String> selection = new ArrayList<String>();

				//set the threshold parameter with the new value provided by the controller
				if (newValue.equals("Occurrence")) {
					thresholdController.setSliderConfig(minAndMaxOccurrence.get(0), minAndMaxOccurrence);	
					thresholdParameter.setDefaultChoice(minAndMaxOccurrence.get(0));
					thresholdParameter.setOptionsPair(minAndMaxOccurrence);


				} else {
					thresholdController.setSliderConfig(minAndMaxFrequency.get(1), minAndMaxFrequency);
					thresholdParameter.setDefaultChoice(minAndMaxFrequency.get(1));
					thresholdParameter.setOptionsPair(minAndMaxFrequency);
				}

				//recompute the corresponding desired events from the threshold value
				selection = Toolbox.computeDesiredEventsFromThreshold(thresholdParameter, rateParameter, eventClasses);
				desiredEventsController.setSelected(selection);	

				//set the rest of the parameters
				desiredEventsParameter.setChosen(selection);	 
				rateParameter.setChosen(rateController.getValue());

			}
		});

		/*
		 * listener for the slider of the threshold. 
		 * the selected value modifies which events are selected from the
		 * desiredEvents parameter
		 */	 
		Slider slider = thresholdController.getSlider();
		slider.valueProperty().addListener(new ChangeListener<Number>() {

			ParameterValueFromRange<Integer> thresholdParameter;
			ParameterMultipleFromSet desiredEventsParameter;
			ParameterOneFromSet rateParameter;

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue,
					Number newValue) {

				//cast the needed parameters
				rateParameter = (ParameterOneFromSet) getParameter("rate");
				desiredEventsParameter = (ParameterMultipleFromSet) getParameter("desiredEvents");
				thresholdParameter =  (ParameterValueFromRange) getParameter("threshold");
				List<String> selection = new ArrayList<String>();

				//set the threshold parameter with the new value provided by the controller
				thresholdParameter.setChosen(newValue.intValue());

				//recompute the corresponding desired events from the threshold value
				selection = Toolbox.computeDesiredEventsFromThreshold(thresholdParameter, rateParameter, eventClasses);
				desiredEventsController.setSelected(selection);

				//set the rest of the parameters
				desiredEventsParameter.setChosen(selection);	 
				rateParameter.setChosen(rateController.getValue());

			}
		});	

	}

	// I did not find any case where a different input log would cause invalidity
	public boolean checkValidity(XLog candidateLog) {	

		if (parameters == null) {
			return true;
		}
		return true;
	}

}
