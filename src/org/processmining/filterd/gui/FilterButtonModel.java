package org.processmining.filterd.gui;

import javax.xml.bind.annotation.XmlElement;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.tools.EmptyLogException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model for the filter buttons. This component contains all the data
 * associated with a filter button.
 */
public class FilterButtonModel {

	private StringProperty name; // name of the filter button (javafx property so that it can be binded)
	private int index; // index of the filter button in the filter button list
	private BooleanProperty selected; // is the filter button selected (javafx property so that it can be binded)
	private FilterdAbstractConfig filterConfig; // filter configuration that this button is associated with
	private XLog inputLog; // input log for the filter
	private XLog outputLog; // output log of the filter
	private BooleanProperty isValid; // is the filter configuration valid (javafx property so that it can be binded)
	private BooleanProperty isEditDisabled; // is the edit button disabled (javafx property so that it can be binded)

	/**
	 * Constructor for importing/exporting. This constructor needs to exist
	 * because JAXB needs a no-argument constructor for unmarshalling.
	 * Properties set here could be overwritten during loading.
	 */
	public FilterButtonModel() {
		name = new SimpleStringProperty("New filter");
		selected = new SimpleBooleanProperty(false);
		isValid = new SimpleBooleanProperty(true);
		isEditDisabled = new SimpleBooleanProperty(true);
	}

	public FilterButtonModel(int index) {
		name = new SimpleStringProperty("New filter");
		selected = new SimpleBooleanProperty(false);
		isValid = new SimpleBooleanProperty(true);
		isEditDisabled = new SimpleBooleanProperty(true);
		this.index = index;
		this.outputLog = null;
	}

	/**
	 * Getter for the name of the filter button.
	 * 
	 * @return name of the filter button
	 */
	public String getName() {
		return name.get();
	}

	/**
	 * Getter for the name JavaFX property
	 * 
	 * @return name JavaFX property
	 */
	public StringProperty getNameProperty() {
		return name;
	}

	/**
	 * Setter for the name of the filter button.
	 * 
	 * @param value
	 *            new name of the filter button
	 */
	public void setName(String value) {
		this.name.set(value);
	}

	/**
	 * Getter for the isEditDisabled boolean variable
	 * 
	 * @return boolean stating whether edit button is disabled
	 */
	public boolean isEditDisabled() {
		return this.isEditDisabled.get();
	}

	/**
	 * Setter for the isEditDisabled boolean variable
	 * 
	 * @param isEditDisabled
	 *            boolean stating whether edit button is disabled
	 */
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

	/**
	 * Setter for the output log of the filter.
	 * 
	 * @param outputLog output log of the filter
	 */
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
		if (inputLog == null) {
			throw new NullPointerException("Input log is null. Upstream filter was not computed.");
		}
		// if the input log is empty, setLog will throw an exception 
		try {
			filterConfig.setLog(inputLog);
		} catch (EmptyLogException e) {
			throw e; // throw exception to notify the computation cell (it will invalidate this filter button)
		}
		// if the filter config. is not valid, we have to inform the cell controller (throw an exception)
		if (filterConfig.isValid()) {
			this.outputLog = filterConfig.filter(); // compute
		} else {
			throw new InvalidConfigurationException("Configuration became invalid.", this); // throw exception to notify the computation cell (it will invalidate this filter button)
		}
	}

	public BooleanProperty isValidProperty() {
		return this.isValid;
	}
}