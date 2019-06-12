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
	public boolean getChosen() {
		return chosen;
	}
	public void setChosen(boolean chosen) {
		this.chosen = chosen;
	}
	
	
}
