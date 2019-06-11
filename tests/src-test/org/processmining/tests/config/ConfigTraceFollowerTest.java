package org.processmining.tests.config;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.configurations.FilterdTraceFollowerConfig;
import org.processmining.filterd.filters.FilterdTraceFollowerFilter;
import org.processmining.tests.filters.FilterdPackageTest;

public class ConfigTraceFollowerTest extends FilterdPackageTest {

	public ConfigTraceFollowerTest() throws Exception {
		super();
	}
	
	/* Testing correct working of can populate */
	@Test
	public void testCanPopulate() throws Exception {
		FilterdTraceFollowerConfig config = new FilterdTraceFollowerConfig(originalLog, new FilterdTraceFollowerFilter());
		assertEquals(true, config.canPopulate(null));
	}
	
	/* a log that is different should be invalid */
	@Test
	public void testInvalid() throws Exception {
		XLog invalid = parseLog("trace-follower", "test_follower_directly_1.xes");
		FilterdTraceFollowerConfig config = new FilterdTraceFollowerConfig(originalLog, new FilterdTraceFollowerFilter());
		assert !config.checkValidity(invalid);
	}

	/* a log that is the same should be valid */
	@Test
	public void testValid() throws Exception {
		FilterdTraceFollowerConfig config = new FilterdTraceFollowerConfig(originalLog, new FilterdTraceFollowerFilter());
		assert config.checkValidity(originalLog);
	}
	
}
