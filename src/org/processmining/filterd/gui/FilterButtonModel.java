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
 * Model for the filter buttons. This component contains all the data associated
 * with a filter button.
 */
public class FilterButtonModel {

	public StringProperty name; // name of the filter button (JavaFX property so that it can be binded)
	private int index; // index of the filter button in the filter button list
	public BooleanProperty selected; // is the filter button selected (JavaFX property so that it can be binded)
	private FilterdAbstractConfig filterConfig; // filter configuration that this button is associated with
	private XLog inputLog; // input log for the filter
	private XLog outputLog; // output log of the filter
	private BooleanProperty isValid; // is the filter configuration valid (JavaFX property so that it can be binded)
	public BooleanProperty isEditDisabled; // is the edit button disabled (JavaFX property so that it can be binded)

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
	 * Setter for the name of the filter button.
	 * 
	 * @param value
	 *            new name of the filter button
	 */
	public void setName(String value) {
		this.name.set(value);
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
	 * @param outputLog
	 *            output log of the filter
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

	public void setSelected(boolean value) {
		this.selected.set(value);
	}

	/**
	 * Getter for the selected property of this filter button. Intended to be
	 * used by controllers to set the bindings.
	 * 
	 * @return selected property of this filter button
	 */
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

	/**
	 * Getter for the valid property of this filter button. Intended to be used
	 * by controllers to set the bindings.
	 * 
	 * @return valid property of this filter button
	 */
	public BooleanProperty isValidProperty() {
		return this.isValid;
	}
}