package org.processmining.tests.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		
		List<Parameter> parameters = getParameters("concept:name",
				"in",
				Arrays.asList("41", "56", "76")
				);
		
		computed = filter.filter(originalLog, parameters);

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
		
		FilterdTraceAttrFilter filter = new FilterdTraceAttrFilter();
		
		List<Parameter> parameters = getParameters("customer",
				"in",
				Arrays.asList("X", "Y", "Z")
				);
		
		computed = filter.filter(originalLog, parameters);
		assert equalLog(expected, computed);
	}




	
	private List<Parameter> getParameters(
			String attribute,
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
						"attribute", 
						"Select attribute", 
						attributes.get(0), 
						attributes);
		
		attributeSelector.setChosen(attribute);
		
		// Create the array list for selecting the type of attribute the user
		// has selected.
		
		// Create the parameter for selecting the type of attribute.
		ParameterOneFromSet selectionTypeSelector = 
				new ParameterOneFromSet(
						"filterInOut", 
						"filterInOut", 
						"in", 
						Arrays.asList("in","out"));
		
		selectionTypeSelector.setChosen("in");
	
		
		ParameterMultipleFromSet desiredValuesParam = new ParameterMultipleFromSet(
				"attrValues",
				"Desired values:",
				chosenValues,
				chosenValues
			);
		
		desiredValuesParam.setChosen(chosenValues);
		
		parameters.add(attributeSelector);
		parameters.add(selectionTypeSelector);
		parameters.add(desiredValuesParam);
		return parameters;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(FilterTraceAttributeTest.class);
	}

}
