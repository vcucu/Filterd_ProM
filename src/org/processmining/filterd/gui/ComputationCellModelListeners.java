package org.processmining.filterd.gui;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.processmining.filterd.models.YLog;

/**
 * Class used to handle property changes in a computation cell. This class is
 * created by the computation cell model, and listened to by the controller (it
 * is passed in the constructor). It currently handles two types of properties:
 * <ul>
 * <li>changing of the options for the input logs ("setInputLogs")</li>
 * <li>request to reload the visualizer ("reloadVisualizer")</li>
 * </ul>
 */
public class ComputationCellModelListeners extends CellModelListeners {

	public ComputationCellModelListeners(CellController controller) {
		super(controller);
	}

	/**
	 * Handler for the change of property.
	 * 
	 * @param event
	 *            event object containing the source of the change. it is not
	 *            expected to contain any other information (e.g. oldValue and
	 *            newValue)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		//check general cell property change 
		super.propertyChange(event);
		if (event.getPropertyName().equals("setInputLogs")) {
			((ComputationCellController) controller).changeInputLogsCombo((List<YLog>) event.getNewValue());
		} else if (event.getPropertyName().equals("reloadVisualizer")) {
			((ComputationCellController) controller).loadVisualizer();
		}
	}
}