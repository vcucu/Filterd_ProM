package org.processmining.filterd.parameters;

import java.util.List;

public class ParameterValueFromRange extends Parameter {
	private Object defaultChoice;
	private Object chosen;
	private List<Object> optionsPair;
	
	public ParameterValueFromRange(Object defaultChoice, Object chosen, List<Object> optionsPair) {
		super();
		this.defaultChoice = defaultChoice;
		this.chosen = chosen;
		this.optionsPair = optionsPair;
	}
	public Object getDefaultChoice() {
		return defaultChoice;
	}
	public void setDefaultChoice(Object defaultChoice) {
		this.defaultChoice = defaultChoice;
	}
	public Object getChosen() {
		return chosen;
	}
	public void setChosen(Object chosen) {
		this.chosen = chosen;
	}
	public List<Object> getOptionsPair() {
		return optionsPair;
	}
	public void setOptionsPair(List<Object> optionsPair) {
		this.optionsPair = optionsPair;
	}
	
	
}
