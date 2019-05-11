package org.processmining.filterd.plugins;

	

import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.deckfour.uitopia.api.model.ViewType;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIContext;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.contexts.uitopia.hub.ProMResourceManager;
import org.processmining.contexts.uitopia.hub.ProMViewManager;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class FilterdVisualizer {

	private UIPluginContext context;
	private XLog log;
	private JFXPanel notebookPanel;
	private ProMViewManager vm; // Current view manager	
    private ProMResourceManager rm; // Current resource manager
	
	@Plugin(name = "Filterd Visualizer", level = PluginLevel.PeerReviewed, parameterLabels = "Filter",
			returnTypes = JComponent.class, returnLabels = "Filterd Notebook Visualizer", userAccessible = true,
			mostSignificantResult = 1, help = "Help text goes here")
	@Visualizer(name = "Filterd Visualizer", pack = "Filterd")
	
	public JComponent visualize(final UIPluginContext context, final XLog log) {
		this.context = context;
		this.log = log;
		
		// Get current view manager and resource manager
		UIContext globalContext = context.getGlobalContext();
    	vm = ProMViewManager.initialize(globalContext); 	
        rm = ProMResourceManager.initialize(globalContext);
		
		// Initialize GUI components
		notebookPanel = new JFXPanel();
		initNotebookPanel(notebookPanel);
		return notebookPanel;
	}
	
	private void initNotebookPanel(final JFXPanel fxPanel) {
		// Prevents the JavaFX Platform from automatically exiting
		// in case the components are no longer visible (i.e. by changing visualization)
		Platform.setImplicitExit(false);
		
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // This method is invoked on JavaFX thread
                Scene scene = createScene();
                fxPanel.setScene(scene);
            }
        });
	}
    
    private Scene createScene() {
        Group root = new Group();
        Scene scene = new Scene(root, Color.ALICEBLUE);
        
        // Label
        Text text = new Text();
        text.setX(40);
        text.setY(100);
        text.setFont(new Font(25));
        text.setText("Welcome JavaFX!");

        root.getChildren().add(text);
        
        // Dropdown to select the visualizer
        ComboBox<String> cmbVisualizer = new ComboBox<>();
        initcmbVisualizer(cmbVisualizer);
        cmbVisualizer.setLayoutX(20);
        cmbVisualizer.setLayoutY(20);
        root.getChildren().add(cmbVisualizer);
        
        // Visualization panel
        SwingNode swgVisualizer = new SwingNode();
        
        // Action event to sync cmbVisualizer and swgVisualizer
        EventHandler<ActionEvent> event = 
                  new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
        		// outputEventLog need not be in the workspace
        		String selectedTypename = (String) cmbVisualizer.getValue();
        		Object outputEventLog = log.clone();
        		JComponent component;
				try {
					component = context.tryToFindOrConstructFirstNamedObject(JComponent.class, selectedTypename, null, null, outputEventLog);
					updateVisualizationPanel(swgVisualizer, component);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            } 
        }; 
        
        cmbVisualizer.setOnAction(event);

        swgVisualizer.setLayoutX(40);
        swgVisualizer.setLayoutY(120);
        
        root.getChildren().add(swgVisualizer);

        return(scene);
    }
    
    private void initcmbVisualizer(ComboBox<String> cmbVisualizer) {
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
    

    
    private void updateVisualizationPanel(final SwingNode swingNode, JComponent visualizer) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                swingNode.setContent(visualizer);
            }
        });
    }
	
}
