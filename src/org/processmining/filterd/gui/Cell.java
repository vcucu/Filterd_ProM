package org.processmining.filterd.gui;

import org.processmining.filterd.gui.NotebookController.NotebookContext;

public abstract class Cell {
	private String name;
	private CellStatus status;
	private NotebookContext notebookContext;
	
	Cell(NotebookContext notebookContext) {
		this.notebookContext = notebookContext;
		status = CellStatus.IDLE;
		name = "Change me...";
	}
	
	public abstract class CellContext {
		
		/**
		 * Abstract method to maximize the cell. If cell is already maximized, nothing should happen.
		 */
		public abstract void show();

		/**
		 * Abstract method to minimize the cell. If cell is already minimized, nothing should happen.
		 */
		public abstract void hide();
	}
}
