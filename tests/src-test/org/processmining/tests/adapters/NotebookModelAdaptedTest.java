package org.processmining.tests.adapters;

import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.gui.CellModel;
import org.processmining.filterd.gui.ComputationCellModel;
import org.processmining.filterd.gui.ComputationMode;
import org.processmining.filterd.gui.TextCellModel;
import org.processmining.filterd.gui.adapters.NotebookModelAdapted;
import org.processmining.filterd.models.YLog;
import org.processmining.tests.filters.FilterdPackageTest;

public class NotebookModelAdaptedTest extends FilterdPackageTest {
	
	NotebookModelAdapted model;
	
	public NotebookModelAdaptedTest() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Test
	public void testNotebookCellsMethods() {
		// Create new model for the notebook
		model = new NotebookModelAdapted();
		// Create new list for the notebook cells
		List<CellModel> cells = new ArrayList<>();
		// Create new computation cell
		ComputationCellModel cell1 = new ComputationCellModel(null, 0, null, new ArrayList<YLog>());
		// Create new text cell
		TextCellModel cell2 = new TextCellModel(null, 0);
		// Add new cells to the cell list
		cells.add(cell1);
		cells.add(cell2);
		// Set the cells list of the notebook model
		model.setCells(cells);
		// Get the cells list of the notebook model
		List<CellModel> newCells = model.getCells();
		// Check that the cells list was properly set
		assertTrue(newCells.equals(cells));
	}
	
	@Test
	public void testNotebookComputationModeMethods() {
		// Create new model for the notebook
		model = new NotebookModelAdapted();
		// Set new computation mode for the notebook
		model.setComputationMode(ComputationMode.MANUAL);
		// Get the computaion mode of the notebook
		ComputationMode mode = model.getComputationMode();
		// Check that the computation mode was properly created
		assertEquals(mode, ComputationMode.MANUAL);
	}
	
	@Test
	public void testNotebookInputMethods() {
		// Create new model for the notebook
		model = new NotebookModelAdapted();
		// Set the input log for the notebook
		model.setInitialInput(originalLog);
		// Get the input log of the notebook
		XLog newLog = model.getInitialInput();
		// Check that the log was properly set
		assertTrue(equalLog(newLog, originalLog));
	}

}
