package org.processmining.tests.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.filters.FilterdModifMergeSubsequentFilter;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;

public class FilterModifMergeSubsequentTest extends FilterdPackageTest {
	public FilterModifMergeSubsequentTest() throws Exception {
		super();
	}

	/* Corresponds to test case 28 from test_specification.xlsx.
	 * See ProM - Merge subsequent events.
	 * Merge by event name, look into all event classes, compare event classes, merge taking last event.
	 * 
	 * Result: case 35 has 7 events with an "add item" event having "item=Gameboy".
	 */
	@Test
	public void testMergeEvents() throws Throwable {
		XLog expected = parseLog("merge-events", "test_merge_events.xes");
		/*manually instantiate the filter's parameters*/
		ArrayList<Parameter> parameters = new ArrayList<>();
		List empty = Collections.EMPTY_LIST;
		List<String> desiredEvents = new ArrayList<>(Arrays.asList("ship parcel", "add item",
				"receive order", "receiver payment", "archive", "pack order" ));
	
		
		ParameterOneFromSet classifierParam = new ParameterOneFromSet("classifier", "classifier", "", empty );
		ParameterMultipleFromSet desiredEventsParam = new ParameterMultipleFromSet("desiredEvents", "desiredEvents", empty, empty);
		ParameterMultipleFromSet relevantAttributesParam = 
				new ParameterMultipleFromSet("relevantAttributes", "relevantAttributes", empty, empty );
		ParameterOneFromSet mergeTypeParam = new ParameterOneFromSet("mergeType", "mergeType", "", empty);
		
		
		
		classifierParam.setChosen("Event Name");
		desiredEventsParam.setChosen(desiredEvents);
		mergeTypeParam.setChosen("Merge taking last event");
		
		parameters.add(classifierParam);
		parameters.add(desiredEventsParam);
		parameters.add(mergeTypeParam);
		parameters.add(relevantAttributesParam);
		
		//instantiate the filter class
		FilterdModifMergeSubsequentFilter filter = new FilterdModifMergeSubsequentFilter();
		XLog computed = filter.filter(originalLog, parameters);	
		assert equalLog(expected, computed);
	}
	
	/*
	 * Merge by event name, look into "add item" event class,
	 * compare event timestamps, merge taking first event
	 * 
	 * Result: case 35 has 7 events with an "add item" event having "item=Walkman".  
	 */

	public void testMergeEventsTimestamp() throws Throwable {
		XLog expected = parseLog("merge-events", "test_merge_timestamp.xes");
		/*manually instantiate the filter's parameters */
		ArrayList<Parameter> parameters = new ArrayList<>();
		List empty = Collections.EMPTY_LIST;
		List<String> desiredEvents = new ArrayList<>(Arrays.asList( "add item" ));
	
		
		ParameterOneFromSet classifierParam = new ParameterOneFromSet("classifier", "classifier", "", empty );
		ParameterMultipleFromSet desiredEventsParam = new ParameterMultipleFromSet("desiredEvents", "desiredEvents", empty, empty);
		ParameterMultipleFromSet relevantAttributesParam = 
				new ParameterMultipleFromSet("relevantAttributes", "relevantAttributes", empty, empty );
		ParameterOneFromSet mergeTypeParam = new ParameterOneFromSet("mergeType", "mergeType", "", empty);
		
		
		
		classifierParam.setChosen("Event Name");
		desiredEventsParam.setChosen(desiredEvents);
		mergeTypeParam.setChosen("Merge taking first event");
		//relevantAttributesParam.setChosen(Arrays.asList("concept:name"));
		
		parameters.add(classifierParam);
		parameters.add(desiredEventsParam);
		parameters.add(mergeTypeParam);
		parameters.add(relevantAttributesParam);
		
		//instantiate the filter class
		FilterdModifMergeSubsequentFilter filter = new FilterdModifMergeSubsequentFilter();
		XLog computed = filter.filter(originalLog, parameters);	
		assert equalLog(expected, computed);
		
	}
	/*
	 * Merge by event name, look into "add item" event class,
	 * compare event class and attributes, merge taking first as 'start'
	 * and last at 'complete' lifecycle transition
	 * 
	 * Result: case 35 has 8 events with the "add item" events having "life-cycle=start" and 
	 * "life-cycle=complete" instead of 2 "add item" events having "life-cycle=complete"
	 */
	public void testMergeEventsClassAndAttributes() throws Throwable {
		XLog expected = parseLog("merge-events", "test_merge_class_and_attributes.xes");
		XLog input = parseLog("merge-events", "test_event_log_orders_modified.xes");
		/*manually instantiate the filter's parameters */
		ArrayList<Parameter> parameters = new ArrayList<>();
		List empty = Collections.EMPTY_LIST;
		List<String> desiredEvents = new ArrayList<>(Arrays.asList( "add item" ));
		List<String> relevantAttr = new ArrayList<>(Arrays.asList("concept:name"));
	
		
		ParameterOneFromSet classifierParam = new ParameterOneFromSet("classifier", "classifier", "", empty );
		ParameterMultipleFromSet desiredEventsParam = new ParameterMultipleFromSet("desiredEvents", "desiredEvents", empty, empty);
		ParameterMultipleFromSet relevantAttributesParam = 
				new ParameterMultipleFromSet("relevantAttributes", "relevantAttributes", empty, empty );
		ParameterOneFromSet mergeTypeParam = new ParameterOneFromSet("mergeType", "mergeType", "", empty);
		
		
		
		classifierParam.setChosen("Event Name");
		desiredEventsParam.setChosen(desiredEvents);
		mergeTypeParam.setChosen("Merge taking first as 'start' and last as 'complete' life-cycle transitions");
		relevantAttributesParam.setChosen(relevantAttr);
		
		parameters.add(classifierParam);
		parameters.add(desiredEventsParam);
		parameters.add(mergeTypeParam);
		parameters.add(relevantAttributesParam);
		
		//instantiate the filter class
		FilterdModifMergeSubsequentFilter filter = new FilterdModifMergeSubsequentFilter();
		XLog computed = filter.filter(input, parameters);
		assert equalLog(expected, computed);
		
	}
}
