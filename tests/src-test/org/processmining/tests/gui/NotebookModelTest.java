package org.processmining.tests.gui;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.hub.ProMResourceManager;
import org.processmining.contexts.uitopia.hub.ProMViewManager;
import org.processmining.filterd.gui.CellModel;
import org.processmining.filterd.gui.ComputationCellModel;
import org.processmining.filterd.gui.ComputationMode;
import org.processmining.filterd.gui.NotebookModel;
import org.processmining.filterd.gui.TextCellModel;
import org.processmining.filterd.models.YLog;
import org.processmining.framework.plugin.ProMCanceller;
import org.processmining.tests.filters.FilterdPackageTest;

import javafx.beans.property.BooleanProperty;

public class NotebookModelTest extends FilterdPackageTest {

	public NotebookModelTest() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	@Test
	public void testNewNotebookModel() {
		try {
			// Create new notebook model instance
			NotebookModel model = new NotebookModel(null, originalLog, null);
			fail("NullPointerException was NOT thrown!");
		} catch (Throwable expected) {
			// Can not create notebook model without ProMViewManager and ProMResourceManager
			assertEquals(NullPointerException.class, expected.getClass());
		}
	}
	
	@Test
	public void testNewNotebookModelHeadless() {
		// Create new notebook model instance
		NotebookModel model = new NotebookModel();
		// Check the cells list was properly initialized
		assertTrue(model.getCells() != null);
	}
	
	@Test
	public void testNotebookModelContext() {
		// Create new notebook model instance
		NotebookModel model = new NotebookModel();
		UIPluginContext context = model.getPromContext();
		assertEquals(context, null);
	}
	
	@Test
	public void testNotebookModelCanceller() {
		// Create new notebook model instance
		NotebookModel model = new NotebookModel();
		ProMCanceller canceller = model.getPromCanceller();
		assertEquals(canceller, null);
	}
	
	@Test
	public void testNotebookModelViewManager() {
		// Create new notebook model instance
		NotebookModel model = new NotebookModel();
		ProMViewManager view = model.getPromViewManager();
		assertEquals(view, null);
	}
	
	@Test
	public void testNotebookModelResourceManager() {
		// Create new notebook model instance
		NotebookModel model = new NotebookModel();
		ProMResourceManager manager = model.getPromResourceManager();
		assertEquals(manager, null);
	}
	
	@Test
	public void testNotebookCellMethods() {
		// Create new notebook model instance
		NotebookModel model = new NotebookModel();
		// Create new computation cell model
		ComputationCellModel computationCell = new ComputationCellModel(null, 0, null, new ArrayList<>());
		// Create new text cell model
		TextCellModel textCell = new TextCellModel(null, 0);
		// Add newly created cells to the notebook model (method #1)
		model.addCell(computationCell);
		// Add newly created cells to the notebook model (method #2)
		model.addCell(1, textCell);
		// Check the new cells were added to the notebook model cell list (size should now be 2)
		assertEquals(model.getCells().size(), 2);
		
		// Add new cells to the notebook via list
		ArrayList<CellModel> newCells = new ArrayList<>();
		ComputationCellModel computationCell1 = new ComputationCellModel(null, 0, null, new ArrayList<>());
		TextCellModel textCell1 = new TextCellModel(null, 0);
		// Add cells to the array list
		newCells.add(computationCell1);
		newCells.add(textCell1);
		// Add cell list to the notebook (method #3)
		model.addCells(newCells);
		// Check the new cells were added to the notebook model cell list (size should now be 4)
		assertEquals(model.getCells().size(), 4);
		
		// Get size of notebook cell list
		int numCells = model.getCells().size();
		// Add cell list to the notebook (method #4)
		model.addCells(numCells - 1, newCells);
		// Check the new cells were added to the notebook model cell list (size should now be 6)
		assertEquals(model.getCells().size(), 6);
	}
	
