package org.processmining.tests.gui;

import org.junit.Test;
import org.processmining.filterd.gui.VisualizerPanelController;

import javafx.scene.layout.AnchorPane;
import junit.framework.TestCase;

public class VisualizerPanelControllerTest extends TestCase {
	
	@Test
	public void testNewVisualizerPanelController() {
		// Create new visualizer panel controller
		VisualizerPanelController controller = new VisualizerPanelController();
		// Check that the empty controller was properly created
		assertTrue(true);
	}
	
	@Test
	public void testSetVisualizer() {
		// Create new visualizer panel controller
		VisualizerPanelController controller = new VisualizerPanelController();
		try {
			// Set a new visualizer for the controller
			controller.setVisualizer(null);	
			// Should throw an error since the view is not initialized
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			assertEquals(NullPointerException.class, exception.getClass());	
		}
	}
	
	@Test
	public void testModalMethods() {
		// Create new visualizer panel controller
		VisualizerPanelController controller = new VisualizerPanelController();
		try {
			// Set a new visualizer for the controller
			controller.showModal(null);	
			// Should throw an error since the view is not initialized
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			assertEquals(NullPointerException.class, exception.getClass());	
		}
		try {
			// Set a new visualizer for the controller
			controller.hideModal();	
			// Should throw an error since the view is not initialized
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable exception) {
			assertEquals(NullPointerException.class, exception.getClass());	
		}
	}
	
	@Test
	public void testGetVisualizerPanel() {
		// Create new visualizer panel controller
		VisualizerPanelController controller = new VisualizerPanelController();
		// Get the visualizer pane of the controller
		AnchorPane panel = controller.getVisualizerPanel();
		// Check that the panel was properly returned
		assertEquals(panel, null);
	}

}
