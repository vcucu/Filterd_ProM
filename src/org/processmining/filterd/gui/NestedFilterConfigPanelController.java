package org.processmining.filterd.gui;

import java.util.ArrayList;
import java.util.List;

import org.processmining.filterd.parameters.Parameter;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class NestedFilterConfigPanelController extends AbstractFilterConfigPanelController {
	
	public NestedFilterConfigPanelController(List<Parameter> parameters) {
		controllers = new ArrayList<>();
		root = new VBox();
		VBox.setMargin(root, new Insets(5.0));
        populateFromParameters(parameters);
	}

	public VBox getNextContainer(Parameter param) {
		return root;
	}

}
