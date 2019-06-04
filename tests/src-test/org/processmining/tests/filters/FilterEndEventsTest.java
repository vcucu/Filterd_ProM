package org.processmining.tests.filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.filters.FilterdTraceEndEventFilter;
import org.processmining.filterd.filters.FilterdTraceStartEventFilter;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterYesNo;

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
		list.add("archive");
		desiredEvents.setChosen(list);
			
		//Create nullHandling parameter
		ParameterYesNo nullHandling = new ParameterYesNo("eventHandling", 
				"Remove if no value provided", true);
		nullHandling.setChosen(true);
		
		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", "", true);
		traceHandling.setChosen(true);
		
		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		parameters.add(nullHandling);
		parameters.add(traceHandling);
		
		//instantiate filter class
		FilterdTraceEndEventFilter filter = new FilterdTraceEndEventFilter();
		XLog computed = filter.filter(originalLog, parameters);
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
		list.add("ship parcel");
		desiredEvents.setChosen(list);
			
		//Create nullHandling parameter		
		ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", 
				"Remove if no value provided", true);
		eventHandling.setChosen(true);
		
		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", "", true);
		traceHandling.setChosen(true);
		
		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		parameters.add(eventHandling);
		parameters.add(traceHandling);
		
		//instantiate filter class
		FilterdTraceEndEventFilter filter = new FilterdTraceEndEventFilter();
		XLog computed = filter.filter(originalLog, parameters);
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

	/*========== NOT ADDED IN UTP ============ */
	
	/*
	 * eventHandling = TRUE
	 * traceHandling = TRUE
	 * selectionType = Filter in
	 * Selects traces with start event "ship parcel+complete"
	 * 
	 */
	@Test
	public void testEndClassifier_111() throws Throwable{
		XLog expected = parseLog("end-events", "test_end_event_classifier_111.xes");
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
		list.add("ship parcel+complete"); 
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
		FilterdTraceEndEventFilter filter = new FilterdTraceEndEventFilter();
		XLog computed = filter.filter(originalLog, parameters);	
		assert equalLog(expected, computed);
	}
	
	
	
	/*
	 * eventHandling = FALSE
	 * traceHandling = FALSE
	 * selectionType = Filter in
	 * Selects traces with end event "delivery", value = 775
	 * 
	 */
	@Test
	public void testEndAttribute_001() throws Throwable{
		XLog expected = parseLog("end-events", "test_end_event_attribute_001.xes");
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
				"Select end values", empty, empty);
		List<String> list = new ArrayList<>();
		list.add("775");
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
		FilterdTraceEndEventFilter filter = new FilterdTraceEndEventFilter();
		XLog computed = filter.filter(originalLog, parameters);	
		assert equalLog(expected, computed);
	}
	
	
	
	
	/*
	 * eventHandling = TRUE
	 * traceHandling = FALSE
	 * selectionType = Filter in
	 * Selects traces with end event "delivery", value = 775 or empty
	 * 
	 * Result: original log
	 * 
	 */
	@Test
	public void testEndAttribute_101() throws Throwable{
		XLog expected = parseLog("end-events", "test_end_event_attribute_101.xes");
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
				"Select end values", empty, empty);
		List<String> list = new ArrayList<>();
		list.add("775");
		desiredEvents.setChosen(list);

		//Create nullHandling parameter
		ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", "", true);
		eventHandling.setChosen(true);

		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		parameters.add(eventHandling);
		parameters.add(traceHandling);

		//instantiate the filter class
		FilterdTraceEndEventFilter filter = new FilterdTraceEndEventFilter();
		XLog computed = filter.filter(originalLog, parameters);	
		assert equalLog(expected, computed);
	}
	
	
	/*
	 * eventHandling = TRUE
	 * traceHandling = TRUE
	 * selectionType = Filter out
	 * Selects traces with end event classifier "archive+complete"
	 * 
	 * Result: only the case id 72 from the original log
	 * 
	 */
	@Test
	public void testEndClassifier_110() throws Throwable{
		XLog expected = parseLog("end-events", "test_end_event_classifier_110.xes");
		List empty = Collections.EMPTY_LIST;
		
		/*manually instantiate the filter's parameters*/
		ArrayList<Parameter> parameters = new ArrayList<>();

		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", "", "", empty);
		attribute.setChosen("MXML Legacy Classifier");

		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", "", true);
		traceHandling.setChosen(true);

		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty);
		selectionType.setChosen("Filter out");

		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet("desiredEvents",
				"Select end values", empty, empty);
		List<String> list = new ArrayList<>();
		list.add("archive+complete");
		desiredEvents.setChosen(list);

		//Create nullHandling parameter
		ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", "", true);
		eventHandling.setChosen(true);

		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		parameters.add(eventHandling);
		parameters.add(traceHandling);

		//instantiate the filter class
		FilterdTraceEndEventFilter filter = new FilterdTraceEndEventFilter();
		XLog computed = filter.filter(originalLog, parameters);	
		assert equalLog(expected, computed);
	}
	
	
	/*
	 * eventHandling = TRUE
	 * traceHandling = FALSE
	 * selectionType = Filter out
	 * Selects traces with attribute item, value = Gameboy
	 * 
	 * Result: original log
	 * 
	 */
	@Test
	public void testEndAttribute_100() throws Throwable{
		XLog expected = parseLog("end-events", "test_end_event_attribute_100.xes");
		List empty = Collections.EMPTY_LIST;
		
		/*manually instantiate the filter's parameters*/
		ArrayList<Parameter> parameters = new ArrayList<>();

		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", "", "", empty);
		attribute.setChosen("item");

		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", "", false);
		traceHandling.setChosen(false);

		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty);
		selectionType.setChosen("Filter out");

		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet("desiredEvents",
				"Select end values", empty, empty);
		List<String> list = new ArrayList<>();
		list.add("Gameboy");
		desiredEvents.setChosen(list);

		//Create nullHandling parameter
		ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", "", true);
		eventHandling.setChosen(true);

		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		parameters.add(eventHandling);
		parameters.add(traceHandling);

		//instantiate the filter class
		FilterdTraceEndEventFilter filter = new FilterdTraceEndEventFilter();
		XLog computed = filter.filter(originalLog, parameters);	
		assert equalLog(expected, computed);
	}
	
	
	/*
	 * eventHandling = FALSE
	 * traceHandling = TRUE
	 * selectionType = Filter in
	 * Selects traces with attribute item, value = Gameboy, Walkman, VHS player
	 * 
	 * Result: empty log
	 * 
	 */
	@Test
	public void testEndAttribute_011() throws Throwable{
		XLog expected = parseLog("end-events", "test_end_event_attribute_011.xes");
		List empty = Collections.EMPTY_LIST;
		
		/*manually instantiate the filter's parameters*/
		ArrayList<Parameter> parameters = new ArrayList<>();

		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", "", "", empty);
		attribute.setChosen("item");

		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", "",true);
		traceHandling.setChosen(true);

		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty);
		selectionType.setChosen("Filter in");

		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet("desiredEvents",
				"Select end values", empty, empty);
		List<String> list = new ArrayList<>();
		list.add("Gameboy");
		list.add("Walkman");
		list.add("VHS Player");
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
		FilterdTraceEndEventFilter filter = new FilterdTraceEndEventFilter();
		XLog computed = filter.filter(originalLog, parameters);	
		assert equalLog(expected, computed);
	}
	
	
	
	/*
	 * eventHandling = FALSE
	 * traceHandling = TRUE
	 * selectionType = FALSE
	 * Selects traces with attribute time:timestamp equal to 2018-12-22T07:24:00+01:00
	 * (the dataset contains .000 for microseconds)
	 * 
	 * Result: all cases except for case id 35
	 * 
	 */
	@Test
	public void testEndAttribute_010() throws Throwable{
		XLog expected = parseLog("end-events", "test_end_event_attribute_010.xes");
		List empty = Collections.EMPTY_LIST;
		
		/*manually instantiate the filter's parameters*/
		ArrayList<Parameter> parameters = new ArrayList<>();

		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", "", "", empty);
		attribute.setChosen("time:timestamp");

		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", "",true);
		traceHandling.setChosen(true);

		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty);
		selectionType.setChosen("Filter out");

		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet("desiredEvents",
				"Select end values", empty, empty);
		List<String> list = new ArrayList<>();
		list.add("2018-12-22T07:24:00+01:00");
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
		FilterdTraceEndEventFilter filter = new FilterdTraceEndEventFilter();
		XLog computed = filter.filter(originalLog, parameters);	
		assert equalLog(expected, computed);
	}
	
	
	/*
	 * eventHandling = FALSE
	 * traceHandling = FALSE
	 * selectionType = FALSE
	 * filters out traces without a dummy attribute or with nulls
	 * 
	 * Result: empty log 
	 * 
	 */
	@Test
	public void testEndAttribute_000() throws Throwable{
		XLog expected = parseLog("end-events", "test_end_event_attribute_000.xes");
		List empty = Collections.EMPTY_LIST;
		
		/*manually instantiate the filter's parameters*/
		ArrayList<Parameter> parameters = new ArrayList<>();

		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", "", "", empty);
		attribute.setChosen("item");

		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", "",false);
		traceHandling.setChosen(false);

		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty);
		selectionType.setChosen("Filter out");

		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet("desiredEvents",
				"Select end values", empty, empty);
		List<String> list = new ArrayList<>();
		list.add("Dummy");
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
		FilterdTraceEndEventFilter filter = new FilterdTraceEndEventFilter();
		XLog computed = filter.filter(originalLog, parameters);	
		assert equalLog(expected, computed);
	}
	
	/*========== NOT ADDED IN UTP ============ */
	
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(FilterEndEventsTest.class);
	}

}
