package org.processmining.filterd.parameters;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParameterText extends Parameter {

	private String defaultChoice;
	private String chosen;
	
	/**
	 * This constructor exists for importing and exporting
	 */
	public ParameterText() {
		
	}
	
	
	public ParameterText(String name, String nameDisplayed, String defaultChoice) {
		super(name, nameDisplayed);
		this.defaultChoice = defaultChoice;
		this.chosen = defaultChoice;
	}
	/**
	 * 
	 * @return default string
	 */
	public String getDefaultChoice() {
		return defaultChoice;
	}
	/**
	 * Setter for the default string
	 * @param defaultChoice
	 */
	public void setDefaultChoice(String defaultChoice) {
		this.defaultChoice = defaultChoice;
	}
	/**
	 * 
	 * @return the inserted String
	 */
	public String getChosen() {
		return chosen;
	}
	/**
	 * Setter for chosen Text
	 * @param chosen
	 */
	public void setChosen(String chosen) {
		this.chosen = chosen;
	}
	
}
