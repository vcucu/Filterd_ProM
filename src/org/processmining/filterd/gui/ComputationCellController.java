package org.processmining.filterd.gui;

import javafx.fxml.FXML;

public class ComputationCellController extends Cell {
	
	//TODO: add other FXML attributes
	
	//TODO: add controller methods
	
	public ComputationCellController(NotebookController controller) {
		super(controller);
	}
	
	@FXML
	public void removeCell() {
		getLayout().getChildren().remove(getCellLayout());
	}

	public void show() {
		// TODO Auto-generated method stub
		
	}

	public void hide() {
		// TODO Auto-generated method stub
		
	}
}
