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
	private UIPluginContext context;
	private XLog log;
	
	
	@FXML private ScrollPane pane;
	@FXML private VBox layout;
	@FXML private Button autoButton;
	@FXML private Button manualButton;
	@FXML private Button computeButton;
	@FXML private Button exportButton;
	@FXML private Button addCellButton;
	
	
	public void initialize() {
    	this.context = model.getContext();
    	this.log = model.getLog();
	}
	
	public NotebookController(NotebookModel model) {
		this.model = model;
	}
	
	@FXML
	public void addCell() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/gui/fxml/AddCell.fxml"));
			AddCellController addCellController = new AddCellController(model, layout);
			loader.setController(addCellController);
			HBox addCellLayout = (HBox) loader.load();
			layout.getChildren().add(addCellLayout);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
