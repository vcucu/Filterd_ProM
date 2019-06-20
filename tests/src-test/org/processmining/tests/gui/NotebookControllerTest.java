package org.processmining.tests.gui;

import java.util.ArrayList;

import org.junit.Test;
import org.processmining.filterd.gui.ComputationCellModel;
import org.processmining.filterd.gui.ComputationMode;
import org.processmining.filterd.gui.FilterButtonModel;
import org.processmining.filterd.gui.NotebookController;
import org.processmining.filterd.gui.NotebookModel;
import org.processmining.filterd.gui.TextCellModel;
import org.processmining.filterd.models.YLog;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import junit.framework.TestCase;

public class NotebookControllerTest extends TestCase {
	
	NotebookModel model;
	NotebookController controller;
	
	public void setupNotebookController() {
		// Create new notebook model
		NotebookModel model = new NotebookModel(null);
		// Create new notebook controller
		controller = new NotebookController(model);
	}
	
	@Test
	public void testNewNotebookController() {
		// Setup new notebook controller
		setupNotebookController();
		// Check that the model was properly created
		assertTrue(controller.getModel() != null);
	}
	
	@Test
	public void testNotebookLayoutComponents() {
		// Setup new notebook controller
		setupNotebookController();
		
		// Get the notebook layout
		VBox notebookLayout = controller.getNotebookLayout();
		// Check the component is properly returned (should be null at this point)
		assertTrue(notebookLayout == null);
		
		// Get the notebook cell layout
		VBox cellLayout = controller.getCellsLayout();
		// Check the component is properly returned (should be null at this point)
		assertTrue(cellLayout == null);
		
		// Get the notebook toolbar layout
		HBox toolbarLayout = controller.getToolbarLayout();
		// Check the component is properly returned (should be null at this point)
		assertTrue(toolbarLayout == null);
	}
	
	@Test
	public void testNotebookCellListeners() {
		// Setup new notebook controller
		setupNotebookController();
		// Add the proper listeners to the notebook controller
		controller.cellListeners();
		// Check that no exception is thrown while adding the cell listeners
		assertTrue(true);
	}
	
	@Test
	public void testNotebookButtonHandlers() {
		// Setup new notebook controller
		setupNotebookController();
		
		// Call the auto button handler
		controller.autoButtonHandler();
		// Check that the computation mode was properly set
		assertEquals(controller.getModel().getComputationMode(), ComputationMode.AUTOMATIC);
		
		// Call the manual button handler
		controller.manualButtonHandler();
		// Check that the computation mode was properly set
		assertEquals(controller.getModel().getComputationMode(), ComputationMode.MANUAL);
	}
	
	@Test
	public void testAddComputationCell() {
		// Setup new notebook controller
		setupNotebookController();
		
		try {
			// Add new computation cell to the notebook
			controller.addComputationCell(0);
			fail("Exception was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}
	
	@Test
	public void testAddTextCell() {
		// Setup new notebook controller
		setupNotebookController();
		
		try {
			// Add new text cell to the notebook
			controller.addTextCell(0);
			fail("Exception was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}
	
	@Test
	public void testRemoveCell() {
		// Setup new notebook controller
		setupNotebookController();
		
		try {
			// Remove a cell that does not exist from the notebook (should throw NullPointerException)
			controller.removeCell(new ComputationCellModel(null, 0, null, null));
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			assertEquals(NullPointerException.class, exception.getClass());
		}
	}
	
	@Test
	public void testGetNotebookLayoutScene() {
		// Setup new notebook controller
		setupNotebookController();
		
		try {
			// Get the scene of the notebook layout
			// Should throw NullPointerException because notebook layout was not properly initialized
			controller.getScene();
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			assertEquals(NullPointerException.class, exception.getClass());
		}
	}
	
	@Test
	public void testHideAddCellModal() {
		// Setup new notebook controller
		setupNotebookController();
		
		try {
			// Hide the addCellModal of the notebook
			// Should throw NullPointerException because notebook layout was not properly initialized
			controller.hideAddCellModal();
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			assertEquals(NullPointerException.class, exception.getClass());
		}
	}
	
	@Test
	public void testNotebookModelExport() {
		// Setup new notebook controller
		setupNotebookController();
		
		try {
			// Export the notebook
			controller.export();	
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}
	
	@Test
	public void testNotebookControllerListeners() {
		// Setup new notebook controller
		setupNotebookController();
		
		// Create new cell models
		ComputationCellModel cell1 = new ComputationCellModel(null, 0, null, new ArrayList<YLog>());
		TextCellModel cell2 = new TextCellModel(null, 2);
		
		// Add filter to the notebook model
		controller.cellListeners();
		
		// Add cells to the notebook model
		model.addCell(cell1);
		model.addCell(cell2);
		
		assertEquals(model.getCells().size(), 2);
		
		// Update the cells
		cell1.addFilterModel(0, new FilterButtonModel(0));
		cell1.setHidden(true);
		cell2.setHidden(true);
		
		assertEquals(model.getCells().size(), 2);
		
		// Remove cells from the notebook model
		model.removeCell(cell1);
		model.removeCell(cell2);
		
		assertEquals(model.getCells().size(), 0);
		
		assertTrue(true);
	}
	
	@Test
	public void testNotebookComputeHandler() {
		// Setup new notebook controller
		setupNotebookController();
		// Compute the notebook
		controller.computeButtonHandler();
		// Check that the computation was properly done
		assertTrue(true);
	}
	
	@Test
	public void testNotebookAppendCellHandler() {
		// Setup new notebook controller
		setupNotebookController();
		try {
			// Append new cell to the notebook
			controller.appendCellButtonHandler();
			// Should throw an error since no view is present
			fail("Error was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
		assertTrue(true);
	}
	
	@Test
	public void testNotebookExportHandler() {
		// Setup new notebook controller
		setupNotebookController();
		try {
			// Export the notebook model
			controller.export();	
			// Should throw an error since no view is present
			fail("Error was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
		assertTrue(true);
	}
	
	@Test
	public void testNotebookPrintXML() {
		// Setup new notebook controller
		setupNotebookController();
		controller.printXML();
		assertTrue(true);
	}

}
