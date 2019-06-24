package org.processmining.filterd.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CellModelListeners implements PropertyChangeListener {

	CellController controller;
	CellStatus status;

	public CellModelListeners(CellController controller) {
		super();
		this.controller = controller;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals("setHidden")) {
			collapseCell(event);
		}else if (event.getPropertyName().equals("setCellStatus")) {
			updateCellStatus(event);
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
			controller.hide();
		} else {
			controller.show();
		}
	}


	/**
	 * Update the statusBar of the cell by color corresponding to the newValue of event
	 * @param event
	 */
	private void updateCellStatus(PropertyChangeEvent event) {
		CellStatus status = (CellStatus)event.getNewValue();
		switch (status) {
			case IDLE : controller.changeStatus("-fx-background-color: #67A767;");
			break;
			case IN_PROGRESS: controller.changeStatus("-fx-background-color: #DBB980;");
			break;
			case INVALID: controller.changeStatus("-fx-background-color: #C25D5D;");
			break;
			case OUT_OF_DATE: 
				controller.changeStatus("-fx-background-color: #799EA4;");
			break;
		}
	}

	/**
	 * Changes name of cell in UI
	 *
	 * @param eventchange
	 *            event fired by the CellName property
	 */
	private void updateCellName(PropertyChangeEvent event) {
		controller.changeCellName((String) event.getNewValue());
	}
}
