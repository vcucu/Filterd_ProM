package org.processmining.filterd.parameters;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class representing a one from set parameter. This parameter gives the user
 * specific options and he can choose a single one. Its UI counter part is a
 * dropdown.
 */
@XmlRootElement
public class ParameterOneFromSet extends Parameter {

	private String defaultChoice; // option selected by default
	private String chosen; // currently selected option
	private List<String> options; // list of all available options
	@XmlElement
	private boolean createsReference; // boolean stating whether the change of this parameter influences a creation of a referenceable filter configuration

	/**
	 * This constructor exists for importing and exporting
	 */
	public ParameterOneFromSet() {
	}

	/**
	 * Default constructor for this class. It does not take a createsReference
	 * parameter, so its value is set to false (default).
	 * 
	 * @param name
	 *            unique identifier of the parameter (used to map parameters and
	 *            parameter controllers in populate methods of filter
	 *            configurations)
	 * @param nameDisplayed
	 *            description of this parameter, used in the UI
	 * @param defaultChoice
	 *            option that is selected by default
	 * @param options
	 *            list of all options available
	 */
	public ParameterOneFromSet(String name, String nameDisplayed, String defaultChoice, List<String> options) {
		super(name, nameDisplayed);
		this.defaultChoice = defaultChoice;
		this.options = options;
		this.chosen = defaultChoice; // set the default values to be the chosen values
		this.createsReference = false; // default value of createsReference
	}

	/**
	 * Default constructor for this class. If you want a parameter that creates
	 * reference, this constructor should be used!
	 * 
	 * @param name
	 *            unique identifier of the parameter (used to map parameters and
	 *            parameter controllers in populate methods of filter
	 *            configurations)
	 * @param nameDisplayed
	 *            description of this parameter, used in the UI
	 * @param defaultChoice
	 *            option that is selected by default
	 * @param options
	 *            list of all options available
	 * @param createsReference
	 *            boolean stating whether this parameter should create a
	 *            reference
	 */
	public ParameterOneFromSet(String name, String nameDisplayed, String defaultChoice, List<String> options,
			boolean createsReference) {
		super(name, nameDisplayed);
		this.defaultChoice = defaultChoice;
		this.options = options;
		this.chosen = defaultChoice; // set the default values to be the chosen values
		this.createsReference = createsReference;
	}

	public String getDefaultChoice() {
		return defaultChoice;
	}

	public void setDefaultChoice(String defaultChoice) {
		this.defaultChoice = defaultChoice;
	}

	/**
	 * Getter for the chosen option.
	 * 
	 * @return chosen option
	 */
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
