package org.processmining.tests.adapters;

import org.junit.Test;
import org.processmining.filterd.gui.CellStatus;
import org.processmining.filterd.gui.adapters.CellModelAdapted;

import junit.framework.TestCase;

public class CellModelAdaptedTest extends TestCase {
	
	CellModelAdapted cell = new CellModelAdapted();
	
	@Test
	public void testCellIndexMethods() {
		// Set the index of the cell model
		cell.setIndex(10);
		// Get the index of the cell model
		int index = cell.getIndex();
		// Check that the index was properly set
		assertEquals(index, 10);
	}
	
	@Test
	public void testCellNameMethods() {
		// Set the name of the cell model
		cell.setCellName("Filterd");
		// Get the name of the cell model
		String name = cell.getCellName();
		// Check that the name was properly set
		assertTrue(name.equals("Filterd"));
	}
	
	@Test
	public void testCellStatusBarMethods() {
		// Set the status of the cell model
		cell.setStatusBar(CellStatus.IDLE);
		// Get the status of the cell model
		CellStatus status = cell.getStatusBar();
		// Check that the status was properly set
		assertEquals(status, CellStatus.IDLE);
	}
	
	@Test
	public void testCellHiddenMethods() {
		// Set the hidden property of the cell model
		cell.setIsHidden(true);
		// Get the hidden property of the cell model
		boolean hidden = cell.getIsHidden();
		// Check that the hidden property was properly set
		assertTrue(hidden);
	}
}
