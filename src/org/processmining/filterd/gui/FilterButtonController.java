package org.processmining.filterd.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * Controller for the filter button UI component.
 */
public class FilterButtonController {

	private ComputationCellController controller; // controller of the parent cell
	private FilterButtonModel model; // model associated with this filter button
	private Pane layout; // root component of this UI element

	@FXML
	private Group buttons; // move up and down, edit, remove, etc. buttons group
	@FXML
	private Label filterName; // label for the name of the filter
	@FXML
	private HBox filterLayout;
	@FXML
	private Label editButton; // edit button
	@FXML
	private Label removeButton; // remove button
	@FXML
	private Label moveUpButton; // button to move up
	@FXML
	private Label moveDownButton; // button to move down

	public FilterButtonController(ComputationCellController controller, FilterButtonModel model) {
		this.controller = controller;
		this.model = model;
	}

	public void initialize() {
		// Bind properties with components
		bindProperties();
	}

	/**
	 * Method to bind the model properties with change events for UI
	 */
	private void bindProperties() {
		// bind the name property with the label's text 
		filterName.textProperty().bind(model.getNameProperty());
		// bind the selected property with the look of the filter button
		model.getSelectedProperty().addListener((observable, oldvalue, newvalue) -> setSelected(newvalue));
		// bind the valid property with the look of the filter button
		model.isValidProperty().addListener(new ChangeListener<Boolean>() {

			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					// filter became valid
					setInvalid(false);
					controller.hideConfigurationModal(false);
					if (model.getSelected()) {
						// filter was previously selected so make it look appropriately
						filterLayout.getStyleClass().add("selected");
						buttons.setVisible(true);
						buttons.setManaged(true);
					} else {
						// filter was previously not selected so make it look appropriately
						filterLayout.getStyleClass().remove("selected");
						buttons.setVisible(false);
						buttons.setManaged(false);
					}
				} else {
					// filter became invalid (empty log or invalid configuration)
					setInvalid(true);
				}
			}
		});
		// bind the edit disabled property with the disabled property of the edit button
		editButton.disableProperty().bind(model.isEditDisabledProperty());
	}

	/**
	 * Method to make the filter button look selected / not selected.
	 * 
	 * @param selected
	 *            should the filter look selected or not selected
	 */
	public void setSelected(boolean selected) {
		// if we are selecting a filter, we are making it valid
		this.model.isValidProperty().set(true);
		// change the look of the filter button based on the passed parameter
		if (selected) {
			filterLayout.getStyleClass().add("selected");
			buttons.setVisible(true);
			buttons.setManaged(true);
		} else {
			filterLayout.getStyleClass().remove("selected");
			buttons.setVisible(false);
			buttons.setManaged(false);
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

	/**
	 * Handler for the on-click event.
	 */
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

	/**
	 * Handler for the on-click event for the edit button.
	 */
	@FXML
	private void editFilterHandler() {
		selectFilterButton();
		this.controller.enableAllFilterButtonsBut(this.model.getIndex());
		if (this.model.getFilterConfig() != null) {
			this.controller.showModalFilterConfiguration(this.model.getFilterConfig(), this);
			this.model.setIsEditDisabled(true);
		}
	}

	/**
	 * Handler for the on-click event for the remove button.
	 */
	@FXML
	public void removeFilterHandler() {
		controller.removeFilter(model);
		//We have removed a filter but have not yet computed the cell with this updated preset so cell is out of date
		controller.getCellModel().setStatusBar(CellStatus.OUT_OF_DATE);
	}

	/**
	 * Handler for the on-click event for the move up button.
	 */
	@FXML
	private void moveUpFilterHandler() {
		int index = model.getIndex();
		if (index > 0) {
			move(index - 1);
			//We have moved a filter but have not yet computed the cell with this updated preset so cell is out of date
			controller.getCellModel().setStatusBar(CellStatus.OUT_OF_DATE);
		}
	}

	/**
	 * Handler for the on-click event for the move down button.
	 */
	@FXML
	private void moveDownFilterHandler() {
		int index = model.getIndex();
		if (index < controller.getCellModel().getFilters().size() - 1) {
			move(index + 1);
			//We have moved a filter but have not yet computed the cell with this updated preset so cell is out of date
			controller.getCellModel().setStatusBar(CellStatus.OUT_OF_DATE);
		}

	}

	/**
	 * Move the filter to the specified position.
	 * 
	 * @param index
	 *            position to which the filter should be moved
	 */
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
