package org.processmining.tests.gui;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.hub.ProMResourceManager;
import org.processmining.contexts.uitopia.hub.ProMViewManager;
import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.configurations.FilterdTraceSampleConfig;
import org.processmining.filterd.filters.FilterdTraceSampleFilter;
import org.processmining.filterd.gui.CellModel;
import org.processmining.filterd.gui.ComputationCellModel;
import org.processmining.filterd.gui.ComputationMode;
import org.processmining.filterd.gui.FilterButtonModel;
import org.processmining.filterd.gui.NotebookModel;
import org.processmining.filterd.gui.TextCellModel;
import org.processmining.filterd.models.YLog;
import org.processmining.filterd.parameters.ParameterValueFromRange;
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
		NotebookModel model = new NotebookModel(null);
		// Check the cells list was properly initialized
		assertTrue(model.getCells() != null);
	}
	
	@Test
	public void testNotebookModelContext() {
		// Create new notebook model instance
		NotebookModel model = new NotebookModel(null);
		UIPluginContext context = model.getPromContext();
		assertEquals(context, null);
	}
	
	@Test
	public void testNotebookModelCanceller() {
		// Create new notebook model instance
		NotebookModel model = new NotebookModel(null);
		ProMCanceller canceller = model.getPromCanceller();
		assertEquals(canceller, null);
	}
	
	@Test
	public void testNotebookModelViewManager() {
		// Create new notebook model instance
		NotebookModel model = new NotebookModel(null);
		ProMViewManager view = model.getPromViewManager();
		assertEquals(view, null);
	}
	
	@Test
	public void testNotebookModelResourceManager() {
		// Create new notebook model instance
		NotebookModel model = new NotebookModel(null);
		ProMResourceManager manager = model.getPromResourceManager();
		assertEquals(manager, null);
	}
	
	@Test
	public void testNotebookCellMethods() {
		// Create new notebook model instance
		NotebookModel model = new NotebookModel(null);
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
		NotebookModel model = new NotebookModel(null);
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
		NotebookModel model = new NotebookModel(null);
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
		NotebookModel model = new NotebookModel(null);
		// Get the output logs until the first cell
		List<YLog> logs = model.getOutputLogsTill(0);
		// Check the logs list that is returned (should only be the initial log)
		assertEquals(logs.size(), 1);
		
		// Call the getOutputLogsTill with -1 index
		logs = model.getOutputLogsTill(-1);
		// Check the logs list that is returned (should only be the initial log)
		assertEquals(logs.size(), 1);
	}
	
	@Test
	public void testNotebookGetOutputLogsTillMultiple() {
		// Create new YLog with initial input log
		YLog logY = new YLog(0, "Original Log", originalLog, 0);
		
		// Create new notebook model instance
		NotebookModel model = new NotebookModel(null);
		model.setInitialInput(logY);
		
		// Create new computation cell model instance
		ComputationCellModel cell = new ComputationCellModel(null, 0, null, new ArrayList<>());
		// Set the initial log for the computation cell
		cell.setInputLog(logY);
		
		// Add some computation cells to the model
		model.addCell(cell);
		model.addCell(cell);
		model.addCell(cell);
		
		// Get the output logs until the first cell
		List<YLog> logs = model.getOutputLogsTill(3);
		// Check the logs list that is returned (should only be the initial log)
		assertEquals(logs.size(), 4);
	}
	
	@Test
	public void testNotebookGetInitialInput() {
		// Create new notebook model instance
		NotebookModel model = new NotebookModel(null);
		// Create new YLog
		YLog initialLog = new YLog(0, "Original Log", originalLog, 0);
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
		NotebookModel model = new NotebookModel(null);
		// Get the computing variable from the notebook
		Boolean computing = model.isComputing();
		// Check that the computing variable is properly returned
		assertFalse(computing);
		// Get the computing property
		BooleanProperty computingProperty = model.isComputingProperty();
		// Check that the computing property is properly returned
		assertFalse(computingProperty.get());
	}
	
	@Test
	public void testNotebookComputeOneCell() {
		// Create new YLog with initial input log
		YLog logY = new YLog(0, "Original Log", originalLog, 0);
		
		// Create new notebook model instance
		NotebookModel model = new NotebookModel(null);
		
		// Create new computation cell model instance
		ComputationCellModel compCell = new ComputationCellModel(null, 0, null, new ArrayList<>());
		// Set the initial log for the computation cell
		compCell.setInputLog(logY);
		
		// Create new filter button model
		FilterButtonModel filterButton = new FilterButtonModel(0);
		// Set the initial log for the filter button model
		filterButton.setInputLog(logY.get());
		// Create new configurations for the filter button
		FilterdAbstractConfig config = new FilterdTraceSampleConfig(logY.get(),
				new FilterdTraceSampleFilter());
		// Set the filter configuration parameters
		((ParameterValueFromRange<Integer>) config.getParameters().get(0)).setChosen(2);
		// Set the filter configuration for the filter button
		filterButton.setFilterConfig(config);
		
		// Add filter buttons to the computation cell filter list
		compCell.addFilterModel(0, filterButton);
		// Add computation cell to the notebook model
		model.addCell(compCell);
		
		// Compute the cell's output
		Thread computation = new Thread(new Runnable()
		{
		   public void run() {
			   model.compute();
		   }
		});

		computation.start();
		
		try {
			computation.join();
			// Check that the computation was successful
			assertEquals(compCell.getOutputLogs().get(0).get().size(), 9);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetXML() {
		// Create new notebook model instance
		NotebookModel model = new NotebookModel(null);
		// Get the XML of the notebook model
		try {
			String notebookModel = model.getXML();
			assertTrue(notebookModel.length() != 0);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
