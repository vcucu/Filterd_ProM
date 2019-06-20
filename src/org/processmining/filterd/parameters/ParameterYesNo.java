package org.processmining.filterd.parameters;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class representing a yes / no parameter. This parameter allows the user to input
 * a boolean. Its UI counter part is a checkbox.
 */
@XmlRootElement
public class ParameterYesNo extends Parameter {
	
	private boolean defaultChoice; // default value
	private boolean chosen; // currently chosen value
	
	/**
	 * This constructor exists for importing and exporting
	 */
	public ParameterYesNo() {
		
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
	public ParameterYesNo(String name, String nameDisplayed, boolean defaultChoice) {
		super(name, nameDisplayed);
		this.defaultChoice = defaultChoice;
		this.chosen = defaultChoice;
	
	}
	public boolean getDefaultChoice() {
		return defaultChoice;
	}
	
	public void setDefaultChoice(boolean defaultChoice) {
		this.defaultChoice = defaultChoice;
	}
	
	/**
	 * Getter for the currently chosen value.
	 * 
	 * @return currently chosen value
	 */
	public boolean getChosen() {
		return chosen;
	}
	
	public void setChosen(boolean chosen) {
		this.chosen = chosen;
	}
	
	
}
