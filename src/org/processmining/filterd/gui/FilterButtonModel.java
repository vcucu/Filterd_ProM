package org.processmining.filterd.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FilterButtonModel {
	
	private PropertyChangeSupport property;
	private StringProperty name;
	private BooleanProperty selected;
	
	public FilterButtonModel() {
		String filterName = "Filter #" + Integer.toString((int) (Math.random() * 900 + 100));
		name = new SimpleStringProperty(filterName);
		selected = new SimpleBooleanProperty();
		this.property = new PropertyChangeSupport(this);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		property.addPropertyChangeListener(listener);
	}
	
	public String getName() {
		return name.get();
	}
	
	public void setName(String value) {
		this.name.set(value);
		property.firePropertyChange("FilterNameChanged", this, value);
	}
	
	public StringProperty nameProperty() {
		return name;
	}

	public boolean getSelected() {
		return selected.get();
	}

	public void setSelected(boolean value) {
		this.selected.set(value);
		property.firePropertyChange("FilterSelected", this, selected);
	}
	
	public BooleanProperty selectedProperty() {
		return selected;
	}
}
