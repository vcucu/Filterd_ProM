package org.processmining.tests.filters;

import org.deckfour.xes.model.XLog;
import org.junit.Test;

/* Test cases for validating the Filter on Trace Followers.
 * Test files xes location: /tests/testfiles/trace-follower/ */
public class FilterTraceFollowerTest extends FilterdPackageTest {
	public FilterTraceFollowerTest() throws Exception{
		super();
	}
	
	/* Corresponds to test case 38 from test_specification.xlsx.
	 * See Disco Follower - traces with "add item" event directly followed by "add item".
	 * 
	 * Result: case 35.
	 */
	@Test
	public void testFollowerDirectlly() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_1.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 39 from test_specification.xlsx.
	 * See Disco Follower - traces with "add item" event eventually followed by "add item".
	 * 
	 * Result: cases 35, 41, 72, 73.
	 */
	@Test
	public void testFollowerEventually() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_2.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 40 from test_specification.xlsx.
	 * See Disco Follower - traces with "add item" event eventually followed by "add item" 
	 * after 1hr.
	 * Result: case 72, 73.
	 */
	@Test
	public void testFollowerEventually2() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_3.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}
	
	/* Corresponds to test case 41 from test_specification.xlsx.
	 * See Disco Follower - traces with "add item" event eventually followed by "add item" different resource.
	 * 
	 * Result: no case.
	 */
	@Test
	public void testFollowerEventually3() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_4.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 42 from test_specification.xlsx.
	 * See Disco Follower - traces with "add item" event eventually followed by "add item" 
	 * different delivery.
	 * 
	 * Result: case 41.
	 */
	@Test
	public void testFollowerEventually4() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_5.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 43 from test_specification.xlsx.
	 * See Disco Follower - traces with "add item" event eventually followed by "add item" 
	 * same delivery.
	 * 
	 * Result: case 35, 72, 73.
	 */
	@Test
	public void testFollowerEventually5() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_6.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(FilterTraceFollowerTest.class);
	}


}
