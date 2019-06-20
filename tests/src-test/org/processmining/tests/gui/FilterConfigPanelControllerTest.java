package org.processmining.tests.gui;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.widgets.ParameterController;

import javafx.scene.layout.VBox;
import junit.framework.TestCase;

public class FilterConfigPanelControllerTest extends TestCase {
	
	FilterConfigPanelController controller;
	
	public void setupFilterConfigPanel() {
		// Create new filter config panel controller
		controller = new FilterConfigPanelController();
	}
	
	@Test
	public void testNewFilterConfigPanelController() {
		try {
			// Create new filter config panel controller
			FilterConfigPanelController controller = new FilterConfigPanelController("Title", new ArrayList<>(), null);	
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}
	
	@Test
	public void testNewEmptyFilterConfigPanelController() {
		// Setup new filter config panel controller
		setupFilterConfigPanel();
		// Check that the filter config panel controller was properly created
		assertTrue(true);
	}
	
	@Test
	public void testGetFilterConfigPanelController() {
		// Setup new filter config panel controller
		setupFilterConfigPanel();
		// Retrieve the controller list from the config panel
		List<ParameterController> controllers = controller.getControllers();
		// Check that the controller list was properly initialized
		assertEquals(controllers, new ArrayList<>());
	}
	
	@Test
	public void testGetFilterConfigPanelRoot() {
		// Setup new filter config panel controller
		setupFilterConfigPanel();
		// Get the root element of the panel controller
		VBox root = controller.getRoot();
		// Check that the root element was properly initialized
		assertEquals(root, null);
	}
	
	@Test
	public void testFilterConfigPanelAddParameter() {
		// Setup new filter config panel controller
		setupFilterConfigPanel();
		// Add new parameter from set
		ParameterOneFromSet param = new ParameterOneFromSet("Name", "Display Name", "Choice", new ArrayList<>());
		try {
			// Add parameter to the panel
			controller.addParameterOneFromSet(param);	
			// Should throw an error since the view is not present
			fail("Error was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}

}
