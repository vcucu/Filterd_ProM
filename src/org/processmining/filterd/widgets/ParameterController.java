package org.processmining.filterd.widgets;

import javafx.scene.layout.VBox;

/**
 * Abstract class for all parameter controllers (UI counterparts of filter parameters). 
 * 
 * @author Filip Davidovic
 */
public abstract class ParameterController {
	protected VBox contents; // UI container for parameter controls 
	protected String name; // unique identifier of the parameter (used to map parameter controllers to actual parameters while populating)
	
	/**
	 * Only constructor which sets the parameter name.
	 * 
	 * @param name  parameter name
	 */
	public ParameterController(String name) {
		this.name = name;
	}
	
	/**
	 * Getter for the contents container.
	 * 
	 * @return  contents container
	 */
	public VBox getContents() {
		return contents;
	}
	
	/**
	 * Getter for the unique identifier.
	 * 
	 * @return  unique identifier
	 */
	public String getName() {
		return name;
	}
}