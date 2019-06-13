package org.processmining.filterd.gui.adapters;

public class ParameterYesNoAdapted extends ParameterAdapted {

	private boolean defaultChoice;
	private boolean chosen;

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
