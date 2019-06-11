package org.processmining.filterd.gui.adapters;

import org.processmining.filterd.gui.CellModel;
import org.processmining.filterd.gui.ComputationCellModel;
import org.processmining.filterd.gui.TextCellModel;

public class CellModelAdapter extends AbstractJAXBAdapter<CellModelAdapted, CellModel> {

	public CellModel unmarshal(CellModelAdapted adaptedModel){
		CellModel model;
		if (adaptedModel.getClass() == ComputationCellModelAdapted.class) {
			//TODO: setcanceller
			model = new ComputationCellModel(staticPromContext, adaptedModel.getIndex(), null, null);
			//((ComputationCellModel) model).addFilterModels(((ComputationCellModelAdapted) adaptedModel).getFilters()); Uncomment once implemented
		} else {
			model = new TextCellModel(staticPromContext, adaptedModel.getIndex());
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
