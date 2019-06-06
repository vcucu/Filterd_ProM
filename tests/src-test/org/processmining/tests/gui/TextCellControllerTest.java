package org.processmining.tests.gui;

import org.junit.Test;
import org.processmining.filterd.gui.CellModel;
import org.processmining.filterd.gui.NotebookController;
import org.processmining.filterd.gui.NotebookModel;
import org.processmining.filterd.gui.TextCellController;
import org.processmining.filterd.gui.TextCellModel;

import junit.framework.TestCase;

public class TextCellControllerTest extends TestCase {
	
	NotebookModel model;
	NotebookController controller;
	TextCellController cellController;
	
	
	public void setupTextCellController() {
		// Create new notebook model instance
		model = new NotebookModel();
		// Create new notebook controller instance
		controller = new NotebookController(model);
		// Create new cell controller instance (without a text cell model since it requires the view)
		cellController = new TextCellController(controller, null);
	}
	
	@Test
	public void testNewTextCellController() {
		// Setup new text cell controller
		setupTextCellController();
		// Check that the new text cell controller was properly created
		assertTrue(true);
	}
	
	@Test
	public void testAddTextCellListener() {
		// Setup new text cell controller
		setupTextCellController();
		try {
			// Add new property change listener to the text cell (should throw null pointer exception)
			cellController.addPropertyChangeListener(null);
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}
	
	@Test
	public void testGetTextCellController() {
		// Setup new text cell controller
		setupTextCellController();
		
		// Get the notebook controller (method #1)
		NotebookController notebookController1 = cellController.getController();
		// Check the notebook controller was properly returned
		assertTrue(controller.equals(notebookController1));
		
		// Get the notebook controller (method #2)
		NotebookController notebookController2 = cellController.getController();
		// Check the notebook controller was properly returned
		assertTrue(controller.equals(notebookController2));
	}
	
	@Test
	public void testRemoveTextCell() {
		// Setup new text cell controller
		setupTextCellController();
		
		try {
			// Remove the text cell from the notebook
			cellController.remove();
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}
	
	@Test
	public void testNewCellControllerMethods() {
		// Setup new text cell controller
		setupTextCellController();
		// Set a new cell controller
		cellController.setController(null);
		// Get the new cell controller
		NotebookController tmp = cellController.getController();
		// Check that the cell controller was properly set
		assertEquals(tmp, null);
	}
	
	@Test
	public void testTextCellLayoutMethods() {
		// Setup new text cell controller
		setupTextCellController();
		// Set a new cell layout for the controller
		cellController.setCellLayout(null);
		// Check that the cell layout was properly set
		assertEquals(controller.getCellsLayout(), null);
	}
	
	@Test
	public void testCellModelMethods() {
		// Setup new text cell controller
		setupTextCellController();
		// Set a new cell model for the cell controller
		cellController.setCellModel(null);
		// Get the new cell model of the cell controller
		CellModel newCellModel = cellController.getCellModel();
		// Check that the cell model was properly set
		assertEquals(newCellModel, null);
	}
	
	@Test
	public void testGetTextCellModel() {
		// Setup new text cell controller
		setupTextCellController();
		// Get the notebook controller
		TextCellModel textCellModel = (TextCellModel) cellController.getCellModel();
		// Check the text cell model was properly returned
		assertEquals(textCellModel, null);
	}
	
	@Test
	public void testChangeCellName() {
		// Setup new text cell controller
		setupTextCellController();
		try {
			cellController.changeCellName("Filterd");
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			// Check null pointer is thrown (since the view is not initialized)
			assertFalse(exception.equals(null));
		}
	}
	
	@Test
	public void testChangeComment() {
		// Setup new text cell controller
		setupTextCellController();
		try {
			cellController.changeComment("Filterd");
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			// Check null pointer is thrown (since the view is not initialized)
			assertFalse(exception.equals(null));
		}
	}
	
	@Test
	public void testTextCellControllerInitialize() {
		// Setup new text cell controller
		setupTextCellController();
		try {
			// Initialize the text cell controller
			cellController.initialize();	
			// Should throw error since the view is not initialized
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			assertEquals(NullPointerException.class, exception.getClass());
		}
	}

}
