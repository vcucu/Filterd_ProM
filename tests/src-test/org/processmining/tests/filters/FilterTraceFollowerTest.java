package org.processmining.tests.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.filters.FilterdTraceFollowerFilter;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;

/* Test cases for validating the Filter on Trace Followers.
 * Test files xes location: /tests/testfiles/trace-follower/ */
public class FilterTraceFollowerTest extends FilterdPackageTest {
	public FilterTraceFollowerTest() throws Exception{
		super();
	}
	
	/* Corresponds to test case 38 from test_specification.xlsx.
	 * See Disco Follower - traces with key: "concept:name" and value: "add item" 
	 * event directly followed by key: "concept:name" and value: "add item".
	 * 
	 * Result: case 35.
	 */
	@Test
	public void testFollowerDirectly() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_1.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Directly followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				false, 
				"The same value", 
				"concept:name", 
				false, 
				"Shorter", 
				1, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 39 from test_specification.xlsx.
	 * See Disco Follower - traces with "add item" event eventually followed by "add item".
	 * 
	 * Result: cases 35, 41, 72, 73.
	 */
	@Test
	public void testFollowerEventually() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_2.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				false, 
				"The same value", 
				"concept:name", 
				false, 
				"Shorter", 
				1, 
				"Millis");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 40 from test_specification.xlsx.
	 * See Disco Follower - traces with "add item" event eventually followed by "add item" 
	 * after 1hr.
	 * Result: case 72, 73.
	 */
	@Test
	public void testFollowerEventually2() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_3.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				false, 
				"The same value", 
				"concept:name", 
				true, 
				"Longer", 
				1, 
				"Hours");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	/* Corresponds to test case 41 from test_specification.xlsx.
	 * See Disco Follower - traces with "add item" event eventually followed by "add item" different resource.
	 * 
	 * Result: no case.
	 */
	@Test
	public void testFollowerEventually3() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_4.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"Different values", 
				"org:resource", 
				false, 
				"Longer", 
				1, 
				"Hours");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 42 from test_specification.xlsx.
	 * See Disco Follower - traces with "add item" event eventually followed by "add item" 
	 * different delivery.
	 * 
	 * Result: case 41.
	 */
	@Test
	public void testFollowerEventually4() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_5.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"Different values", 
				"delivery", 
				false, 
				"Longer", 
				1, 
				"Hours");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 43 from test_specification.xlsx.
	 * See Disco Follower - traces with "add item" event eventually followed by "add item" 
	 * same delivery.
	 * 
	 * Result: case 35, 72, 73.
	 */
	@Test
	public void testFollowerEventually5() throws Throwable {
		XLog expected = parseLog("trace-follower", "test_follower_6.xes");
		XLog computed = null; // insert filter operation
		
		FilterdTraceFollowerFilter filter = new FilterdTraceFollowerFilter();
		
		List<Parameter> parameters = getParameters(
				"concept:name", 
				"Eventually followed", 
				Arrays.asList("add item"), 
				Arrays.asList("add item"), 
				true, 
				"The same value", 
				"delivery", 
				false, 
				"Longer", 
				1, 
				"Hours");
		
		computed = filter.filter(originalLog, parameters);

		assert equalLog(expected, computed);
	}
	
	private List<Parameter> getParameters(
			String filterByAttribute,
			String followType,
			List<String> referenceValues,
			List<String> followerValues,
			boolean valueMatching,
			String sameOrDifferentValue,
			String attributeOfValueMatching,
			boolean timeRestriction,
			String shorterOrLonger,
			int duration,
			String durationType) {
		
		List<Parameter> parameters = new ArrayList<>();
		
		// Create the parameter for selecting the attribute.
		ParameterOneFromSet attributeSelector = 
				new ParameterOneFromSet(
						"attrType", 
						"Select attribute", 
						filterByAttribute, 
						Arrays.asList(filterByAttribute));
		
		List<String> selectionTypeList = new ArrayList<String>();
		selectionTypeList.add("Directly followed");
		selectionTypeList.add("Never directly followed");
		selectionTypeList.add("Eventually followed");
		selectionTypeList.add("Never eventually followed");
		
		// Create the parameter for selecting the type.
		ParameterOneFromSet selectionType = new ParameterOneFromSet(
								"followType", 
								"Select follow type", 
								followType, 
								Arrays.asList(followType));
		
		ParameterMultipleFromSet referenceParameter = 
				new ParameterMultipleFromSet(
					"attrValues",
					"Desired values:",
					referenceValues,
					referenceValues
				);
		
		// Create parameter for follower event values.
		ParameterMultipleFromSet followerParameters = 
				new ParameterMultipleFromSet(
					"attrValues",
					"Desired values:",
					followerValues,
					followerValues
				);
		
		
		// Create parameter for value matching.
		ParameterYesNo valueMatchingParameter = new ParameterYesNo(
				"Value matching", 
				"Value matching", 
				valueMatching);
		
		// Create parameter for either same value or different value.
		ParameterOneFromSet sameOrDifferentParameter = new ParameterOneFromSet(
				"Same or Different value", 
				"Select same or different value", 
				sameOrDifferentValue, 
				Arrays.asList(sameOrDifferentValue));
		
		// Create parameter for selecting the attribute whose value has to be
		// matched.
		ParameterOneFromSet valueMatchingAttributeParameter = 
				new ParameterOneFromSet(
				"Attribute for value matching", 
				"Select attribute", 
				attributeOfValueMatching, 
				Arrays.asList(attributeOfValueMatching));
		
		// Create parameter for a time restriction.
		ParameterYesNo timeRestrictionParameter = new ParameterYesNo(
				"Time restrictions", 
				"Time restrictions", 
				timeRestriction);
		
		// Create parameter for selecting whether the time needs to be longer
		// or shorter than the time selected.
		ParameterOneFromSet shorterOrLongerParameter = new ParameterOneFromSet(
				"Shorter or longer", 
				"Select shorter or longer", 
				shorterOrLonger, 
				Arrays.asList(shorterOrLonger));
	
		// Create parameter for selecting time duration.
		ParameterValueFromRange<Integer> timeDurationParameter = 
				new ParameterValueFromRange<Integer>(
				"duration", 
				"Select time duration", 
				duration, 
				Arrays.asList(1, 999),
				Integer.TYPE);
		
		// Create parameter for selecting the time type.
		ParameterOneFromSet timeTypeParameter = 
				new ParameterOneFromSet(
						"timeType", 
						"Select time type", 
						durationType, 
						Arrays.asList(
								"Millis",
								"Seconds",
								"Minutes",
								"Hours",
								"Days",
								"Weeks",
								"Years"));
		
		
		parameters.add(attributeSelector);
		parameters.add(selectionType);
		parameters.add(referenceParameter);
		parameters.add(followerParameters);
		parameters.add(timeRestrictionParameter);
		parameters.add(valueMatchingParameter);
		parameters.add(shorterOrLongerParameter);
		parameters.add(sameOrDifferentParameter);
		parameters.add(timeDurationParameter);
		parameters.add(valueMatchingAttributeParameter);
		parameters.add(timeTypeParameter);
		
		return parameters;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(FilterTraceFollowerTest.class);
	}


}
