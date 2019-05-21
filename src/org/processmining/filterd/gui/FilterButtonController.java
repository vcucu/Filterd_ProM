package org.processmining.filterd.gui;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class FilterButtonController {
	
	private ComputationCellController controller;
	private Pane layout;
	private FilterButtonModel model;
	private ArrayList<ImageView> buttons;
	
	@FXML 
	private HBox filterLayout;
	@FXML
	private ImageView editButton;
	@FXML
	private ImageView removeButton;
	@FXML
	private ImageView moveUpButton;
	@FXML
	private ImageView moveDownButton;
	
	public FilterButtonController(ComputationCellController controller) {
		this.controller = controller;
		this.model = new FilterButtonModel();
		this.buttons = new ArrayList<>();
		model.addPropertyChangeListener(new FilterButtonListener(this));
	}
	
	public void initialize() {
		// Add the ImageView buttons to the ArrayList (allows easier future access)
		buttons.add(removeButton);
		buttons.add(moveUpButton);
		buttons.add(moveDownButton);
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
	
	public void showButtons() {
		for (ImageView button : buttons) {
			button.setVisible(true);
		}
		// filterLayout.setStyle("-fx-background-color: #0bd50b");
	}
	
	public void hideButtons() {
		for (ImageView button : buttons) {
			button.setVisible(false);
		}
		// filterLayout.setStyle("-fx-background-color: #eeeeee");
	}
	
	private void updateSelection() {
		for (FilterButtonModel filterModel : controller.getFiltersOL()) {
			if (filterModel.isSelected()) {
				filterModel.setSelected(false);
			}
		}
	}
	
	@FXML
	public void selectFilterButton() {
		updateSelection();
		model.setSelected(true);
	}
	
	@FXML
	private void editFilterHandler() {
		System.out.println("Edit filter handler!");
	}
	
	@FXML
	public void removeFilterHandler() {
		controller.getPanelLayout().getChildren().remove(filterLayout);
		controller.getFiltersOL().remove(model);
		System.out.println("New filtersOL size: " + controller.getFiltersOL().size());
	}
	
	@FXML
	private void moveUpFilterHandler() {
		System.out.println("MoveUp filter handler!");
	}
	
	@FXML
	private void moveDownFilterHandler() {
		System.out.println("MoveDown filter handler!");
	}
}
