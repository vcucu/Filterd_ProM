package org.processmining.tests.gui;

import java.beans.PropertyChangeSupport;

import org.junit.Test;
import org.processmining.filterd.gui.CellModel;

import junit.framework.TestCase;

public class CellModelTest extends TestCase {
	
	@Test
	public void testNewCellModel() {
		// Create new cell model instance
		CellModel cell = new CellModel(null);
		// Get the property change support of the newly created cell
		PropertyChangeSupport property = cell.getProperty();
		// Check if the property change support variable was initialized properly
		assertTrue(property != null);
		// Check the isHidden variable is properly initialized
		assertFalse(cell.isHidden());
	}
	
	@Test
	public void testHiddenProperty() {
		// Create new cell model instance
		CellModel cell = new CellModel(null);
		
	}

}
