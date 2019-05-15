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
}
