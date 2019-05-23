package org.processmining.tests.filterdpackage;

import org.deckfour.xes.model.XLog;
import org.junit.Test;


/* Test cases for validating the Filter on Start Event Attributes.
 * Test files xes location: /tests/testfiles/start-events/ */
public class FilterStartEventsTest extends FilterdPackageTest{
	
	public FilterStartEventsTest() throws Exception {
		super();
	}
	
	/* Corresponds to test case 2 from test_specification.xlsx.
	 * See ProM - Log on simple Heuristic.
	 * Selects traces with start event "receive order".
	 * 
	 * Result: original log without case 34.
	 */
	@Test
	public void testStartOrder() throws Throwable {
		XLog expected = parseLog("start-events", "test_start_order.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 3 from test_specification.xlsx.
	 * See ProM - Log on simple Heuristic.
	 * Selects traces with start event "receive payment".
	 * 
	 * Result: only case 34.
	 */
	@Test
	public void testStartPayment() throws Throwable {
		XLog expected = parseLog("start-events", "test_start_payment.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 4 from test_specification.xlsx.
	 * See ProM - Log on simple Heuristic.
	 * Selects traces with the most popular (frequent) start event.
	 * 
	 * Result: original log without case 34.
	 */
	@Test
	public void testStartFrequent() throws Throwable {
		XLog expected = parseLog("start-events", "test_start_frequent.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(FilterStartEventsTest.class);
	}


}
