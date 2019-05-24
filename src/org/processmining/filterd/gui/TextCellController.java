package org.processmining.filterd.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class TextCellController extends CellController {
	NotebookController controller;
	TextCellModel cell;
	@FXML
	private TextArea commentField;

	//TODO: add other FXML attributes

	//TODO: add controller methods

	public TextCellController(NotebookController controller, TextCellModel cell) {
		super(controller, cell);
	}

	public void initialize() {
		cellModel.getProperty().addPropertyChangeListener(new CellModelListeners(this));
	}
	
	/**
	 * Handle saving of the comment from the text area in model
	 */
	@FXML
	public void handleComment() {
		cell.setComment(commentField.getText());
	}
	
	public void changeComment(String comment) {
		commentField.setText(comment);
	}
}
