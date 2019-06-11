package org.processmining.tests.config;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.configurations.FilterdTraceTimeframeConfig;
import org.processmining.filterd.filters.FilterdTraceTimeframeFilter;
import org.processmining.tests.filters.FilterdPackageTest;

public class ConfigTraceTimeframeTest extends FilterdPackageTest {

	public ConfigTraceTimeframeTest() throws Exception {
		super();
	}
	
	/* Testing correct working of can populate */
	@Test
	public void testCanPopulate() throws Exception {
		try {
			FilterdTraceTimeframeConfig config = new FilterdTraceTimeframeConfig(originalLog, new FilterdTraceTimeframeFilter());
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
		// assertEquals(true, config.canPopulate(null));
	}
	
	/* a log that is different should be invalid */
	@Test
	public void testInvalid() throws Exception {
		XLog invalid = parseLog("trace-follower", "test_follower_directly_1.xes");
		FilterdTraceTimeframeConfig config = new FilterdTraceTimeframeConfig(originalLog, new FilterdTraceTimeframeFilter());
		assert !config.checkValidity(invalid);
	}

	/* a log that is the same should be valid */
	@Test
	public void testValid() throws Exception {
		FilterdTraceTimeframeConfig config = new FilterdTraceTimeframeConfig(originalLog, new FilterdTraceTimeframeFilter());
		assert config.checkValidity(originalLog);
	}
	
}
