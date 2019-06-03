package org.processmining.tests.filters;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.junit.Test;
import org.processmining.filterd.filters.FilterdTraceTimeframeFilter;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.tools.Toolbox;

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
				25, 
				31,
				"Intersecting timeframe",
				originalLog);
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	private List<Parameter> getTestParameters(
			int lowThreshold,
			int highThreshold,
			String keepTracesOption, XLog log) {
		
		List<Parameter> parameters = new ArrayList<>();
		String key = "time:timestamp";

		ArrayList<String> times = new ArrayList<>();
		for (XTrace trace: log) {
			for (XEvent event : trace) {
				/* timestamp format YYYY-MM-DDTHH:MM:SS.ssssGMT with GMT = {Z, + , -} */
				if (!event.getAttributes().containsKey(key)) continue;
				String value = event.getAttributes().get(key).toString();
				LocalDateTime time = Toolbox.synchronizeGMT(value);
				if(time == null)
					System.out.println("time is null");
				if(value == null) {
					System.out.println("value is null");
				}
				times.add(time.toString());
			}
		}
		Collections.sort(times);
		ParameterRangeFromRange<Integer>range = new ParameterRangeFromRange<Integer>("time-range",
				"Select timeframe", Arrays.asList(lowThreshold, highThreshold),
				Arrays.asList(0, times.size()-1), Integer.TYPE);
	
		range.setTimes(times);
		
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
		
		parameters.add(range);
		parameters.add(keepTracesParameter);
		
		return parameters;
	}

}
