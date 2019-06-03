package org.processmining.tests.gui;

import org.junit.Test;
import org.processmining.filterd.gui.ComputationCellModel;
import org.processmining.filterd.gui.ComputationMode;
import org.processmining.filterd.gui.NotebookController;
import org.processmining.filterd.gui.NotebookModel;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import junit.framework.TestCase;

public class NotebookControllerTest extends TestCase {
	
	@Test
	public void testNewNotebookController() {
		// Create new notebook model
		NotebookModel model = new NotebookModel();
		// Create new notebook controller
		NotebookController controller = new NotebookController(model);
		// Check that the model was properly created
		assertTrue(controller.getModel() != null);
	}
	
	@Test
	public void testNotebookLayoutComponents() {
		// Create new notebook model
		NotebookModel model = new NotebookModel();
		// Create new notebook controller
		NotebookController controller = new NotebookController(model);
		
		// Get the notebook scroll pane
		ScrollPane scrollPane = controller.getScrollPane();
		// Check the component is properly returned (should be null at this point)
		assertTrue(scrollPane == null);
		
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
		// Create new notebook model
		NotebookModel model = new NotebookModel();
		// Create new notebook controller
		NotebookController controller = new NotebookController(model);
		// Add the proper listeners to the notebook controller
		controller.cellListeners();
		// Check that no exception is thrown while adding the cell listeners
		assertTrue(true);
	}
	
	@Test
	public void testNotebookButtonHandlers() {
		// Create new notebook model
		NotebookModel model = new NotebookModel();
		// Create new notebook controller
		NotebookController controller = new NotebookController(model);
		
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
		// Create new notebook model
		NotebookModel model = new NotebookModel();
		// Create new notebook controller
		NotebookController controller = new NotebookController(model);
		
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
		// Create new notebook model
		NotebookModel model = new NotebookModel();
		// Create new notebook controller
		NotebookController controller = new NotebookController(model);
		
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
		// Create new notebook model
		NotebookModel model = new NotebookModel();
		// Create new notebook controller
		NotebookController controller = new NotebookController(model);
		
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
		// Create new notebook model
		NotebookModel model = new NotebookModel();
		// Create new notebook controller
		NotebookController controller = new NotebookController(model);
		
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
		// Create new notebook model
		NotebookModel model = new NotebookModel();
		// Create new notebook controller
		NotebookController controller = new NotebookController(model);
		
		try {
			// Hide the addCellModal of the notebook
			// Should throw NullPointerException because notebook layout was not properly initialized
			controller.hideAddCellModal();
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			assertEquals(NullPointerException.class, exception.getClass());
		}
	}

}
