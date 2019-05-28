package org.processmining.tests.filters;

import org.deckfour.xes.model.XLog;
import org.junit.Test;

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

		assert equalLog(expected, computed);
	}
	

}
