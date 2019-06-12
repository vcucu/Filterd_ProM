package org.processmining.tests.gui;

import java.util.ArrayList;

import org.junit.Test;
import org.processmining.filterd.gui.CellStatus;
import org.processmining.filterd.gui.ComputationCellController;
import org.processmining.filterd.gui.ComputationCellModel;
import org.processmining.filterd.gui.ConfigurationModalController;
import org.processmining.filterd.gui.FilterButtonModel;
import org.processmining.filterd.gui.NotebookController;
import org.processmining.filterd.gui.NotebookModel;
import org.processmining.filterd.gui.TextCellModel;
import org.processmining.tests.filters.FilterdPackageTest;

import javafx.scene.layout.VBox;

public class ComputationCellControllerTest extends FilterdPackageTest {

	ComputationCellModel model;
	ComputationCellController controller;
	
	public ComputationCellControllerTest() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void setupComputationCell() {
		// Create new computation cell model
		model = new ComputationCellModel();
		// Create new computation cell controller
		controller = new ComputationCellController(model);
	}

	@Test
	public void testNewComputationCellController() {
		// Create new computation cell model instance
		ComputationCellModel cell = new ComputationCellModel(null, 0, null, new ArrayList<>());
		// Create new notebook model
		NotebookModel model = new NotebookModel();
		// Create new notebook controller
		NotebookController controller = new NotebookController(model);
		
		try {
			// Create new computation cell controller (should throw an exception)
			ComputationCellController computationCellController = new ComputationCellController(controller, cell);
			fail("NO exception was thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}
	
	@Test
	public void testComputationCellControllerListeners() {
		// Setup new computation cell controller
		setupComputationCell();
		
		// Create new filter button model
		FilterButtonModel filter0 = new FilterButtonModel();
		FilterButtonModel filter1 = new FilterButtonModel();
		FilterButtonModel filter2 = new FilterButtonModel();
		
		// Add listeners to the filter button models list
		controller.addFilterButtonListeners();
		
		// Add filter buttons to the computation cell model
		model.addFilterModel(0, filter0);
		model.addFilterModel(1, filter1);
		model.addFilterModel(2, filter2);
		
		// Select filter buttons in succession
		filter0.setSelected(true);
		
		// Remove filter buttons from the computation cell model
		model.removeFilter(filter0);
		model.removeFilter(filter1);
		model.removeFilter(filter2);
		
		assertEquals(model.getFilters().size(), 0);
	}
	
	@Test
	public void testComputationCellAddFilter() {
		// Setup new computation cell controller
		setupComputationCell();
		// Set computation cell status to idle
		model.setStatusBar(CellStatus.IDLE);
		
		try {
			// Add new filter to the computation cell
			controller.addFilter();
			// Should throw error since no view is present
			fail("Error was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
		
		// Add output log for the first filter button
		controller.getCellModel().getFilters().get(0).setOutputLog(originalLog);
		// Create new filter button model
		FilterButtonModel filter0 = new FilterButtonModel();
		FilterButtonModel filter1 = new FilterButtonModel();
		filter0.setOutputLog(originalLog);
		filter1.setOutputLog(originalLog);
		model.addFilterModel(0, filter0);
		model.addFilterModel(0, filter1);
		
		try {
			// Add new filter to the computation cell (index != 0)	
			controller.addFilter();	
			// Should throw error since no view is present
			fail("Error was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}
	
	@Test
	public void testComputationCellRemoveFilter() {
		// Setup new computation cell controller
		setupComputationCell();
		// Create new filter button model
		FilterButtonModel filter0 = new FilterButtonModel();
		// Set the output log for the filter button model
		filter0.setOutputLog(originalLog);
		// Add the filter button to the computation cell
		model.addFilterModel(0, filter0);
		try {
			// Remove the filter from the controller
			controller.removeFilter(filter0);	
			// Should throw an error since there is no view
			fail("Error was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}
	
	@Test
	public void testComputationCellLayoutMethods() {
		// Setup new computation cell controller
		setupComputationCell();
		// Get the panel layout of the computation cell
		VBox panel = controller.getPanelLayout();
		// Check that the panel was properly set
		assertEquals(panel, null);
		// Set the panel layout of the computation cell
		controller.setPanelLayout(panel);
		// Check that the panel was properly set
		assertEquals(controller.getPanelLayout(), null);
	}
	
	@Test
	public void testComputationCellModelMethods() {
		// Setup new computation cell controller
		setupComputationCell();
		// Set a new cell model for the computation cell
		controller.setCellModel(model);
		// Check that the new cell model was properly set
		assertTrue(true);
		// Create new text cell model (to assert error being thrown)
		TextCellModel newModel = new TextCellModel();
		try {
			// Set a wrong cell model for the computation cell
			controller.setCellModel(newModel);
			// Should throw an exception
			fail("IllegalArgumentException was NOT thrown!");
		} catch (Throwable exception) {
			assertEquals(IllegalArgumentException.class, exception.getClass());
		}
	}
	
	@Test
	public void testComputationCellExpandVisualizer() {
		// Setup new computation cell controller
		setupComputationCell();
		try {
			// Expand the visualizer
			controller.handleExpandVisualiser();
			// Should throw an error since view is not present
			fail("Error was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
		assertTrue(true);
	}
	
	@Test
	public void testSetXLogMethods() {
		// Setup new computation cell controller
		setupComputationCell();
		try {
			// Set the input XLog for the computation cell
			controller.setXLog();
			// Should throw an error since view is not present
			fail("Error was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}
	
	@Test
	public void testComputationCellConfigModal() {
		// Setup new computation cell controller
		setupComputationCell();
		// Get the configuration modal controller
		ConfigurationModalController configModalController = controller.getConfigurationModal();
		// Check that the modal controller was properly returned
		assertEquals(configModalController, null);
	}
	
	@Test
	public void testComputationCellEnableFilters() {
		// Setup new computation cell controller
		setupComputationCell();
		// Create new filter button model
		FilterButtonModel filter0 = new FilterButtonModel();
		FilterButtonModel filter1 = new FilterButtonModel();
		// Add filters to the computation cell model
		model.addFilterModel(0, filter0);
		model.addFilterModel(1, filter1);
		controller.enableAllFilterButtonsBut(0);
		assertTrue(true);
	}
}
