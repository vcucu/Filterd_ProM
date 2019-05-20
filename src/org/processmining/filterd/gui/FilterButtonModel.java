package org.processmining.filterd.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class FilterButtonModel {
	
	private PropertyChangeSupport property;
	private String text;
	
	private FilterButtonController controller;
	private boolean selected;
	
	public FilterButtonModel(FilterButtonController controller) {
		this.selected = false;
		this.controller = controller;
		this.property = new PropertyChangeSupport(this);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		property.firePropertyChange("FilterSelected", false, selected);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		property.addPropertyChangeListener(listener);
	}

	public FilterButtonController getController() {
		return controller;
	}

	public void setController(FilterButtonController controller) {
		this.controller = controller;
	}
}
