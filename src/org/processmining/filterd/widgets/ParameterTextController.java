package org.processmining.filterd.widgets;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ParameterTextController extends ParameterController {
	
	@FXML private TextField textfield;
	@FXML private Label label;
	
	public ParameterTextController(String nameDisplayed, String name, String defaultValue) {
		super(name);
		// load contents
        this.loadFXMLContents(this, "/org/processmining/filterd/widgets/fxml/ParameterText.fxml");
		// set specifics
		label.setText(nameDisplayed);
        textfield.setText(defaultValue);
	}
	
	public String getValue() {
		return textfield.getText();
	}
}