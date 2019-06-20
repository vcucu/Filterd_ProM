package org.processmining.filterd.widgets;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

/**
 * Yes / no parameter UI counterpart.
 * 
 * @author Filip Davidovic
 */
public class ParameterYesNoController extends ParameterController {

	@FXML
	private CheckBox checkbox; // checkbox for the user's input
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
	 *            default value of the checkbox
	 */
	public ParameterYesNoController(String nameDisplayed, String name, boolean defaultValue) {
		super(name);
		// load contents
        this.loadFXMLContents(this, "/org/processmining/filterd/widgets/fxml/ParameterYesNo.fxml");
        // set specifics
        label.setText(nameDisplayed); // set description 
        checkbox.setSelected(defaultValue); // set the initial value of the checkbox
	}

	/**
	 * Getter for the currently chosen value.
	 * 
	 * @return currently chosen value
	 */
	public boolean getValue() {
		return checkbox.isSelected();
	}

	/**
	 * Getter for the checkbox UI component. Used by filter configuration to
	 * further customize the behavior of this component.
	 * 
	 * @return checkboox UI component
	 */
	public CheckBox getCheckbox() {
		return checkbox;
	}
}