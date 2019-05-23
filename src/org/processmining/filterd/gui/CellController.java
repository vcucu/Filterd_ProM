package org.processmining.filterd.gui;

import java.beans.PropertyChangeListener;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public abstract class CellController {

	//TODO: add all the attributes from the UI Diagram
	protected NotebookController controller;
	protected CellModel cellModel;
	protected VBox cellLayout;
	
	@FXML protected Region statusBar; // has 8 states, Color x isHidden
	@FXML protected TextField cellName;
	@FXML protected HBox cellBody;
	

	public CellController(NotebookController controller, CellModel cellModel) {
		this.controller = controller; 
		this.cellModel = cellModel;
	}

	//	public void intialize() {
	//		//add PropertyChangeListeners for each of cell model properties
	//		cellModel.getProperty().addPropertyChangeListener(new CellModelListeners(this));
	//
	//	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		cellModel.getProperty().addPropertyChangeListener(listener);
	}

	public NotebookController getController() {
		return controller;
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
		return controller;
	}

	public void setController(NotebookController controller) {
		this.controller = controller;
	}

	/**
	 * Returns the layout of the current cell.
	 * @return The layout of the current cell.
	 */
	public VBox getCellLayout() {
		return cellLayout;
	}

	public void setCellLayout(VBox cellLayout) {
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

	@FXML
	public void handleStatusBar() {
		//this causes for the isHidden attribute to fire a Change event to the CellControllerListeners 
		//that in turn updates the view 
		if (cellModel.isHidden()) {
			cellModel.setHidden(false);
			//System.out.println("setHidden in cell controller");
		} else {
			cellModel.setHidden(true);

		}
	}

	public void changeCellName(String cellName) {
		this.cellName.setText(cellName);
	}
	
	@FXML
	public void prependCellButtonHandler() {
		CellModel model = getCellModel();
		int index = controller.getModel().getCells().indexOf(model);
		controller.toggleAddCellModal(index);
	}

	public void show() {
		//System.out.println("We are now updating ui!");
		//System.out.println(cellBody.equals(null));
		cellBody.setVisible(true); // makes the content of the HBox invisible.
		cellBody.setManaged(true); // makes the HBox take up no space. This option is note available in the Scene Builder.		
	}

	public void hide() {
		cellBody.setVisible(false); // makes the content of the HBox invisible.
		cellBody.setManaged(false); // makes the HBox take up no space. This option is note available in the Scene Builder.
	}
}
