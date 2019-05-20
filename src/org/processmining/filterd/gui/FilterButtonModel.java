package org.processmining.filterd.gui;

public class FilterButtonModel {
	
	private FilterButtonController controller;
	private boolean selected;
	
	public FilterButtonModel(FilterButtonController controller) {
		this.selected = false;
		this.controller = controller;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public FilterButtonController getController() {
		return controller;
	}

	public void setController(FilterButtonController controller) {
		this.controller = controller;
	}
}
