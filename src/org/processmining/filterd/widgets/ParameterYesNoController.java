package org.processmining.filterd.widgets;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class ParameterYesNoController extends ParameterController {
	
	@FXML private CheckBox checkbox;
	@FXML private Label label;
	
	public ParameterYesNoController(String nameDisplayed, String name, boolean defaultValue) {
		super(name);
		// load contents
        this.loadFXMLContents(this, "/org/processmining/filterd/widgets/fxml/ParameterYesNo.fxml");
        // set specifics
        label.setText(nameDisplayed);
        checkbox.setSelected(defaultValue);
	}

	public boolean getValue() {
		return checkbox.isSelected();
	}
	
	public CheckBox getCheckbox() {
		return checkbox;
	}
}