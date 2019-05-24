package org.processmining.tests.filterdpackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.filters.FilterdEventAttrFilter;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;

/* Test cases for validating the Filter on Event Attributes.
 * Test files xes location: /tests/testfiles/event-attribute/ */
public class FilterEventAttributeTest extends FilterdPackageTest {

	public FilterEventAttributeTest() throws Exception {
		super();
	}
	
	/* Corresponds to test case 36 from test_specification.xlsx.
	 * See Disco Attributes - keep selected events with concept:name != "ship parcel"
	 * 
	 * Result: case 34
	 * 		 cases 41, 56, 73, 74, 75, 76 - 6 events
	 * 		 cases 35, 72 - 7 events
	 */
	@Test
	public void testAttributesKeep() throws Throwable {

		XLog expected = parseLog("event-attribute", "test_attributes_1.xes");	
		
		List empty = Collections.EMPTY_LIST;
		/* manually instantiate the filter's parameters */
		ArrayList<Parameter> parameters = new ArrayList<>();
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty); 
		selectionType.setChosen("Filter out");
		parameters.add(selectionType);
		
		/* remove empty traces */ 
		ParameterYesNo nullHandling = new ParameterYesNo("nullHandling", "", true);
		nullHandling.setChosen(false);
		parameters.add(nullHandling);
		
		ParameterYesNo emptyHandling = new ParameterYesNo("emptyHandling", "", true);
		emptyHandling.setChosen(false);
		parameters.add(emptyHandling);
		
		ParameterMultipleFromSet values = new ParameterMultipleFromSet("desiredValues", "", empty, empty);
		ArrayList<String> chosen = new ArrayList<>();
		chosen.add("ship parcel");
		values.setChosen(chosen);
		parameters.add(values);
		
		FilterdEventAttrFilter filter = new FilterdEventAttrFilter();
		filter.setKey("concept:name");
		XLog computed = filter.filterCategorical(null, originalLog, parameters);
	
		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 19 from test_specification.xlsx.
	 * See ProM - Project Log onto Events // Filter log on event attribute value.
	 * Keep events with lifecycle:transition = abort.
	 * 
	 * Result: cases 56, 74, 75, 76 - 1 event.
	 */
	@Test
	public void testEventAttributeIn() throws Throwable {
		
		XLog expected = parseLog("event-attribute", "test_event_attribute_abort.xes");
		List empty = Collections.EMPTY_LIST;
		
		/* manually instantiate the filter's parameters */
		ArrayList<Parameter> parameters = new ArrayList<>();
		
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty); 
		selectionType.setChosen("Filter in");
		parameters.add(selectionType);
		
		/* remove empty traces */ 
		ParameterYesNo nullHandling = new ParameterYesNo("nullHandling", "", true);
		nullHandling.setChosen(false);
		parameters.add(nullHandling);
		
		ParameterYesNo emptyHandling = new ParameterYesNo("emptyHandling", "", true);
		emptyHandling.setChosen(false);
		parameters.add(emptyHandling);
		
		ParameterMultipleFromSet values = new ParameterMultipleFromSet("desiredValues", "", empty, empty);
		ArrayList<String> chosen = new ArrayList<>();
		chosen.add("abort");
		values.setChosen(chosen);
		parameters.add(values);
		
		FilterdEventAttrFilter filter = new FilterdEventAttrFilter();
		filter.setKey("lifecycle:transition");
		XLog computed = filter.filterCategorical(null, originalLog, parameters);
		
		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 20 from test_specification.xlsx.
	 * See ProM - Project Log onto Events // Filter log on event attribute value.
	 * Keep events with delivery = 514, 623 and "remove if no value provided = true".
	 * 
	 * Result: case 41 - 4 events
	 * 		 cases 56, 76 - 1 event.
	 */
	@Test
	public void testEventAttribute2() throws Throwable {
		XLog expected = parseLog("event-attribute", "test_event_attribute_delivery_true.xes");
		/* manually instantiate the filter's parameters */
		ArrayList<Parameter> parameters = new ArrayList<>();
		List empty = Collections.EMPTY_LIST;

		
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty); 
		selectionType.setChosen("Filter in");
		parameters.add(selectionType);
		
