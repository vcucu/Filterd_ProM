package org.processmining.tests.adapters;

import org.junit.Test;
import org.processmining.filterd.gui.ComputationMode;
import org.processmining.filterd.gui.NotebookModel;
import org.processmining.filterd.gui.adapters.NotebookModelAdapted;
import org.processmining.filterd.gui.adapters.NotebookModelAdapter;

import junit.framework.TestCase;

public class NotebookModelAdapterTest extends TestCase {
	
	NotebookModelAdapter adapter = new NotebookModelAdapter();
	
	@Test
	public void testNotebookModelAdapter() {
		
		// --------------- TESTING MARSHAL ---------------
		// Create new notebook model
		NotebookModel model = new NotebookModel(null);
		// Set the computation mode of the notebook
		model.setComputationMode(ComputationMode.MANUAL);
		// Compute adapted notebook model
		NotebookModelAdapted adaptedModel = adapter.marshal(model);
		// Check the adapted model was properly created
		assertEquals(adaptedModel.getComputationMode(), ComputationMode.MANUAL);
		assertEquals(adaptedModel.getCells().size(), 0);
		
		// --------------- TESTING UNMARSHAL ---------------
		try {
			// Create new notebook from the adapted model
			NotebookModel newModel = adapter.unmarshal(adaptedModel);
			// Should throw an error since there is no ProM instance running
			fail("Error was NOT thrown!");
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}

}
