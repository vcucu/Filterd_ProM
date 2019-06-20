package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.filterd.widgets.ParameterOneFromSetController;
import org.processmining.filterd.widgets.ParameterRangeFromRangeController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class FilterdTraceFrequencyConfig extends FilterdAbstractConfig {

	private Map<XEventClassifier, List<Integer>> minMax;

	public FilterdTraceFrequencyConfig(XLog log, Filter filterType) {
		super(log, filterType);

		//initialize the configuration's parameters list
		parameters = new ArrayList<>();
		minMax = new HashMap<>();

		// build the list of classifiers
		List<XEventClassifier> classifiers = Toolbox.computeAllClassifiers(log);

		// initialize the classifier dropdown
		ParameterOneFromSet classifierParameter = 
				new ParameterOneFromSet("classifier", "Select classifier", 
						classifiers.get(0).toString(), 
						classifiers.stream().map(x -> x.toString())
						.collect(Collectors.toList()));		

		// update the map with the first classifier
		minMax.put(classifiers.get(0), minMaxOccurence(classifiers.get(0)));
		
		// Initialize the threshold type parameter and add it to the parameters 
		// list
		List<String> foOptions = new ArrayList<String>();
		foOptions.add("frequency");
		foOptions.add("occurrence");

		ParameterOneFromSet frequencyOccurranceParameter = 
				new ParameterOneFromSet(
						"FreqOcc", 
						"Threshold type", 
						foOptions.get(0), 
						foOptions);

		// Initialize the frequency range options parameter and add it to the 
		// parameters list
		List<Double> thrOptions = new ArrayList<>();
		//since the default option is "frequency", it goes from 0% to 100%
		thrOptions.add(0.0);
		thrOptions.add(100.0);

		ParameterRangeFromRange<Double> rangeFreq = 
				new ParameterRangeFromRange<>(
						"rangeFreq",
						"Threshold",
						thrOptions,
						thrOptions,
						Double.TYPE
						);
		rangeFreq.setDisappearable(true);

		// initialize the occurence range and add it to the parameters list
		ParameterRangeFromRange<Integer> rangeOcc = 
				new ParameterRangeFromRange<Integer>(
						"rangeOcc",
						"Threshold",
						minMax.get(classifiers.get(0)),
						minMax.get(classifiers.get(0)),
						Integer.TYPE
						);
		rangeOcc.setDisappearable(true);

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

		// add every parameter
		parameters.add(classifierParameter);
		parameters.add(frequencyOccurranceParameter);
		parameters.add(rangeFreq);
		parameters.add(rangeOcc);
		parameters.add(filterInOut);
	}

	/* method that computes the minimum number of occurences and the maximum
	 * number based on a classifier
	 */
	private List<Integer> minMaxOccurence(XEventClassifier classifier){
		List<Integer> results = new ArrayList<Integer>();
		// get the variants of each trace
		Map<XTrace, List<Integer>> variantsToTraceIndices = 
				Toolbox.getVariantsToTraceIndices(log, classifier);

		int minOccurrence = Integer.MAX_VALUE;
		int maxOccurrence = Integer.MIN_VALUE;

		/* compute the min and max */
		for (List<Integer> list : variantsToTraceIndices.values()) {
			if (list.size() < minOccurrence) {
				minOccurrence = list.size();
			}

			if (list.size() > maxOccurrence) {
				maxOccurrence = list.size();
			}
		}

		/* return the results */
		results.add(minOccurrence);
		results.add(maxOccurrence);

		return results;
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};

	@Override
	public AbstractFilterConfigPanelController getConfigPanel() {
		if (this.configPanel == null) {
			this.configPanel = new FilterConfigPanelController(
					"Filter Trace Frequency Configuration", 
					parameters, 
					this);
			parameterListeners();
		}

		return configPanel;
	}

	public void parameterListeners() {
		/* get the frequency slider controller */
		ParameterRangeFromRangeController<Double> rangeFreqControl = 
				(ParameterRangeFromRangeController<Double>)
				configPanel.getControllers().stream()
				.filter(c -> c.getName().equals("rangeFreq"))
				.findFirst()
				.get();
		
		/* get the occurence slider controller */
		ParameterRangeFromRangeController<Integer> rangeOccControl = 
				(ParameterRangeFromRangeController<Integer>)
				configPanel.getControllers().stream()
				.filter(c -> c.getName().equals("rangeOcc"))
				.findFirst()
				.get();
		

		// classifierParameter controller
		ParameterOneFromSetController classifierControl = (ParameterOneFromSetController)
				configPanel.getControllers().stream()
				.filter(c -> c.getName().equals("classifier"))
				.findFirst()
				.get();

		/* add listener such that whenever you select a new classifier,
		 * the occurence range changes according to the number of variants.
		 */
		classifierControl.getComboBox().valueProperty().addListener(new ChangeListener<String>() {
			@Override 
			public void changed(ObservableValue ov, String oldValue, String newValue) {
				// get the classifier corresponding to the new selected value
				XEventClassifier classifier = Toolbox.computeAllClassifiers(log).stream()
						.filter(c -> c.toString().equals(newValue))
						.findFirst()
						.get();
				List<Integer> logMinAndMaxSize;
				// if the min max occurence has not been computed before, compute it
				if (!minMax.containsKey(classifier)) minMax.put(classifier, minMaxOccurence(classifier));
				logMinAndMaxSize = minMax.get(classifier);

				// adjust the slides accordingly 
				rangeOccControl.setSliderConfig(logMinAndMaxSize, logMinAndMaxSize);

				ParameterRangeFromRange<Integer> occParameter = (ParameterRangeFromRange<Integer>) getParameter("rangeOcc");
				occParameter.setDefaultPair(logMinAndMaxSize);
				occParameter.setOptionsPair(logMinAndMaxSize);
			}
		});

		//
		// frequency or occurence parameter controller
		ParameterOneFromSetController freqoccControl = (ParameterOneFromSetController)
				configPanel.getControllers().stream()
				.filter(c -> c.getName().equals("FreqOcc"))
				.findFirst()
				.get();
		
		/* by default the dropdown is set to frequency and thus the occurence
		 * slider must be hidden
		 */
		if (freqoccControl.getComboBox().getValue().contains("frequency")) {
			rangeOccControl.getContents().setVisible(false);
			rangeOccControl.getContents().setManaged(false);
		}
		/* add listener such that depending on whether the user has chosen
		 * frequency or occurence the slider changes accordingly
		 */
		freqoccControl.getComboBox().valueProperty().addListener(new ChangeListener<String>() {
			@Override 
			public void changed(ObservableValue ov, String oldValue, String newValue) {
				if (newValue.equals("frequency")) {
					/* if using frequency, show freq slider and hide the occurence */
					rangeFreqControl.getContents().setVisible(true);
					rangeFreqControl.getContents().setManaged(true);
					rangeOccControl.getContents().setVisible(false);
					rangeOccControl.getContents().setManaged(false);
				} else {
					/* if using occurence, hide freq slider and show the occurence */
					rangeFreqControl.getContents().setVisible(false);
					rangeFreqControl.getContents().setManaged(false);
					rangeOccControl.getContents().setVisible(true);
					rangeOccControl.getContents().setManaged(true);
				}
			}
		});
	}


	public boolean checkValidity(XLog log) {
		// If the threshold type is "occurrence" and the currently selected 
		// threshold is higher than the log's size, then the configuration is 
		// not valid for that log.
		if (parameters == null) {
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
