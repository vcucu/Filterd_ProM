package org.processmining.filterd.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CellModelListeners implements PropertyChangeListener {

	CellController controller;

	public CellModelListeners(CellController controller) {
		super();
		this.controller = controller;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals("setHidden")) {
			collapseCell(event);
		} else if (event.getPropertyName().equals("setCellName")) {
			updateCellName(event);
		}
	}

	/**
	 * Collapses cell in UI
	 * 
	 * @param event
	 *            change event fired by the isHidden property update
	 */
	private void collapseCell(PropertyChangeEvent event) {
		if (((Boolean) event.getOldValue()) == false) {
			//System.out.println("This cell should collapse!");
			controller.hide();
		} else {
			//System.out.println("This cell should expand!");
			controller.show();
		}
	}

	/**
	 * Changes name of cell in UI
	 * 
	 * @param eventchange
	 *            event fired by the CellName property
	 */
	private void updateCellName(PropertyChangeEvent event) {
		//System.out.println("cell name is:" + (String) event.getNewValue());
		controller.changeCellName((String) event.getNewValue());
	}
}