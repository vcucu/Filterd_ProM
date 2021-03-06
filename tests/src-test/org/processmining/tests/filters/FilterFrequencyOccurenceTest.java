package org.processmining.tests.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.junit.Test;
import org.processmining.filterd.filters.FilterdEventRateFilter;
import org.processmining.filterd.filters.FilterdTraceFrequencyFilter;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.tools.Toolbox;

/* Test cases for validating the Filter on Frequency AND the
 * Filter on Occurrence.
 * Test files xes location: /tests/testfiles/freq-occurence/*
 */
public class FilterFrequencyOccurenceTest extends FilterdPackageTest{
	
	public FilterFrequencyOccurenceTest() throws Exception {
		super();
	}
	
	/*
	 * Corresponds to test case 12 from test_specification.xlsx
	 * Threshold =  80
	 * Rate  = frequency
	 * Selection type = Filter in
	 * Result: remove events where pack order is complete/aborted/resume/suspend
	 */
	@Test
	public void testEventRateFrequency() throws Throwable {
		XLog expected = parseLog("freq-occurence", "test_event_rate_frequency_80.xes");
		XLog computed = null;
		List empty = Collections.EMPTY_LIST;
		List<Parameter> parameters = new ArrayList<>();
		List<String> desiredEventClasses = new ArrayList<>(Arrays.asList("receive payment+complete",
				"receive order+complete", "archive+complete", "pack order+start", "add item+complete", 
				"ship parcel+complete"));
		
		ParameterOneFromSet rate = new ParameterOneFromSet("rate", "", "Frequency", empty);
		ParameterValueFromRange threshold = new ParameterValueFromRange("threshold", "", 80, empty);
		ParameterOneFromSet selectionType = new 
				ParameterOneFromSet("selectionType", "", "Filter in", empty);
		ParameterMultipleFromSet desiredEvents = new 
				ParameterMultipleFromSet("desiredEvents","", empty, empty);
		
		rate.setChosen("Frequency");
		threshold.setChosen(80);
		selectionType.setChosen("Filter in");
		desiredEvents.setChosen(desiredEventClasses);
		
		
		parameters.add(rate);
		parameters.add(threshold);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		
		FilterdEventRateFilter filter = new FilterdEventRateFilter();
		computed = filter.filter(originalLog, parameters);
		assert equalLog(expected, computed);
	}
	
