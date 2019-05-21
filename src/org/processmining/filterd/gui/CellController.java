package org.processmining.filterd.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public abstract class CellController {

	//TODO: add all the attributes from the UI Diagram
	private NotebookController notebookController;
	private CellModel cellModel;
	private Pane cellLayout;
	@FXML
	private Region statusBar;
	@FXML 
	private TextField cellName;
	

	public CellController(NotebookController controller, CellModel cellModel) {
		this.notebookController = controller;
		this.cellModel = cellModel;
	}

	/**
	 * Handler for the status bar. Toggles the collapsing of the cell.
	 */
	@FXML
	public void handleStatusBar() {
		if (cellModel.isHidden()) {
			this.hide();
		}else {
			this.show();
		}
	}
	
	/**
	 * Handler for the cell name. Sets the cell name in the model.
	 */
	@FXML 
	public void handleCellName() {
		cellModel.setCellName(cellName.getText()); 
	}
	
	/**
	 * Removes the current cell (model) from the notebook model.
	 */
	@FXML
	public void removeCell() {
		getNotebookController().removeCell(getCellModel());
	}
	
	/**
	 * Returns the controller of the notebook this cell is in.
	 * @return The notebook controller.
	 */
	public NotebookController getNotebookController() {
		return notebookController;
	}

	public void setController(NotebookController controller) {
		this.notebookController = controller;
	}

	/**
	 * Returns the layout of the current cell.
	 * @return The layout of the current cell.
	 */
	public Pane getCellLayout() {
		return cellLayout;
	}

	public void setCellLayout(Pane cellLayout) {
		this.cellLayout = cellLayout;
	}

	/**
	 * Returns the model of the current cell.	
	 * @return The model of the current cell.
	 */
	public CellModel getCellModel() {
		return cellModel;
	}

	public void setCellModel(CellModel cellModel) {
		this.cellModel = cellModel;
	}

	/**
	 * Shows (un-collapses) the cell.
	 */
	public abstract void show();

	/**
	 * Hides (collapses) the cell.
	 */
	public abstract void hide();
}
