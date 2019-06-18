package org.processmining.filterd.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class FilterButtonController {

	private ComputationCellController controller;
	private FilterButtonModel model;
	private Pane layout;

	@FXML
	private Group buttons;
	@FXML
	private Label filterName;
	@FXML
	private HBox filterLayout;
	@FXML
	private Label editButton;
	@FXML
	private Label removeButton;
	@FXML
	private Label moveUpButton;
	@FXML
	private Label moveDownButton;

	public FilterButtonController(ComputationCellController controller, FilterButtonModel model) {
		this.controller = controller;
		this.model = model;
	}

	public void initialize() {
		// Bind properties with components
		bindProperties();
	}

	private void bindProperties() {
		// Name
		filterName.textProperty().bind(model.getNameProperty());

		// Selected property 
		model.getSelectedProperty().addListener((observable, oldvalue, newvalue) -> setSelected(newvalue));

		// valid property
		model.isValidProperty().addListener(new ChangeListener<Boolean>() {

			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				System.out.print("Setting valid to ");
				System.out.println(newValue);
				if (newValue) {
					// filter became valid
					controller.hideConfigurationModal(false);
					if (model.getSelected()) {
						filterLayout.getStyleClass().add("selected");
						buttons.setVisible(true);
					} else {
						filterLayout.getStyleClass().remove("selected");
						buttons.setVisible(false);
					}
					setInvalid(false);
				} else {
					// filter became invalid (empty log or invalid configuration)
					setInvalid(true);
				}
			}
		});

		// edit disabled property
		editButton.disableProperty().bind(model.isEditDisabledProperty());
	}

	public void setSelected(boolean selected) {
		this.model.isValidProperty().set(true);
		if (selected) {
			filterLayout.getStyleClass().add("selected");
			buttons.setVisible(true);
		} else {
			filterLayout.getStyleClass().remove("selected");
			buttons.setVisible(false);
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

	public void setInvalid(boolean val) {
		if (val) {
			filterLayout.getStyleClass().add("invalid");
		} else {
			filterLayout.getStyleClass().remove("invalid");
		}
	}

	@FXML
	public void selectFilterButton() {
		controller.enableAllFilterButtonsBut(-1);
		if (!model.getSelected()) {
			controller.hideConfigurationModal(true);
		}
		controller.getCellModel().selectFilter(model);
	}

	public void enableEditFilterHandler() {
		this.model.setIsEditDisabled(false);
	}

	@FXML
	private void editFilterHandler() {
		selectFilterButton();
		this.controller.enableAllFilterButtonsBut(this.model.getIndex());
		if (this.model.getFilterConfig() != null) {
			this.controller.showModalFilterConfiguration(this.model.getFilterConfig(), this);
			this.model.setIsEditDisabled(true);
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
