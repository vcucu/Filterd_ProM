package org.processmining.tests.config;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.configurations.FilterdEventRateConfig;
import org.processmining.filterd.filters.FilterdEventRateFilter;
import org.processmining.tests.filters.FilterdPackageTest;

public class ConfigEventRateTest extends FilterdPackageTest{

	public ConfigEventRateTest() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Test
	public void test() throws Throwable {
		XLog typed = parseLog("freq-occurence", "test_check_validity_invalid.xes");
		FilterdEventRateConfig config = new FilterdEventRateConfig(typed, new FilterdEventRateFilter());
		assert(config.checkValidity(originalLog));
		
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(ConfigEventRateTest.class);
	}

}
