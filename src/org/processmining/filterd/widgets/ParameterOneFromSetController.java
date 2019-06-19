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

/**
 * Parameter one from set UI counterpart.
 * 
 * @author Filip Davidovic
 */
public class ParameterOneFromSetController extends ParameterController {
	
	@FXML private ComboBox<String> combobox; // one from set combo box
	@FXML private Label label; // label describing the parameter
	
	/**
	 * Default constructor which should be used in all actual code.
	 * 
	 * @param nameDisplayed
	 *            description of the parameter
	 * @param name
	 *            unique identified of the parameter (used to map UI parameter
	 *            to actual parameter in populate method of the filter
	 *            configuration)
	 * @param defaultValue
	 *            default value of the combo box
	 * @param list
	 *            list of options for the combo box
	 */
	public ParameterOneFromSetController(String nameDisplayed, String name, String defaultValue, List<String> list) {
		super(name);
		// load UI contents
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/widgets/fxml/ParameterOneFromSet.fxml"));
        fxmlLoader.setController(this);
        try {
            contents = (VBox) fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // set specifics
        label.setText(nameDisplayed); // set description
        ObservableList<String> observableList = FXCollections.observableList(list); // transform list to an observable list (this is what the combo box expects)
        combobox.setItems(observableList); // set items of the combo box
        combobox.getSelectionModel().select(defaultValue); // set the initial value of the combo box
	}
	
	/**
	 * Getter for the combo box UI element.
	 * Used by filter configurations to further customize the behavior of this component. 
	 * 
	 * @return combo box UI element
	 */
	public ComboBox<String> getComboBox() {
		return this.combobox;
	}
	
	/**
	 * Getter for the value of the combo box
	 * 
	 * @return current value of the combo box
	 */
	public String getValue() {
		return combobox.getSelectionModel().getSelectedItem();
	}
	
}