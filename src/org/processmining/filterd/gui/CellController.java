package org.processmining.filterd.gui;

import java.beans.PropertyChangeListener;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
//		// add PropertyChangeListeners for each of cell model properties
//		cellModel.getProperty().addPropertyChangeListener(new CellModelListeners(this));
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
	public void handleCellName(KeyEvent e) {
		 if (e.getCode() == KeyCode.ENTER) {
		        //System.out.println("Enter was pressed");
		        //System.out.println(cellName.getText());
				cellModel.setCellName(cellName.getText());
		    }
	}
	
	/**
	 * Removes the current cell (model) from the notebook model.
	 */
	@FXML
	public void remove() {
		//create pop up to confirm deletion
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete cell");
		//alert.setHeaderText("Look, a Confirmation Dialog");
		alert.setContentText("Are you sure you want to delete this cell?");
		ButtonType buttonYes = new ButtonType("Yes", ButtonData.OK_DONE);
		ButtonType buttonNo = new ButtonType("No", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonYes, buttonNo);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		 stage.setAlwaysOnTop(true);// make sure window always at front when open

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonYes){
		    //user chose Yes so remove cell
			getNotebookController().removeCell(getCellModel());
		}
		//user chose No or closed the dialog don't remove cell
		
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
		int index = getCellModel().getIndex();
		controller.toggleAddCellModal(index);
	}
	
	@FXML
	private void moveUp() {
		int index = getCellModel().getIndex();
		if (index > 0) {
			move(index - 1);
		}
	}
	
	@FXML
	private void moveDown() {
		int index = getCellModel().getIndex();
		if (index < controller.getModel().getCells().size() - 1) {
			move(index + 1);
		}
	}
	
	private void move(int index) {
		// Hide AddCell modal
		controller.hideAddCellModal();
		// Remove layout
		controller.getCellsLayout().getChildren().remove(cellLayout);
		// Remove model
		controller.getModel().getCells().remove(getCellModel());
		// Add model at new position
		controller.getModel().getCells().add(index, getCellModel());
		// Add layout at new position
		controller.getCellsLayout().getChildren().add(index, cellLayout);
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
