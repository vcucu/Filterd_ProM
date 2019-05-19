package org.processmining.filterd.widgets;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ParameterTextController extends ParameterController {
	
	@FXML private TextField textfield;
	@FXML private Label label;
	
	public ParameterTextController(String nameDisplayed, String name, String defaultValue) {
		super(name);
		// load contents
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/widgets/fxml/ParameterText.fxml"));
        fxmlLoader.setController(this);
        try {
            contents = (VBox) fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
		// set specifics
		label.setText(nameDisplayed);
        textfield.setText(defaultValue);
	}
	
	public String getValue() {
		return textfield.getText();
	}
}