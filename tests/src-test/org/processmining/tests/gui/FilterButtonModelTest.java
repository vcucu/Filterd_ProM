package org.processmining.tests.gui;

import org.junit.Test;
import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.configurations.FilterdTraceSampleConfig;
import org.processmining.filterd.configurations.FilterdTraceStartEventConfig;
import org.processmining.filterd.filters.FilterdTraceSampleFilter;
import org.processmining.filterd.filters.FilterdTraceStartEventFilter;
import org.processmining.filterd.gui.FilterButtonModel;
import org.processmining.filterd.gui.InvalidConfigurationException;
import org.processmining.filterd.models.YLog;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.tests.filters.FilterdPackageTest;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;

public class FilterButtonModelTest extends FilterdPackageTest {

	public FilterButtonModelTest() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	@Test
	public void testNewFilterButtonModel() throws Throwable {
		// Create new YLog
		YLog initialLog = new YLog(0, "Original Log", originalLog);
		// Create new filter button instance
		FilterButtonModel filter = new FilterButtonModel(0, initialLog);
		// Check the filter button was created 
		assertTrue(filter instanceof FilterButtonModel);
		// Check the filter button is not null
		assertTrue(filter != null);
		// Check the filter button name is properly initialized
		assertTrue(filter.getName().length() > 0);
		// Check the filter button index is properly initialized
		assertTrue(filter.getIndex() >= 0);
		// Check the filter button selected is properly initialized
		assertFalse(filter.getSelected());
	}
	
	@Test
	public void testNameProperty() {
		// Create new YLog
		YLog initialLog = new YLog(0, "Original Log", originalLog);
		// Create new filter button instance
		FilterButtonModel filter = new FilterButtonModel(0, initialLog);
		// Set a new filter button name
		filter.setName("Filterd");
		// Get the new filter button name
		String name = filter.getName();
		// Check the filter button name
		assertTrue(name.equals("Filterd"));
		// Check the type of the name property is properly returned 
		assertTrue(filter.nameProperty() instanceof StringProperty);
	}
	
	@Test
	public void testIndexProperty() {
		// Create new YLog
		YLog initialLog = new YLog(0, "Original Log", originalLog);
		// Create new filter button instance
		FilterButtonModel filter = new FilterButtonModel(0, initialLog);
		// Set a new filter button index
		filter.setIndex(10);
		// Get the filter button index
		int index = filter.getIndex();
		// Check the filter button index
		assertTrue(index == 10);
	}
	
	@Test
	public void testSelectedProperty() {
		// Create new YLog
		YLog initialLog = new YLog(0, "Original Log", originalLog);
		// Create new filter button instance
		FilterButtonModel filter = new FilterButtonModel(0, initialLog);
		// Set a new filter button selected variable
		filter.setSelected(true);
		// Get the filter button selected variable
		boolean selected = filter.getSelected();
		// Check the filter button selected variable
		assertTrue(selected);
		// Check the type of the selected property is properly returned
		assertTrue(filter.selectedProperty() instanceof BooleanProperty);
	}
	
	@Test
	public void testFilterButtonConfig() {
		// Create new YLog
		YLog initialLog = new YLog(0, "Original Log", originalLog);
		// Create new filter button instance
		FilterButtonModel filter = new FilterButtonModel(0, initialLog);
		// Create new abstract filter configuration
		FilterdAbstractConfig config = new FilterdTraceStartEventConfig(originalLog,
				new FilterdTraceStartEventFilter());
		// Set a new filter button configuration
		filter.setFilterConfig(config);
		// Check the filter button configuration
		assertTrue(config == filter.getFilterConfig());
		// Check the type of the filter button configuration
		assertTrue(filter.getFilterConfig() instanceof FilterdAbstractConfig);
	}
	
	@Test
	public void testFilterButtonOutputLog() {
		// Create new YLog
		YLog initialLog = new YLog(0, "Original Log", originalLog);
		// Create new filter button instance
		FilterButtonModel filter = new FilterButtonModel(0, initialLog);
		// Get the output log of the filter button
		YLog outputLog = filter.getOutputLog();
		// Check the output log is not null
		assertTrue(outputLog != null);
	}
	
	@Test
	public void testFilterButtonComputeIllegalStateException() {
		// Create new YLog
		YLog initialLog = new YLog(0, "Original Log", originalLog);
		// Create new filter button instance
		FilterButtonModel filter = new FilterButtonModel(0, initialLog);
		// Create new configuration for the button instance
		FilterdAbstractConfig config = new FilterdTraceSampleConfig(initialLog.get(),
				new FilterdTraceSampleFilter());
		// Set the filter configuration parameters
		((ParameterValueFromRange<Integer>) config.getParameters().get(0)).setChosen(initialLog.get().size()+5);
		// Set the configuration for the filter button
		filter.setFilterConfig(config);
		
		try {
			// Compute the cell's output
			filter.compute();
			fail("InvalidConfigurationException was NOT thrown!");
		} catch (Throwable exception) {
			assertEquals(InvalidConfigurationException.class, exception.getClass());
		}
	}
	
	@Test
	public void testFilterButtonComputeNullLog() {
		// Create new YLog
		YLog initialLog = new YLog(0, "Original Log", originalLog);
		// Create new filter button instance
		FilterButtonModel filter = new FilterButtonModel(0, initialLog);
		// Create new configuration for the button instance
		FilterdAbstractConfig config = new FilterdTraceSampleConfig(initialLog.get(),
				new FilterdTraceSampleFilter());
		// Set the filter configuration parameters
		((ParameterValueFromRange<Integer>) config.getParameters().get(0)).setChosen(initialLog.get().size()+5);
		// Set the configuration for the filter button
		filter.setFilterConfig(config);
		
		// Set the initial log to null
		initialLog.setLog(null);
		
		try {
			// Compute the cell's output
			filter.compute();
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			assertEquals(NullPointerException.class, exception.getClass());
		}
	}
}
