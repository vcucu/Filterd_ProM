package org.processmining.filterd.gui;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class FilterButtonController {

	private ComputationCellController controller;
	private Pane layout;
	private FilterButtonModel model;
	private ArrayList<ImageView> buttons;
	
	@FXML
	private Label filterName;
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
	
	public FilterButtonController(ComputationCellController controller, FilterButtonModel model) {
		this.controller = controller;
		this.model = model;
		this.buttons = new ArrayList<>();
	}
	
	public void initialize() {
		// Add the ImageView buttons to the ArrayList (allows easier future access)
		buttons.add(removeButton);
		buttons.add(moveUpButton);
		buttons.add(moveDownButton);
		
		updateFilterButtonView();
	}
	
	public void updateFilterButtonView() {
		filterName.setText(model.getName());
		if (model.getSelected()) {
			showButtons();
		} else {
			hideButtons();
		}
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
	
	public void setFilterName(String value) {
		filterName.setText(value);
	}
	
	public void showButtons() {
		for (ImageView button : buttons) {
			button.setVisible(true);
		}
		filterLayout.setStyle("-fx-background-color: #abecab");
	}
	
	public void hideButtons() {
		for (ImageView button : buttons) {
			button.setVisible(false);
		}
		filterLayout.setStyle("-fx-background-color: #eeeeee");
	}

	@FXML
	public void selectFilterButton() {
		if (!model.getSelected()) {
			controller.getCellModel().selectFilter(model);
		}
	}

	@FXML
	private void editFilterHandler() {
		System.out.println("Edit filter handler!");
	}
	
	@FXML
	public void removeFilterHandler() {
		controller.getPanelLayout().getChildren().remove(filterLayout);
		controller.getCellModel().removeFilterModel(model);
		controller.getCellModel().removeFilterController(this);
	}
	
	@FXML
	private void moveUpFilterHandler() {
		int index = model.getIndex();
		if (index > 0) {
			moveFilterButton(index - 1);
		}
	}
	
	@FXML
	private void moveDownFilterHandler() {
		int index = model.getIndex();
		if (index < controller.getCellModel().getFilters().size() - 1) {
			moveFilterButton(index + 1);
		}
	}
	
	private void moveFilterButton(int index) {
		controller.getPanelLayout().getChildren().remove(filterLayout);
		
		controller.getCellModel().getFilters().remove(model);
		controller.getCellModel().getFilterControllers().remove(this);
		
		controller.getCellModel().addFilterModel(index, model);
		controller.getCellModel().addFilterController(index, this);
		
		controller.getPanelLayout().getChildren().add(index, filterLayout);
	}
}
