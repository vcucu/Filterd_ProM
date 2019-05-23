package org.processmining.tests.filterdpackage;

import org.deckfour.xes.model.XLog;
import org.junit.Test;

/* Test cases for validating the Filter on Frequency AND the
 * Filter on Occurence.
 * Test files xes location: /tests/testfiles/freq-occurence/ */
public class FilterFrequencyOccurenceTest extends FilterdPackageTest{
	
	public FilterFrequencyOccurenceTest() throws Exception {
		super();
	}
	
	/* Corresponds to test case 13 from test_specification.xlsx.
	 * See ProM - Filter In High Frequency Traces.
	 * Threshold 50%.
	 * 
	 * Result: cases 41, 73, 56, 74, 75, 76.
	 */
	@Test
	public void testInFrequency1() throws Throwable {
		XLog expected = parseLog("freq-occurence", "test_fin_50.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 14 from test_specification.xlsx.
	 * See ProM - Filter In High Frequency Traces.
	 * Threshold 25%.
	 * 
	 * Result: cases 56, 74, 75, 76.
	 */
	@Test
	public void testInFrequency2() throws Throwable {
		XLog expected = parseLog("freq-occurence", "test_fin_25.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 15 from test_specification.xlsx.
	 * See ProM - Filter Out Low Occurence Traces.
	 * Threshold 2%.
	 * 
	 * Result: cases 41, 56, 73, 74, 75, 76.
	 */
	@Test
	public void testOutOccurence1() throws Throwable {
		XLog expected = parseLog("freq-occurence", "test_oout_2.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 16 from test_specification.xlsx.
	 * See ProM - Filter Out Low Occurence Traces.
	 * Threshold 3%.
	 * 
	 * Result: cases 56, 74, 75, 76.
	 */
	@Test
	public void testOutOccurence2() throws Throwable {
		XLog expected = parseLog("freq-occurence", "test_oout_3.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 17 from test_specification.xlsx.
	 * See ProM - Filter Out Low Frequency Traces.
	 * Threshold 5%.
	 * 
	 * Result: original log.
	 */
	@Test
	public void testOutFrequency1() throws Throwable {
		XLog expected = parseLog("freq-occurence", "test_fout_5.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 18 from test_specification.xlsx.
	 * See ProM - Filter Out Low Frequency Traces.
	 * Threshold 25%.
	 * 
	 * Result: cases 41, 56, 73, 74, 75, 76.
	 */
	@Test
	public void testOutFrequency2() throws Throwable {
		XLog expected = parseLog("freq-occurence", "test_fout_25.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(FilterFrequencyOccurenceTest.class);
	}


}
