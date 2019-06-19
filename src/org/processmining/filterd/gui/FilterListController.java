package org.processmining.filterd.gui;

import java.io.IOException;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;

/**
 * Controller for the list of filters which is shown inside the configuration
 * modal in the ConfigurationStep.ADD_FILTER step.
 * 
 * @author Filip Davidovic
 */
public class FilterListController {

	@FXML
	private Label statusLabel; // deprecated
	@FXML
	private ListView<String> filterList; // list view for the list of filters
	private VBox root; // root element of the component UI
	private BooleanProperty isApplyDisabled; // property stating whether the apply (next) button should be disabled
	private ObservableList<String> options; // list of options shown in the list view

	/**
	 * Default constructor for this class.
	 * 
	 * @param options
	 *            list of options to be shown in the list view
	 */
	public FilterListController(List<String> options) {
		// set variables
		this.isApplyDisabled = new SimpleBooleanProperty(true);
		if (options == null) {
			// user won't be able to click apply (next) if there are no options (selected)
			throw new NullPointerException("Options cannot be null!");
		}
		if(options.size() <= 0) {
			// user won't be able to click apply (next) if there are no options (selected)
			throw new IllegalArgumentException("List of options must contain at least one element!");
		}
		this.options = FXCollections.observableList(options); // convert to an observable list (this is what list view expects)
		// load UI
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/org/processmining/filterd/gui/fxml/FilterList.fxml"));
		loader.setController(this);
		try {
			root = (VBox) loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void initialize() {
		// set label text
		this.statusLabel.setText("Select a filter"); // set the description of the step
		// populate list
		this.filterList.setItems(this.options); // set the items in the list view
		this.filterList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); // user can select only one filter from the list
		// if nothing is selected, the apply (next) button should be disabled
		isApplyDisabled.bind(Bindings.isEmpty(this.filterList.getSelectionModel().getSelectedItems()));
	}

	public void setStatusLabelText(String text) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// set text
				statusLabel.setText(text);
			}
		});
	}

	public BooleanProperty isApplyDisabledProperty() {
		return this.isApplyDisabled;
	}

	public VBox getRoot() {
		return this.root;
	}

	public String getSelection() {
		return this.filterList.getSelectionModel().getSelectedItem();
	}

}
