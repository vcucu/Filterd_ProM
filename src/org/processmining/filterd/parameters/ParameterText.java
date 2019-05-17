package org.processmining.filterd.parameters;

public class ParameterText extends Parameter {

	private String defaultChoice;
	private String chosen;
	
	public ParameterText(String name, String nameDisplayed, String defaultChoice) {
		super(name, nameDisplayed);
		this.defaultChoice = defaultChoice;
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
