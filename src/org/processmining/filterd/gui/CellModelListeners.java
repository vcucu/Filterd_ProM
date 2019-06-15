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
}