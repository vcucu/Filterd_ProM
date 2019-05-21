package org.processmining.filterd.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class FilterButtonListener implements PropertyChangeListener {
	
	private FilterButtonController controller;
	
	public FilterButtonListener(FilterButtonController controller) {
		this.controller = controller;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals("FilterSelected")) {
			FilterButtonModel model = (FilterButtonModel) event.getOldValue();
			// Update the filter layout based on whether it is selected or not
			if (model.isSelected()) {
				controller.showButtons();
			} else {
				controller.hideButtons();
			}
		}
	}
}