package org.processmining.filterd.gui;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

public class NotebookController {
	
	private NotebookModel model;
	private UIPluginContext context;
	private XLog log;
	
	
	@FXML private Pane pane;
	@FXML private ScrollPane scrollPane;
	
	public void initialize() {
    	this.context = model.getContext();
    	this.log = model.getLog();
	}
	
	public NotebookController(NotebookModel model) {
		this.model = model;
	}
}