	@Test
	public void testNotebookCellRemoveMethods() {
		// Create new notebook model instance
		NotebookModel model = new NotebookModel();
		// Create new computation cell model
		ComputationCellModel computationCell = new ComputationCellModel(null, 0, null, new ArrayList<>());
		// Create new text cell model
		TextCellModel textCell = new TextCellModel(null, 0);
		// Add newly created cells to the notebook model (method #1)
		model.addCell(computationCell);
		// Add newly created cells to the notebook model (method #2)
		model.addCell(1, textCell);
		// Check the new cells were added to the notebook model cell list (size should now be 2)
		assertEquals(model.getCells().size(), 2);
		
		// Remove cell from notebook list (method #1)
		model.removeCell(computationCell);
		// Check if notebook list was updated properly
		assertEquals(model.getCells().size(), 1);
		
		// Remove cell from notebook list (method #2)
		model.removeCell(0);
		// Check if notebook list was updated properly
		assertEquals(model.getCells().size(), 0);
		
		// Add new cells to the notebook via list
		ArrayList<CellModel> newCells = new ArrayList<>();
		ComputationCellModel computationCell1 = new ComputationCellModel(null, 0, null, new ArrayList<>());
		TextCellModel textCell1 = new TextCellModel(null, 0);
		// Add cells to the array list
		newCells.add(computationCell1);
		newCells.add(textCell1);
		// Add cell list to the notebook (method #3)
		model.addCells(newCells);
		// Check the new cells were added to the notebook model cell list (size should now be 4)
		assertEquals(model.getCells().size(), 2);
		
		// Remove cells from notebook list (method #3)
		model.removeCells(newCells);
		// Check if notebook list was updated properly
		assertEquals(model.getCells().size(), 0);
	}
	
	@Test
	public void testNotebookComputationMethods() {
		// Create new notebook model instance
		NotebookModel model = new NotebookModel();
		// Get computation mode of the model
		ComputationMode mode = model.getComputationMode();
		// Check computation mode of the model
		assertEquals(mode, ComputationMode.MANUAL);
		
		// Set new computation mode for the model
		model.setComputationMode(ComputationMode.AUTOMATIC);
		// Check computation mode of the model
		assertEquals(model.getComputationMode(), ComputationMode.AUTOMATIC);
		
		// Set new computation mode for the model
		model.setComputationMode(ComputationMode.MANUAL);
		// Check computation mode of the model
		assertEquals(model.getComputationMode(), ComputationMode.MANUAL);
	}
	
	@Test
	public void testNotebookGetOutputLogsTill() {
		// Create new notebook model instance
		NotebookModel model = new NotebookModel();
		// Get the output logs until the first cell
		List<YLog> logs = model.getOutputLogsTill(0);
		// Check the logs list that is returned (should only be the initial log)
		assertEquals(logs.size(), 1);
	}
	
	@Test
	public void testNotebookGetInitialInput() {
		// Create new notebook model instance
		NotebookModel model = new NotebookModel();
		// Create new YLog
		YLog initialLog = new YLog(0, "Original Log", originalLog);
		// Set the initial input log for the notebook
		model.setInitialInput(initialLog);
		// Check the initial input log was properly set
		assertTrue(model.getInitialInput() != null);
		// Get the initial input log of the notebook
		YLog log = model.getInitialInput();
		// Check the log was properly set
		assertTrue(equalLog(log.get(), originalLog));
	}
	
	@Test
	public void testNotebookModelComputingProperty() {
		// Create new notebook model instance
		NotebookModel model = new NotebookModel();
		// Get the computing variable from the notebook
		Boolean computing = model.isComputing();
		// Check that the computing variable is properly returned
		assertFalse(computing);
		// Get the computing property
		BooleanProperty computingProperty = model.isComputingProperty();
		// Check that the computing property is properly returned
		assertFalse(computingProperty.get());
	}
	
