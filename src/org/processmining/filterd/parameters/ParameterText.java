package org.processmining.filterd.parameters;

public class ParameterText extends Parameter {

	private String defaultChoice;
	private String chosen;
	
	public ParameterText(String defaultChoice, String chosen) {
		super();
		this.defaultChoice = defaultChoice;
		this.chosen = chosen;
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
