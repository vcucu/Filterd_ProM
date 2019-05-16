package org.processmining.filterd.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class AddCellController extends Cell {
	
	@FXML private Button addComputationCellButton;
	@FXML private Button addTextCellButton;
	
	public AddCellController(NotebookController controller) {
		super(controller);
	}
	
	@FXML
	public void addComputationCell() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/gui/fxml/ComputationCell.fxml"));
			ComputationCellController newController = new ComputationCellController(getController());
			loader.setController(newController);
			VBox newCellLayout = (VBox) loader.load();
			getLayout().getChildren().add(newCellLayout);
			newController.setCellLayout(newCellLayout);
			getLayout().getChildren().remove(getCellLayout());
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void addTextCell() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/gui/fxml/TextCell.fxml"));
			TextCellController newController = new TextCellController(getController());
			loader.setController(newController);
			VBox newCellLayout = (VBox) loader.load();
			getLayout().getChildren().add(newCellLayout);
			newController.setCellLayout(newCellLayout);
			getLayout().getChildren().remove(getCellLayout());
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void show() {
		// TODO Auto-generated method stub
		
	}

	public void hide() {
		// TODO Auto-generated method stub
		
	}
}
