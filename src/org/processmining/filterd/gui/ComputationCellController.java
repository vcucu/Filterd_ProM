package org.processmining.filterd.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ComputationCellController extends Cell {
	
	//TODO: add other FXML attributes
	@FXML 
	private VBox panelLayout;
	
	//TODO: add controller methods
	
	public ComputationCellController(NotebookController controller) {
		super(controller);
		this.setCellModel(new CellModel());
	}
	
	@FXML
	public void addFilter() {
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/org/processmining/filterd/gui/fxml/FilterButton.fxml"));
			// ComputationCellController newController = new ComputationCellController(this);
			// loader.setController(newController);
			HBox newLayout = (HBox) loader.load();
			panelLayout.getChildren().add(newLayout);
			// newController.setCellLayout(newCellLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void removeCell() {
		getLayout().getChildren().remove(getCellLayout());
	}

	public void show() {
		// TODO Auto-generated method stub
		
	}

	public void hide() {
		// TODO Auto-generated method stub
		
	}
}
