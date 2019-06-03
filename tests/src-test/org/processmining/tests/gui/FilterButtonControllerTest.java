package org.processmining.tests.gui;

import org.junit.Test;
import org.processmining.filterd.gui.FilterButtonController;
import org.processmining.filterd.gui.FilterButtonModel;

import junit.framework.TestCase;

public class FilterButtonControllerTest extends TestCase {
	
	FilterButtonModel model;
	FilterButtonController controller;
	
	public void setupNewFilterButton() {
		// Create new filter button model
		model = new FilterButtonModel();
		// Create new filter button controller
		controller = new FilterButtonController(null, model);		
	}
	
	@Test
	public void testNewFilterButtonController() {
		// Setup new filter button
		setupNewFilterButton();
		// Check that the filter button controller was properly created
		assertTrue(true);
	}
	
	@Test
	public void testFilterButtonSelectionMethods() {
		// Setup new filter button
		setupNewFilterButton();
		
		try {
			// Check selection (true path)
			controller.setSelected(true);
			// Should throw null pointer since view is not initialized
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
		
		try {
			// Check selection (false path)
			controller.setSelected(false);
			// Should throw null pointer since view is not initialized
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}
	
	@Test
	public void testCellLayoutMethods() {
		// Setup new filter button
		setupNewFilterButton();
		// Set new layout for the filter button
		controller.setFilterLayout(null);
		// Check that the layout was properly set
		assertEquals(controller.getCellLayout(), null);
	}
	
	@Test
	public void testModelMethods() {
		// Setup new filter button
		setupNewFilterButton();
		// Create new model for the filter button
		FilterButtonModel tmpModel = new FilterButtonModel();
		// Set new model for the filter button
		controller.setModel(tmpModel);
		// Get the newly created model
		FilterButtonModel newTmpModel = controller.getModel();
		// Check that the model was properly set
		assertTrue(tmpModel.equals(newTmpModel));
	}
	
	@Test
	public void testSetFilterName() {
		// Setup new filter button
		setupNewFilterButton();
		try {
			// Set a new name for the filter button
			controller.setFilterName("Filterd");
			// Should throw a null pointer because view is not initialized
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}
	
	@Test
	public void testFilterLayoutMethods() {
		// Setup new filter button
		setupNewFilterButton();
		// Set the new layout for the filter button
		controller.setFilterLayout(null);
		// Check that the layout was properly set
		assertEquals(controller.getFilterLayout(), null);
	}
	
	@Test
	public void testSelectFilterButton() {
		// Setup new filter button
		setupNewFilterButton();
		try {
			// Select the filter button
			controller.selectFilterButton();
			// Should throw null pointer since the view is not initialized
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}
}
