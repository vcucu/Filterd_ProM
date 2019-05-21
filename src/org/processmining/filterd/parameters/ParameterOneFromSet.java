package org.processmining.filterd.parameters;

import java.util.List;

public class ParameterOneFromSet extends Parameter  {
	
	private String defaultChoice;
	private String chosen;
	private List<String> options;
	private boolean createsReference; 
	
	public ParameterOneFromSet(String name, String nameDisplayed, String defaultChoice, List<String> options) {
		super(name, nameDisplayed);
		this.defaultChoice = defaultChoice;
		this.options = options;
		this.createsReference = false;
	}
	
	public ParameterOneFromSet(String name, String nameDisplayed, 
			String defaultChoice, List<String> options, boolean createsReference) {
		super(name, nameDisplayed);
		this.defaultChoice = defaultChoice;
		this.options = options;
		this.createsReference = createsReference;
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
	public boolean getCreatesReference() {
		return createsReference;
	}
	
}
