package org.processmining.tests.filters;

import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.filters.FilterdTraceSampleFilter;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterValueFromRange;

/* Test cases for other filters.
 * Test files xes location: /tests/testfiles/others/ */
public class FilterOtherTest extends FilterdPackageTest {
	public FilterOtherTest() throws Exception {
		super();
	}
	

	/* Corresponds to test case 45 from test_specification.xlsx.
	 * See ProM - Extract sample of random traces with parameter 3 and empty seed.
	 * 
	 * Result: log with 3 random UNIQUE traces (which were part of the original log).
	 */
	@Test
	public void testExtractSample() throws Throwable {
		XLog expected = originalLog;
		XLog computed = null; // insert filter operation
		
		int numberOfSamples = 5;
		
		/* manually instantiate the filter's parameters */
		List<Parameter> parameters = new ArrayList<>();
		
		List<Integer> optionsPair = new ArrayList<Integer>();
		optionsPair.add(0);
		optionsPair.add(originalLog.size());
		
		System.out.println(
				"number of original traces is " + originalLog.size()
				);
		
		System.out.println("Taking " + numberOfSamples + " sample traces");
		
		ParameterValueFromRange<Integer> numberOfSamplesParameter = 
				new ParameterValueFromRange<Integer>(
						"Number of samples", 
						"Select number of samples", 
						optionsPair.get(0), 
						optionsPair);
		
		numberOfSamplesParameter.setChosen(numberOfSamples);
		
		parameters.add(numberOfSamplesParameter);
		
		FilterdTraceSampleFilter filter = new FilterdTraceSampleFilter();
		computed = filter.filter(originalLog, parameters);

		assert containsLog(expected, computed);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(FilterOtherTest.class);
	}

}
