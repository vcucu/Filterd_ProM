package org.processmining.tests.adapters;

import org.junit.Test;
import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.configurations.FilterdTraceSampleConfig;
import org.processmining.filterd.filters.FilterdTraceSampleFilter;
import org.processmining.filterd.gui.FilterButtonModel;
import org.processmining.filterd.gui.adapters.FilterButtonAdapted;
import org.processmining.filterd.gui.adapters.FilterButtonAdapter;
import org.processmining.filterd.models.YLog;
import org.processmining.tests.filters.FilterdPackageTest;

public class FilterButtonAdapterTest extends FilterdPackageTest {
	
	FilterButtonAdapter adapter = new FilterButtonAdapter();

	public FilterButtonAdapterTest() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	@Test
	public void testFilterButtonAdapter() {
		
		// --------------- TESTING MARSHAL ---------------
		// Create new YLog
		YLog log = new YLog(0, "Original Log", originalLog, 0);
		// Create new filter model
		FilterButtonModel filter = new FilterButtonModel(0);
		// Create new config for the filter model
		FilterdAbstractConfig config = new FilterdTraceSampleConfig(log.get(), 
				new FilterdTraceSampleFilter());
		// Set filter model index
		filter.setIndex(10);
		// Set filter model name
		filter.setName("Filterd");
		// Set filter model config
		filter.setFilterConfig(config);
		// Compute adapted computation cell model
		FilterButtonAdapted newFilter = null;
		try {
			newFilter = adapter.marshal(filter);
		} catch (Exception e) {
			fail("Error was thrown!");
		}
		// Check the adapted model was properly created
		assertTrue(newFilter.getName().equals("Filterd"));
		assertEquals(newFilter.getIndex(), 10);
		
		// --------------- TESTING UNMARSHAL ---------------
		// Create new computation cell from the adapted model
		FilterButtonModel adaptedFilter = null;
		try {
			adaptedFilter = adapter.unmarshal(newFilter);
		} catch (Exception e) {
			fail("Error was thrown!");
		}
		// Check that the new cell was properly created
		assertTrue(adaptedFilter.getName().equals("Filterd"));
		assertEquals(adaptedFilter.getIndex(), 10);
	}
}
