package org.processmining.filterd.gui.adapters;

import java.util.ArrayList;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.processmining.filterd.gui.CellModel;
import org.processmining.filterd.gui.ComputationCellModel;
import org.processmining.filterd.gui.TextCellModel;
import org.processmining.filterd.models.YLog;

public class CellModelAdapter extends XmlAdapter<CellModelAdapted, CellModel> {

	public CellModel unmarshal(CellModelAdapted adaptedModel) {
		CellModel model;
		if (adaptedModel.getClass() == ComputationCellModelAdapted.class) {
			model = new ComputationCellModel(null, adaptedModel.getIndex(), null, new ArrayList<YLog>());
			((ComputationCellModel) model).addFilterModels(((ComputationCellModelAdapted) adaptedModel).getFilters());
			((ComputationCellModel) model).setIndexOfInputOwner(((ComputationCellModelAdapted) adaptedModel).getIndexOfInputOwner());
		} else {
			model = new TextCellModel(null, adaptedModel.getIndex());
			((TextCellModel) model).setComment(((TextCellModelAdapted) adaptedModel).getComment());
		}
		
		model.setCellName(adaptedModel.getCellName());
		model.setStatusBar(adaptedModel.getStatusBar());
		model.setHidden(adaptedModel.getIsHidden());
		
		return model;
	}

	public CellModelAdapted marshal(CellModel model){
		CellModelAdapted adaptedModel;
		
		// Cell type specific attributes.
		if (model.getClass() == ComputationCellModel.class) {
			adaptedModel = new ComputationCellModelAdapted();
			((ComputationCellModelAdapted) adaptedModel).setFilters(((ComputationCellModel) model).getFilters());
			// set the input log (-1 is initial input log, >= 0 are cell outputs)
			((ComputationCellModelAdapted) adaptedModel).setIndexOfInputOwner(
				((ComputationCellModel) model)
				.getInputLog()
				.getIndexOfOwner());
		} else {
			adaptedModel = new TextCellModelAdapted();
			((TextCellModelAdapted) adaptedModel).setComment(((TextCellModel) model).getComment());
		}
		
		// general attributes
		adaptedModel.setIndex(model.getIndex());
		adaptedModel.setCellName(model.getCellName());
		adaptedModel.setStatusBar(model.getStatusBar());
		adaptedModel.setIsHidden(model.isHidden());
		
		return adaptedModel;
	}
}
