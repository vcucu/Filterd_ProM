package org.processmining.tests.config;

import org.junit.Test;
import org.processmining.filterd.configurations.FilterdTraceTimeframeConfig;
import org.processmining.filterd.filters.FilterdTraceTimeframeFilter;
import org.processmining.tests.filters.FilterdPackageTest;

public class ConfigTraceTimeframeTest extends FilterdPackageTest {

	public ConfigTraceTimeframeTest() throws Exception {
		super();
	}
	
	
	/* Testing correct working of constructor */
	@Test
	public void testConstructor() throws Exception {
		try {
			FilterdTraceTimeframeConfig config = new FilterdTraceTimeframeConfig(originalLog, new FilterdTraceTimeframeFilter());
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}
	
}
