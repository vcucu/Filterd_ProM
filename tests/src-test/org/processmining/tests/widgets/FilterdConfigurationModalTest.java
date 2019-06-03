package org.processmining.tests.widgets;

import javax.swing.JPanel;

import org.junit.Test;
import org.processmining.filterd.widgets.FilterdConfigurationModal;

import junit.framework.TestCase;

public class FilterdConfigurationModalTest extends TestCase {

	JPanel panel;
	FilterdConfigurationModal modal;
	
	public void setupConfigurationModal() {
		// Create new JPanel (to be passed as owner for the configuration modal)
		panel = new JPanel();
		// Create new filter configuration modal
		modal = new FilterdConfigurationModal("Filterd", panel);
	}
	
	@Test
	public void testNewFilterdConfigurationModal() {
		// Setup new configuration modal
		setupConfigurationModal();
		// Check that the modal was properly created (no errors were thrown)
		assertTrue(true);
	}
	
	@Test
	public void testHideFilterdConfigurationModal() {
		// Setup new configuration modal
		setupConfigurationModal();
		// Hide the configuration modal
		modal.hideModal();
		// Check that the modal was properly hidden
		assertFalse(modal.isVisible());
	}
	
	@Test
	public void testShowFilterdConfigurationModal() {
		// Setup new configuration modal
		setupConfigurationModal();
		// Show the configuration modal
		modal.showModal();
		// Check that the modal was properly shown
		assertTrue(modal.isVisible());
	}
	
	@Test
	public void testToggleFilterdConfigurationModel() {
		// Setup new configuration modal
		setupConfigurationModal();
		// Check that the configuration modal is shown
		assertTrue(modal.isVisible());
		// Toggle the modal (should hide the modal)
		modal.toggle();
		// Check that the modal was properly hidden
		assertFalse(modal.isVisible());
		// Toggle the modal (should show the modal)
		modal.toggle();
		// Check that the modal was properly shown
		assertTrue(modal.isVisible());
	}
}
