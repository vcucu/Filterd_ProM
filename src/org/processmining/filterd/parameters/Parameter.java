package org.processmining.filterd.parameters;

public abstract class Parameter {
	
	protected String nameDisplayed;
	protected String explanation;
	protected String name;
	
	public Parameter(String name, String nameDisplayed) {
		this.name = name;
		this.nameDisplayed = nameDisplayed;
		
	}
	
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
