package org.processmining.tests.filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.filters.FilterdTraceEndEventFilter;
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
		XLog computed = filter.filter(null, originalLog, parameters);
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
		XLog computed = filter.filter(null, originalLog, parameters);
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
