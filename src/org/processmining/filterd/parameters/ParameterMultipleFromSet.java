package org.processmining.filterd.parameters;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParameterMultipleFromSet extends Parameter {

	private List<String> defaultChoice;
	private List<String> chosen;
	private List<String> options;
	
	/**
	 * This constructor exists for importing and exporting
	 */
	public ParameterMultipleFromSet() {
		
	}
	
	public ParameterMultipleFromSet(String name, String nameDisplayed, List<String> defaultChoice, List<String> options) {
		super(name, nameDisplayed);
		//default selection from the options
		this.defaultChoice = defaultChoice;
		//options provided to select from
		this.options = options;
		//chosen stores the choice of the user in the UI, initialized to the default
		this.chosen = defaultChoice;
	}
	/**
	 * 
	 * @return List of default choices
	 */
	public List<String> getDefaultChoice() {
		return defaultChoice;
	}
	
	/**
	 * Setter for the default choice
	 * @param defaultChoice
	 */
	public void setDefaultChoice(List<String> defaultChoice) {
		this.defaultChoice = defaultChoice;
	}
	
	/**
	 * 
	 * @return list of chosen values
	 */
	public List<String> getChosen() {
		return chosen;
	}
	
	/**
	 * Setter for chosen values
	 * @param chosen
	 */
	public void setChosen(List<String> chosen) {
		this.chosen = chosen;
	}
	/**
	 * 
	 * @return option values
	 */
	public List<String> getOptions() {
		return options;
	}
	/**
	 * Setter for the options provided to the user by the config
	 * @param options
	 */
	public void setOptions(List<String> options) {
		this.options = options;
	}

}
