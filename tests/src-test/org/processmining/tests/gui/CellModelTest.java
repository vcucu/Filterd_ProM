package org.processmining.tests.gui;

import java.beans.PropertyChangeSupport;

import org.junit.Test;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.filterd.gui.CellModel;
import org.processmining.filterd.gui.CellStatus;

import junit.framework.TestCase;

public class CellModelTest extends TestCase {
	
	@Test
	public void testNewCellModel() {
		// Create new cell model instance
		CellModel cell = new CellModel(null, 0);
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
		CellModel cell = new CellModel(null, 0);
		// Set a new value for the cell model hidden variable
		cell.setHidden(true);
		// Get the value for the cell model hidden variable
		boolean hidden = cell.isHidden();
		assertTrue(hidden);
	}
	
	@Test
	public void testCellStatusBar() {
		// Create new cell model instance
		CellModel cell = new CellModel(null, 0);
		// Set a new value for the cell status bar
		cell.setStatusBar(CellStatus.IDLE);
		// Get the value of the cell status bar
		CellStatus cellStatus = cell.getStatusBar();
		// Check the value of the cell status bar
		assertTrue(cellStatus == CellStatus.IDLE);
		
		// Set a new value for the cell status bar
		cell.setStatusBar(CellStatus.IN_PROGRESS);
		// Get the value of the cell status bar
		cellStatus = cell.getStatusBar();
		// Check the value of the cell status bar
		assertTrue(cellStatus == CellStatus.IN_PROGRESS);
	}
	
	@Test
	public void testCellName() {
		// Create new cell model instance
		CellModel cell = new CellModel(null, 0);
		// Set a new value for the cell name
		cell.setCellName("Filterd");
		// Get the value of the cell name
		String name = cell.getCellName();
		// Check the value of the cell name
		assertTrue(name.equals("Filterd"));
	}
	
	@Test
	public void testCellContext() {
		// Create new cell model instance
		CellModel cell = new CellModel(null, 0);
		// Get the UIPluginContext of the cell model
		UIPluginContext context = cell.getContext();
		// Check the cell context is set to null
		assertTrue(context == null);
	}
	
	@Test
	public void testCellIndex() {
		// Create new cell model instance
		CellModel cell = new CellModel(null, 0);
		// Get the index of the newly created cell
		int index = cell.getIndex();
		// Check the index is properly set
		assertEquals(index, 0);
		
		// Set the index to a new value
		cell.setIndex(10);
		// Check the index was properly set
		assertEquals(cell.getIndex(), 10);
	}

}
