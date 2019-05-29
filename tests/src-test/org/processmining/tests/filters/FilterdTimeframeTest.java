package org.processmining.tests.filters;

import org.deckfour.xes.model.XLog;
import org.junit.Test;

public class FilterdTimeframeTest extends FilterdPackageTest {

	public FilterdTimeframeTest() throws Exception {
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

		assert equalLog(expected, computed);
	}

}
