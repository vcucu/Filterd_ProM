package org.processmining.filterd.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class AddCellController {
	
	private NotebookModel model;
	private VBox layout;
	
	@FXML private Button addComputationCellButton;
	@FXML private Button addTextCellButton;
	
	public AddCellController(NotebookModel model, VBox layout) {
		this.model = model;
		this.layout = layout;
	}
	
	@FXML
	public void addComputationCell() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/gui/fxml/ComputationCell.fxml"));
			VBox cellLayout = (VBox) loader.load();
			layout.getChildren().add(cellLayout);
			layout.getChildren().remove(layout.getChildren().size() - 2);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void addTextCell() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/gui/fxml/TextCell.fxml"));
			VBox cellLayout = (VBox) loader.load();
			layout.getChildren().add(cellLayout);
			layout.getChildren().remove(layout.getChildren().size() - 2);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
