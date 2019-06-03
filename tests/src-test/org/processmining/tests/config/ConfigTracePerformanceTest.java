package org.processmining.tests.config;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.configurations.FilterdTracePerformanceConfig;
import org.processmining.filterd.filters.FilterdTracePerformanceFilter;
import org.processmining.tests.filters.FilterdPackageTest;

public class ConfigTracePerformanceTest extends FilterdPackageTest {

	public ConfigTracePerformanceTest() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	/*
	 * Return true since checkValidity() does not contain restrictions on the candidate log
	 */
	
	@Test
	public void test() throws Throwable {
		XLog typed = parseLog("start-events", "test_check_validity_invalid.xes");
		FilterdTracePerformanceConfig config = new FilterdTracePerformanceConfig(typed, 
				new FilterdTracePerformanceFilter());
		
		assert(config.checkValidity(originalLog));
		
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(ConfigTracePerformanceTest.class);
	}

}
