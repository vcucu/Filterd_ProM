package org.processmining.filterd.gui;

import java.io.IOException;

import org.processmining.filterd.configurations.FilterdAbstractConfig;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ConfigurationModalController {
	
	@FXML private Button cancel;
	@FXML private Button apply;
	@FXML private HBox contentPane;
	private VBox root;
	private FilterdAbstractConfig filterConfig;
	private FilterConfigPanelController currentContentsController;
	private ComputationCellController parent;
	
	public ConfigurationModalController(ComputationCellController parent) {
		this.parent = parent;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/gui/fxml/ConfigurationModal.fxml"));
        fxmlLoader.setController(this);
        try {
            root = (VBox) fxmlLoader.load();
            HBox.setHgrow(root, Priority.ALWAYS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
	
	public void clear() {
		filterConfig = null;
		contentPane.getChildren().clear();
	}
	
	@FXML 
	public void cancel() {
		// remove everything from this modal
		clear();
		parent.hideConfigurationModal();
	}
	
	@FXML 
	public void apply() {
		// check if the config. can be populated
		if(filterConfig.canPopulate(currentContentsController)) {
			// if so populate it 
			filterConfig.populate(currentContentsController);
			parent.hideConfigurationModal();
		}
	}
	
//	public void hide() {
//		root.setVisible(false);
//		root.setManaged(false);
//	}
//	
//	public void show() {
//		root.setVisible(true);
//		root.setManaged(true);
//	}
	
	public void setContent(FilterdAbstractConfig filterConfig) {
		// set internal variables
		this.filterConfig = filterConfig;
		this.currentContentsController = filterConfig.getConfigPanel();
		// populate contents pane
		HBox.setHgrow(currentContentsController.getRoot(), Priority.ALWAYS); // make the content 100% of the available width
		contentPane.getChildren().clear(); // remove anything that may be left over by previous contents
		contentPane.getChildren().add(currentContentsController.getRoot());
	}
	
	public VBox getRoot() {
		return root;
	}
}