		/* remove empty traces */ 
		ParameterYesNo traceHandling = new ParameterYesNo("nullHandling", "", true);
		traceHandling.setChosen(false);
		parameters.add(traceHandling);
		
		ParameterYesNo eventHandling = new ParameterYesNo("emptyHandling", "", true);
		eventHandling.setChosen(false);
		parameters.add(eventHandling);
		
		ParameterMultipleFromSet values = new ParameterMultipleFromSet("desiredValues", "", empty, empty);
		ArrayList<String> chosen = new ArrayList<>();
		chosen.add("514");
		chosen.add("623");
		values.setChosen(chosen);
		parameters.add(values);
		
		FilterdEventAttrFilter filter = new FilterdEventAttrFilter();
		filter.setKey("delivery");
		XLog computed = filter.filterCategorical(null, originalLog, parameters);

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 21 from test_specification.xlsx.
	 * See ProM - Project Log onto Events // Filter log on event attribute value.
	 * Keep events with delivery = 514, 623 and "remove if no value provided = false".
	 * 
	 * Result: cases 56, 76, 74, 75, 34, 35, 41, 72, 73.
	 */
	@Test
	public void testEventAttribute3() throws Throwable {
		XLog expected = parseLog("event-attribute", "test_event_attribute_delivery_false.xes");
		ArrayList<Parameter> parameters = new ArrayList<>();	
		List empty = Collections.EMPTY_LIST;

		
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty); 
		selectionType.setChosen("Filter in");
		parameters.add(selectionType);
		
		/* remove empty traces */ 
		ParameterYesNo traceHandling = new ParameterYesNo("nullHandling", "", true);
		traceHandling.setChosen(false);
		parameters.add(traceHandling);
		
		ParameterYesNo eventHandling = new ParameterYesNo("emptyHandling", "", true);
		eventHandling.setChosen(true);
		parameters.add(eventHandling);
		
		ParameterMultipleFromSet values = new ParameterMultipleFromSet("desiredValues", "", empty, empty);
		ArrayList<String> chosen = new ArrayList<>();
		chosen.add("514");
		chosen.add("623");
		values.setChosen(chosen);
		parameters.add(values);
		
		FilterdEventAttrFilter filter = new FilterdEventAttrFilter();
		filter.setKey("delivery");
		XLog computed = filter.filterCategorical(null, originalLog, parameters);

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 22 from test_specification.xlsx.
	 * See ProM - Project Log onto Events // Filter log on event attribute value.
	 * Keep events with org:resource=System and "remove if no value provided = false".
	 * 
	 * Result: cases 35, 56, 74, 75, 76 - 2 events
	 * 		 case 34 - 1 event.
	 */
	@Test
	public void testEventAttribute4() throws Throwable {
		XLog expected = parseLog("event-attribute", "test_event_attribute_org.xes");
		ArrayList<Parameter> parameters = new ArrayList<>();	
		List empty = Collections.EMPTY_LIST;

		
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty); 
		selectionType.setChosen("Filter in");
		parameters.add(selectionType);
		
		/* remove empty traces */ 
		ParameterYesNo traceHandling = new ParameterYesNo("nullHandling", "", true);
		traceHandling.setChosen(false);
		parameters.add(traceHandling);
		
		ParameterYesNo eventHandling = new ParameterYesNo("emptyHandling", "", true);
		eventHandling.setChosen(true);
		parameters.add(eventHandling);
		
		ParameterMultipleFromSet values = new ParameterMultipleFromSet("desiredValues", "", empty, empty);
		ArrayList<String> chosen = new ArrayList<>();
		chosen.add("System");
		values.setChosen(chosen);
		parameters.add(values);
		
