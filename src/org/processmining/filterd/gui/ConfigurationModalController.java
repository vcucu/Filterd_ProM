package org.processmining.filterd.gui;

import java.io.IOException;
import java.util.List;

import org.processmining.filterd.configurations.FilterdAbstractConfig;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class ConfigurationModalController {
	
	@FXML private Button cancel;
	@FXML private Button apply;
	@FXML private HBox contentPane;
	private VBox root;
	private FilterdAbstractConfig filterConfig;
	private AbstractFilterConfigPanelController currentContentsController;
	private ComputationCellController parent;
	private boolean isConfiguringFilter;
	private Callback<String, FilterdAbstractConfig> filterSelectionCallback;
	
	public ConfigurationModalController(ComputationCellController parent) {
		this.parent = parent;
		this.isConfiguringFilter = true;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/gui/fxml/ConfigurationModal.fxml"));
        fxmlLoader.setController(this);
        try {
            root = (VBox) fxmlLoader.load();
            HBox.setHgrow(root, Priority.ALWAYS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
	
	public void showFilterList(List<String> options, Callback<String, FilterdAbstractConfig> callback) {
		if(callback == null) {
			throw new IllegalArgumentException("Callback cannot be null");
		}
		resetModal();
		this.filterSelectionCallback = callback; // set the callback which will be invoked when the user clicks Next/Apply
		ObservableList<String> observableOptions = FXCollections.observableList(options);
		ListView<String> list = new ListView<>(observableOptions);
		list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		HBox.setHgrow(list, Priority.ALWAYS);
		contentPane.getChildren().clear();
		contentPane.getChildren().add(list);
		apply.setText("Next");
		apply.disableProperty().bind(Bindings.isEmpty(list.getSelectionModel().getSelectedItems())); // disable the button if nothing is selected
		isConfiguringFilter = false;
	}
	
	public void showFilterConfiguration(FilterdAbstractConfig filterConfig) {
		if(filterConfig == null) {
			throw new IllegalArgumentException("Filter configuration cannot be null");
		}
		resetModal();
		// set internal variables
		this.filterConfig = filterConfig;
		this.currentContentsController = filterConfig.getConfigPanel();
		// populate contents pane
		HBox.setHgrow(currentContentsController.getRoot(), Priority.ALWAYS); // make the content 100% of the available width
		contentPane.getChildren().clear(); // remove anything that may be left over by previous contents
		contentPane.getChildren().add(currentContentsController.getRoot());
		isConfiguringFilter = true;
	}
	
	@FXML 
	private void cancel() {
		resetModal();
		parent.hideConfigurationModal();
	}
	
	@FXML 
	private void apply() {
		if(isConfiguringFilter) {
			// user is configuring a filter config. -> apply changes if possible
			FilterConfigPanelController casted = (FilterConfigPanelController) currentContentsController; 
			if(filterConfig.canPopulate(casted)) {
				filterConfig.populate(currentContentsController);
			}
			parent.hideConfigurationModal();
		} else {
			// user is picking a filter to use -> move on to the configuration screen
			if(filterSelectionCallback == null) {
				throw new IllegalStateException("apply() was called before showFilterList()");
			}
			if(!(contentPane.getChildren().get(0) instanceof ListView)) {
				throw new IllegalStateException("Contents of the root node do not have a ListView");
			}
			ListView<String> list = (ListView) contentPane.getChildren().get(0); // cast is checked above
			String userSelection = list.getSelectionModel().getSelectedItem();
			showFilterConfiguration(filterSelectionCallback.call(userSelection));
		}
	}
	
	public void resetModal() {
		filterConfig = null;
		contentPane.getChildren().clear();
		apply.setText("Apply");
		apply.disableProperty().unbind(); // remove any bindings that may have been added
		filterSelectionCallback = null;
	}
	
	public VBox getRoot() {
		return root;
	}
}
