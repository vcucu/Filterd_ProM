package org.processmining.filterd.gui;

import javafx.scene.layout.Pane;

public class Cell {

	//TODO: add all the attributes from the UI Diagram
	
	private NotebookController controller;
	private Pane layout;
	private Pane cellLayout;
	
	public Cell(NotebookController controller) {
		this.controller = controller;
		this.layout = controller.getLayout();
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
}
