package org.processmining.filterd.parameters;

import java.util.List;

public class ParameterOneFromSet {
	
	private String defaultChoice;
	private String chosen;
	private List<String> options;
	
	public ParameterOneFromSet(String defaultChoice, String chosen, List<String> options) {
		super();
		this.defaultChoice = defaultChoice;
		this.chosen = chosen;
		this.options = options;
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
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
	
}
