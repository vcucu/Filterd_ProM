package org.processmining.tests.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.junit.Test;
import org.processmining.filterd.filters.FilterdTraceFrequencyFilter;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.tools.Toolbox;

/* Test cases for validating the Filter on Frequency AND the
 * Filter on Occurence.
 * Test files xes location: /tests/testfiles/freq-occurence/ */
public class FilterFrequencyOccurenceTest extends FilterdPackageTest{
	
	public FilterFrequencyOccurenceTest() throws Exception {
		super();
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

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 15 from test_specification.xlsx.
	 * See ProM - Filter Out Low Occurence Traces.
	 * Threshold 2.
	 * 
	 * Result: cases 41, 56, 73, 74, 75, 76.
	 */
	@Test
	public void testOutOccurence1() throws Throwable {
		XLog expected = parseLog("freq-occurence", "test_oout_2.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFrequencyFilter filter = new FilterdTraceFrequencyFilter();
		
		List<Parameter> parameters = getOccurenceParameters(2d, 4d);
		
		computed = filter.filter(null, originalLog, parameters);

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 16 from test_specification.xlsx.
	 * See ProM - Filter Out Low Occurence Traces.
	 * Threshold 3.
	 * 
	 * Result: cases 56, 74, 75, 76.
	 */
	@Test
	public void testOutOccurence2() throws Throwable {
		XLog expected = parseLog("freq-occurence", "test_oout_3.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFrequencyFilter filter = new FilterdTraceFrequencyFilter();
		
		List<Parameter> parameters = getOccurenceParameters(3d, 4d);
		
		computed = filter.filter(null, originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	private List<Parameter> getOccurenceParameters(double lowThreshold, 
			double highThreshold) {
		
		//initialize the configuration's parameters list
		List<Parameter> parameters = new ArrayList<>();
		
		//initialize the threshold type parameter and add it to the parameters list
		List<String> foOptions = new ArrayList<String>();
		
		foOptions.add("frequency");
		foOptions.add("occurrance");
		
		ParameterOneFromSet frequencyOccurranceParameter = 
				new ParameterOneFromSet(
						"FreqOcc", 
						"Threshold type", 
						"frequency", 
						foOptions
						);
		
		frequencyOccurranceParameter.setChosen("occurrance");
		
		
		//initialize the threshold options parameter and add it to the parameters list
		List<Double> thrOptions = new ArrayList<Double>();
		
		Map<XTrace, List<Integer>> variantsToTraceIndices = 
				Toolbox.getVariantsToTraceIndices(originalLog);
		
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
		
		ParameterRangeFromRange<Double> threshold = new ParameterRangeFromRange<Double>(
				"threshold",
				"Threshold",
				thrOptions,
				thrOptions
				);
		
		List<Double> chosenOptions = new ArrayList<>();
		chosenOptions.add(lowThreshold);
		chosenOptions.add(highThreshold);
		
		threshold.setChosenPair(chosenOptions);
		
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
		
		filterInOut.setChosen("in");
		
		parameters.add(frequencyOccurranceParameter);
		parameters.add(threshold);
		parameters.add(filterInOut);
		
		return parameters;
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

		assert equalLog(expected, computed);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(FilterFrequencyOccurenceTest.class);
	}


}
