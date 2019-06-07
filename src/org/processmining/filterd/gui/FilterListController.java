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
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;

public class FilterListController {
	
	@FXML
	private ListView<String> filterList;
	@FXML
	private Label statusLabel;
	@FXML
	private ProgressIndicator progressIndicator;
	private VBox root;
	private BooleanProperty isApplyDisabled;
	private ObservableList<String> options;

	public FilterListController(List<String> options) {
		// set variables
		this.isApplyDisabled = new SimpleBooleanProperty(true);
        if(options == null) {
        	throw new NullPointerException("Options cannot be null!");
        }
        this.options = FXCollections.observableList(options);
		// load UI
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/gui/fxml/FilterList.fxml"));
		loader.setController(this);
        try {
            root = (VBox) loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
	
	public void initialize() {
		// hide progress indicator
		this.progressIndicator.setVisible(false);
		this.progressIndicator.setManaged(false);
		// set label text
		this.statusLabel.setText("Select a filter");
		// populate list
		this.filterList.setItems(this.options);
		this.filterList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); // user can select only one filter from the list
		// add bindings
		isApplyDisabled.bind(Bindings.isEmpty(this.filterList.getSelectionModel().getSelectedItems()));
	}
	
	public void setStatusLabelText(String text) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// set text
				statusLabel.setText(text);
				// show progress indicator
				progressIndicator.setVisible(true);
				progressIndicator.setManaged(true);
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
