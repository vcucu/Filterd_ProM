package org.processmining.tests.gui;

import org.junit.Test;
import org.processmining.filterd.gui.CellController;
import org.processmining.filterd.gui.NotebookController;
import org.processmining.filterd.gui.NotebookModel;
import org.processmining.filterd.gui.TextCellController;
import org.processmining.filterd.gui.TextCellModel;

import junit.framework.TestCase;

public class TextCellControllerTest extends TestCase {
	
	NotebookModel model;
	NotebookController controller;
	CellController cellController;
	
	
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
			assertEquals(NullPointerException.class, exception.getClass());
		}
	}
	
	@Test
	public void testGetTextCellController() {
		// Setup new text cell controller
		setupTextCellController();
		// Get the notebook controller
		NotebookController notebookController = cellController.getController();
		// Check the notebook controller was properly returned
		assertTrue(controller.equals(notebookController));
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
	public void testChangeComment() {
		// Setup new text cell controller
		setupTextCellController();
		try {
			cellController.changeCellName("Filterd");
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			// Check null pointer is thrown (since the view is not initialized)
			assertEquals(NullPointerException.class, exception.getClass());
		}
	}

}
