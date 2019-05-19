package org.processmining.filterd.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class FilterButtonListener implements PropertyChangeListener {

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals("FilterSelected")) {
			System.out.println("New filter selected !");
		}
	}
}
