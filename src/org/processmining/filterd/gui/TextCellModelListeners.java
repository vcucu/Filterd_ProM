package org.processmining.filterd.gui;

import java.beans.PropertyChangeEvent;

public class TextCellModelListeners extends CellModelListeners{

	TextCellModelListeners(CellController controller) {
		super(controller);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		//check general cell property change 
		super.propertyChange(event);
		if(event.getPropertyName().equals("setComment")) {
			updateComment(event);
		}
		
	}
	
	private void updateComment(PropertyChangeEvent event){
		((TextCellController)controller).changeComment((String) event.getNewValue());
	}
}
