package org.processmining.tests.gui;

import java.util.ArrayList;

import org.junit.Test;
import org.processmining.filterd.gui.ComputationCellController;
import org.processmining.filterd.gui.ComputationCellModel;
import org.processmining.filterd.gui.NotebookController;
import org.processmining.filterd.gui.NotebookModel;

import junit.framework.TestCase;

public class ComputationCellControllerTest extends TestCase {
	
	ComputationCellModel cell;
	NotebookModel model;
	NotebookController controller;
	
	public void setupComputationCell() {
		// Create new computation cell model instance
		ComputationCellModel cell = new ComputationCellModel(null, 0, null, new ArrayList<>());
		// Create new notebook model
		NotebookModel model = new NotebookModel();
		// Create new notebook controller
		NotebookController controller = new NotebookController(model);
	}

	@Test
	public void testNewComputationCellController() {
		// Setup new computation cell
		setupComputationCell();
		
		try {
			// Create new computation cell controller (should throw an exception)
			ComputationCellController computationCellController = new ComputationCellController(controller, cell);
			fail("NO exception was thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}
}
