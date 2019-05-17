package org.processmining.filterd.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public abstract class CellController {

	//TODO: add all the attributes from the UI Diagram
	private NotebookController controller;
	private CellModel cellModel;
	private Pane layout;
	private Pane cellLayout;
	@FXML
	private Region statusBar;
	@FXML 
	private TextField cellName;
	

	public CellController(NotebookController controller, CellModel cellModel) {
		this.controller = controller;
		this.layout = controller.getLayout();	//to be redone 
		this.cellModel = cellModel;
	}

	public NotebookController getController() {
		return controller;
	}

	public void setController(NotebookController controller) {
		this.controller = controller;
	}

	public Pane getLayout() {
		return layout;
	}

	public void setLayout(Pane layout) {
		this.layout = layout;
	}

	public Pane getCellLayout() {
		return cellLayout;
	}

	public void setCellLayout(Pane cellLayout) {
		this.cellLayout = cellLayout;
	}

	public CellModel getCellModel() {
		return cellModel;
	}

	public void setCellModel(CellModel cellModel) {
		this.cellModel = cellModel;
	}
	
	@FXML
	public void handleStatusBar() {
		if (cellModel.isHidden()) {
			this.hide();
		}else {
			this.show();
		}
	}
	
	@FXML 
	public void handleCellName() {
		cellModel.setCellName(cellName.getText()); 
	}

	public abstract void show();

	public abstract void hide();
}