	// TODO: This test keeps failing because the JUnit thread terminates before the computation thread is done
//	@Test
//	public void testNotebookComputeOneCell() {
//		// Create new notebook model instance
//		NotebookModel model = new NotebookModel();
//		// Create new YLog
//		YLog initialLog = new YLog(0, "Original Log", originalLog);
//		// Set the initial input log for the notebook
//		model.setInitialInput(initialLog);
//		// Create new computation cell model instance
//		ComputationCellModel cellModel0 = new ComputationCellModel(null, 0, null, new ArrayList<>());
//		// Set the initial log for the computation cell
//		cellModel0.setInputLog(model.getInitialInput());
//		// Add cell model to the notebook model
//		model.addCell(cellModel0);
//		// Create new filter button models
//		FilterButtonModel filterModel0 = new FilterButtonModel(0);
//		// Add filter buttons to the computation cell filter list
//		cellModel0.addFilterModel(0, filterModel0);
//		// Add filter configurations to the filter buttons
//		FilterdAbstractConfig filterConfig0 = new FilterdTraceSampleConfig(cellModel0.getInputLog().get(),
//				new FilterdTraceSampleFilter());
//		// Set the filter configuration parameters
//		((ParameterValueFromRange<Integer>) filterConfig0.getParameters().get(0)).setChosen(2);
//		// Set filter configurations for the filter buttons
//		filterModel0.setFilterConfig(filterConfig0);
//		// Compute the cell's output
//		model.compute();
//		// Check that the computation was successful
//		assertEquals(2, cellModel0.getOutputLogs().get(0).get().size());
//	}
	
	// TODO: This test keeps failing because the JUnit thread terminates before the computation thread is done
//	@Test
//	public void testNotebookComputeTwoCells() {
//		// Create new notebook model instance
//		NotebookModel model = new NotebookModel();
//		// Create new YLog
//		YLog initialLog = new YLog(0, "Initial Log", originalLog);
//		// Set the initial input log for the notebook
//		model.setInitialInput(initialLog);
//		// Create new computation cell model instance
//		ComputationCellModel cellModel0 = new ComputationCellModel(null, 0, null, new ArrayList<>());
//		ComputationCellModel cellModel1 = new ComputationCellModel(null, 0, null, new ArrayList<>());
//		// Set the initial log for the computation cell
//		cellModel0.setInputLog(model.getInitialInput());
//		cellModel1.setInputLog(cellModel0.getOutputLogs().get(0));
//		// Add cell model to the notebook model
//		model.addCell(cellModel0);
//		model.addCell(cellModel1);
//		// Create new filter button models
//		FilterButtonModel filterModel0 = new FilterButtonModel(0);
//		FilterButtonModel filterModel1 = new FilterButtonModel(0);
//		// Add filter buttons to the computation cell filter list
//		cellModel0.addFilterModel(0, filterModel0);
//		cellModel1.addFilterModel(0, filterModel1);
//		// Add filter configurations to the filter buttons
//		FilterdAbstractConfig filterConfig0 = new FilterdTraceSampleConfig(cellModel0.getInputLog().get(),
//				new FilterdTraceSampleFilter());
//		FilterdAbstractConfig filterConfig1 = new FilterdTraceSampleConfig(cellModel1.getInputLog().get(),
//				new FilterdTraceSampleFilter());
//		// Set the filter configuration parameters
//		((ParameterValueFromRange<Integer>) filterConfig0.getParameters().get(0)).setChosen(2);
//		((ParameterValueFromRange<Integer>) filterConfig1.getParameters().get(0)).setChosen(1);
//		// Set filter configurations for the filter buttons
//		filterModel0.setFilterConfig(filterConfig0);
//		filterModel1.setFilterConfig(filterConfig1);
//		// Compute the cell's output
//		model.compute();
//		// Check that the computation was successful
//		assertEquals(1, cellModel1.getOutputLogs().get(0).get().size());
//	}
}
