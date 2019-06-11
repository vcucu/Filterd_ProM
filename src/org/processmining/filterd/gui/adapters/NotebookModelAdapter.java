package org.processmining.filterd.gui.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.processmining.filterd.gui.NotebookModel;

/**
 * Converts the NotebookModel into a NotebokModelAdapted and vice versa.
 *
 */
public class NotebookModelAdapter extends XmlAdapter<NotebookModelAdapted, NotebookModel> {

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
		NotebookModel model = new NotebookModel(null, null, null);
		model.addCells(adaptedModel.getCells());
		model.setComputationMode(adaptedModel.getComputationMode());
		return model;
	}

}