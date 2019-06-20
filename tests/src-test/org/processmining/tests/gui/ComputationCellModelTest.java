package org.processmining.tests.gui;

import java.util.ArrayList;
import java.util.List;

import org.deckfour.uitopia.api.model.ViewType;
import org.junit.Test;
import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.configurations.FilterdTraceSampleConfig;
import org.processmining.filterd.filters.FilterdTraceSampleFilter;
import org.processmining.filterd.gui.ComputationCellModel;
import org.processmining.filterd.gui.FilterButtonModel;
import org.processmining.filterd.models.YLog;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.tests.filters.FilterdPackageTest;

import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class ComputationCellModelTest extends FilterdPackageTest {

	public ComputationCellModelTest() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	@Test
	public void testNewComputationCellModel() {
		// Create new list of input logs for the computation cell
		List<YLog> logs = new ArrayList<>();
		// Create new YLog
		YLog initialLog = new YLog(0, "Original Log", originalLog, 0);
		logs.add(initialLog);
		// Create new computation cell model instance
		ComputationCellModel cell = new ComputationCellModel(null, 0, null, logs);
		// Check that the computation cell model was properly created
		assertTrue(cell instanceof ComputationCellModel);
		// Get the list of input logs
		List<YLog> inputLogs = cell.inputLogs;
		// Check the list of input logs was properly initialized
		assertTrue(logs.equals(inputLogs));
		// Check the output list is empty
		assertEquals(cell.getOutputLogs().size(), 1);
	}
	
	@Test
	public void testFiltersList() {
		// Create new computation cell model instance
		ComputationCellModel cell = new ComputationCellModel(null, 0, null, new ArrayList<>());
		// Create new YLog
		YLog initialLog = new YLog(0, "Original Log", originalLog, 0);
		
		// Create new filter button model (to be added to the computation cell filter panel)
		FilterButtonModel model = new FilterButtonModel(0);
		model.setInputLog(originalLog);
		
		// Add model to the filter models list
		cell.addFilterModel(0, model);
		// Check the filter models list was properly updated
		assertEquals(cell.getFilters().size(), 1);
		assertTrue(cell.getFilters().get(0).equals(model));
		
		// Remove model from filter list
		cell.removeFilter(model);
		// Check the filter models list was properly updated
		assertEquals(cell.getFilters().size(), 0);
	}
	
	@Test
	public void testInputLog() {
		// Create new computation cell model
		ComputationCellModel cell = new ComputationCellModel(null, 0, null, new ArrayList<>());
		// Create new YLog
		YLog initialLog = new YLog(0, "Original Log", originalLog, 0);
		// Set a new new log for the cell
		cell.setInputLog(initialLog);
		// Check that the new log was properly set
		assertTrue(equalLog(originalLog, cell.inputLog.get()));
	}
	
	@Test
	public void testNullInputLog() {
		try {
			// Create new computation cell model
			ComputationCellModel cell = new ComputationCellModel(null, 0, null, new ArrayList<>());
			// Create new null YLog (to be passed to the computation cell)
			YLog initialLog = null;
			// Set a new log for the cell
			cell.setInputLog(initialLog);
		} catch (Throwable expected) {
			assertEquals(IllegalArgumentException.class, expected.getClass());
		}
	}
	
	@Test
	public void testInputLogs() {
		// Create new list of input logs for the computation cell
		List<YLog> logs = new ArrayList<>();
		// Create new YLog
		YLog initialLog = new YLog(0, "Original Log", originalLog, 0);
		logs.add(initialLog);
		// Create new computation cell model instance
		ComputationCellModel cell = new ComputationCellModel(null, 0, null, logs);
		// Set a new input log list
		cell.setInputLogs(logs);
		// Get the newly set input log list
		List<YLog> inputLogs = cell.inputLogs;
		// Check that the list is equal to what was initially set
		assertTrue(logs.equals(inputLogs));
	}
	
	@Test
	public void testOutputLogs() {
		// Create new list of input logs for the computation cell
		ObservableList<YLog> logs = FXCollections.observableArrayList();
		// Create new YLog
		YLog initialLog = new YLog(0, "Original Log", originalLog, 0);
		logs.add(initialLog);
		// Create new computation cell model instance
		ComputationCellModel cell = new ComputationCellModel(null, 0, null, logs);
		// Set a new input log list
		cell.setOutputLogs(logs);
		// Get the newly set input log list
		List<YLog> outputLogs = cell.inputLogs;
		// Check that the list is equal to what was initially set
		assertTrue(logs.equals(outputLogs));
	}
	
	@Test
	public void testSelectFilterButton() {
		// Create new computation cell model instance
		ComputationCellModel cell = new ComputationCellModel(null, 0, null, new ArrayList<>());
		// Create new YLog
		YLog initialLog = new YLog(0, "Original Log", originalLog, 0);
		
		// Create new filter button models (to be added to the computation cell filter panel)
		FilterButtonModel model0 = new FilterButtonModel(0);
		model0.setInputLog(originalLog);
		FilterButtonModel model1 = new FilterButtonModel(0);
		model1.setInputLog(originalLog);
		
		// Add models to the filter models list
		cell.addFilterModel(0, model0);
		cell.addFilterModel(0, model1);
		
		// Check that all filters were added to the cell filter list
		assertEquals(cell.getFilters().size(), 2);
		
		// Select first filter button and check if the other button models were properly updated
		cell.selectFilter(model0);
		
		// Count the number of unselected filter buttons (from within the filter panel)
		int unselectedButtons = 0;
		for (FilterButtonModel model : cell.getFilters()) {
			if (model.getSelected() == false) {
				unselectedButtons += 1;
			}
		}
		
		// Check the number of unselected filter buttons is the expected one
		assertEquals(unselectedButtons, 1);
	}
	
	@Test
	public void testGetVisualizers() {
		try {
			// Create new computation cell model instance
			ComputationCellModel cell = new ComputationCellModel(null, 0, null, new ArrayList<>());
			// Create new YLog
			YLog initialLog = new YLog(0, "Original Log", originalLog, 0);
			cell.setInputLog(initialLog);
			List<ViewType> visualizers = cell.getVisualizers();
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable expected) {
			assertEquals(NullPointerException.class, expected.getClass());
		}
	}
	
	@Test
	public void testComputationCellComputeSimple() {
		// Create new computation cell model instance
		ComputationCellModel cell = new ComputationCellModel(null, 0, null, new ArrayList<>());
		// Create new YLog
		YLog initialLog = new YLog(0, "Original Log", originalLog, 0);
		// Set the initial log for the computation cell
		cell.setInputLog(initialLog);
		// Create new filter button models
		FilterButtonModel model0 = new FilterButtonModel(0);
		model0.setInputLog(originalLog);
		// Add filter buttons to the computation cell filter list
		cell.addFilterModel(0, model0);
		// Add filter configurations to the filter buttons
		FilterdAbstractConfig config = new FilterdTraceSampleConfig(initialLog.get(),
				new FilterdTraceSampleFilter());
		// Set the filter configuration parameters
		((ParameterValueFromRange<Integer>) config.getParameters().get(0)).setChosen(2);
		// Set filter configurations for the filter buttons
		model0.setFilterConfig(config);
		// Compute the cell's output
		cell.computeWithTask(new Task<Void> () {

			protected Void call() throws Exception {
				return null;
			}
			
		});
		// Check that the computation was successful
		assertEquals(cell.getOutputLogs().get(0).get().size(), 2);
	}
	
	@Test
	public void testComputationCellComputeInvalidConfiguration() {
		// Create new computation cell model instance
		ComputationCellModel cell = new ComputationCellModel(null, 0, null, new ArrayList<>());
		// Create new YLog
		YLog initialLog = new YLog(0, "Original Log", originalLog, 0);
		// Set the initial log for the computation cell
		cell.setInputLog(initialLog);
		// Create new filter button models
		FilterButtonModel model0 = new FilterButtonModel(0);
		model0.setInputLog(originalLog);
		FilterButtonModel model1 = new FilterButtonModel(1);
		model0.setInputLog(originalLog);
		// Add filter buttons to the computation cell filter list
		cell.addFilterModel(0, model0);
		cell.addFilterModel(1, model1);
		// Add filter configurations to the filter buttons
		FilterdAbstractConfig config0 = new FilterdTraceSampleConfig(initialLog.get(),
				new FilterdTraceSampleFilter());
		FilterdAbstractConfig config1 = new FilterdTraceSampleConfig(initialLog.get(),
				new FilterdTraceSampleFilter());
		// Set the filter configuration parameters
		((ParameterValueFromRange<Integer>) config0.getParameters().get(0)).setChosen(1);
		((ParameterValueFromRange<Integer>) config1.getParameters().get(0)).setChosen(2);
		// Set filter configurations for the filter buttons
		model0.setFilterConfig(config0);
		model0.setFilterConfig(config1);
		
		try {
			// Compute the cell's output
			cell.computeWithTask(new Task<Void> () {

				protected Void call() throws Exception {
					return null;
				}
				
			});
		} catch (Throwable exception) {
			// Check that the computation was unsuccessful (TODO: there should be a message displayed somewhere)
			assertFalse(exception.equals(null));
		}
	}
	
	@Test
	public void testComputingProperty() {
		// Create new computation cell model instance
		ComputationCellModel cell = new ComputationCellModel(null, 0, null, new ArrayList<>());
		// Get the state of the cell model
		boolean computing = cell.isComputing.get();
		// Check the state of the cell model
		assertFalse(computing);
		// Get the computing property of the cell model
		BooleanProperty compProp = cell.isComputing;
		// Check that the property was properly returned
		assertFalse(compProp.get());
	}
}
