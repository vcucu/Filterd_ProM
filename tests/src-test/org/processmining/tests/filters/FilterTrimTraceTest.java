package org.processmining.tests.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.filters.FilterdTraceFollowerFilter;
import org.processmining.filterd.filters.FilterdTraceTrimFilter;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;

/* Test cases for validating trimming traces.
 * Test files xes location: /tests/testfiles/trace-trim/ */
public class FilterTrimTraceTest extends FilterdPackageTest{
	public FilterTrimTraceTest() throws Exception {
		super();
	}
	
	/* Corresponds to test case 32 from test_specification.xlsx.
	 * See Disco Endpoints - trim longest ("receive order" - "ship parcel").
	 * 
	 * Result: cases 41, 73 - 6 events
	 * 		 case  35 - 5 events
	 * 		 case  72 - 8 events.
	 */
	@Test
	public void testEndpointsLongest() throws Throwable {
		XLog expected = parseLog("trace-trim", "test_endpoints_2.xes");
		XLog computed = null; // insert filter operation

		FilterdTraceTrimFilter filter = new FilterdTraceTrimFilter();
		computed = filter.filter(originalLog, getParameters
				("concept:name",
				"Trim longest",
				Arrays.asList("receive order"),
				Arrays.asList("ship parcel")
				)
			);
		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 33 from test_specification.xlsx.
	 * See Disco Endpoints - trim first ("receive order" - "add item").
	 * 
	 * Result: cases 35, 41, 72, 73 - 3 events
	 * 		 cases 56, 74, 75, 76 - 4 events.
	 */
	@Test
	public void testEndpointsFirst() throws Throwable {
		XLog expected = parseLog("trace-trim", "test_endpoints_3.xes");
		XLog computed = null; // insert filter operation
		FilterdTraceTrimFilter filter = new FilterdTraceTrimFilter();
		computed = filter.filter(originalLog, getParameters("concept:name",
				"Trim first",
				Arrays.asList("receive order"),
				Arrays.asList("add item")));
		assert equalLog(expected, computed);
	}

	private List<Parameter> getParameters(String attribute,
			String selectionType,
			List<String> refValues,
			List<String> folValues) {
		ParameterOneFromSet attributeSelector = 
				new ParameterOneFromSet(
						"attrType", 
						"Select attribute", 
						attribute, 
						Arrays.asList(attribute));
		ParameterOneFromSet selectionTypeParam = new ParameterOneFromSet(
				"followType", 
				"Select follow type", 
				selectionType, 
				Arrays.asList(selectionType));
		
		ParameterMultipleFromSet firstEvents = 
				new ParameterMultipleFromSet(
					"attrValues",
					"Desired values:",
					refValues,
					refValues
				);
		
		ParameterMultipleFromSet endEvents = 
				new ParameterMultipleFromSet(
					"attrValues",
					"Desired values:",
					folValues,
					folValues
				);
		return Arrays.asList(attributeSelector, selectionTypeParam, firstEvents,
				endEvents);
	}
	public static void main(String[] args) {
		junit.textui.TestRunner.run(FilterTrimTraceTest.class);
	}

}
