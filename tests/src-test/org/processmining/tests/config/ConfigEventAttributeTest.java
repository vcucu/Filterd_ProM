package org.processmining.tests.config;

import org.junit.Test;
import org.processmining.filterd.configurations.FilterdEventAttrDateConfig;
import org.processmining.filterd.filters.FilterdEventAttrFilter;
import org.processmining.tests.filters.FilterdPackageTest;

public class ConfigEventAttributeTest extends FilterdPackageTest {

	// check validity of an xlog
	
	public ConfigEventAttributeTest() throws Exception {
		super();
	}
	
	@Test
	public void checkValidityTrue() {
	}
	
	@Test
	public void test() {
		FilterdEventAttrDateConfig config = new FilterdEventAttrDateConfig(originalLog, new FilterdEventAttrFilter());
		assert config.checkValidity(originalLog);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(ConfigEventAttributeTest.class);
	}
}
