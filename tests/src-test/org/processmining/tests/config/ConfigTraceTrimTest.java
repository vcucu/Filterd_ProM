package org.processmining.tests.config;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.configurations.FilterdTraceTrimConfig;
import org.processmining.filterd.filters.FilterdTraceTrimFilter;
import org.processmining.tests.filters.FilterdPackageTest;

public class ConfigTraceTrimTest extends FilterdPackageTest {

	public ConfigTraceTrimTest() throws Exception {
		super();
	}
	
	/* Testing correct working of can populate */
	@Test
	public void testCanPopulate() throws Exception {
		FilterdTraceTrimConfig config = new FilterdTraceTrimConfig(originalLog, new FilterdTraceTrimFilter());
		assertEquals(true, config.canPopulate(null));
	}
	
	/* a log that is different should be invalid */
	@Test
	public void testInvalid() throws Exception {
		XLog invalid = parseLog("trace-trim", "test_endpoints_2.xes");
		FilterdTraceTrimConfig config = new FilterdTraceTrimConfig(originalLog, new FilterdTraceTrimFilter());
		assert !config.checkValidity(invalid);
	}

	/* a log that is the same should be valid */
	@Test
	public void testValid() throws Exception {
		FilterdTraceTrimConfig config = new FilterdTraceTrimConfig(originalLog, new FilterdTraceTrimFilter());
		assert config.checkValidity(originalLog);
	}

}
