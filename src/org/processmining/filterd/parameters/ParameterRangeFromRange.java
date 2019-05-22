package org.processmining.filterd.parameters;

import java.util.List;

public class ParameterRangeFromRange<T> extends Parameter  {
	
	private List<T> defaultPair;
	private List<T> chosenPair;
	private List<T> optionsPair;
	private Class<T> genericTypeClass;
	
	public ParameterRangeFromRange(String name, String nameDisplayed, List<T> defaultPair, List<T> optionsPair) {
		super(name, nameDisplayed);
		this.defaultPair = defaultPair;
		this.optionsPair = optionsPair;
		this.genericTypeClass = (Class<T>) Double.TYPE;
	}
	
	public ParameterRangeFromRange(String name, String nameDisplayed, List<T> defaultPair, List<T> optionsPair, Class<T> genericTypeClass) {
		super(name, nameDisplayed);
		this.defaultPair = defaultPair;
		this.optionsPair = optionsPair;
		this.genericTypeClass = genericTypeClass;
	}
	
	public List<T> getDefaultPair() {
		return defaultPair;
	}
	public void setDefaultPair(List<T> defaultPair) {
		this.defaultPair = defaultPair;
	}
	public List<T> getChosenPair() {
		return chosenPair;
	}
	public void setChosenPair(List<T> chosenPair) {
		this.chosenPair = chosenPair;
	}
	public List<T> getOptionsPair() {
		return optionsPair;
	}
	public void setOptionsPair(List<T> optionsPair) {
		this.optionsPair = optionsPair;
	}
	
	public Class<T> getGenericTypeClass() {
		return genericTypeClass;
	}
	
}
