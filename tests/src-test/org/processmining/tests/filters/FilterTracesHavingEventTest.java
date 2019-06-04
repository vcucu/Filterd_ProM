package org.processmining.tests.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.filters.FilterdTracesHavingEvent;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;

public class FilterTracesHavingEventTest extends FilterdPackageTest {
	public FilterTracesHavingEventTest() throws Exception {
		super();
	}

	/* Corresponds to test case 25 from test_specification.xlsx.
	 * See ProM - Filter log by attributes.
	 * Keep traces with an event having delivery = 623, 514.
	 * 
	 * Result: cases 56, 76 - 6 events
	 * 		 case 41 - 8 events.
	 */
	@Test
	public void testEventHasAttribute1() throws Throwable {
		XLog expected = parseLog("trace-attribute", "test_log_attribute_delivery.xes");
		XLog computed = null; // insert filter operation

		FilterdTracesHavingEvent filter = new FilterdTracesHavingEvent();
		List<Parameter> parameters = getParameters("delivery", Arrays.asList("623", "514"), "Mandatory");
		computed = filter.filter(originalLog, parameters);
		assert equalLog(expected, computed);
	}
	
	/* Corresponds to test case 26 from test_specification.xlsx.
	 * See ProM - Filter log by attributes.
	 * Keep traces with an event having lifecycle:transition = abort.
	 * 
	 * Result: cases 56, 74, 75, 76 - 6 events.
	 */
	@Test
	public void testEventHasAttribute2() throws Throwable {
		XLog expected = parseLog("trace-attribute", "test_log_attribute_abort.xes");
		XLog computed = null; // insert filter operation

		FilterdTracesHavingEvent filter = new FilterdTracesHavingEvent();
		
		List<Parameter> parameters = getParameters("lifecycle:transition", Arrays.asList("abort"), "Mandatory");
		computed = filter.filter(originalLog, parameters);
		assert equalLog(expected, computed);
	}
	
	private List<Parameter> getParameters(String attrType,
			List<String> attrValues,
			String selectionType) {
		
		// Initialize the configuration's parameters list.
				List<Parameter> parameters = new ArrayList<>();
				
				ParameterOneFromSet attrTypeParam = new ParameterOneFromSet(
						"attrType",
						"Select attribute:",
						attrType,
						Arrays.asList(attrType));
				
				ParameterMultipleFromSet attrValuesParam = new ParameterMultipleFromSet(
						"attrValues",
						"Select values:",
						attrValues,
						attrValues);
				
				ParameterOneFromSet selectionTypeParam = new ParameterOneFromSet(
						"selectionType",
						"Selection type:",
						selectionType,
						Arrays.asList(selectionType));
				
			
				return Arrays.asList(attrTypeParam, attrValuesParam, selectionTypeParam);
						
		
	}
}
