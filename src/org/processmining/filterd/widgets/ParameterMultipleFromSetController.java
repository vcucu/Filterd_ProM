package org.processmining.filterd.widgets;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;

public class ParameterMultipleFromSetController extends ParameterController {
	@FXML private ListView<String> list;
	@FXML private Label label;
	
	public ParameterMultipleFromSetController(String title, String id, List<String> defaultValues, List<String> options) {
		super(id);
		// load contents
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/widgets/fxml/ParameterMultipleFromSet.fxml"));
        fxmlLoader.setController(this);
        try {
            contents = (VBox) fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // set specifics
        label.setText(title);
        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ObservableList<String> observableList = FXCollections.observableList(options);
        list.setItems(observableList);
        for(String option : defaultValues) {
        	list.getSelectionModel().select(option);
        }
        list.scrollTo(defaultValues.get(0));
	}
}