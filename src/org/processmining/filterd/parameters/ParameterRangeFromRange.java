package org.processmining.filterd.parameters;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.processmining.filterd.gui.adapters.GenericTypeClassAdapter;

@XmlRootElement
public class ParameterRangeFromRange<T> extends Parameter  {
	
	private List<T> defaultPair;
	private List<T> chosenPair;
	private List<T> optionsPair;
	@XmlElement
	@XmlJavaTypeAdapter(GenericTypeClassAdapter.class)
	private Class<T> genericTypeClass;
	private ArrayList<String> times; // for timeframe
	
	/**
	 * This constructor exists for importing and exporting
	 */
	public ParameterRangeFromRange() {
	}
	
	public ParameterRangeFromRange(String name, String nameDisplayed, List<T> defaultPair, List<T> optionsPair) {
		super(name, nameDisplayed);
		this.defaultPair = defaultPair;
		this.optionsPair = optionsPair;
		this.chosenPair = defaultPair;
		this.genericTypeClass = (Class<T>) Double.TYPE;
		this.times = new ArrayList<>();
	}
	
	public ParameterRangeFromRange(String name, String nameDisplayed, List<T> defaultPair, List<T> optionsPair, Class<T> genericTypeClass) {
		super(name, nameDisplayed);
		this.defaultPair = defaultPair;
		this.optionsPair = optionsPair;
		this.chosenPair = defaultPair;
		this.genericTypeClass = genericTypeClass;
		this.times = new ArrayList<>();
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
	
	public void setTimes(ArrayList<String> times) {
		this.times = times;
	}
	
	public ArrayList<String> getTimes() {
		return this.times;
	}
	
}
