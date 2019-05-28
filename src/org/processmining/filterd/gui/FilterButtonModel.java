package org.processmining.filterd.gui;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.models.YLog;
import org.processmining.filterd.tools.Toolbox;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FilterButtonModel {
	
	private StringProperty name;
	private int index;
	private BooleanProperty selected;
	private FilterdAbstractConfig filterConfig;
	private YLog inputLog;
	private YLog outputLog;
	
	public FilterButtonModel(int index, YLog inputLog) {
		String filterName = "Filter #" + Integer.toString((int) (Math.random() * 900 + 100));
		name = new SimpleStringProperty(filterName);
		selected = new SimpleBooleanProperty(false);
		this.index = index;
		this.inputLog = inputLog;
		this.outputLog = new YLog(Toolbox.getNextId(), filterName + " output log", inputLog.get());
	}
	
	public String getName() {
		return name.get();
	}
	
	public StringProperty getNameProperty() {
		return name;
	}
	
	public void setName(String value) {
		this.name.set(value);
	}
	
	public YLog getOutputLog() {
		return this.outputLog;
	}
	
	public StringProperty nameProperty() {
		return name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean getSelected() {
		return selected.get();
	}
	
	public BooleanProperty getSelectedProperty() {
		return selected;
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
	
	public void compute() {
		if(inputLog.get() == null) {
			throw new IllegalStateException("Input log is null. Upstream filter was not computed.");
		}
		filterConfig.setLog(inputLog.get());
		if(filterConfig.isValid()) {
			// compute
			XLog output = filterConfig.filter();
			outputLog.setLog(output);
		} else {
			// throw exception to notify the user that the computation could not be completed
			throw new InvalidConfigurationException("Configuration became invalid", this);
		}
	}
}