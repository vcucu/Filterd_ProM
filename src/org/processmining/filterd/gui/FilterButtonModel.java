package org.processmining.filterd.gui;

import org.processmining.filterd.configurations.FilterdAbstractConfig;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FilterButtonModel {
	
	private StringProperty name;
	private IntegerProperty index;
	private BooleanProperty selected;
	private FilterdAbstractConfig filterConfig;
	
	public FilterButtonModel(int value) {
		String filterName = "Filter #" + Integer.toString((int) (Math.random() * 900 + 100));
		name = new SimpleStringProperty(filterName);
		selected = new SimpleBooleanProperty(false);
		index = new SimpleIntegerProperty(value);
	}
	
	public String getName() {
		return name.get();
	}
	
	public void setName(String value) {
		this.name.set(value);
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
	}
	
	public BooleanProperty selectedProperty() {
		return selected;
	}
	
	public FilterdAbstractConfig getFilterConfig() {
		return this.filterConfig;
	}
	
	public void setFilterConfig(FilterdAbstractConfig filterConfig) {
		this.filterConfig = filterConfig;
	}
}