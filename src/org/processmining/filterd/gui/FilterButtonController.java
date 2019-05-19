package org.processmining.filterd.gui;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class FilterButtonController {
	
	private ComputationCellController controller;
	private Pane layout;
	private FilterButtonModel model;
	private int index;
	
	@FXML 
	private HBox filterButtonLayout;
	@FXML
	private ImageView remove;
	
	public FilterButtonController(ComputationCellController controller) {
		this.controller = controller;
		this.model = new FilterButtonModel(this);
		model.addPropertyChangeListener(new FilterButtonListener());
	}
	
	public Pane getCellLayout() {
		return layout;
	}
	
	public void setCellLayout(Pane layout) {
		this.layout = layout;
	}
	
	public FilterButtonModel getModel() {
		return model;
	}
	
	public void setModel(FilterButtonModel model) {
		this.model = model;
	}
	
	private void hideOldSelectedFilterButton() {
		for (FilterButtonModel filterModel : controller.getFiltersOL()) {
			if (filterModel.isSelected()) {
				filterModel.setSelected(false);
				filterModel.getController().hideRemove();
			}
		}
	}
	
	@FXML
	public void selectFilterButton() {
		String temp = "Select filter button #" + Integer.toString(index);
		System.out.println(temp);
		
		hideOldSelectedFilterButton();
		
		model.setSelected(true);
		showRemove();
	}
	
	@FXML
	public void removeFilterButton() {
		String temp = "Remove filter button #" + Integer.toString(index);
		System.out.println(temp);
		controller.getPanelLayout().getChildren().remove(filterButtonLayout);
	}
	
	public void showRemove() {
		remove.setVisible(true);
	}
	
	public void hideRemove() {
		remove.setVisible(false);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
