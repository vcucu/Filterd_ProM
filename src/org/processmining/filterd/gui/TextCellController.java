package org.processmining.filterd.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;

public class TextCellController extends CellController {
	
	@FXML private TextArea commentField;
	@FXML private MenuButton menuBtnCellSettings;

	public TextCellController(NotebookController controller, TextCellModel cell) {
		super(controller, cell);
	}

	public void initialize() {
		cellModel.getProperty().addPropertyChangeListener(new CellModelListeners(this));
		getCellModel().bindCellName(cellName.textProperty()); // bind the cell name to the cell name variable.
		getCellModel().bindComment(commentField.textProperty()); // bind the text in the UI to its variable counterpart.
		// binding for cell name 
		this.cellName.setText(this.cellModel.getCellName());
		this.cellModel.cellNameProperty().addListener(new ChangeListener<String>() {

			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!cellName.getText().equals(newValue)) {
					cellName.setText(newValue);
				}
			}
		});
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
