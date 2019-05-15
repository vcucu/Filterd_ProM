package org.processmining.filterd.parameters;

import java.util.List;

public class ParameterRangeFromRange extends Parameter  {
	
	private List<Object> defaultPair;
	private List<Object> chosenPair;
	private List<Object> optionsPair;
	
	public ParameterRangeFromRange(String name, String nameDisplayed, List<Object> defaultPair, List<Object> optionsPair) {
		super(name, nameDisplayed);
		this.defaultPair = defaultPair;
		this.optionsPair = optionsPair;
	}
	public List<Object> getDefaultPair() {
		return defaultPair;
	}
	public void setDefaultPair(List<Object> defaultPair) {
		this.defaultPair = defaultPair;
	}
	public List<Object> getChosenPair() {
		return chosenPair;
	}
	public void setChosenPair(List<Object> chosenPair) {
		this.chosenPair = chosenPair;
	}
	public List<Object> getOptionsPair() {
		return optionsPair;
	}
	public void setOptionsPair(List<Object> optionsPair) {
		this.optionsPair = optionsPair;
	}
	
}
