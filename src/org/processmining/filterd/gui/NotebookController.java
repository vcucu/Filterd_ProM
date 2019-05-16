package org.processmining.filterd.gui;

import java.io.IOException;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class NotebookController {
	
	private NotebookModel model;
//	private UIPluginContext context;
//	private XLog log;
	
	@FXML private Button autoButton;
	@FXML private Button manualButton;
	@FXML private Button computeButton;
	@FXML private Button exportButton;
	@FXML private ScrollPane pane;
	@FXML private VBox layout;
	@FXML private Button addCellButton;
	
	
	public void initialize() {
    	// TODO bindings
	}
	
	public NotebookController(NotebookModel model) {
		this.model = model;
	}
	
	@FXML
	public void setComputationModeToAutomatic() {
		model.setCompMode(ComputationMode.AUTOMATIC);
	}

	@FXML
	public void setComputationModeToManual() {
		model.setCompMode(ComputationMode.MANUAL);
	}
	
	@FXML
	public void addCell() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/gui/fxml/AddCell.fxml"));
			AddCellController newController = new AddCellController(this);
			loader.setController(newController);
			HBox newCellLayout = (HBox) loader.load();
			layout.getChildren().add(newCellLayout);
			newController.setCellLayout(newCellLayout);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public NotebookModel getModel() {
		return model;
	}

	public void setModel(NotebookModel model) {
		this.model = model;
	}

	public VBox getLayout() {
		return layout;
	}

	public void setLayout(VBox layout) {
		this.layout = layout;
	}
}