		FilterdEventAttrFilter filter = new FilterdEventAttrFilter();
		filter.setKey("org:resource");
		XLog computed = filter.filterCategorical(null, originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* Corresponds to test case 8 from test_specification.xlsx.
	 * See ProM - Log on simple Heuristic.
	 * Keep all events of all life-cycle types.
	 * 
	 * Result: original log.
	 */
	@Test
	public void testLifeCycleAll() throws Throwable {
		XLog expected = parseLog("event-attribute", "test_lc_all.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 9 from test_specification.xlsx.
	 * See ProM - Log on simple Heuristic.
	 * Remove events of with life-cycle type "start".
	 * 
	 * Result: e.g., case 35 no longer contains "pack order + start", has only 7 events
	 */
	@Test
	public void testLifeCycleRemoveStart() throws Throwable {
		XLog expected = parseLog("event-attribute", "test_lc_start.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 10 from test_specification.xlsx.
	 * See ProM - Log on simple Heuristic.
	 * Remove events of with life-cycle type "resume" and "suspend".
	 * 
	 * Result: e.g., case 72 has only 6 events.
	 */
	@Test
	public void testLifeCycleRemoveMultiples() throws Throwable {
		XLog expected = parseLog("event-attribute", "test_lc_resume.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 11 from test_specification.xlsx.
	 * See ProM - Filter Log on Event Attribute Values.
	 * Remove events with name "pack order".
	 * 
	 * Result: e.g. case 34 stays the same, case 35 has 6 events.
	 */
	@Test
	public void testNameRemove() throws Throwable {
		XLog expected = parseLog("event-attribute", "test_event_name_remove.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}


	/* Corresponds to test case 29 from test_specification.xlsx.
	 * Keeps events contained in 01/01/2019 - end of original log.
	 * 
	 * Result: case 76 - 6 events.
	 */
	@Test
	public void testTimeframeInFalse() throws Throwable {
		XLog expected = parseLog("event-attribute", "test_timeframe_1.xes");
		
		List empty = Collections.EMPTY_LIST;
		
		/* manually instantiate the filter's parameters */
		ArrayList<Parameter> parameters = new ArrayList<>();
		
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty); 
		selectionType.setChosen("Filter in");
		parameters.add(selectionType);
		
		/* remove empty traces */ 
		ParameterYesNo nullHandling = new ParameterYesNo("", "", true);
		nullHandling.setChosen(false);
		parameters.add(nullHandling);
		
		ParameterYesNo emptyHandling = new ParameterYesNo("", "", true);
		emptyHandling.setChosen(false);
		parameters.add(emptyHandling);
		
		ParameterRangeFromRange<String> range = new ParameterRangeFromRange<String>("range", "", empty, empty);
		ArrayList<String> chosen = new ArrayList<>();
		chosen.add("2019-01-01T00:00:00.000");
		chosen.add("2019-12-27T09:02:00.000"); // random date far after the original log
		range.setChosenPair(chosen);
		parameters.add(range);
		
		FilterdEventAttrFilter filter = new FilterdEventAttrFilter();
		XLog computed = filter.filterTimestamp(null, originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	
	/* Keeps events NOT contained in 01/01/2019 - end of original log.
	 * 
	 * Result: original log - case 76.
	 */
	@Test
	public void testTimeframeOutFalse() throws Throwable {
		XLog expected = parseLog("event-attribute", "test_timeframe_3.xes");
		
		List empty = Collections.EMPTY_LIST;
		
		/* manually instantiate the filter's parameters */
		ArrayList<Parameter> parameters = new ArrayList<>();
		
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty); 
		selectionType.setChosen("Filter out");
		parameters.add(selectionType);
		
		ParameterYesNo nullHandling = new ParameterYesNo("", "", true);
		nullHandling.setChosen(false);
		parameters.add(nullHandling);
		
		ParameterYesNo emptyHandling = new ParameterYesNo("", "", true);
		emptyHandling.setChosen(false);
		parameters.add(emptyHandling);
		
		ParameterRangeFromRange<String> range = new ParameterRangeFromRange<String>("range","", empty, empty);
		ArrayList<String> chosen = new ArrayList<>();
		chosen.add("2019-01-01T00:00:00.000");
		chosen.add("2019-12-27T09:02:00.000"); // random date far after the original log
		range.setChosenPair(chosen);
		parameters.add(range);
		
		FilterdEventAttrFilter filter = new FilterdEventAttrFilter();
		XLog computed = filter.filterTimestamp(null, originalLog, parameters);

		assert equalLog(expected, computed);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(FilterEventAttributeTest.class);
	}

}
