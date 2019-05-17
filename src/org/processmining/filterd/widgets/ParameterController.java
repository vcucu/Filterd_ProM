package org.processmining.filterd.widgets;

import javafx.scene.layout.VBox;

public abstract class ParameterController {
	protected VBox contents;
	protected String name;
	
	public ParameterController(String name) {
		this.name = name;
	}
	
	public VBox getContents() {
		return contents;
	}
	
	public String getName() {
		return name;
	}
}
