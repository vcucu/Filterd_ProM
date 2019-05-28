package org.processmining.filterd.parameters;

import java.util.List;

public class ParameterMultipleFromSet extends Parameter {

	private List<String> defaultChoice;
	private List<String> chosen;
	private List<String> options;
	
	public ParameterMultipleFromSet(String name, String nameDisplayed, List<String> defaultChoice, List<String> options) {
		super(name, nameDisplayed);
		this.defaultChoice = defaultChoice;
		this.options = options;
		this.chosen = defaultChoice;
	}
	
	public List<String> getDefaultChoice() {
		return defaultChoice;
	}
	public void setDefaultChoice(List<String> defaultChoice) {
		this.defaultChoice = defaultChoice;
	}
	public List<String> getChosen() {
		return chosen;
	}
	public void setChosen(List<String> chosen) {
		this.chosen = chosen;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}

}
