package org.processmining.filterd.parameters;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class representing a text parameter. This parameter allows the user to input
 * text. Its UI counter part is a text field / area.
 */
@XmlRootElement
public class ParameterText extends Parameter {

	private String defaultChoice; // default value
	private String chosen; // currently chosen value 

	/**
	 * This constructor exists for importing and exporting
	 */
	public ParameterText() {

	}

	/**
	 * Default constructor of this class. It should be used in all actual code!
	 * 
	 * @param name
	 *            unique identifier of the parameter (used to map parameters and
	 *            parameter controllers in populate methods of filter
	 *            configurations)
	 * @param nameDisplayed
	 *            description of this parameter, used in the UI
	 * @param defaultChoice
	 *            default value of the parameter
	 */
	public ParameterText(String name, String nameDisplayed, String defaultChoice) {
		super(name, nameDisplayed);
		this.defaultChoice = defaultChoice;
		this.chosen = defaultChoice;
	}

	public String getDefaultChoice() {
		return defaultChoice;
	}

	public void setDefaultChoice(String defaultChoice) {
		this.defaultChoice = defaultChoice;
	}

	/**
	 * Getter for the current value of the parameter.
	 * 
	 * @return current value of the parameter
	 */
	public String getChosen() {
		return chosen;
	}

	public void setChosen(String chosen) {
		this.chosen = chosen;
	}

}
