package org.processmining.filterd.widgets;

import javafx.scene.layout.VBox;

public abstract class ParameterController {
	protected VBox contents;
	protected String id;
	
	public ParameterController(String id) {
		this.id = id;
	}
	
	public VBox getContents() {
		return contents;
	}
}
