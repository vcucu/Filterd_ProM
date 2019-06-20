package org.processmining.filterd.parameters;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class representing a multiple from set parameter. This parameter gives the
 * user specific options and he can choose multiple of them. Its UI counter part
 * is a list with multiple selection enabled.
 */
@XmlRootElement
public class ParameterMultipleFromSet extends Parameter {

	private List<String> defaultChoice; // list of options selected by default
	private List<String> chosen; // list of the currently chosen options
	private List<String> options; // list of all available options

	/**
	 * This constructor exists for importing and exporting
	 */
	public ParameterMultipleFromSet() {

	}

	/**
	 * Default constructor for this class. This constructor should be used in
	 * all actual code!
	 * 
	 * @param name
	 *            unique identifier of the parameter (used to map parameters and
	 *            parameter controllers in populate methods of filter
	 *            configurations)
	 * @param nameDisplayed
	 *            description of this parameter, used in the UI
	 * @param defaultChoice
	 *            list of options that are selected by default
	 * @param options
	 *            list of all options available
	 */
	public ParameterMultipleFromSet(String name, String nameDisplayed, List<String> defaultChoice,
			List<String> options) {
		super(name, nameDisplayed);
		this.defaultChoice = defaultChoice;
		this.options = options;
		this.chosen = defaultChoice; // set the default values to be the chosen values
	}

	public List<String> getDefaultChoice() {
		return defaultChoice;
	}

	public void setDefaultChoice(List<String> defaultChoice) {
		this.defaultChoice = defaultChoice;
	}

	/**
	 * Getter for the list of chosen options.
	 * 
	 * @return list of chosen options
	 */
	public List<String> getChosen() {
		return chosen;
	}

	public void setChosen(List<String> chosen) {
		this.chosen = chosen;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

}
