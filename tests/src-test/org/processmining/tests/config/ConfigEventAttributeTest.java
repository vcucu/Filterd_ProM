package org.processmining.tests.config;

import java.util.ArrayList;
import java.util.Arrays;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.configurations.FilterdEventAttrCategoricalConfig;
import org.processmining.filterd.configurations.FilterdEventAttrConfig;
import org.processmining.filterd.configurations.FilterdEventAttrDateConfig;
import org.processmining.filterd.configurations.FilterdEventAttrNumericalConfig;
import org.processmining.filterd.filters.FilterdEventAttrFilter;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.tests.filters.FilterdPackageTest;

public class ConfigEventAttributeTest extends FilterdPackageTest {

	public ConfigEventAttributeTest() throws Exception {
		super();
	}
	
	/* a log without any timestamp attribute should be invalid */
	@Test
	public void testTimeInvalid() throws Exception {
		XLog invalid = parseLog("event-attribute", "test_timeframe_invalid.xes");
		FilterdEventAttrDateConfig config = new FilterdEventAttrDateConfig(originalLog, new FilterdEventAttrFilter());
		/* initialize random range parameter from original log */
		ParameterRangeFromRange<Integer> range = (ParameterRangeFromRange) config.getParameter("time-range");
		range.setChosenPair(new ArrayList<Integer>( 
	            Arrays.asList(2, 16)));
		assert !config.checkValidity(invalid);
	}
	
	@Test
	public void testTimeValid() throws Exception {
		FilterdEventAttrDateConfig config = new FilterdEventAttrDateConfig(originalLog, new FilterdEventAttrFilter());
		/* initialize random range parameter from original log */
		ParameterRangeFromRange<Integer> range = (ParameterRangeFromRange) config.getParameter("time-range");
		range.setChosenPair(new ArrayList<Integer>( 
	            Arrays.asList(2, 16)));
		assert config.checkValidity(originalLog); // original log should be valid
	}
	
	@Test
	public void testCatValid() throws Exception {
		FilterdEventAttrCategoricalConfig config = 
				new FilterdEventAttrCategoricalConfig(originalLog, new FilterdEventAttrFilter(), "lifecycle:transition");
		assert config.checkValidity(originalLog); // original log should be valid
	}
	
	@Test
	public void testCatInvalid() throws Exception {
		XLog typed = parseLog("event-attribute", "test_attribute_types.xes");
		FilterdEventAttrCategoricalConfig config = 
				new FilterdEventAttrCategoricalConfig(originalLog, new FilterdEventAttrFilter(), "lifecycle:transition");
		assert !config.checkValidity(typed); // original log should be valid
	}
	
	@Test
	public void testNumericalValid() throws Exception {
		FilterdEventAttrNumericalConfig config = 
				new FilterdEventAttrNumericalConfig(originalLog, new FilterdEventAttrFilter(), "delivery");
		assert config.checkValidity(originalLog); // original log should be valid
	}
	
	@Test
	public void testNumericalInvalid() throws Exception {
		XLog typed = parseLog("event-attribute", "test_attribute_types.xes");
		FilterdEventAttrNumericalConfig config = 
				new FilterdEventAttrNumericalConfig(originalLog, new FilterdEventAttrFilter(), "delivery");
		assert !config.checkValidity(typed); // original log should be valid
	}
	
	@Test
	public void testSwitchReference() throws Exception {
		XLog typed = parseLog("event-attribute", "test_attribute_types.xes");
		FilterdEventAttrConfig config = new FilterdEventAttrConfig(typed, new FilterdEventAttrFilter());
		/* switch the reference to name - literal - categorical */
		config.switchReference("concept:name");
		assert (config.getConcreteReference() instanceof FilterdEventAttrCategoricalConfig);
		/* switch the reference to bool - boolean - categorical */
		config.switchReference("bool");
		assert (config.getConcreteReference() instanceof FilterdEventAttrCategoricalConfig);
		/* switch the reference to cost - continuous - numerical */
		config.switchReference("cost:total");
		assert (config.getConcreteReference() instanceof FilterdEventAttrNumericalConfig);
		/* switch the reference to discrete - discrete - numerical */
		config.switchReference("discrete");
		assert (config.getConcreteReference() instanceof FilterdEventAttrNumericalConfig);
		/* switch the reference to id - id - categorical */
		config.switchReference("identity:id");
		assert (config.getConcreteReference() instanceof FilterdEventAttrCategoricalConfig);
		/* switch the reference to time - timestamp - date */
		config.switchReference("time:timestamp");
		assert (config.getConcreteReference() instanceof FilterdEventAttrDateConfig);
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(ConfigEventAttributeTest.class);
	}
}
