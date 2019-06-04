package org.processmining.filterd.gui;

import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
	
	@FXML private Label filterName;
	@FXML private HBox filterLayout;
	@FXML private ImageView editButton;
	@FXML private ImageView removeButton;
	@FXML private ImageView moveUpButton;
	@FXML private ImageView moveDownButton;
	
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
		
		// Bind properties with components
		bindProperties();
	}
	
	private void bindProperties() {
		// Name
		filterName.textProperty().bind(model.getNameProperty());
		
		// Selected property 
		model.getSelectedProperty().addListener(
				(observable, oldvalue, newvalue) ->
				setSelected(newvalue)
		);
		
		// valid property
		model.isValidProperty().addListener(new ChangeListener<Boolean>() {

			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					// filter became valid
					controller.hideConfigurationModal(false);
					setSelected(model.getSelected());
				} else {
					// filter became invalid (empty log or invalid configuration)
					makeInvalid();
				}
			}
		});
		
	}
	
	public void setSelected(boolean selected) {
		if (selected) {
			showButtons();
		} else {
			hideButtons();
		}
	}

	public Pane getCellLayout() {
		return layout;
	}

	public void setFilterLayout(Pane layout) {
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
	
	public void setFilterLayout(HBox temp) {
		this.filterLayout = temp;
	}
	
	public HBox getFilterLayout() {
		return this.filterLayout;
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
		enableEditFilterHandler();
	}
	
	public void makeInvalid() {
		filterLayout.setStyle("-fx-background-color: #ecabab");
	}

	@FXML
	public void selectFilterButton() {
		if(!model.getSelected()) {
			controller.hideConfigurationModal(false);
			controller.getCellModel().selectFilter(model);
		}
	}
	
	public void enableEditFilterHandler() {
		this.editButton.setDisable(false);
	}

	@FXML
	private void editFilterHandler() {
		selectFilterButton();
		showButtons();
		if(this.model.getFilterConfig() != null) {
			this.controller.showModalFilterConfiguration(this.model.getFilterConfig(), this);
			this.editButton.setDisable(true);
		}
	}
	
	@FXML
	public void removeFilterHandler() {
		controller.removeFilter(model);
	}
	
	@FXML
	private void moveUpFilterHandler() {
		int index = model.getIndex();
		if (index > 0) {
			move(index - 1);
		}
	}
	
	@FXML
	private void moveDownFilterHandler() {
		int index = model.getIndex();
		if (index < controller.getCellModel().getFilters().size() - 1) {
			move(index + 1);
		}
	}
	
	private void move(int index) {
		// Remove layout
		controller.getPanelLayout().getChildren().remove(filterLayout);
		// Remove model
		controller.getCellModel().getFilters().remove(model);
		// Add model at new position
		controller.getCellModel().getFilters().add(index, model);
		// Add layout at new position
		controller.getPanelLayout().getChildren().add(index, filterLayout);
	}
}
