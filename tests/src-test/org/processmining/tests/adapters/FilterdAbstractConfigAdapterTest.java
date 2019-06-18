package org.processmining.tests.adapters;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.configurations.FilterdTraceSampleConfig;
import org.processmining.filterd.filters.FilterdTraceSampleFilter;
import org.processmining.filterd.gui.adapters.FilterdAbstractConfigAdapted;
import org.processmining.filterd.gui.adapters.FilterdAbstractConfigAdapter;
import org.processmining.tests.filters.FilterdPackageTest;

public class FilterdAbstractConfigAdapterTest extends FilterdPackageTest {
	
	FilterdAbstractConfigAdapter adapter = new FilterdAbstractConfigAdapter();

	public FilterdAbstractConfigAdapterTest() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Test
	public void testFilterdAbstractConfigAdapter() throws IllegalStateException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		// --------------- TESTING MARSHAL ---------------
		// Create new filter
		FilterdTraceSampleFilter filter = new FilterdTraceSampleFilter();
		// Create new filter configuration
		FilterdTraceSampleConfig config = new FilterdTraceSampleConfig(originalLog, filter);
		// Compute adapted filter configuration model
		FilterdAbstractConfigAdapted adaptedConfig = adapter.marshal(config);
		// Check that the configuration was properly created
		assertEquals(adaptedConfig.getClassName(), config.getClass().getName());
		
		// --------------- TESTING UNMARSHAL ---------------
		FilterdAbstractConfig newConfig = null;
		try {
			// Create new configuration from the adapted one
			newConfig = adapter.unmarshal(adaptedConfig);
			// Should throw an error since there is no input log
			fail("Error was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
		
		// Set the input of the adapter
		adapter.setInitialInput(originalLog);
		// Get the input of the adapter
		XLog log = adapter.getInitialInput();
		assertFalse(log.equals(null));
		
		newConfig = adapter.unmarshal(adaptedConfig);
		// Check that the configuration was properly created
		assertEquals(newConfig.getClass().getName(), config.getClass().getName());
	}
}
