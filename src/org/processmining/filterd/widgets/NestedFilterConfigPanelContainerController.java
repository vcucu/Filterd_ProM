package org.processmining.filterd.widgets;

import java.io.IOException;

import org.processmining.filterd.gui.NestedFilterConfigPanelController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class NestedFilterConfigPanelContainerController {
	
	@FXML private Pane panel;
	private VBox root;
	private NestedFilterConfigPanelController nestedConfigPanel;
	
	public NestedFilterConfigPanelContainerController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/widgets/fxml/NestedFilterConfigPanelContainer.fxml"));
        fxmlLoader.setController(this);
        try {
            root = (VBox) fxmlLoader.load();
            HBox.setHgrow(root, Priority.ALWAYS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
	
	public void setContents(NestedFilterConfigPanelController nestedConfigPanel) {
		panel.getChildren().clear();
		panel.getChildren().add(nestedConfigPanel.getRoot());
		this.nestedConfigPanel = nestedConfigPanel;
	}
	
	public VBox getRoot() {
		return root;
	}
	
	public NestedFilterConfigPanelController getNestedConfigPanel() {
		return nestedConfigPanel;
	}

}
