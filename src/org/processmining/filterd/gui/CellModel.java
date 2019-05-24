package org.processmining.filterd.gui;

import java.beans.PropertyChangeSupport;

import org.processmining.contexts.uitopia.UIPluginContext;

public class CellModel {

	private boolean isHidden;
	private UIPluginContext context;
	private CellStatus statusBar;
	private String cellName;
	//property used to register property listeners for each bound property
	protected PropertyChangeSupport property;

	public CellModel(UIPluginContext context) {
		this.context = context;
		//adding property to register all change listeners to all bounded properties of the model
		this.property = new PropertyChangeSupport(this);
		isHidden = false;
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

	public String getCellName() {
		return cellName;
	}

	/**
	 * Sets cellName of cell model to cellName and fires a change event
	 * 
	 * @param cellName
	 */
	public void setCellName(String cellName) {
		String oldState = this.cellName;
		this.cellName = cellName;
		//System.out.println("cellName in cell model with cellName value:" + cellName + " and old value" + oldState);
		property.firePropertyChange("setCellName", oldState, cellName);
		this.cellName = cellName;
	}

	public UIPluginContext getContext() {
		return context;
	}
}