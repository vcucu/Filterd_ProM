package org.processmining.filterd.widgets;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ParameterYesNoController extends ParameterController {
	
	@FXML private CheckBox checkbox;
	@FXML private Label label;
	
	public ParameterYesNoController(String nameDisplayed, String name, boolean defaultValue) {
		super(name);
		// load contents
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/widgets/fxml/ParameterYesNo.fxml"));
        fxmlLoader.setController(this);
        try {
            contents = (VBox) fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // set specifics
        label.setText(nameDisplayed);
        checkbox.setSelected(defaultValue);
	}

	public boolean getValue() {
		return checkbox.isSelected();
	}
}