package org.processmining.filterd.gui;

import javafx.fxml.FXML;

public class TextCellController extends Cell {
	
	//TODO: add other FXML attributes
	
	//TODO: add controller methods
	
	public TextCellController(NotebookController controller) {
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
