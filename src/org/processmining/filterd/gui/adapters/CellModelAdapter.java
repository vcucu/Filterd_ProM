package org.processmining.filterd.gui.adapters;

import java.util.ArrayList;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.processmining.filterd.gui.CellModel;
import org.processmining.filterd.gui.ComputationCellModel;
import org.processmining.filterd.gui.TextCellModel;
import org.processmining.filterd.models.YLog;

/**
 * Class used to marshal (export) and unmarshal (import) the CellModel class.
 * Lower-level classes, like FilterButtonModel, are marshaled and unmarshaled
 * automatically by JAXB.
 */
public class CellModelAdapter extends XmlAdapter<CellModelAdapted, CellModel> {

	/**
	 * Method used by JAXB to import and serialize a cell model.
	 * 
	 * @param adaptedModel
	 *            adapted model that is to be serialized into a cell model
	 * @return serialized cell model
	 */
	public CellModel unmarshal(CellModelAdapted adaptedModel) {
		CellModel model;
		// different cell types require different cell models
		if (adaptedModel.getClass() == ComputationCellModelAdapted.class) {
			// computation cell models have associated filters and an input log
			model = new ComputationCellModel(null, adaptedModel.getIndex(), null, new ArrayList<YLog>());
			((ComputationCellModel) model).addFilterModels(((ComputationCellModelAdapted) adaptedModel).getFilters());
			((ComputationCellModel) model)
					.setIndexOfInputOwner(((ComputationCellModelAdapted) adaptedModel).getIndexOfInputOwner());
		} else {
			model = new TextCellModel(null, adaptedModel.getIndex());
			// text cell models have associated text comments
			((TextCellModel) model).setComment(((TextCellModelAdapted) adaptedModel).getComment());
		}
		// all cells have a name, status bas and a hidden property
		model.setCellName(adaptedModel.getCellName());
		model.setStatusBar(adaptedModel.getStatusBar());
		model.setHidden(adaptedModel.getIsHidden());

		return model;
	}

	/**
	 * Method used by JAXB to deserialize a cell model into an adapted cell
	 * model.
	 * 
	 * @param model
	 *            cell model that is to be deserialized
	 * @return deserialized cell model i.e. adapted cell model
	 */
	public CellModelAdapted marshal(CellModel model) {
		CellModelAdapted adaptedModel;
		// different cell types require different adapted cell models
		if (model.getClass() == ComputationCellModel.class) {
			adaptedModel = new ComputationCellModelAdapted();
			// computation cells have associated filters and an input log
			((ComputationCellModelAdapted) adaptedModel).setFilters(((ComputationCellModel) model).getFilters());
			// set the input log (-1 is initial input log, >= 0 are cell outputs)
			((ComputationCellModelAdapted) adaptedModel)
					.setIndexOfInputOwner(((ComputationCellModel) model).getInputLog().getIndexOfOwner());
		} else {
			adaptedModel = new TextCellModelAdapted();
			((TextCellModelAdapted) adaptedModel).setComment(((TextCellModel) model).getComment());
		}
		// all cells have a name, status bas and a hidden property
		adaptedModel.setIndex(model.getIndex());
		adaptedModel.setCellName(model.getCellName());
		adaptedModel.setStatusBar(model.getStatusBar());
		adaptedModel.setIsHidden(model.isHidden());

		return adaptedModel;
	}
}
