package org.processmining.filterd.parameters;

public abstract class Parameter {
	
	private String nameDisplayed;
	private String explanation;
	private String name;
	
	public String getNameDisplayed() {
		return nameDisplayed;
	}
	public void setNameDisplayed(String nameDisplayed) {
		this.nameDisplayed = nameDisplayed;
	}
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
