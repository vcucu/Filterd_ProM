package org.processmining.filterd.parameters;

public class ParameterYesNo extends Parameter {
	
	private boolean defaultChoice;
	private boolean chosen;
	
	public ParameterYesNo(boolean defaultChoice, boolean chosen) {
		super();
		this.defaultChoice = defaultChoice;
		this.chosen = chosen;
	}
	public boolean isDefaultChoice() {
		return defaultChoice;
	}
	public void setDefaultChoice(boolean defaultChoice) {
		this.defaultChoice = defaultChoice;
	}
	public boolean isChosen() {
		return chosen;
	}
	public void setChosen(boolean chosen) {
		this.chosen = chosen;
	}
	
	
}
