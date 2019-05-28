package org.processmining.tests.filters;

import java.util.Arrays;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.filters.FilterdTracePerformanceFilter;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;

public class FilterTracePerformanceTest extends FilterdPackageTest {

	public FilterTracePerformanceTest() throws Exception {
		super();
	}
	
	/* Corresponds to test case 34 from test_specification.xlsx.
	 * See Disco Performance - duration of min 24-hours - max inf.
	 * 
	 * Result: cases 35, 41, 72.
	 */
	@Test
	public void testPerformanceDuration() throws Throwable {
		XLog expected = parseLog("trace-attribute", "test_performance_1.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTracePerformanceFilter filter = 
				new FilterdTracePerformanceFilter();
		
		List<Parameter> parameters = getTestParameters(
				"filter on duration", 
				86400000, 
				Integer.MAX_VALUE);
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 35 from test_specification.xlsx.
	 * See Disco Endpoints - number of events: min 3 events - max 6 events.
	 * See ProM - Filter log by attributes = "Select traces from log".
	 * 
	 * Result: cases 56, 74, 75, 76.
	 */
	@Test
	public void testPerformanceEvents() throws Throwable {
		XLog expected = parseLog("trace-attribute", "test_performance_2.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTracePerformanceFilter filter = 
				new FilterdTracePerformanceFilter();
		
		List<Parameter> parameters = getTestParameters(
				"filter on number of events", 
				3, 
				6);
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	private List<Parameter> getTestParameters(
			String chosenOption,
			int lowThreshold,
			int highThreshold) {
		
		// Create performance options parameter and set the option to duration
		// as default.
		ParameterOneFromSet performanceOptionsParameter = 
				new ParameterOneFromSet(
						"performanceOptions", 
						"Select performance option", 
						chosenOption, 
						Arrays.asList(chosenOption));
		
		// Use duration as default because this is also set in the performance
		// options parameter.
		ParameterRangeFromRange<Integer> valueParameter = 
				new ParameterRangeFromRange<Integer>(
						"threshold", 
						"Select the threshold", 
						Arrays.asList(lowThreshold, highThreshold), 
						Arrays.asList(lowThreshold, highThreshold));
		
		return Arrays.asList(performanceOptionsParameter, valueParameter);
	}
	

}
