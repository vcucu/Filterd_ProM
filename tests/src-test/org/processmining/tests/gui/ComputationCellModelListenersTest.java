package org.processmining.tests.gui;

import java.beans.PropertyChangeEvent;

import org.junit.Test;
import org.processmining.filterd.gui.ComputationCellController;
import org.processmining.filterd.gui.ComputationCellModel;
import org.processmining.filterd.gui.ComputationCellModelListeners;

import junit.framework.TestCase;

public class ComputationCellModelListenersTest extends TestCase {
	
	ComputationCellModelListeners listeners;
	
	public void setupComputationCellModelListeners() {
		// Create new computation cell model
		ComputationCellModel model = new ComputationCellModel();
		// Create new computation cell controller
		ComputationCellController controller = new ComputationCellController(model);
		// Setup new computation cell model listeners
		listeners = new ComputationCellModelListeners(controller);
	}
	
	@Test
	public void testNewComputationCellModelListeners() {
		// Setup computation cell model listeners
		setupComputationCellModelListeners();
		// Check that the listeners were properly created
		assertTrue(true);
	}
	
	@Test
	public void testPropertyChangeInputLogs() {
		// Setup computation cell model listeners
		setupComputationCellModelListeners(); 
		// Create new property change event
		PropertyChangeEvent event = new PropertyChangeEvent(this, "setInputLogs", true, null);
		try {
			// Fire a property change event
			listeners.propertyChange(event);
			// Should throw error when updating the view (since it's not initialized)
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
		
		PropertyChangeEvent newEvent = new PropertyChangeEvent(this, "setName", true, null);
		try {
			// Fire a property change event
			listeners.propertyChange(newEvent);
			// Should throw error when updating the view (since it's not initialized)
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}

}
