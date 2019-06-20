package org.processmining.filterd.gui;

import java.util.List;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public interface dummy {

	public VBox getNotebookLayout();

	public VBox getCellsLayout();

	public HBox getToolbarLayout();

	public void initialize();

	public void cellListeners();

	public void autoButtonHandler();

	public void manualButtonHandler();

	public void computeButtonHandler();

	public void appendCellButtonHandler();

	public void setComputationMode(ComputationMode mode);

	public void export();

	public void addComputationCell(int index);

	public void addTextCell(int index);

	public void loadCells(List<CellModel> cells);

	public void removeCell(CellModel cell);

	public NotebookModel getModel();

	public Scene getScene();

	public void hideAddCellModal();

	public void toggleAddCellModal(int index);

	public void printXML();
}
