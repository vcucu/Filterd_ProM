package org.processmining.filterd.plugins;


import java.awt.GridLayout;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.filterd.gui.NotebookController;
import org.processmining.filterd.gui.NotebookModel;
import org.processmining.filterd.gui.adapters.NotebookModelAdapted;
import org.processmining.framework.plugin.ProMCanceller;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class FilterdVisualizer {

	private static JPanel mainPanel;
	private static JFXPanel filterdPanel;
	
	private NotebookModel model;
	private NotebookController controller;
	
	public static final String NAME = "Filterd Visualizer";
	public static final String HELP = "Help text goes here.";
	
	@Plugin(name = NAME, level = PluginLevel.PeerReviewed, parameterLabels = { "Log", "Canceller" },
			returnTypes = JComponent.class, returnLabels = "Filterd Notebook Visualizer", userAccessible = true,
			mostSignificantResult = 1, help = HELP)
	@Visualizer(name = "Filterd Visualizer", pack = "Filterd")
	public JComponent visualize(final UIPluginContext context, final XLog log, final ProMCanceller canceller) {

		if (log.isEmpty()) {
			return new JLabel("The " + NAME + " doesn't support emply XLogs.");
		}
		
		model = new NotebookModel(context, log, canceller);
		controller = new NotebookController(model);
		
		// Initialize GUI components
		filterdPanel = new JFXPanel();
		initGUI(filterdPanel);
		
		// Initialize JPanel to be returned
		mainPanel = new JPanel(new GridLayout(1,1));
		mainPanel.add(filterdPanel);
		
		return mainPanel;
	}
	
	
	// Used for import/export
	@Plugin(name = NAME, level = PluginLevel.PeerReviewed, parameterLabels = { "NotebookModelAdapted", "Canceller" },
			returnTypes = JComponent.class, returnLabels = "Filterd Notebook Visualizer", userAccessible = true,
			mostSignificantResult = 1, help = HELP)
	@Visualizer(name = "Filterd Visualizer", pack = "Filterd")
	public JComponent visualize(final UIPluginContext context, final NotebookModelAdapted adaptedModel, final ProMCanceller canceller) {

		// initialize an empty notebook model and its controller
		model = new NotebookModel(context, adaptedModel.getInitialInput(), canceller);
		controller = new NotebookController(model);
		
		// Initialize GUI components
		filterdPanel = new JFXPanel();
		initGUI(filterdPanel);
		
		// Set imported state to the empty notebook
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // This method is invoked on JavaFX thread
				controller.setComputationMode(adaptedModel.getComputationMode());
				controller.loadCells(adaptedModel.getCells());
                
            }
        });
		
		// Initialize JPanel to be returned
		mainPanel = new JPanel(new GridLayout(1,1));
		mainPanel.add(filterdPanel);
		
		return mainPanel;
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
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/gui/fxml/Notebook.fxml"));
					loader.setController(controller);
					Parent root = (Parent) loader.load();
		            Scene scene = new Scene(root);
		            
		            fxPanel.setScene(scene);
				} catch (IOException e) {
					e.printStackTrace();
				}
                
            }
        });
	}

	public static void changeView(JComponent component) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					mainPanel.remove(0);
					mainPanel.add(component);
					mainPanel.repaint(); 	// useless
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void revertView() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					mainPanel.remove(0);
					mainPanel.add(filterdPanel);
					mainPanel.repaint();	// useless
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}