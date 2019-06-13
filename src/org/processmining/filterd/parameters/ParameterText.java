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
	public String getDefaultChoice() {
		return defaultChoice;
	}
	public void setDefaultChoice(String defaultChoice) {
		this.defaultChoice = defaultChoice;
	}
	public String getChosen() {
		return chosen;
	}
	public void setChosen(String chosen) {
		this.chosen = chosen;
	}
	
}
