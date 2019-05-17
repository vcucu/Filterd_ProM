package org.processmining.filterd.gui;

import javafx.fxml.FXML;

public class TextCellController extends CellController {
	NotebookController controller;
	TextCellModel cell;
	
	//TODO: add other FXML attributes
	
	//TODO: add controller methods
	
	public TextCellController(NotebookController controller, TextCellModel cell) {
		super(controller, cell);
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
