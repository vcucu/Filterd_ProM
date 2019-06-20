package org.processmining.filterd.parameters;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.processmining.filterd.gui.adapters.GenericTypeClassAdapter;

/**
 * Class representing a range from range parameter. This parameter gives the
 * user a range in which she can choose the low and high values (i.e. a range
 * from a range). Its UI counter part is slider with two thumbs.
 * 
 * @param <T>
 *            type of this parameter (can be either Integer or Double)
 */
@XmlRootElement
public class ParameterRangeFromRange<T> extends Parameter {

	private List<T> defaultPair; // default low ([0]) and high ([1]) values
	private List<T> chosenPair; // currently chosen low ([0]) and high ([1]) values
	private List<T> optionsPair; // minimum and maximum values 
	@XmlElement
	@XmlJavaTypeAdapter(GenericTypeClassAdapter.class)
	private Class<T> genericTypeClass; // type of the generic class (T) for this object cannot be determined at runtime, so it passed in the constructor 
	private ArrayList<String> times; // for timeframe

	/**
	 * This constructor exists for importing and exporting
	 */
	public ParameterRangeFromRange() {
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
	 *            low and high values selected by default
	 * @param optionsPair
	 *            minimum and maximum values
	 */
	public ParameterRangeFromRange(String name, String nameDisplayed, List<T> defaultPair, List<T> optionsPair) {
		super(name, nameDisplayed);
		this.defaultPair = defaultPair;
		this.optionsPair = optionsPair;
		this.chosenPair = defaultPair; // set the default values to be the chosen values
		this.genericTypeClass = (Class<T>) Double.TYPE; // default value of genericTypeClass
		this.times = new ArrayList<>();
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
	 *            low and high values selected by default
	 * @param optionsPair
	 *            minimum and maximum values
	 * @param genericTypeClass
	 *            type of the generic class for this object
	 */
	public ParameterRangeFromRange(String name, String nameDisplayed, List<T> defaultPair, List<T> optionsPair,
			Class<T> genericTypeClass) {
		super(name, nameDisplayed);
		this.defaultPair = defaultPair;
		this.optionsPair = optionsPair;
		this.chosenPair = defaultPair; // set the default values to be the chosen values
		this.genericTypeClass = genericTypeClass;
		this.times = new ArrayList<>();
	}

	public List<T> getDefaultPair() {
		return defaultPair;
	}

	public void setDefaultPair(List<T> defaultPair) {
		this.defaultPair = defaultPair;
	}

	/**
	 * Getter for the chosen pair of values.
	 * 
	 * @return chosen pair of values
	 */
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