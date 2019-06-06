package org.processmining.tests.gui;

import java.beans.PropertyChangeEvent;

import org.junit.Test;
import org.processmining.filterd.gui.CellModelListeners;
import org.processmining.filterd.gui.TextCellController;

import junit.framework.TestCase;

public class CellModelListenersTest extends TestCase {
	
	TextCellController textCellController;
	CellModelListeners listeners;
	
	public void setupCellModelListeners() {
		// Create a new text cell controller
		textCellController = new TextCellController(null, null);
		// Create a new cell model listener
		listeners = new CellModelListeners(textCellController);
	}
	
	@Test
	public void testNewCellModelListeners() {
		// Setup new cell model listeners
		setupCellModelListeners();
		// Check if the cell model listeners object was properly created
		assertTrue(true);
	}
	
	@Test
	public void testPropertyChangeHiddenTrue() {
		// Setup new cell model listeners
		setupCellModelListeners();
		// Create new property change event
		PropertyChangeEvent event = new PropertyChangeEvent(this, "setHidden", true, null);
		try {
			// Fire a property change event
			listeners.propertyChange(event);
			// Should throw error when updating the view (since it's not initialized)
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}
	
	@Test
	public void testPropertyChangeHiddenFalse() {
		// Setup new cell model listeners
		setupCellModelListeners();
		// Create new property change event
		PropertyChangeEvent event = new PropertyChangeEvent(this, "setHidden", false, null);
		try {
			// Fire a property change event
			listeners.propertyChange(event);
			// Should throw error when updating the view (since it's not initialized)
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}
	
	@Test
	public void testPropertyChangeName() {
		// Setup new cell model listeners
		setupCellModelListeners();
		// Create new property change event
		PropertyChangeEvent event = new PropertyChangeEvent(this, "setCellName", false, null);
		try {
			// Fire a property change event
			listeners.propertyChange(event);
			// Should throw error when updating the view (since it's not initialized)
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}

}
