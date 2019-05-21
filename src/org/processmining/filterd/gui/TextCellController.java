package org.processmining.filterd.gui;

public class TextCellController extends CellController {
	NotebookController controller;
	TextCellModel cell;

	//TODO: add other FXML attributes

	//TODO: add controller methods

	public TextCellController(NotebookController controller, TextCellModel cell) {
		super(controller, cell);
	}

	public void initialize() {
		cellModel.getProperty().addPropertyChangeListener(new CellModelListeners(this));
	}
}
