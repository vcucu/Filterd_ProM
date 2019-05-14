package org.processmining.filterd.gui;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.deckfour.uitopia.api.model.ViewType;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.hub.ProMResourceManager;
import org.processmining.contexts.uitopia.hub.ProMViewManager;
import org.processmining.framework.connections.Connection;

import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class NotebookController {
	
	private NotebookModel model;
	private UIPluginContext context;
	private XLog log;
	
	@FXML private Text text;
	@FXML private ComboBox<String> cmbVisualizer;
	@FXML private SwingNode swgVisualizer;
	
	public void initialize() {
    	this.context = model.getContext();
    	this.log = model.getLog();
		
		// Welcome text
        text.setFont(new Font(25));
        //TESTING CODE BY EWOUD
        text.setText("Welcome to JavaFX " + System.getProperty("java.version") + ", running on Java " + System.getProperty("javafx.version") + ".");
        
        // Dropdown to select the visualizer
        initcmbVisualizer();
	}
	
	public NotebookController(NotebookModel model) {
		this.model = model;
	}
	
    private void initcmbVisualizer() {
    	ProMViewManager vm = model.getVm(); // Current view manager	
        ProMResourceManager rm = model.getRm(); // Current resource manager
        
        XLog inputEventLog = log; // Needs to be in the workspace!
        String exclude = "Filterd Visualizer"; // Visualizer to exclude from list.
        
        // Get the possible visualizers for the input event log.
        List<ViewType> logViewTypes = vm.getViewTypes(rm.getResourceForInstance(inputEventLog));
        
        // Count how many should be added.
        int size = 0;
        for (ViewType type : logViewTypes) {
        	if (!type.getTypeName().equals(exclude)) {
        		size++;
        	}
        }
        
        // Add them all.
        String values[] = new String[size];
        int index = 0;
        for (ViewType type : logViewTypes) {
        	if (!type.getTypeName().equals(exclude)) {
        		//	Add the name of this view type.
        		values[index] = type.getTypeName();
        		index++;
        		System.out.println(type.getTypeName());
        	}
        }
        
        cmbVisualizer.getItems().addAll(values);
    }
    
    @FXML protected void loadVisualizer(ActionEvent event) {	
		// outputEventLog need not be in the workspace
		String selectedTypename =  (String) cmbVisualizer.getValue();	// "@2 Log Summary";
		Object outputEventLog = log.clone(); //
		JComponent component;
		try {
			component = context.tryToFindOrConstructFirstNamedObject(JComponent.class, selectedTypename, null, null, outputEventLog);
			updateVisualizationPanel(component);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    }
    
    private void updateVisualizationPanel(JComponent visualizer) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	swgVisualizer.setContent(visualizer);
            }
        });
    }
    
    
}
