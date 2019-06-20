package org.processmining.filterd.parameters;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
/*
 * Class representing a text parameter. It is represented
 * by a text box in the UI.
 */
public class ParameterText extends Parameter {

	private String defaultChoice;
	private String chosen;
	
	/**
	 * This constructor exists for importing and exporting
	 */
	public ParameterText() {
		
	}
	/*
	 * constructor of the parameter
	 */
	public ParameterText(String name, String nameDisplayed, String defaultChoice) {
		super(name, nameDisplayed);
		this.defaultChoice = defaultChoice;
		this.chosen = defaultChoice;
	}
	/*
	 * the default text which should appear
	 * in the text box
	 */
	public String getDefaultChoice() {
		return defaultChoice;
	}
	/*
	 * change the default text which should appear
	 * in the text box
	 */
	public void setDefaultChoice(String defaultChoice) {
		this.defaultChoice = defaultChoice;
	}
	/*
	 * retrieve the text value set by the user
	 * in the UI
	 */
	public String getChosen() {
		return chosen;
	}
	/*
	 * change the text value set by the user
	 * in the UI
	 */
	public void setChosen(String chosen) {
		this.chosen = chosen;
	}
	
}
