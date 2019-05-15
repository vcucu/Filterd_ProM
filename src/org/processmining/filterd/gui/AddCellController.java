package org.processmining.filterd.gui;

import java.io.IOException;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class AddCellController {
	
	private NotebookModel model;
	private UIPluginContext context;
	private XLog log;
	private VBox layout;
	
	@FXML private Button addComputationCell;
	@FXML private Button addTextCell;
	
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
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
