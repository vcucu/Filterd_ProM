package org.processmining.filterd.parameters;

import javax.xml.bind.annotation.XmlRootElement;

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
	 * Constructor for the parameter corresponding to a binary selection 
	 * @param name
	 * @param nameDisplayed
	 * @param defaultChoice
	 */
	public ParameterYesNo(String name, String nameDisplayed, boolean defaultChoice) {
		super(name, nameDisplayed);
		this.defaultChoice = defaultChoice;
		this.chosen = defaultChoice;
	
	}
	/**
	 * 
	 * @return default selection defaultChoice
	 */
	public boolean getDefaultChoice() {
		return defaultChoice;
	}
	/**
	 * Setter for the defaultChoice
	 * @param defaultChoice
	 */
	public void setDefaultChoice(boolean defaultChoice) {
		this.defaultChoice = defaultChoice;
	}
	/**
	 * 
	 * @return the chosen value
	 */
	public boolean getChosen() {
		return chosen;
	}
	/**
	 * Setter for the chosen value
	 * @param chosen
	 */
	public void setChosen(boolean chosen) {
		this.chosen = chosen;
	}
	
	
}
