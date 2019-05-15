package org.processmining.filterd.parameters;

import java.util.List;

public class ParameterMultipleFromSet extends Parameter {

	private String defaultChoice;
	private List<String> choice;
	private List<String> options;
	
	public ParameterMultipleFromSet(String defaultChoice, List<String> choice, List<String> options) {
		super();
		this.defaultChoice = defaultChoice;
		this.choice = choice;
		this.options = options;
	}
	
	public String getDefaultChoice() {
		return defaultChoice;
	}
	public void setDefaultChoice(String defaultChoice) {
		this.defaultChoice = defaultChoice;
	}
	public List<String> getChoice() {
		return choice;
	}
	public void setChoice(List<String> choice) {
		this.choice = choice;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}

}
