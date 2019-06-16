package org.processmining.tests.adapters;

import org.junit.Test;
import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.configurations.FilterdTraceSampleConfig;
import org.processmining.filterd.filters.FilterdTraceSampleFilter;
import org.processmining.filterd.gui.adapters.FilterButtonAdapted;
import org.processmining.filterd.models.YLog;
import org.processmining.tests.filters.FilterdPackageTest;

public class FilterButtonAdaptedTest extends FilterdPackageTest {
	
	FilterButtonAdapted filter = new FilterButtonAdapted();
	
	public FilterButtonAdaptedTest() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Test
	public void testIndexMethods() {
		// Set the index of the filter model
		filter.setIndex(10);
		// Get the index of the filter model
		int index = filter.getIndex();
		// Check that the index was properly set
		assertEquals(index, 10);
	}
	
	@Test
	public void testNameMethods() {
		// Set the name of the filter model
		filter.setName("Filterd");
		// Get the name of the filter model
		String name = filter.getName();
		// Check that the name was properly set
		assertTrue(name.equals("Filterd"));
	}
	
	@Test
	public void testFilterConfigMethods() {
		// Create new YLog
		YLog log = new YLog(0, "Original Log", originalLog, 0);
		// Create new configuration for the filter model
		FilterdAbstractConfig config = new FilterdTraceSampleConfig(log.get(), 
				new FilterdTraceSampleFilter());
		// Set the configuration of the filter model
		filter.setFilterConfig(config);
		// Get the configuration of the newly created filter model
		FilterdAbstractConfig newConfig = filter.getFilterConfig();
		// Check that the configuration was properly set
		assertTrue(newConfig.equals(config));
	}

}
