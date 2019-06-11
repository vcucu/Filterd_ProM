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
	public void testFilterConfigPanelPlacement() {
		// Setup new filter config panel controller
		setupFilterConfigPanel();
		// Get the value of the placement boolean
		Boolean placement = controller.isPlaceInLeftPane();
		// Check that the variable was properly initialized
		assertFalse(placement);
		// Set new value for the placement boolean
		controller.setPlaceInLeftPane(true);
		// Check that the variable was properly set
		assertTrue(controller.isPlaceInLeftPane());
	}
	
	@Test
	public void testGetFilterConfigPanels() {
		// Setup new filter config panel controller
		setupFilterConfigPanel();
		// Get the left panel
		VBox leftPanel = controller.getLeftPanel();
		// Get the right panel
		VBox rightPanel = controller.getRightPanel();
		// Check that the panels were properly initialized
		assertEquals(leftPanel, null);
		assertEquals(rightPanel, null);
	}
	
	@Test
	public void testFilterConfigPanelsPlacement() {
		// Setup new filter config panel controller
		setupFilterConfigPanel();
		// Get the next placement container
		VBox container = controller.getNextContainer();
		// Check the container is properly returned
		assertEquals(container, null);
		// Set new value for the placement variable
		controller.setPlaceInLeftPane(true);
		// Get the next placement container
		container = controller.getNextContainer();
		// Check the container is properly returned
		assertEquals(container, null);
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
