package org.processmining.filterd.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class FilterButtonModel {
	
	private PropertyChangeSupport property;
	private String text;
	private boolean selected;
	
	public FilterButtonModel() {
		this.selected = false;
		this.property = new PropertyChangeSupport(this);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		property.firePropertyChange("FilterSelected", this, selected);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		property.addPropertyChangeListener(listener);
	}
}
