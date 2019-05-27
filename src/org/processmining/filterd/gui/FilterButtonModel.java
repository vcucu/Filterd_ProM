package org.processmining.filterd.gui;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.models.YLog;
import org.processmining.filterd.tools.Toolbox;

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
	private YLog inputLog;
	private YLog outputLog;
	
	public FilterButtonModel(int value, YLog inputLog) {
		String filterName = "Filter #" + Integer.toString((int) (Math.random() * 900 + 100));
		name = new SimpleStringProperty(filterName);
		selected = new SimpleBooleanProperty(false);
		index = new SimpleIntegerProperty(value);
		this.inputLog = inputLog;
		this.outputLog = new YLog(Toolbox.getNextId(), filterName + " output log", inputLog.get());
	}
	
	public String getName() {
		return name.get();
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
	
	public void compute() {
		if(inputLog.get() == null) {
			throw new IllegalStateException("Input log is null. Upstream filter was not computed.");
		}
		filterConfig.setLog(inputLog.get());
		if(filterConfig.isValid()) {
			// compute
			XLog output = filterConfig.filter(null);
			outputLog.setLog(output);
		} else {
			// throw exception to notify the user that the computation could not be completed
			throw new InvalidConfigurationException("Configuration became invalid", this);
		}
	}
}