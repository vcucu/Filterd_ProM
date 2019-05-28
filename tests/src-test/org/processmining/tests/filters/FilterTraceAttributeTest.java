package org.processmining.tests.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.filters.FilterdTraceAttrFilter;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterYesNo;

/* Test cases for validating the Filter on Trace Attributes.
 * Test files xes location: /tests/testfiles/trace-attribute/ */
public class FilterTraceAttributeTest extends FilterdPackageTest {
	
	public FilterTraceAttributeTest() throws Exception {
		super();
	}
	

	/* Corresponds to test case 23 from test_specification.xlsx.
	 * See ProM - Filter log on trace attribute values.
	 * Keep traces with concept:name = 41, 56, 76.
	 * 
	 * Result: cases 41, 56, 76.
	 */
	@Test
	public void testTraceAttribute1() throws Throwable {
		XLog expected = parseLog("trace-attribute", "test_trace_attribute_name.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceAttrFilter filter = new FilterdTraceAttrFilter();
		
		List<Parameter> parameters = getParametersCategorical("concept:name","Categorical",
				false,
				"Mandatory",
				Arrays.asList("41", "56", "76")
				);
		
		computed = filter.filter(null, originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* Corresponds to test case 37 from test_specification.xlsx.
	 * See Disco Attributes - mandatory traces having at least one event with
	 * "concept:name" != "archive" and "receive payment"
	 * 
	 * Result: each case except case 34.
	 */
	@Test
	public void testAttributesMandatory() throws Throwable {
		XLog expected = parseLog("event-attributes", "test_attributes_2.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}
	
	/* Corresponds to test case 24 from test_specification.xlsx.
	 * See ProM - Filter log on trace attribute values.
	 * Keep traces with customer = X, Y, Z.
	 * 
	 * Result: cases 34, 35, 41, 56, 74, 75.
	 */
	@Test
	public void testTraceAttribute2() throws Throwable {
		XLog expected = parseLog("trace-attribute", "test_trace_attribute_customer.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}


	/* Corresponds to test case 25 from test_specification.xlsx.
	 * See ProM - Filter log by attributes.
	 * Keep traces with an event having delivery = 623, 514.
	 * 
	 * Result: cases 56, 76 - 6 events
	 * 		 case 41 - 8 events.
	 */
	@Test
	public void testLogAttribute1() throws Throwable {
		XLog expected = parseLog("trace-attribute", "test_log_attribute_delivery.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}


	/* Corresponds to test case 26 from test_specification.xlsx.
	 * See ProM - Filter log by attributes.
	 * Keep traces with an event having lifecycle:transition = abort.
	 * 
	 * Result: cases 56, 74, 75, 76 - 6 events.
	 */
	@Test
	public void testLogAttribute2() throws Throwable {
		XLog expected = parseLog("trace-attribute", "test_log_attribute_abort.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* NO TEST FILE!!!!!!
	 * Corresponds to test case 27 from test_specification.xlsx.
	 * See ProM - Filter log by attributes.
	 * Keep traces with minimum number of events = 3 and maximum = 7.
	 * 
	 * Result: cases 56, 74, 75, 76 - 6 events.
	 */
	@Test
	public void testLogAttribute3() throws Throwable {
		XLog expected = parseLog("trace-attribute", "test_log_attribute_min_max.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}
	
	/* Corresponds to test case 30 from test_specification.xlsx.
	 * Keeps trace intersecting with 24/12/2018 0:00 - 26/12/2018 23:59:59.
	 * 
	 * Result: case 41 - 8 events.
	 */
	@Test
	public void testTimeframeIntersect() throws Throwable {
		XLog expected = parseLog("trace-attribute", "test_timeframe_2.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}
	
	/* Corresponds to test case 34 from test_specification.xlsx.
	 * See Disco Performance - duration of min 24-hours - max inf.
	 * 
	 * Result: cases 35, 41, 72.
	 */
	@Test
	public void testPerformanceDuration() throws Throwable {
		XLog expected = parseLog("trace-attribute", "test_performance_1.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 35 from test_specification.xlsx.
	 * See Disco Endpoints - number of events: min 3 events - max 6 events.
	 * See ProM - Filter log by attributes = "Select traces from log".
	 * 
	 * Result: cases 56, 74, 75, 76.
	 */
	@Test
	public void testPerformanceEvents() throws Throwable {
		XLog expected = parseLog("trace-attribute", "test_performance_2.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}
	
	private List<Parameter> getParametersCategorical(
			String attribute,
			String attributeType,
			boolean nullHandling,
			String selectionType,
			List<String> chosenValues
			) {
		
		// Initialize the configuration's parameters list.
		List<Parameter> parameters = new ArrayList<>();
		
		List<String> attributes = new ArrayList<>();
		attributes.add(attribute);
		
		// Create the parameter for selecting the attribute.
		ParameterOneFromSet attributeSelector = 
				new ParameterOneFromSet(
						"Attribute", 
						"Select attribute", 
						attributes.get(0), 
						attributes);
		
		attributeSelector.setChosen(attribute);
		
		// Create the array list for selecting the type of attribute the user
				// has selected.
				List<String> attributeTypeList = new ArrayList<String>();
				attributeTypeList.add("Categorical");		
				attributeTypeList.add("Numerical");
				attributeTypeList.add("Timeframe");
				attributeTypeList.add("Duration");
				attributeTypeList.add("Filter on events");
				
				// Create the parameter for selecting the type of attribute.
				ParameterOneFromSet attributeTypeSelector = 
						new ParameterOneFromSet(
								"Attribute type", 
								"Select attribute type", 
								attributeTypeList.get(0), 
								attributeTypeList);
				
				attributeTypeSelector.setChosen(attributeType);
			
				ParameterYesNo nullHandlingParam = new ParameterYesNo("nullHandling",
						"Null handling:",
						true);
				nullHandlingParam.setChosen(nullHandling);
				List<String> selectionTypeOptions = new ArrayList<>();
				selectionTypeOptions.add("Mandatory");
				selectionTypeOptions.add("Forbidden");
				
				ParameterOneFromSet selectionTypeParam = new ParameterOneFromSet("selectionType",
					"Selection type:",
					"Mandatory",
					selectionTypeOptions);
				selectionTypeParam.setChosen(selectionType);
				
				ParameterMultipleFromSet desiredValuesParam = new ParameterMultipleFromSet(
						"desiredValues",
						"Desired values:",
						chosenValues,
						chosenValues
					);
				
				desiredValuesParam.setChosen(chosenValues);
		
		parameters.add(attributeSelector);
		parameters.add(attributeTypeSelector);
		parameters.add(nullHandlingParam);
		parameters.add(selectionTypeParam);
		parameters.add(desiredValuesParam);
		return parameters;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(FilterTraceAttributeTest.class);
	}

}
