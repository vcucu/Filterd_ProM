package org.processmining.tests.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.filters.FilterdTraceTimeframeFilter;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;

public class FilterTimeframeTest extends FilterdPackageTest {

	public FilterTimeframeTest() throws Exception {
		super();
	}
	
	
	/* Corresponds to test case 30 from test_specification.xlsx.
	 * Keeps trace intersecting with 24/12/2018 0:00 - 26/12/2018 23:59:59.
	 * 
	 * Result: case 41 - 8 events.
	 */
	@Test
	public void testTimeframeIntersect() throws Throwable {
		XLog expected = parseLog("trace-timeframe", "test_timeframe_2.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceTimeframeFilter filter = 
				new FilterdTraceTimeframeFilter();
		
		List<Parameter> parameters = getTestParameters(
				259199000d, 
				565079000d,
				"Intersecting timeframe");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	private List<Parameter> getTestParameters(
			double lowThreshold,
			double highThreshold,
			String keepTracesOption) {
		
		List<Parameter> parameters = new ArrayList<>();
		
		// Create time frame parameter, the user can select here what time
		// frame he wants.
		ParameterRangeFromRange<Double> timeframeParameter = 
				new ParameterRangeFromRange<Double>(
						"timeframe", 
						"Select timeframe", 
						Arrays.asList(
								lowThreshold,
								highThreshold),
						Arrays.asList(
								lowThreshold,
								highThreshold));
		
		// Create the keep traces parameter, the user can select here how the
		// traces should be kept based on the time frame.
		ParameterOneFromSet keepTracesParameter = 
				new ParameterOneFromSet(
						"keep traces", 
						"Select how to keep traces", 
						keepTracesOption, 
						Arrays.asList(
								"Contained in timeframe",
								"Intersecting timeframe",
								"Started in timeframe",
								"Completed in timeframe",
								"Trim to timeframe"));
		
		parameters.add(timeframeParameter);
		parameters.add(keepTracesParameter);
		
		return parameters;
	}

}
