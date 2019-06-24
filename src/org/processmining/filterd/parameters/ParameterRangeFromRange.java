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
	
	/**
	 * COnstructor to be used when Generic type is not specified, so it is set to Double
	 * @param name
	 * @param nameDisplayed
	 * @param defaultPair
	 * @param optionsPair
	 */
	public ParameterRangeFromRange(String name, String nameDisplayed, List<T> defaultPair, List<T> optionsPair) {
		super(name, nameDisplayed);
		this.defaultPair = defaultPair;
		this.optionsPair = optionsPair;
		this.chosenPair = defaultPair;
		this.genericTypeClass = (Class<T>) Double.TYPE;
		this.times = new ArrayList<>();
	}
	
	/**
	 * Constructor to be used when generic type class needs to be specified
	 * @param name
	 * @param nameDisplayed
	 * @param defaultPair
	 * @param optionsPair
	 * @param genericTypeClass
	 */
	public ParameterRangeFromRange(String name, String nameDisplayed, List<T> defaultPair, List<T> optionsPair, Class<T> genericTypeClass) {
		super(name, nameDisplayed);
		this.defaultPair = defaultPair;
		this.optionsPair = optionsPair;
		this.chosenPair = defaultPair;
		this.genericTypeClass = genericTypeClass;
		this.times = new ArrayList<>();
	}
	
	/**
	 * 
	 * @return the range selected by default
	 */
	public List<T> getDefaultPair() {
		return defaultPair;
	}
	/**
	 * Setter for the default range
	 * @param defaultPair
	 */
	public void setDefaultPair(List<T> defaultPair) {
		this.defaultPair = defaultPair;
	}
	/**
	 * 
	 * @return The chosen range
	 */
	public List<T> getChosenPair() {
		return chosenPair;
	}
	/**
	 * Setter for the chosen range
	 * @param chosenPair
	 */
	public void setChosenPair(List<T> chosenPair) {
		this.chosenPair = chosenPair;
	}
	/**
	 * 
	 * @return List containing the minimum and maximum values that can be chosen
	 */
	public List<T> getOptionsPair() {
		return optionsPair;
	}
	/**
	 * Setter for the range containing the minimum and maximum values that can be chosen
	 * @param optionsPair
	 */
	public void setOptionsPair(List<T> optionsPair) {
		this.optionsPair = optionsPair;
	}
	
	public Class<T> getGenericTypeClass() {
		return genericTypeClass;
	}
	
	/**
	 * Setter for the times within the range used for timestamp ranges
	 * @param times
	 */
	public void setTimes(ArrayList<String> times) {
		this.times = times;
	}
	
	/**
	 * 
	 * @return  the times within the range used for timestamp ranges
	 */
	public ArrayList<String> getTimes() {
		return this.times;
	}
	
}