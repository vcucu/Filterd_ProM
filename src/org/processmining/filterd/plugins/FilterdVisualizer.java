package org.processmining.filterd.plugins;


import java.io.IOException;

import javax.swing.JComponent;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.filterd.gui.NotebookController;
import org.processmining.filterd.gui.NotebookModel;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class FilterdVisualizer {

	private JFXPanel notebookPanel;
	private NotebookModel model;
	private NotebookController controller;
	
	@Plugin(name = "Filterd Visualizer", level = PluginLevel.PeerReviewed, parameterLabels = "Filter",
			returnTypes = JComponent.class, returnLabels = "Filterd Notebook Visualizer", userAccessible = true,
			mostSignificantResult = 1, help = "Help text goes here")
	@Visualizer(name = "Filterd Visualizer", pack = "Filterd")
	
	
	public JComponent visualize(final UIPluginContext context, final XLog log) {

		model = new NotebookModel(context, log);
		controller = new NotebookController(model);
		
		// Initialize GUI components
		notebookPanel = new JFXPanel();
		initGUI(notebookPanel);
		
		return notebookPanel;
	}
	
	private void initGUI(final JFXPanel fxPanel) {
		
		Platform.setImplicitExit(false);
		// Prevents the JavaFX Platform from automatically exiting
		// in case the components are no longer visible (i.e. by changing visualization)
		
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // This method is invoked on JavaFX thread
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/gui/fxml/FilterdNotebook.fxml"));
					loader.setController(controller);
					Parent root = (Parent) loader.load();
		            Scene scene = new Scene(root);
		            scene.getStylesheets().add(getClass().getResource("/org/processmining/filterd/gui/css/Notebook.css").toExternalForm());
		            
		            fxPanel.setScene(scene);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
            }
        });
	}

	
}
