package org.processmining.filterd.parameters;

import java.util.List;

public class ParameterValueFromRange<T> extends Parameter {
	private T defaultChoice;
	private T chosen;
	private List<T> optionsPair;
	
	public ParameterValueFromRange(String name, String nameDisplayed, T defaultChoice, List<T> optionsPair) {
		super(name, nameDisplayed);
		this.defaultChoice = defaultChoice;
		this.optionsPair = optionsPair;
	}
	public T getDefaultChoice() {
		return defaultChoice;
	}
	public void setDefaultChoice(T defaultChoice) {
		this.defaultChoice = defaultChoice;
	}
	public T getChosen() {
		return chosen;
	}
	public void setChosen(T chosen) {
		this.chosen = chosen;
	}
	public List<T> getOptionsPair() {
		return optionsPair;
	}
	public void setOptionsPair(List<T> optionsPair) {
		this.optionsPair = optionsPair;
	}
	
	
}
