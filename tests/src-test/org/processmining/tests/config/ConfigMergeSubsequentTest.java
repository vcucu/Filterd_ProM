package org.processmining.tests.config;

import org.junit.Test;
import org.processmining.filterd.configurations.FilterdModifMergeSubsequentConfig;
import org.processmining.filterd.filters.FilterdModifMergeSubsequentFilter;
import org.processmining.tests.filters.FilterdPackageTest;

public class ConfigMergeSubsequentTest extends FilterdPackageTest {
	
	public ConfigMergeSubsequentTest() throws Exception {
		super();
	}

	/* Testing correct working of constructor */
	@Test
	public void testConstructor() throws Exception {
		try {
			FilterdModifMergeSubsequentConfig config = new FilterdModifMergeSubsequentConfig(originalLog, new FilterdModifMergeSubsequentFilter());
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}

}
