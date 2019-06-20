package org.processmining.filterd.parameters;

import javax.xml.bind.annotation.XmlRootElement;

/*
 * Class representing a yes/no parameter.
 * It is represented by a checkbox in the UI
 */
@XmlRootElement
public class ParameterYesNo extends Parameter {
	
	private boolean defaultChoice;
	private boolean chosen;
	
	/**
	 * This constructor exists for importing and exporting
	 */
	public ParameterYesNo() {
		
	}
	/**
	 * Constructor of the parameter
	 * @param name, the identifier of the parameter
	 * @param nameDisplayed, the short explanation displayed in the UI
	 * @param defaultChoice, the default value of the parameter
	 */
	public ParameterYesNo(String name, String nameDisplayed, boolean defaultChoice) {
		super(name, nameDisplayed);
		this.defaultChoice = defaultChoice;
		this.chosen = defaultChoice;
	
	}
	/*
	 * retrieve the default status of the checkbox
	 */
	public boolean getDefaultChoice() {
		return defaultChoice;
	}
	/*
	 * change the default status of the checkbox
	 */
	public void setDefaultChoice(boolean defaultChoice) {
		this.defaultChoice = defaultChoice;
	}
	/*
	 * retrieve the status of the checkbox
	 * according to the input of the user
	 */
	public boolean getChosen() {
		return chosen;
	}
	/*
	 * change the status of the checkbox
	 * after the input of the user
	 */
	public void setChosen(boolean chosen) {
		this.chosen = chosen;
	}
	
	
}
