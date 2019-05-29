package org.processmining.filterd.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class TextCellController extends CellController {
	
	@FXML private TextArea commentField;

	public TextCellController(NotebookController controller, TextCellModel cell) {
		super(controller, cell);
	}

	public void initialize() {
		cellModel.getProperty().addPropertyChangeListener(new CellModelListeners(this));
		getCellModel().bindCellName(cellName.textProperty()); // bind the cell name to the cell name variable.
		getCellModel().bindComment(commentField.textProperty()); // bind the text in the UI to its variable counterpart.
	}
	
	public void changeComment(String comment) {
		commentField.setText(comment);
	}
	
	/**
	 * Gets the cell model of the current cell. This method is overridden so it
	 * returns an object of type TextCellModel, this prevents us from
	 * having to cast the returned object to TextCellModel every single
	 * time it is called.
	 */
	@Override
	public TextCellModel getCellModel() {
		return (TextCellModel) super.getCellModel();
	}
}
