package org.processmining.tests.filterdpackage;

import org.deckfour.xes.model.XLog;
import org.junit.Test;

/* Test cases for validating the Filter on End Event Attributes.
 * Test files xes location: /tests/testfiles/end-events/ */
public class FilterEndEventsTest extends FilterdPackageTest{
	
	public FilterEndEventsTest() throws Exception {
		super();
	}
	
	/* Corresponds to test case 5 from test_specification.xlsx.
	 * See ProM - Log on simple Heuristic.
	 * Selects traces with end event "archive".
	 * 
	 * Result: original log without case 72.
	 */
	@Test
	public void testEndArchive() throws Throwable {
		XLog expected = parseLog("end-events", "test_end_archive.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}
	
	
	/* Corresponds to test case 6 from test_specification.xlsx.
	 * See ProM - Log on simple Heuristic.
	 * Selects traces with end event "ship parcel".
	 * 
	 * Result: only case 72.
	 */
	@Test
	public void testEndParcel() throws Throwable {
		XLog expected = parseLog("end-events", "test_end_parcel.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 7 from test_specification.xlsx.
	 * See ProM - Log on simple Heuristic.
	 * Selects traces with the most popular (frequent) end event.
	 * 
	 * Result: original log without case 72.
	 */
	@Test
	public void testEndFrequent() throws Throwable {
		XLog expected = parseLog("end-events", "test_end_frequent.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(FilterEndEventsTest.class);
	}

}