	/*
	 * Filter on event rate test
	 * Rate = Occurrence
	 * Threshold = 6
	 * Selection type = Filter in
	 * 
	 * Result: remove events where "pack order" is complete/aborted/resume/suspend or 
	 * "ship parcel" is complete
	 */
	@Test
	public void testEventRateOccurrence() throws Throwable {
		XLog expected = parseLog("freq-occurence", "test_event_rate_occurrence_6.xes");
		XLog computed = null;
		List empty = Collections.EMPTY_LIST;
		List<Parameter> parameters = new ArrayList<>();

		List<String> desiredEventClasses = new ArrayList<>(Arrays.asList("receive payment+complete",
				"receive order+complete", "archive+complete", "pack order+start", "add item+complete"));
		
		ParameterOneFromSet rate = new ParameterOneFromSet("rate", "", "Occurrence", empty);
		ParameterValueFromRange threshold = new ParameterValueFromRange("threshold", "", 6, empty);
		ParameterOneFromSet selectionType = new 
				ParameterOneFromSet("selectionType", "", "Filter in", empty);
		ParameterMultipleFromSet desiredEvents = new 
				ParameterMultipleFromSet("desiredEvents","", empty, empty);
		
		rate.setChosen("Occurrence");
		threshold.setChosen(6);
		selectionType.setChosen("Filter in");
		desiredEvents.setChosen(desiredEventClasses);
		
		parameters.add(rate);
		parameters.add(threshold);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		
		FilterdEventRateFilter filter = new FilterdEventRateFilter();
		computed = filter.filter(originalLog, parameters);
		
		assert equalLog(expected, computed);
	}
	
	
	/* Corresponds to test case 13 from test_specification.xlsx.
	 * See ProM - Filter In High Frequency Traces.
	 * Threshold 50%.
	 * 
	 * Result: cases 41, 73, 56, 74, 75, 76.
	 */
	@Test
	public void testInFrequency1() throws Throwable {
		XLog expected = parseLog("freq-occurence", "test_fin_50.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFrequencyFilter filter = new FilterdTraceFrequencyFilter();
		
		List<XEventClassifier> classifiers = Toolbox.computeAllClassifiers(originalLog);
		// get the classifier object
		XEventClassifier classifier = classifiers.stream()
				.filter(c -> c.toString().equals("Resource"))
				.findFirst()
				.get();
		
		List<Parameter> parameters = getOccurenceParameters(
				51, 
				100, 
				classifier,
				"frequency", 
				"in"
				);
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 14 from test_specification.xlsx.
	 * See ProM - Filter In High Frequency Traces.
	 * Threshold 25%.
	 * 
	 * Result: cases 56, 74, 75, 76.
	 */
	@Test
	public void testInFrequency2() throws Throwable {
		XLog expected = parseLog("freq-occurence", "test_fin_25.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFrequencyFilter filter = new FilterdTraceFrequencyFilter();
		
		List<XEventClassifier> classifiers = Toolbox.computeAllClassifiers(originalLog);
		// get the classifier object
		XEventClassifier classifier = classifiers.stream()
				.filter(c -> c.toString().equals("Resource"))
				.findFirst()
				.get();
		
		List<Parameter> parameters = getOccurenceParameters(
				76, 
				100, 
				classifier,
				"frequency", 
				"in"
				);
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* Corresponds to test case 17 from test_specification.xlsx.
	 * See ProM - Filter Out Low Frequency Traces.
	 * Threshold 5%.
	 * 
	 * Result: original log.
	 */
	@Test
	public void testOutFrequency1() throws Throwable {
		XLog expected = parseLog("freq-occurence", "test_fout_5.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFrequencyFilter filter = new FilterdTraceFrequencyFilter();
		
		List<XEventClassifier> classifiers = Toolbox.computeAllClassifiers(originalLog);
		// get the classifier object
		XEventClassifier classifier = classifiers.stream()
				.filter(c -> c.toString().equals("Resource"))
				.findFirst()
				.get();
		
		List<Parameter> parameters = getOccurenceParameters(
				0, 
				5, 
				classifier,
				"frequency", 
				"out"
				);
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 18 from test_specification.xlsx.
	 * See ProM - Filter Out Low Frequency Traces.
	 * Threshold 25%.
	 * 
	 * Result: cases 41, 56, 73, 74, 75, 76.
	 */
	@Test
	public void testOutFrequency2() throws Throwable {
		XLog expected = parseLog("freq-occurence", "test_fout_25.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFrequencyFilter filter = new FilterdTraceFrequencyFilter();
		
		List<XEventClassifier> classifiers = Toolbox.computeAllClassifiers(originalLog);
		// get the classifier object
		XEventClassifier classifier = classifiers.stream()
				.filter(c -> c.toString().equals("Resource"))
				.findFirst()
				.get();
		
		List<Parameter> parameters = getOccurenceParameters(
				0, 
				25, 
				classifier,
				"frequency", 
				"out"
				);
		
		computed = filter.filter(originalLog, parameters);


		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 15 from test_specification.xlsx.
	 * See ProM - Filter Out Low Occurrence Traces.
	 * Threshold 2.
	 * 
	 * Result: cases 41, 56, 73, 74, 75, 76.
	 */
	@Test
	public void testInOccurence1() throws Throwable {
		XLog expected = parseLog("freq-occurence", "test_oout_2.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFrequencyFilter filter = new FilterdTraceFrequencyFilter();
		
		List<XEventClassifier> classifiers = Toolbox.computeAllClassifiers(originalLog);
		// get the classifier object
		XEventClassifier classifier = classifiers.stream()
				.filter(c -> c.toString().equals("Resource"))
				.findFirst()
				.get();
		
		List<Parameter> parameters = getOccurenceParameters(
				2, 
				4, 
				classifier,
				"occurrence",
				"in"
				);
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 16 from test_specification.xlsx.
	 * See ProM - Filter Out Low Occurrence Traces.
	 * Threshold 3.
	 * 
	 * Result: cases 56, 74, 75, 76.
	 */
	@Test
	public void testInOccurence2() throws Throwable {
		XLog expected = parseLog("freq-occurence", "test_oout_3.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFrequencyFilter filter = new FilterdTraceFrequencyFilter();
		
		List<XEventClassifier> classifiers = Toolbox.computeAllClassifiers(originalLog);
		// get the classifier object
		XEventClassifier classifier = classifiers.stream()
				.filter(c -> c.toString().equals("Resource"))
				.findFirst()
				.get();
		
		List<Parameter> parameters = getOccurenceParameters(
				3, 
				4,
				classifier, 
				"occurrence",
				"in"
				);
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * Filter in variants occurring only twice.
	 * Threshold 2 - 2.
	 * 
	 * Result: cases 41, 73
	 */
	@Test
	public void testInOccurence3() throws Throwable {
		XLog expected = parseLog("freq-occurence", "test_oout2-2.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFrequencyFilter filter = new FilterdTraceFrequencyFilter();
		
		List<XEventClassifier> classifiers = Toolbox.computeAllClassifiers(originalLog);
		// get the classifier object
		XEventClassifier classifier = classifiers.stream()
				.filter(c -> c.toString().equals("Resource"))
				.findFirst()
				.get();
		
		List<Parameter> parameters = getOccurenceParameters(
				2, 
				2, 
				classifier,
				"occurrence", 
				"in"
				);
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * Filter out high occurrence trace variants that occur three times or more.
	 * Threshold 3 - max.
	 * 
	 * Result: cases 34, 35, 41, 72, 73
	 */
	@Test
	public void testOutOccurrence4() throws Throwable {
		XLog expected = parseLog("freq-occurence", "test_oout3-max.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFrequencyFilter filter = new FilterdTraceFrequencyFilter();
		
		List<XEventClassifier> classifiers = Toolbox.computeAllClassifiers(originalLog);
		// get the classifier object
		XEventClassifier classifier = classifiers.stream()
				.filter(c -> c.toString().equals("Resource"))
				.findFirst()
				.get();
		
		List<Parameter> parameters = getOccurenceParameters(
				3, 
				4, 
				classifier,
				"occurrence", 
				"out"
				);
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	private List<Parameter> getOccurenceParameters(
			int lowThreshold, 
			int highThreshold,
			XEventClassifier classifier,
			String occurrenceOrFrequency,
			String inOrOut) {
		
		//initialize the configuration's parameters list
		List<Parameter> parameters = new ArrayList<>();
		
		// initialize the classifier dropdown
		ParameterOneFromSet classifierParameter = 
				new ParameterOneFromSet("classifier", "Select classifier", 
						"Resource", 
						Arrays.asList("Resource"));	
		
		//initialize the threshold type parameter and add it to the parameters list
		List<String> foOptions = new ArrayList<String>();
		
		foOptions.add("frequency");
		foOptions.add("occurrence");
		
		ParameterOneFromSet frequencyOccurranceParameter = 
				new ParameterOneFromSet(
						"FreqOcc", 
						"Threshold type", 
						"frequency", 
						foOptions
						);
		
		frequencyOccurranceParameter.setChosen(occurrenceOrFrequency);
		
		
		//initialize the threshold options parameter and add it to the parameters list
		List<Double> thrOptions = new ArrayList<Double>();
		
		Map<XTrace, List<Integer>> variantsToTraceIndices = 
				Toolbox.getVariantsToTraceIndices(originalLog, classifier);
		
		double smallestNumber = Double.MAX_VALUE;
		double largestNumber = -Double.MAX_VALUE;
		
		for (List<Integer> traces : variantsToTraceIndices.values()) {
			
			if (traces.size() < smallestNumber) {
				smallestNumber = traces.size();
			}
			
			if (traces.size() > largestNumber) {
				largestNumber = traces.size();
			}
			
		}
		
		thrOptions.add(smallestNumber);
		thrOptions.add(largestNumber);
		
		ParameterRangeFromRange<Double> rangeFreq = new ParameterRangeFromRange<Double>(
				"rangeFreq",
				"Threshold",
				thrOptions,
				thrOptions
				);
		
		List<Double> chosenOptions = new ArrayList<>();
		chosenOptions.add(new Double(lowThreshold));
		chosenOptions.add(new Double(highThreshold));
		
		rangeFreq.setChosenPair(chosenOptions);
		
		ParameterRangeFromRange<Integer> rangeOcc = new ParameterRangeFromRange<Integer>(
				"rangeOcc",
				"Threshold",
				new ArrayList<Integer>(Arrays.asList(lowThreshold, highThreshold)),
				new ArrayList<Integer>(Arrays.asList(lowThreshold, highThreshold))
				);
		rangeOcc.setChosenPair(Arrays.asList(lowThreshold, highThreshold));
		
		//initialize the filter mode options parameter and add it to the parameters list
		List<String> fModeOptions = new ArrayList<String>();
		
		fModeOptions.add("in");
		fModeOptions.add("out");
		
		ParameterOneFromSet filterInOut = new ParameterOneFromSet(
				"filterInOut",
				"Filter mode",
				"in",
				fModeOptions
				);
		
		filterInOut.setChosen(inOrOut);
		

		/* add every parameter */
		parameters.add(classifierParameter);
		parameters.add(frequencyOccurranceParameter);
		parameters.add(rangeFreq);
		parameters.add(rangeOcc);
		parameters.add(filterInOut);
		
		return parameters;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(FilterFrequencyOccurenceTest.class);
	}


}
