package org.processmining.filterd.parameters;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.processmining.filterd.gui.adapters.GenericTypeClassAdapter;

/**
 * Class representing a value from range parameter. This parameter gives the
 * user a range in which she can choose a single value (i.e. a value from a
 * range). Its UI counter part is slider with one thumbs.
 * 
 * @param <T>
 *            type of this parameter (can be either Integer or Double)
 */
@XmlRootElement
public class ParameterValueFromRange<T> extends Parameter {
	private T defaultChoice; // default value 
	private T chosen; // currently chosen value
	private List<T> optionsPair; // minimum and maximum values
	@XmlElement
	@XmlJavaTypeAdapter(GenericTypeClassAdapter.class)
	private Class<T> genericTypeClass; // type of the generic class (T) for this object cannot be determined at runtime, so it passed in the constructor

	/**
	 * This constructor exists for importing and exporting
	 */
	public ParameterValueFromRange() {
	}

	/**
	 * Default constructor for this class. It does not take a genericTypeClass
	 * parameter, so its value is set to Double (default).
	 * 
	 * @param name
	 *            unique identifier of the parameter (used to map parameters and
	 *            parameter controllers in populate methods of filter
	 *            configurations)
	 * @param nameDisplayed
	 *            description of this parameter, used in the UI
	 * @param defaultPair
	 *            value selected by default
	 * @param optionsPair
	 *            minimum and maximum values
	 */
	public ParameterValueFromRange(String name, String nameDisplayed, T defaultChoice, List<T> optionsPair) {
		super(name, nameDisplayed);
		this.defaultChoice = defaultChoice;
		this.optionsPair = optionsPair;
		this.genericTypeClass = (Class<T>) Double.TYPE; // default value of genericTypeClass
		this.chosen = defaultChoice; // set the default values to be the chosen values
	}

	/**
	 * Default constructor for this class.
	 * 
	 * @param name
	 *            unique identifier of the parameter (used to map parameters and
	 *            parameter controllers in populate methods of filter
	 *            configurations)
	 * @param nameDisplayed
	 *            description of this parameter, used in the UI
	 * @param defaultPair
	 *            value selected by default
	 * @param optionsPair
	 *            minimum and maximum values
	 * @param genericTypeClass
	 *            type of the generic class for this object
	 */
	public ParameterValueFromRange(String name, String nameDisplayed, T defaultChoice, List<T> optionsPair,
			Class<T> genericTypeClass) {
		super(name, nameDisplayed);
		this.defaultChoice = defaultChoice;
		this.optionsPair = optionsPair;
		this.genericTypeClass = genericTypeClass;
		this.chosen = defaultChoice; // set the default values to be the chosen values
	}

	public T getDefaultChoice() {
		return defaultChoice;
	}

	public void setDefaultChoice(T defaultChoice) {
		this.defaultChoice = defaultChoice;
	}

	/**
	 * Getter for the currently selected value.
	 * 
	 * @return currently selected value
	 */
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

	public Class<T> getGenericTypeClass() {
		return genericTypeClass;
	}
}
