package org.processmining.tests.config;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.configurations.FilterdTraceSampleConfig;
import org.processmining.filterd.filters.FilterdTraceSampleFilter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.tests.filters.FilterdPackageTest;

public class ConfigTraceSampleTest extends FilterdPackageTest {

	public ConfigTraceSampleTest() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * Setting a random value bigger than the number of traces returns false
	 * Log size 2. 
	 * Chosen value: 10
	 */
	@Test
	public void testLogSizeSmaller() throws Throwable {
		XLog typed = originalLog;
		FilterdTraceSampleConfig config = new FilterdTraceSampleConfig(typed, new FilterdTraceSampleFilter());
		
		ParameterValueFromRange<Integer> casted = (ParameterValueFromRange<Integer>) config.getParameter("threshold");
		casted.setChosen(10);
		XLog cand = parseLog("start-events", "test_check_validity_invalid.xes");
		
		assert(!config.checkValidity(cand));
	}
	
	
	@Test
	/*
	 * CanPopulate always returns true for this configuration.
	 */
	public  void testCanPopulate() throws Throwable {
		XLog typed = originalLog;
		FilterdTraceSampleConfig config = new FilterdTraceSampleConfig(typed, new FilterdTraceSampleFilter());
		ParameterValueFromRange<Integer> casted = (ParameterValueFromRange<Integer>) config.getParameter("threshold");
		casted.setChosen(1);
		assertTrue(config.canPopulate(new FilterConfigPanelController()));
		
	}
	public static void main(String[] args) {
		junit.textui.TestRunner.run(ConfigTraceSampleTest.class);
	}

}
