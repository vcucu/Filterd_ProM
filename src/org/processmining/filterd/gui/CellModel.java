package org.processmining.filterd.gui;

import java.beans.PropertyChangeSupport;

import org.processmining.contexts.uitopia.UIPluginContext;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CellModel {

	private UIPluginContext context;
	private boolean isHidden;
	private CellStatus statusBar;
	protected StringProperty cellName;
	protected int index;
	//property used to register property listeners for each bound property
	protected PropertyChangeSupport property;

	/**
	 * Constructor for importing/exporting. This constructor needs to exist because JAXB needs a no-argument constructor for unmarshalling.
	 * Properties set here could be overwritten during loading.
	 */
	public CellModel() {
		this.property = new PropertyChangeSupport(this);
		isHidden = false;
		setStatusBar(CellStatus.IDLE); // set the initial cell status to idle
		cellName = new SimpleStringProperty(); // initialize the cellName
		setCellName("Cell #" + Integer.toString((int) (Math.random() * 900 + 100))); // assign an initial name to the cell
	}
	
	public CellModel(UIPluginContext context, int index) {
		this.context = context;
		//adding property to register all change listeners to all bounded properties of the model
		this.property = new PropertyChangeSupport(this);
		this.index = index;
		isHidden = false;
		setStatusBar(CellStatus.IDLE); // set the initial cell status to idle
		cellName = new SimpleStringProperty(); // initialize the cellName
		setCellName("Cell #" + Integer.toString((int) (Math.random() * 900 + 100))); // assign an initial name to the cell
	}
	
	/**
	 * Binds StringProperty to the cell name so they will always contain the same value.
	 * @param stringProperty The variable to bind to the cell name.
	 */
	public void bindCellName(StringProperty stringProperty) {
		cellName.bindBidirectional(stringProperty);
	}

	public boolean isHidden() {
		return isHidden;
	}

	//enables the controller to get the PropertyChangeSupport of the CellModel bean. CellController can then add
	//listeners to each attribute in the bean that fires a change event and update view accordingly
	public PropertyChangeSupport getProperty() {
		return property;
	}

	public void setHidden(boolean isHidden) {
		boolean oldState = this.isHidden;
		this.isHidden = isHidden;
		//System.out.println("setHidden in cell model with hidden value:" + isHidden + " and old value" + oldState);
		property.firePropertyChange("setHidden", oldState, isHidden);
	}

	public CellStatus getStatusBar() {
		return statusBar;
	}

	public void setStatusBar(CellStatus statusBar) {
		this.statusBar = statusBar;
	}

	/**
	 * Returns the string value contained in the StringProperty cellName. Corresponds to the text in the cell name of the Cell.
	 * @return THe string value contained in the StringProperty.
	 */
	public String getCellName() {
		return cellName.getValue();
	}
	
	public StringProperty cellNameProperty() {
		return this.cellName;
	}

	/**
	 * Sets cellName of cell model to cellName and fires a change event
	 * 
	 * @param cellName The name to give to the cell
	 */
	public void setCellName(String cellName) {
		this.cellName.setValue(cellName);
	}

	public UIPluginContext getContext() {
		return context;
	}
	
	/**
	 * Sets the ProM context. Used when loading a notebook from File.
	 * @param context
	 */
	public void setContext(UIPluginContext context) {
		this.context = context;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}