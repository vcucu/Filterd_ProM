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
		ParameterYesNo nullHandling = new ParameterYesNo("nullHandling", 
				"Remove if no value provided", true);
		nullHandling.setChosen(true);
		
		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		parameters.add(nullHandling);
		
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
		ParameterYesNo nullHandling = new ParameterYesNo("nullHandling", 
				"Remove if no value provided", true);
		nullHandling.setChosen(true);
		
		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		parameters.add(nullHandling);
		
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
	public void testStartClassifier_111() throws Throwable{
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
	 * selectionType = Filter out
	 * Selects traces with start event "delivery", value = 775
	 * 
	 */
	@Test
	public void testStartAttribute_000() throws Throwable{
		XLog expected = parseLog("end-events", "test_end_event_attribute_000.xes");
		List empty = Collections.EMPTY_LIST;
		List<String> oneElement = new ArrayList<>();
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
	
	/*========== NOT ADDED IN UTP ============ */
	
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(FilterEndEventsTest.class);
	}

}
