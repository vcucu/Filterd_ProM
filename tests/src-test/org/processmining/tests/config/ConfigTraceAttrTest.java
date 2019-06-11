package org.processmining.tests.config;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.configurations.FilterdTraceAttrConfig;
import org.processmining.filterd.filters.FilterdTraceAttrFilter;
import org.processmining.tests.filters.FilterdPackageTest;

public class ConfigTraceAttrTest extends FilterdPackageTest {

	public ConfigTraceAttrTest() throws Exception {
		super();
	}
	
	/* Testing correct working of can populate */
	@Test
	public void testCanPopulate() throws Exception {
		FilterdTraceAttrConfig config = new FilterdTraceAttrConfig(originalLog, new FilterdTraceAttrFilter());
		assertEquals(true, config.canPopulate(null));
	}
	
	/* a log that is different should be invalid */
	@Test
	public void testInvalid() throws Exception {
		XLog invalid = parseLog("trace-attribute", "test_empty_event_log.xes");
		FilterdTraceAttrConfig config = new FilterdTraceAttrConfig(originalLog, new FilterdTraceAttrFilter());
		assert !config.checkValidity(invalid);
	}

	/* a log that is the same should be valid */
	@Test
	public void testValid() throws Exception {
		FilterdTraceAttrConfig config = new FilterdTraceAttrConfig(originalLog, new FilterdTraceAttrFilter());
		assert config.checkValidity(originalLog);
	}

}
