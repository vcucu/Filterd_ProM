package org.processmining.tests.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.filters.FilterdTraceFollowerFilter;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;

/* Test cases for validating the Filter on Trace Followers.
 * Test files xes location: /tests/testfiles/trace-follower/ */
public class FilterTraceFollowerTest extends FilterdPackageTest {
	public FilterTraceFollowerTest() throws Exception{
		super();
	}
	
	/* Corresponds to test case 38 from test_specification.xlsx.
	 * See Disco Follower - traces with key: "concept:name" and value: "add item" 
	 * event directly followed by key: "concept:name" and value: "add item".
	 * 
	 * Result: case 35.
	 */
	@Test
	public void testFollowerDirectly1() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_directly_1.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Directly followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				false, 
				"The same value", 
				"concept:name", 
				false, 
				"Shorter", 
				1, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with key: "concept:name" and value: "add item" 
	 * event directly followed by key: "concept:name" and value: "add item". 
	 * Duration between reference and follower event less than 999 milliseconds.
	 * 
	 * Result: case 35.
	 */
	@Test
	public void testFollowerDirectly2() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_directly_2.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Directly followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				false, 
				"The same value", 
				"concept:name", 
				true, 
				"Shorter", 
				999, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with key: "concept:name" and value: "add item" 
	 * event directly followed by key: "concept:name" and value: "add item". 
	 * Duration between reference and follower event longer than 999 
	 * milliseconds.
	 * 
	 * Result: empty log.
	 */
	@Test
	public void testFollowerDirectly3() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_directly_3.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Directly followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				false, 
				"The same value", 
				"concept:name", 
				true, 
				"Longer", 
				999, 
				"Seconds");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with key: "concept:name" and value: "add item" 
	 * event directly followed by key: "concept:name" and value: "add item". 
	 * Reference and follower event having the same value for key "org:resource"
	 * 
	 * Result: case 35.
	 */
	@Test
	public void testFollowerDirectly4() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_directly_4.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Directly followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"The same value", 
				"org:resource", 
				false, 
				"Shorter", 
				999, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with key: "concept:name" and value: "add item" 
	 * event directly followed by key: "concept:name" and value: "add item". 
	 * Reference and follower event having different values for key "org:resource"
	 * 
	 * Result: empty log.
	 */
	@Test
	public void testFollowerDirectly5() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_directly_5.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Directly followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"Different values", 
				"org:resource", 
				false, 
				"Shorter", 
				999, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with key: "concept:name" and value: "add item" 
	 * event directly followed by key: "concept:name" and value: "add item". 
	 * Reference and follower event having the same value for key "org:resource"
	 * and duration between reference and follower event shorter than 999 
	 * milliseconds.
	 * 
	 * Result: case 35.
	 */
	@Test
	public void testFollowerDirectly6() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_directly_6.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Directly followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"The same value", 
				"org:resource", 
				true, 
				"Shorter", 
				999, 
				"Minutes");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with key: "concept:name" and value: "add item" 
	 * event directly followed by key: "concept:name" and value: "add item". 
	 * Reference and follower event having different values for key "org:resource"
	 * and duration between reference and follower event shorter than 999 
	 * milliseconds.
	 * 
	 * Result: empty log.
	 */
	@Test
	public void testFollowerDirectly7() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_directly_7.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Directly followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"Different values", 
				"org:resource", 
				true, 
				"Shorter", 
				999, 
				"Days");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with key: "concept:name" and value: "add item" 
	 * event directly followed by key: "concept:name" and value: "add item". 
	 * Reference and follower event having the same value for key "org:resource"
	 * and duration between reference and follower event longer than 999 
	 * milliseconds.
	 * 
	 * Result: empty log.
	 */
	@Test
	public void testFollowerDirectly8() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_directly_8.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Directly followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"The same value", 
				"org:resource", 
				true, 
				"Longer", 
				999, 
				"Weeks");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with key: "concept:name" and value: "add item" 
	 * event directly followed by key: "concept:name" and value: "add item". 
	 * Reference and follower event having the same value for key "org:resource"
	 * and duration between reference and follower event longer than 999 
	 * milliseconds.
	 * 
	 * Result: empty log.
	 */
	@Test
	public void testFollowerDirectly9() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_directly_9.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Directly followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"Different values", 
				"org:resource", 
				true, 
				"Longer", 
				999, 
				"Years");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with key: "concept:name" and value: "add item" 
	 * event never directly followed by key: "concept:name" and value: "add item".
	 * 
	 * Result: cases 34, 41, 56, 72, 73, 74, 75, 76
	 */
	@Test
	public void testFollowerNeverDirectly1() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_never_directly_1.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Never directly followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				false, 
				"Different values", 
				"org:resource", 
				false, 
				"Longer", 
				999, 
				"Years");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with key: "concept:name" and value: "add item" 
	 * event never directly followed by key: "concept:name" and value: "add item".
	 * 
	 * Result: cases 34, 41, 56, 72, 73, 74, 75, 76
	 */
	@Test
	public void testFollowerNeverDirectly2() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_never_directly_2.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Never directly followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				false, 
				"Different values", 
				"org:resource", 
				true, 
				"Shorter", 
				999, 
				"Hours");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with key: "concept:name" and value: "add item" 
	 * event never directly followed by key: "concept:name" and value: "add item".
	 * 
	 * Result: cases 34, 35, 41, 56, 72, 73, 74, 75, 76: original log
	 */
	@Test
	public void testFollowerNeverDirectly3() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_never_directly_3.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Never directly followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				false, 
				"Different values", 
				"org:resource", 
				true, 
				"Longer", 
				999, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with key: "concept:name" and value: "add item" 
	 * event never directly followed by key: "concept:name" and value: "add item".
	 * Follower and reference event should have the same value for the key 
	 * "org:resource"
	 * 
	 * Result: cases 34, 41, 56, 72, 73, 74, 75, 76
	 */
	@Test
	public void testFollowerNeverDirectly4() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_never_directly_4.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Never directly followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"The same value", 
				"org:resource", 
				false, 
				"Longer", 
				999, 
				"Years");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with key: "concept:name" and value: "add item" 
	 * event never directly followed by key: "concept:name" and value: "add item".
	 * Follower and reference event should have different values for the key 
	 * "org:resource"
	 * 
	 * Result: cases 34, 35, 41, 56, 72, 73, 74, 75, 76: original log
	 */
	@Test
	public void testFollowerNeverDirectly5() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_never_directly_5.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Never directly followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"Different values", 
				"org:resource", 
				false, 
				"Longer", 
				999, 
				"Years");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with key: "concept:name" and value: "add item" 
	 * event never directly followed by key: "concept:name" and value: "add item".
	 * 	Follower and reference event should have the same value for the key 
	 * "org:resource" and the duration in between the events should be shorter
	 * than 999 milliseconds.
	 * 
	 * Result: cases 34, 41, 56, 72, 73, 74, 75, 76
	 */
	@Test
	public void testFollowerNeverDirectly6() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_never_directly_6.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Never directly followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"The same value", 
				"org:resource", 
				true, 
				"Shorter", 
				999, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with key: "concept:name" and value: "add item" 
	 * event never directly followed by key: "concept:name" and value: "add item".
	 * Follower and reference event should have different values for the key 
	 * "org:resource" and the duration in between the events should be shorter
	 * than 999 milliseconds.
	 * 
	 * Result: cases 34, 35, 41, 56, 72, 73, 74, 75, 76: original log
	 */
	@Test
	public void testFollowerNeverDirectly7() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_never_directly_7.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Never directly followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"Different values", 
				"org:resource", 
				true, 
				"Shorter", 
				999, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with key: "concept:name" and value: "add item" 
	 * event never directly followed by key: "concept:name" and value: "add item".
	 * Follower and reference event should have the same value for the key 
	 * "org:resource" and the duration in between the events should be longer
	 * than 999 milliseconds.
	 * 
	 * Result: cases 34, 35, 41, 56, 72, 73, 74, 75, 76: original log
	 */
	@Test
	public void testFollowerNeverDirectly8() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_never_directly_8.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Never directly followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"Different values", 
				"org:resource", 
				true, 
				"Shorter", 
				999, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with key: "concept:name" and value: "add item" 
	 * event never directly followed by key: "concept:name" and value: "add item".
	 * Follower and reference event should have different values for the key 
	 * "org:resource" and the duration in between the events should be longer
	 * than 999 milliseconds.
	 * 
	 * Result: cases 34, 35, 41, 56, 72, 73, 74, 75, 76: original log
	 */
	@Test
	public void testFollowerNeverDirectly9() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_never_directly_9.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Never directly followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"Different values", 
				"org:resource", 
				true, 
				"Shorter", 
				999, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 39 from test_specification.xlsx.
	 * See Disco Follower - traces with "add item" event eventually followed by "add item".
	 * 
	 * Result: cases 35, 41, 72, 73.
	 */
	@Test
	public void testFollowerEventually1() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_eventually_1.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				false, 
				"The same value", 
				"concept:name", 
				false, 
				"Shorter", 
				1, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with "add item" event eventually followed by "add item" 
	 * shorter than 999 milliseconds.
	 * Result: cases 35
	 */
	@Test
	public void testFollowerEventually2() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_eventually_2.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				false, 
				"The same value", 
				"concept:name", 
				true, 
				"Shorter", 
				999, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 40 from test_specification.xlsx.
	 * See Disco Follower - traces with "add item" event eventually followed by "add item" 
	 * after 1hr.
	 * Result: case 72, 73.
	 */
	@Test
	public void testFollowerEventually3() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_eventually_3.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				false, 
				"The same value", 
				"concept:name", 
				true, 
				"Longer", 
				1, 
				"Hours");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* Corresponds to test case 43 from test_specification.xlsx.
	 * See Disco Follower - traces with "add item" event eventually followed by "add item" 
	 * same delivery.
	 * 
	 * Result: case 35, 72, 73.
	 */
	@Test
	public void testFollowerEventually4() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_eventually_4.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"The same value", 
				"delivery", 
				false, 
				"Longer", 
				1, 
				"Hours");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* Corresponds to test case 41 from test_specification.xlsx.
	 * See Disco Follower - traces with "add item" event eventually followed by "add item" different resource.
	 * 
	 * Result: no case.
	 */
	@Test
	public void testFollowerEventually5() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_eventually_5.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"Different values", 
				"org:resource", 
				false, 
				"Longer", 
				1, 
				"Hours");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with "add item" event eventually followed by "add item" 
	 * shorter than 999 milliseconds and having the same value for key
	 * "concept:name".
	 * Result: cases 35
	 */
	@Test
	public void testFollowerEventually6() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_eventually_6.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"The same value", 
				"concept:name", 
				true, 
				"Shorter", 
				999, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with "add item" event eventually followed by "add item" 
	 * shorter than 999 milliseconds and having different values for key
	 * "concept:name".
	 * Result: empty log
	 */
	@Test
	public void testFollowerEventually7() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_eventually_7.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"Different values", 
				"concept:name", 
				true, 
				"Shorter", 
				999, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with "add item" event eventually followed by "add item" 
	 * longer than 999 milliseconds and having the same value for key
	 * "concept:name".
	 * Result: cases 41, 72, 73
	 */
	@Test
	public void testFollowerEventually8() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_eventually_8.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"The same value", 
				"concept:name", 
				true, 
				"Longer", 
				999, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with "add item" event eventually followed by "add item" 
	 * longer than 999 milliseconds and having different values for key
	 * "concept:name".
	 * Result: empty log
	 */
	@Test
	public void testFollowerEventually9() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_eventually_9.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"Different values", 
				"concept:name", 
				true, 
				"Later", 
				999, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}

	
	
	/* Corresponds to test case 42 from test_specification.xlsx.
	 * See Disco Follower - traces with "add item" event eventually followed by "add item" 
	 * different delivery.
	 * 
	 * Result: case 41.
	 */
	@Test
	public void testFollowerEventually10() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_eventually_10.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"Different values", 
				"delivery", 
				false, 
				"Longer", 
				1, 
				"Hours");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	
	/* 
	 * traces with "add item" event never eventually followed by "add item".
	 * 
	 * Result: cases 34, 56, 74, 75, 76
	 */
	@Test
	public void testFollowerNeverEventually1() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_never_eventually_1.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Never eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				false, 
				"The same value", 
				"concept:name", 
				false, 
				"Shorter", 
				999, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with "add item" event never eventually followed by "add item".
	 * The duration between the reference and the follower event should be less
	 * than 999 milliseconds.
	 * 
	 * Result: cases 34, 41, 56, 72, 73 74, 75, 76
	 */
	@Test
	public void testFollowerNeverEventually2() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_never_eventually_2.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Never eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				false, 
				"The same value", 
				"concept:name", 
				true, 
				"Shorter", 
				999, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with "add item" event never eventually followed by "add item".
	 * The duration between the reference and the follower event should be 
	 * longer than 999 milliseconds.
	 * 
	 * Result: cases 34, 35, 56, 74, 75, 76
	 */
	@Test
	public void testFollowerNeverEventually3() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_never_eventually_3.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Never eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				false, 
				"The same value", 
				"concept:name", 
				true, 
				"Longer", 
				999, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with "add item" event never eventually followed by "add item".
	 * The follower and reference event should have the same value for the key
	 * "concept:name"
	 * 
	 * Result: cases 34, 56, 74, 75, 76
	 */
	@Test
	public void testFollowerNeverEventually4() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_never_eventually_4.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Never eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"The same value", 
				"concept:name", 
				false, 
				"Shorter", 
				999, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with "add item" event never eventually followed by "add item".
	 * The follower and reference event should have different values for the key
	 * "concept:name"
	 * 
	 * Result: cases 34, 35, 41, 56, 72, 73, 74, 75, 76: initial log
	 */
	@Test
	public void testFollowerNeverEventually5() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_never_eventually_5.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Never eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"Different values", 
				"concept:name", 
				false, 
				"Shorter", 
				999, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with "add item" event never eventually followed by "add item".
	 * The follower and reference event should have the same value for the key
	 * "concept:name" and have a duration less than 999 milliseconds.
	 * 
	 * Result: cases 34, 41, 56, 72, 73 74, 75, 76
	 */
	@Test
	public void testFollowerNeverEventually6() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_never_eventually_6.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Never eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"The same value", 
				"concept:name", 
				true, 
				"Shorter", 
				999, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with "add item" event never eventually followed by "add item".
	 * The follower and reference event should have different values for the key
	 * "concept:name" and have a duration less than 999 milliseconds.
	 * 
	 * Result: cases 34, 35, 41, 56, 72, 73 74, 75, 76: initial log
	 */
	@Test
	public void testFollowerNeverEventually7() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_never_eventually_7.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Never eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"Different values", 
				"concept:name", 
				true, 
				"Shorter", 
				999, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with "add item" event never eventually followed by "add item".
	 * 
	 * Result: cases 34, 56, 74, 75, 76
	 */
	@Test
	public void testFollowerNeverEventually8() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_never_eventually_8.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Never eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"The same value", 
				"concept:name", 
				true, 
				"Longer", 
				999, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* 
	 * traces with "add item" event never eventually followed by "add item".
	 * 
	 * Result: cases 34, 35, 41, 56, 72, 73, 74, 75, 76: initial log.
	 */
	@Test
	public void testFollowerNeverEventually9() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_never_eventually_9.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Never eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"Different values", 
				"concept:name", 
				true, 
				"Longer", 
				999, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	
	private List<Parameter> getParameters(
			String filterByAttribute,
			String followType,
			List<String> referenceValues,
			List<String> followerValues,
			boolean valueMatching,
			String sameOrDifferentValue,
			String attributeOfValueMatching,
			boolean timeRestriction,
			String shorterOrLonger,
			int duration,
			String durationType) {
		
		List<Parameter> parameters = new ArrayList<>();
		
		// Create the parameter for selecting the attribute.
		ParameterOneFromSet attributeSelector = 
				new ParameterOneFromSet(
						"attrType", 
						"Select attribute", 
						filterByAttribute, 
						Arrays.asList(filterByAttribute));
		
		List<String> selectionTypeList = new ArrayList<String>();
		selectionTypeList.add("Directly followed");
		selectionTypeList.add("Never directly followed");
		selectionTypeList.add("Eventually followed");
		selectionTypeList.add("Never eventually followed");
		
		// Create the parameter for selecting the type.
		ParameterOneFromSet selectionType = new ParameterOneFromSet(
								"followType", 
								"Select follow type", 
								followType, 
								Arrays.asList(followType));
		
		ParameterMultipleFromSet referenceParameter = 
				new ParameterMultipleFromSet(
					"attrValues",
					"Desired values:",
					referenceValues,
					referenceValues
				);
		
		// Create parameter for follower event values.
		ParameterMultipleFromSet followerParameters = 
				new ParameterMultipleFromSet(
					"attrValues",
					"Desired values:",
					followerValues,
					followerValues
				);
		
		
		// Create parameter for value matching.
		ParameterYesNo valueMatchingParameter = new ParameterYesNo(
				"Value matching", 
				"Value matching", 
				valueMatching);
		
		// Create parameter for either same value or different value.
		ParameterOneFromSet sameOrDifferentParameter = new ParameterOneFromSet(
				"Same or Different value", 
				"Select same or different value", 
				sameOrDifferentValue, 
				Arrays.asList(sameOrDifferentValue));
		
		// Create parameter for selecting the attribute whose value has to be
		// matched.
		ParameterOneFromSet valueMatchingAttributeParameter = 
				new ParameterOneFromSet(
				"Attribute for value matching", 
				"Select attribute", 
				attributeOfValueMatching, 
				Arrays.asList(attributeOfValueMatching));
		
		// Create parameter for a time restriction.
		ParameterYesNo timeRestrictionParameter = new ParameterYesNo(
				"Time restrictions", 
				"Time restrictions", 
				timeRestriction);
		
		// Create parameter for selecting whether the time needs to be longer
		// or shorter than the time selected.
		ParameterOneFromSet shorterOrLongerParameter = new ParameterOneFromSet(
				"Shorter or longer", 
				"Select shorter or longer", 
				shorterOrLonger, 
				Arrays.asList(shorterOrLonger));
	
		// Create parameter for selecting time duration.
		ParameterValueFromRange<Integer> timeDurationParameter = 
				new ParameterValueFromRange<Integer>(
				"duration", 
				"Select time duration", 
				duration, 
				Arrays.asList(1, 999),
				Integer.TYPE);
		
		// Create parameter for selecting the time type.
		ParameterOneFromSet timeTypeParameter = 
				new ParameterOneFromSet(
						"timeType", 
						"Select time type", 
						durationType, 
						Arrays.asList(
								"Millis",
								"Seconds",
								"Minutes",
								"Hours",
								"Days",
								"Weeks",
								"Years"));
		
		
		parameters.add(attributeSelector);
		parameters.add(selectionType);
		parameters.add(referenceParameter);
		parameters.add(followerParameters);
		parameters.add(timeRestrictionParameter);
		parameters.add(valueMatchingParameter);
		parameters.add(shorterOrLongerParameter);
		parameters.add(sameOrDifferentParameter);
		parameters.add(timeDurationParameter);
		parameters.add(valueMatchingAttributeParameter);
		parameters.add(timeTypeParameter);
		
		return parameters;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(FilterTraceFollowerTest.class);
	}


}
