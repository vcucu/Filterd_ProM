package org.processmining.filterd.widgets;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ParameterOneFromSetController extends ParameterController {
	
	@FXML private ComboBox<String> combobox;
	@FXML private Label label;
	
	public ParameterOneFromSetController(String title, String id, String defaultValue, List<String> list) {
		super(id);
		// load contents
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/widgets/fxml/ParameterOneFromSet.fxml"));
        fxmlLoader.setController(this);
        try {
            contents = (VBox) fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // set specifics
        label.setText(title);
        ObservableList<String> observableList = FXCollections.observableList(list);
        combobox.setItems(observableList);
        combobox.getSelectionModel().select(defaultValue);
	}
	
}
