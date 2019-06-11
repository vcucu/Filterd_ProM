package org.processmining.filterd.gui;
import java.beans.PropertyChangeEvent;
import java.util.List;

import org.processmining.filterd.models.YLog;

public class ComputationCellModelListeners extends CellModelListeners {

	public ComputationCellModelListeners(CellController controller) {
		super(controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		//check general cell property change 
		super.propertyChange(event);
		if (event.getPropertyName().equals("setInputLogs")) {		
			((ComputationCellController) controller).changeInputLogsCombo((List<YLog>)event.getNewValue());
		}
	}
}