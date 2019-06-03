package org.processmining.tests.widgets;

import org.junit.Test;
import org.processmining.filterd.widgets.FilterdConfigurationDialog;

import junit.framework.TestCase;

public class FilterdConfigurationDialogTest extends TestCase {

	@Test
	public void testNewFilterdConfigurationDialog() {
		// Create new filter configuration dialog
		FilterdConfigurationDialog dialog = new FilterdConfigurationDialog("Filterd");
		// Check that the filter configuration dialog is properly created
		assertTrue(true);
	}
	
	@Test
	public void testFilterConfigurationDialogClose() {
		// Create new filter configuration dialog
		FilterdConfigurationDialog dialog = new FilterdConfigurationDialog("Filterd");
		// Call the close method of the filter configuration dialog
		dialog.closeDialog();
		// Check that the dialog was properly closed (no errors were thrown)
		assertTrue(true);
	}
	
}
