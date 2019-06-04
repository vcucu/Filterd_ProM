package org.processmining.tests.filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.filters.FilterdTraceStartEventFilter;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterYesNo;



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
		List empty = Collections.EMPTY_LIST;

		/*manually instantiate the filter's parameters*/
		ArrayList<Parameter> parameters = new ArrayList<>();

		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", "", "", empty);
		attribute.setChosen("concept:name");

		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty);
		selectionType.setChosen("Filter in");

		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet("desiredEvents",
				"Select start values", empty, empty);
		List<String> list = new ArrayList<>();
		list.add("receive order");
		desiredEvents.setChosen(list);

		//Create empty events parameter
		ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", 
				"", true);
		eventHandling.setChosen(true);

		//Create empty traces parameter
		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", 
				"empty traces", true);
		traceHandling.setChosen(true);

		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		parameters.add(eventHandling);
		parameters.add(traceHandling);

		//instantiate the filter class
		FilterdTraceStartEventFilter filter = new FilterdTraceStartEventFilter();
		XLog computed = filter.filter(originalLog, parameters);	
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
		List empty = Collections.EMPTY_LIST;

		/*manually instantiate the filter's parameters*/
		ArrayList<Parameter> parameters = new ArrayList<>();

		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", "", "", empty);
		attribute.setChosen("concept:name");

		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty);
		selectionType.setChosen("Filter in");

		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet("desiredEvents",
				"Select start values", empty, empty);
		List<String> list = new ArrayList<>();
		list.add("receive payment");
		desiredEvents.setChosen(list);

		//Create empty events parameter
		ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", 
				"", true);
		eventHandling.setChosen(true);

		//Create empty traces parameter
		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", 
				"empty traces", true);
		traceHandling.setChosen(true);

		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		parameters.add(eventHandling);
		parameters.add(traceHandling);

		//instantiate filter class
		FilterdTraceStartEventFilter filter = new FilterdTraceStartEventFilter();
		XLog computed = filter.filter(originalLog, parameters);
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

	/*========== NOT ADDED IN UTP ============ */

	/*
	 * eventHandling = TRUE
	 * traceHandling = TRUE
	 * selectionType = Filter in
	 * Selects traces with start event "receive order+complete"
	 * 
	 * Result: original log without case 34.
	 */
	@Test
	public void testStartClassifier_111() throws Throwable{
		XLog expected = parseLog("start-events", "test_start_event_classifier_111.xes");
		List empty = Collections.EMPTY_LIST;

		/*manually instantiate the filter's parameters*/
		ArrayList<Parameter> parameters = new ArrayList<>();

		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", "", "", empty);
		attribute.setChosen("MXML Legacy Classifier");

		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", "", true);
		traceHandling.setChosen(true);

		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty);
		selectionType.setChosen("Filter in");

		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet("desiredEvents",
				"Select start values", empty, empty);
		List<String> list = new ArrayList<>();
		list.add("receive order+complete");
		desiredEvents.setChosen(list);

		//Create nullHandling parameter
		ParameterYesNo eventHandling = new ParameterYesNo("nullHandling", 
				"Remove if no value provided", true);
		eventHandling.setChosen(true);

		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		parameters.add(eventHandling);
		parameters.add(traceHandling);

		//instantiate the filter class
		FilterdTraceStartEventFilter filter = new FilterdTraceStartEventFilter();
		XLog computed = filter.filter(originalLog, parameters);	
		assert equalLog(expected, computed);
	}

	/*
	 * eventHandling = FALSE
	 * traceHandling = FALSE
	 * selectionType = Filter out
	 * Selects traces with start event "delivery", value = 423
	 * 
	 * Result: original log 
	 */
	@Test
	public void testStartAttribute_000() throws Throwable{
		XLog expected = parseLog("start-events", "test_start_event_attribute_000.xes");
		List empty = Collections.EMPTY_LIST;
		/*manually instantiate the filter's parameters*/
		ArrayList<Parameter> parameters = new ArrayList<>();

		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", "", "", empty);
		attribute.setChosen("delivery");

		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", "", false);
		traceHandling.setChosen(false);

		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty);
		selectionType.setChosen("Filter out");

		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet("desiredEvents",
				"Select start values", empty, empty);
		List<String> list = new ArrayList<>();
		list.add("432");
		desiredEvents.setChosen(list);

		//Create nullHandling parameter
		ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", "", false);
		eventHandling.setChosen(false);

		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		parameters.add(eventHandling);
		parameters.add(traceHandling);

		//instantiate the filter class
		FilterdTraceStartEventFilter filter = new FilterdTraceStartEventFilter();
		XLog computed = filter.filter(originalLog, parameters);	
		assert equalLog(expected, computed);
	}

	/*
	 * eventHandling = FALSE
	 * traceHandling = FALSE
	 * selectionType = Filter in
	 * Selects traces with start event "delivery", value = 423
	 * 
	 * Result: empty log
	 */
	@Test
	public void testStartAttribute_001() throws Throwable{
		XLog expected = parseLog("start-events", "test_start_event_attribute_001.xes");
		List empty = Collections.EMPTY_LIST;
		/*manually instantiate the filter's parameters*/
		ArrayList<Parameter> parameters = new ArrayList<>();

		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", "", "", empty);
		attribute.setChosen("delivery");

		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", "", false);
		traceHandling.setChosen(false);


		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty);
		selectionType.setChosen("Filter in");

		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet("desiredEvents",
				"Select start values", empty, empty);
		List<String> list = new ArrayList<>();
		list.add("432");
		desiredEvents.setChosen(list);

		//Create nullHandling parameter
		ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", "", false);
		eventHandling.setChosen(false);

		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		parameters.add(eventHandling);
		parameters.add(traceHandling);

		//instantiate the filter class
		FilterdTraceStartEventFilter filter = new FilterdTraceStartEventFilter();
		XLog computed = filter.filter(originalLog, parameters);	
		assert equalLog(expected, computed);
	}


	/*
	 * eventHandling = FALSE
	 * traceHandling = TRUE
	 * selectionType = Filter out
	 * Selects traces with start event "delivery", value = 423
	 * 
	 * Result: empty log
	 */
	@Test
	public void testStartAttribute_010() throws Throwable{
		XLog expected = parseLog("start-events", "test_start_event_attribute_010.xes");
		List empty = Collections.EMPTY_LIST;
		/*manually instantiate the filter's parameters*/
		ArrayList<Parameter> parameters = new ArrayList<>();

		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", "", "", empty);
		attribute.setChosen("delivery");

		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", "", true);
		traceHandling.setChosen(true);

		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty);
		selectionType.setChosen("Filter out");

		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet("desiredEvents",
				"Select start values", empty, empty);
		List<String> list = new ArrayList<>();
		list.add("432");
		desiredEvents.setChosen(list);

		//Create nullHandling parameter
		ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", "", false);
		eventHandling.setChosen(false);

		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		parameters.add(eventHandling);
		parameters.add(traceHandling);

		//instantiate the filter class
		FilterdTraceStartEventFilter filter = new FilterdTraceStartEventFilter();
		XLog computed = filter.filter(originalLog, parameters);	
		assert equalLog(expected, computed);
	}

	/*
	 * eventHandling = FALSE
	 * traceHandling = TRUE
	 * selectionType = Filter in
	 * Selects traces with start event "delivery", value = 423
	 * 
	 * Result: empty log
	 */
	@Test
	public void testStartAttribute_011() throws Throwable{
		XLog expected = parseLog("start-events", "test_start_event_attribute_011.xes");
		List empty = Collections.EMPTY_LIST;
		/*manually instantiate the filter's parameters*/
		ArrayList<Parameter> parameters = new ArrayList<>();

		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", "", "", empty);
		attribute.setChosen("delivery");


		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet("desiredEvents",
				"Select start values", empty, empty);
		List<String> list = new ArrayList<>();
		list.add("432");
		desiredEvents.setChosen(list);

		//Create nullHandling parameter
		ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", "", false);
		eventHandling.setChosen(false);
		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", "", true);
		traceHandling.setChosen(true);

		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty);
		selectionType.setChosen("Filter in");

		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		parameters.add(eventHandling);
		parameters.add(traceHandling);

		//instantiate the filter class
		FilterdTraceStartEventFilter filter = new FilterdTraceStartEventFilter();
		XLog computed = filter.filter(originalLog, parameters);	
		assert equalLog(expected, computed);
	}

	/*
	 * eventHandling = TRUE
	 * traceHandling = FALSE
	 * selectionType = Filter out
	 * Selects traces with start event "delivery", value = 423
	 * 
	 * Result: empty log
	 */
	@Test
	public void testStartAttribute_100() throws Throwable{
		XLog expected = parseLog("start-events", "test_start_event_attribute_100.xes");
		List empty = Collections.EMPTY_LIST;
		/*manually instantiate the filter's parameters*/
		ArrayList<Parameter> parameters = new ArrayList<>();

		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", "", "", empty);
		attribute.setChosen("delivery");


		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet("desiredEvents",
				"Select start values", empty, empty);
		List<String> list = new ArrayList<>();
		list.add("432");
		desiredEvents.setChosen(list);

		//Create nullHandling parameter
		ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", "", false);
		eventHandling.setChosen(true);
		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", "", true);
		traceHandling.setChosen(false);

		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty);
		selectionType.setChosen("Filter out");

		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		parameters.add(eventHandling);
		parameters.add(traceHandling);

		//instantiate the filter class
		FilterdTraceStartEventFilter filter = new FilterdTraceStartEventFilter();
		XLog computed = filter.filter(originalLog, parameters);	
		assert equalLog(expected, computed);
	}


	/*
	 * eventHandling = TRUE
	 * traceHandling = TRUE
	 * selectionType = Filter out
	 * Selects traces with start event "delivery", value = 423
	 * 
	 * Result: empty log
	 */
	@Test
	public void testStartAttribute_110() throws Throwable{
		XLog expected = parseLog("start-events", "test_start_event_attribute_110.xes");
		List empty = Collections.EMPTY_LIST;
		/*manually instantiate the filter's parameters*/
		ArrayList<Parameter> parameters = new ArrayList<>();

		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", "", "", empty);
		attribute.setChosen("delivery");


		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet("desiredEvents",
				"Select start values", empty, empty);
		List<String> list = new ArrayList<>();
		list.add("432");
		desiredEvents.setChosen(list);

		//Create nullHandling parameter
		ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", "", false);
		eventHandling.setChosen(true);
		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", "", true);
		traceHandling.setChosen(true);

		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty);
		selectionType.setChosen("Filter out");

		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		parameters.add(eventHandling);
		parameters.add(traceHandling);

		//instantiate the filter class
		FilterdTraceStartEventFilter filter = new FilterdTraceStartEventFilter();
		XLog computed = filter.filter(originalLog, parameters);	
		assert equalLog(expected, computed);
	}

	/*
	 * eventHandling = TRUE
	 * traceHandling = FALSE
	 * selectionType = Filter in
	 * Selects traces with start event "delivery", value = 423
	 * 
	 * Result: original log
	 */
	@Test
	public void testStartAttribute_101() throws Throwable{
		XLog expected = parseLog("start-events", "test_start_event_attribute_101.xes");
		List empty = Collections.EMPTY_LIST;
		/*manually instantiate the filter's parameters*/
		ArrayList<Parameter> parameters = new ArrayList<>();

		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", "", "", empty);
		attribute.setChosen("delivery");


		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet("desiredEvents",
				"Select start values", empty, empty);
		List<String> list = new ArrayList<>();
		list.add("432");
		desiredEvents.setChosen(list);

		//Create nullHandling parameter
		ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", "", false);
		eventHandling.setChosen(true);
		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", "", true);
		traceHandling.setChosen(false);

		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty);
		selectionType.setChosen("Filter in");

		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		parameters.add(eventHandling);
		parameters.add(traceHandling);

		//instantiate the filter class
		FilterdTraceStartEventFilter filter = new FilterdTraceStartEventFilter();
		XLog computed = filter.filter(originalLog, parameters);	
		assert equalLog(expected, computed);
	}

	/*
	 * eventHandling = TRUE
	 * traceHandling = TRUE
	 * selectionType = Filter in
	 * Selects traces with start event "delivery", value = 423
	 * 
	 * Result: original log
	 */
	@Test
	public void testStartAttribute_111() throws Throwable{
		XLog expected = parseLog("start-events", "test_start_event_attribute_111.xes");
		List empty = Collections.EMPTY_LIST;
		List<String> oneElement = new ArrayList<>();
		/*manually instantiate the filter's parameters*/
		ArrayList<Parameter> parameters = new ArrayList<>();

		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", "", "", empty);
		attribute.setChosen("delivery");


		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet("desiredEvents",
				"Select start values", empty, empty);
		List<String> list = new ArrayList<>();
		list.add("432");
		desiredEvents.setChosen(list);

		//Create nullHandling parameter
		ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", "", false);
		eventHandling.setChosen(true);
		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", "", true);
		traceHandling.setChosen(true);

		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty);
		selectionType.setChosen("Filter in");

		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		parameters.add(eventHandling);
		parameters.add(traceHandling);

		//instantiate the filter class
		FilterdTraceStartEventFilter filter = new FilterdTraceStartEventFilter();
		XLog computed = filter.filter(originalLog, parameters);	
		assert equalLog(expected, computed);
	}
	
	
	/*
	 * eventHandling = TRUE
	 * traceHandling = TRUE
	 * selectionType = Filter in
	 * Selects traces with start event "register request+"
	 * 
	 * Result: original log
	 */

	@Test
	public void testStartClassifier_empty() throws Throwable {
		XLog expected = parseLog("start-events", "test_start_classifier_empty.xes");
		XLog original = parseLog("start-events", "test_start_classifier_empty.xes");
		List empty = Collections.EMPTY_LIST;
		List<String> oneElement = new ArrayList<>();
		/*manually instantiate the filter's parameters*/
		ArrayList<Parameter> parameters = new ArrayList<>();

		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", "", "", empty);
		attribute.setChosen("MXML Legacy Classifier");


		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet("desiredEvents",
				"Select start values", empty, empty);
		List<String> list = new ArrayList<>();
		list.add("register request+");
		desiredEvents.setChosen(list);

		//Create nullHandling parameter
		ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", "", false);
		eventHandling.setChosen(true);
		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", "", true);
		traceHandling.setChosen(true);

		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty);
		selectionType.setChosen("Filter in");

		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		parameters.add(eventHandling);
		parameters.add(traceHandling);

		//instantiate the filter class
		FilterdTraceStartEventFilter filter = new FilterdTraceStartEventFilter();
		XLog computed = filter.filter(original, parameters);	
		assert equalLog(expected, computed);
	}

	/*//========== NOT ADDED IN UTP ============ */
	

	public static void main(String[] args) {
		junit.textui.TestRunner.run(FilterStartEventsTest.class);
	}


}
