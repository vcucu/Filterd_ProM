package org.processmining.tests.adapters;

import java.util.ArrayList;

import org.junit.Test;
import org.processmining.filterd.gui.ComputationCellModel;
import org.processmining.filterd.gui.TextCellModel;
import org.processmining.filterd.gui.adapters.CellModelAdapted;
import org.processmining.filterd.gui.adapters.CellModelAdapter;
import org.processmining.filterd.gui.adapters.TextCellModelAdapted;
import org.processmining.filterd.models.YLog;
import org.processmining.tests.filters.FilterdPackageTest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CellModelAdapterTest extends  FilterdPackageTest {
	
	CellModelAdapter adapter = new CellModelAdapter();
	ObservableList<YLog> logs;
	
	public CellModelAdapterTest() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Test
	public void testComputationCellModelAdapter() {
		
		// --------------- TESTING MARSHAL ---------------
		// Create new YLog
		YLog log = new YLog(0, "Original Log", originalLog, 0);
		logs = FXCollections.observableArrayList();
		// Add the new log to the array list
		logs.add(log);
		// Create new computation cell model
		ComputationCellModel cell = new ComputationCellModel(null, 0, null, new ArrayList<YLog>());
		// Set the output logs of the computation cell
		cell.setOutputLogs(logs);
		// Set the index for the computation cell
		cell.setIndex(10);
		// Set the output log for the computation cell
		cell.setOutputLogs(logs);
		// Set the input log for the computation cell
		cell.setInputLog(log);
		// Set the name for the computation cell
		cell.setCellName("Filterd");
		// Compute adapted computation cell model
		CellModelAdapted newCell = adapter.marshal(cell);
		// Check the adapted model was properly created
		assertTrue(newCell.getCellName().equals("Filterd"));
		assertEquals(newCell.getIndex(), 10);
		
		// --------------- TESTING UNMARSHAL ---------------
		// Create new computation cell from the adapted model
		ComputationCellModel adaptedCell = (ComputationCellModel) adapter.unmarshal(newCell);
		// Check that the new cell was properly created
		assertTrue(adaptedCell.getCellName().equals("Filterd"));
		assertEquals(adaptedCell.getIndex(), 10);
	}
	
	@Test
	public void testTextCellModelAdapter() {
		
		// --------------- TESTING MARSHAL ---------------
		// Create new text cell model
		TextCellModel cell = new TextCellModel(null, 0);
		// Set the name for the text cell
		cell.setCellName("Filterd");
		try {
			// Compute adapted computation cell model
			CellModelAdapted newCell = adapter.marshal(cell);
			// Should throw exception since no view is present
			fail("Error was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
		
		// --------------- TESTING UNMARSHAL ---------------
		try {
			TextCellModelAdapted adaptedCellModel = new TextCellModelAdapted();
			TextCellModel newCell = (TextCellModel) adapter.unmarshal(adaptedCellModel);
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}
}
