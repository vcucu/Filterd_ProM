package org.processmining.filterd.parameters;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.processmining.filterd.gui.adapters.GenericTypeClassAdapter;

@XmlRootElement
public class ParameterValueFromRange<T> extends Parameter {
	private T defaultChoice;
	private T chosen;
	private List<T> optionsPair;
	@XmlElement
	@XmlJavaTypeAdapter(GenericTypeClassAdapter.class)
	private Class<T> genericTypeClass;
	
	/**
	 * This constructor exists for importing and exporting
	 */
	public ParameterValueFromRange() {
	}
	
	/**
	 * This constructor is to be used whe the generic class is not specified
	 * which sets it to Double
	 * 
	 * @param name
	 * @param nameDisplayed
	 * @param defaultChoice
	 * @param optionsPair
	 */
	public ParameterValueFromRange(String name, String nameDisplayed, 
			T defaultChoice, List<T> optionsPair) {
		super(name, nameDisplayed);
		this.defaultChoice = defaultChoice;
		this.optionsPair = optionsPair;
		this.genericTypeClass = (Class<T>) Double.TYPE;
		this.chosen = defaultChoice;
	}
	
	/**
	 * This constructor shall be used to specify the generic class
	 * @param name
	 * @param nameDisplayed
	 * @param defaultChoice
	 * @param optionsPair
	 * @param genericTypeClass
	 */
	public ParameterValueFromRange(String name, String nameDisplayed,
			T defaultChoice, List<T> optionsPair, Class<T> genericTypeClass) {
		super(name, nameDisplayed);
		this.defaultChoice = defaultChoice;
		this.optionsPair = optionsPair;
		this.genericTypeClass = genericTypeClass;
		this.chosen = defaultChoice;
	}
	
	
	/**
	 * 
	 * @return default value
	 */
	public T getDefaultChoice() {
		return defaultChoice;
	}
	/**
	 * Setter for the default value
	 * @param defaultChoice
	 */
	public void setDefaultChoice(T defaultChoice) {
		this.defaultChoice = defaultChoice;
	}
	/**
	 * 
	 * @return chosen value
	 */
	public T getChosen() {
		return chosen;
	}
	/**
	 * Setter for the chosen value
	 * @param chosen
	 */
	public void setChosen(T chosen) {
		this.chosen = chosen;
	}
	/**
	 * 
	 * @return list with two values speciying the range form which one can choose
	 */
	public List<T> getOptionsPair() {
		return optionsPair;
	}
	/**
	 * Setter for the range from which the user can choose
	 * @param optionsPair
	 */
	public void setOptionsPair(List<T> optionsPair) {
		this.optionsPair = optionsPair;
	}
	
	public Class<T> getGenericTypeClass() {
		return genericTypeClass;
	}
}
