package org.processmining.tests.filterdpackage;

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
			
		//Create nullHandling parameter
		ParameterYesNo nullHandling = new ParameterYesNo("nullHandling", 
				"Remove if no value provided", true);
		nullHandling.setChosen(true);
		
		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		parameters.add(nullHandling);
		
		//instantiate the filter class
		FilterdTraceStartEventFilter filter = new FilterdTraceStartEventFilter();
		XLog computed = filter.filter(null, originalLog, parameters);	
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
			
		//Create nullHandling parameter
		ParameterYesNo nullHandling = new ParameterYesNo("nullHandling", 
				"Remove if no value provided", true);
		nullHandling.setChosen(true);
		
		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.add(desiredEvents);
		parameters.add(nullHandling);
		
		//instantiate filter class
		FilterdTraceStartEventFilter filter = new FilterdTraceStartEventFilter();
		XLog computed = filter.filter(null, originalLog, parameters);
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
