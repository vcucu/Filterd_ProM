package org.processmining.filterd.gui;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.tools.EmptyLogException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@XmlAccessorType(XmlAccessType.NONE) // Makes sure only explicitly named elements get added to the XML.
@XmlRootElement(name = "FilterButtonModel") // Needed by JAXB to generate an XML.
public class FilterButtonModel {
	
	private StringProperty name;
	@XmlElement
	private int index;
	private BooleanProperty selected;
	//@XmlElement
	private FilterdAbstractConfig filterConfig;
	private XLog inputLog;
	private XLog outputLog;
	private BooleanProperty isValid;
	private BooleanProperty isEditDisabled;
	
	/**
	 * Constructor for importing/exporting. This constructor needs to exist because JAXB needs a no-argument constructor for unmarshalling.
	 * Properties set here could be overwritten during loading.
	 */
	public FilterButtonModel() {
		String filterName = "Filter #" + Integer.toString((int) (Math.random() * 900 + 100));
		name = new SimpleStringProperty(filterName);
		selected = new SimpleBooleanProperty(false);
		isValid = new SimpleBooleanProperty(true);
		isEditDisabled = new SimpleBooleanProperty(true);
	}	
	
	public FilterButtonModel(int index) {
		String filterName = "Filter #" + Integer.toString((int) (Math.random() * 900 + 100));
		name = new SimpleStringProperty(filterName);
		selected = new SimpleBooleanProperty(false);
		isValid = new SimpleBooleanProperty(true);
		isEditDisabled = new SimpleBooleanProperty(true);
		this.index = index;
		this.outputLog = null;
	}
	
	@XmlElement
	public String getName() {
		return name.get();
	}
	
	public StringProperty getNameProperty() {
		return name;
	}
	
	public void setName(String value) {
		this.name.set(value);
	}
	
	public boolean isEditDisabled() {
		return this.isEditDisabled.get();
	}
	
	public void setIsEditDisabled(boolean isEditDisabled) {
		this.isEditDisabled.set(isEditDisabled);
	}
	
	public BooleanProperty isEditDisabledProperty() {
		return this.isEditDisabled;
	}
	
	public XLog getInputLog() {
		return this.inputLog;
	}
	
	public void setInputLog(XLog inputLog) {
		this.inputLog = inputLog;
	}
	
	public XLog getOutputLog() {
		return this.outputLog;
	}
	
	public void setOutputLog(XLog outputLog) {
		this.outputLog = outputLog;
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

	@XmlElement
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
	
	/**
	 * Compute the output of this filter.
	 */
	public void compute() {
		// check that the upstream filters have finished computation
		if(inputLog == null) {
			throw new NullPointerException("Input log is null. Upstream filter was not computed.");
		}
		// if the input log is empty, setLog will throw an exception 
		try {
			filterConfig.setLog(inputLog);
		} catch(EmptyLogException e) {
			throw e; // throw exception to notify the computation cell (it will invalidate this filter button)
		}
		// if the filter config. is not valid, we have to inform the cell controller (throw an exception)
		if(filterConfig.isValid()) {
			this.outputLog = filterConfig.filter(); // compute
		} else {
			throw new InvalidConfigurationException("Configuration became invalid.", this); // throw exception to notify the computation cell (it will invalidate this filter button)
		}
	}
	
	public BooleanProperty isValidProperty() {
		return this.isValid;
	}
}