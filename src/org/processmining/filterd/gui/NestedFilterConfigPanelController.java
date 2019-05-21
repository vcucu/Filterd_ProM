package org.processmining.filterd.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.processmining.filterd.parameters.Parameter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class NestedFilterConfigPanelController extends AbstractFilterConfigPanelController {
	
	@FXML private VBox panel;
	
	public NestedFilterConfigPanelController(List<Parameter> parameters) {
		controllers = new ArrayList<>();
		// load UI
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/gui/fxml/NestedFilterConfigPanel.fxml"));
		loader.setController(this);
        try {
            root = (VBox) loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        populateFromParameters(parameters);
	}

	protected VBox getNextContainer() {
		return panel;
	}

}
