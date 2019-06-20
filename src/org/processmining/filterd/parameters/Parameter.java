package org.processmining.filterd.parameters;

public abstract class Parameter {
	
	protected String nameDisplayed;
	protected String explanation;
	protected String name;
	protected boolean disappearable;
	
	/**
	 * This constructor exists for importing and exporting
	 */
	public Parameter( ) {
		
	}
	
	public Parameter(String name, String nameDisplayed) {
		//name of the parameter, using which the parameter can be
		//retrieved from the array of parameters later
		//both in configurations and in filters
		//and needs to match the name of the corresponding ui controller
		this.name = name;
		//name that will be displayed in the ui next to the 
		//controller which corresponds to this parameter
		this.nameDisplayed = nameDisplayed;
		//disappearable signifies whether paramater, depending on the chosen
		//selection within anotther parameter, appears and disappears
		//from the UI 
		this.disappearable = false;
		
	}
	//getter the name visible in the UI
	public String getNameDisplayed() {
		return nameDisplayed;
	}
	//setter the name visible in the UI
	public void setNameDisplayed(String nameDisplayed) {
		this.nameDisplayed = nameDisplayed;
	}
	//getter the textual explanation in the UI
	public String getExplanation() {
		return explanation;
	}
	//setter the textual explanation in the UI
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	//getter the name identifier of the parameter
	public String getName() {
		return name;
	}
	//setter the name identifier of the parameter
	public void setName(String name) {
		this.name = name;
	}
	//getter the disappearable property
	public boolean getDisappearable() {
		return this.disappearable;
	}
	//setter the disappearable property
	public void setDisappearable(boolean bool) {
		this.disappearable = bool;
	}
	
	/**
	 * Method allowing for comparison of a parameter to another parameter
	 * If they are of the same parameter class and they have the same name,
	 * they are considered to be equal.
	 * @param param
	 * @return true if the parameters are equal
	 */
	public boolean equals(Parameter param) {
		boolean check = false;
	    if (param == null) return false;
	    if (param == this) return true;
	    if (param.getClass() == this.getClass()) {
	    	if (param.getName().contentEquals(this.name)) {
	    		check = true;
	    	}
	    }
	    return check;
	}
	
	
	
}
