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
	
	public ParameterYesNoController(String title, String id, boolean defaultValue) {
		super(id);
		// load contents
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/widgets/fxml/ParameterYesNo.fxml"));
        fxmlLoader.setController(this);
        try {
            contents = (VBox) fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // set specifics
        label.setText(title);
        checkbox.setSelected(defaultValue);
	}

}
