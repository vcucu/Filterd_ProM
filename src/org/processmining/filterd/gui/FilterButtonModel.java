package org.processmining.filterd.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FilterButtonModel {
	
	private PropertyChangeSupport property;
	private StringProperty name;
	private IntegerProperty index;
	private BooleanProperty selected;
	
	public FilterButtonModel() {
		String filterName = "Filter #" + Integer.toString((int) (Math.random() * 900 + 100));
		name = new SimpleStringProperty(filterName);
		selected = new SimpleBooleanProperty(false);
		index = new SimpleIntegerProperty();
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

	public int getIndex() {
		return index.get();
	}

	public void setIndex(int value) {
		this.index.set(value);
	}
	
	public IntegerProperty indexProperty() {
		return index;
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
