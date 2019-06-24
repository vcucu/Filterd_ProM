package org.processmining.filterd.widgets;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Parameter text UI counterpart.
 * 
 * @author Filip Davidovic
 */
public class ParameterTextController extends ParameterController {

	@FXML
	private TextField textfield; // text field for user's input
	@FXML
	private Label label; // label describing the parameter

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
	 *            default value of the text field
	 */
	public ParameterTextController(String nameDisplayed, String name, String defaultValue) {
		super(name);
		// load contents
        this.loadFXMLContents(this, "/org/processmining/filterd/widgets/fxml/ParameterText.fxml");
		// set specifics
		label.setText(nameDisplayed); // set description
		textfield.setText(defaultValue); // set the initial value of the text field
	}

	/**
	 * Getter for the current value of the text field.
	 * 
	 * @return current value of the text field
	 */
	public String getValue() {
		return textfield.getText();
	}
}