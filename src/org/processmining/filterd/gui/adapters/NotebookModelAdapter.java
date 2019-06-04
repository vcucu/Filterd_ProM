package org.processmining.filterd.gui.adapters;

import org.processmining.filterd.gui.AbstractJAXBAdapter;
import org.processmining.filterd.gui.NotebookModel;

/**
 * Converts the NotebookModel into a NotebokModelAdapted and vice versa.
 *
 */
public class NotebookModelAdapter extends AbstractJAXBAdapter<NotebookModelAdapted, NotebookModel> {

	/**
	 * Converts the NotebookMode into a NotebookModelAdapted.
	 */
	public NotebookModelAdapted marshal(NotebookModel model) {
		NotebookModelAdapted adaptedModel = new NotebookModelAdapted();
		adaptedModel.setCells(model.getCells());
		adaptedModel.setComputationMode(model.getComputationMode());
		return adaptedModel;
	}

	/**
	 * Converts a NotebookModelAdapted to a NotebookModel
	 */
	public NotebookModel unmarshal(NotebookModelAdapted adaptedModel) throws NullPointerException {
		// Parameters come from the static variables in AbstractJAXBAdapter
		if (staticPromContext == null) {
			throw new NullPointerException(
					"org.processmining.filterd.gui.adapters.NotebookModelAdapter.unmarshal():" +
			"org.processmining.filterd.gui.AbstractJAXBAdapter.staticContext is null");
		} else if (staticInitialInput == null) {
			throw new NullPointerException(
					"org.processmining.filterd.gui.adapters.NotebookModelAdapter.unmarshal():" +
			"org.processmining.filterd.gui.AbstractJAXBAdapter.staticInitialInput is null");
		}
		NotebookModel model = new NotebookModel(staticPromContext, staticInitialInput, null);
		//model.addCells(adaptedModel.getCells());
		model.setComputationMode(adaptedModel.getComputationMode());
		return model;
	}

}