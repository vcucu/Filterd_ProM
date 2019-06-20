package org.processmining.tests.adapters;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.processmining.filterd.gui.FilterButtonModel;
import org.processmining.filterd.gui.adapters.ComputationCellModelAdapted;

import junit.framework.TestCase;

public class ComputationCellModelAdaptedTest extends TestCase {
	
	ComputationCellModelAdapted cell = new ComputationCellModelAdapted();
	
	@Test
	public void testIndexOwnerMethods() {
		// Set the index of owner of the cell model
		cell.setIndexOfInputOwner(10);
		// Get the index of owner of the cell model
		int index = cell.getIndexOfInputOwner();
		// Check that the index of owner was properly set
		assertEquals(index, 10);
	}
	
	@Test
	public void testFilterMethods() {
		// Create new filter list to add to the cell model
		List<FilterButtonModel> filters = new ArrayList<>();
		// Create new filter button model
		FilterButtonModel filter = new FilterButtonModel(0);
		// Add newly created filter to the filters list
		filters.add(filter);
		// Set the filter list of the cell model
		cell.setFilters(filters);
		// Get the list of filters of the cell model
		List<FilterButtonModel> newFilters = cell.getFilters();
		// Check that the filter list was properly created
		assertEquals(newFilters.size(), 1);
		assertTrue(filters.equals(newFilters));
	}
}
