package org.processmining.filterd.parameters;

public class ParameterYesNo extends Parameter {
	
	private boolean defaultChoice;
	private boolean chosen;
	
	public ParameterYesNo(String name, String nameDisplayed, boolean defaultChoice) {
		super(name, nameDisplayed);
		this.defaultChoice = defaultChoice;
	
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
