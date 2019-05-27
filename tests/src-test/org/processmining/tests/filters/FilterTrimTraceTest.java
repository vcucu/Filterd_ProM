package org.processmining.tests.filters;

import org.deckfour.xes.model.XLog;
import org.junit.Test;

/* Test cases for validating trimming traces.
 * Test files xes location: /tests/testfiles/trace-trim/ */
public class FilterTrimTraceTest extends FilterdPackageTest{
	public FilterTrimTraceTest() throws Exception {
		super();
	}
	
	/* Corresponds to test case 32 from test_specification.xlsx.
	 * See Disco Endpoints - trim longest ("receive order" - "ship parcel").
	 * 
	 * Result: cases 41, 73 - 6 events
	 * 		 case  35 - 5 events
	 * 		 case  72 - 8 events.
	 */
	@Test
	public void testEndpointsLongest() throws Throwable {
		XLog expected = parseLog("trace-trim", "test_endpoints_2.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 33 from test_specification.xlsx.
	 * See Disco Endpoints - trim first ("receive order" - "add item").
	 * 
	 * Result: cases 35, 41, 72, 73 - 3 events
	 * 		 cases 56, 74, 75, 76 - 4 events.
	 */
	@Test
	public void testEndpointsFirst() throws Throwable {
		XLog expected = parseLog("trace-trim", "test_endpoints_3.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(FilterTrimTraceTest.class);
	}

}
