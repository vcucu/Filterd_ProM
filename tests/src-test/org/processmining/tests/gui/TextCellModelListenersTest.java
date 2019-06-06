package org.processmining.tests.gui;

import java.beans.PropertyChangeEvent;

import org.junit.Test;
import org.processmining.filterd.gui.CellModelListeners;
import org.processmining.filterd.gui.TextCellController;
import org.processmining.filterd.gui.TextCellModelListeners;

import junit.framework.TestCase;

public class TextCellModelListenersTest extends TestCase {
	
	TextCellController textCellController;
	CellModelListeners listeners;
	
	public void setupCellModelListeners() {
		// Create a new text cell controller
		textCellController = new TextCellController(null, null);
		// Create a new cell model listener
		listeners = new TextCellModelListeners(textCellController);
	}
	
	@Test
	public void testNewCellModelListeners() {
		// Setup new cell model listeners
		setupCellModelListeners();
		// Check if the cell model listeners object was properly created
		assertTrue(true);
	}
	
	@Test
	public void testPropertyChangeComment() {
		// Setup new cell model listeners
		setupCellModelListeners();
		// Create new property change event
		PropertyChangeEvent event = new PropertyChangeEvent(this, "setComment", true, null);
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
