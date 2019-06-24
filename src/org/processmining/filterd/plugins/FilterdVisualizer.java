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
import org.processmining.filterd.gui.CellModel;
import org.processmining.filterd.gui.ComputationCellModel;
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

/**
 * Filterd visualizer plug-in. This class defines the plug-in which initializes
 * the Filterd notebook. This plug-in in shown in the visualizers dropdown on
 * the Views tab of ProM.
 */
public class FilterdVisualizer {

	private static JPanel mainPanel; // root component of the whole plug-in (it has to be a JComponent because this is what ProM expects) 
	private static JFXPanel filterdPanel; // top-level component which contains the whole notebook
	private NotebookModel model; // model of the notebook for this instance of the plug-in (there can be only one in each instance) 
	private NotebookController controller; // controller of the notebook for this instance of the plug-in (there can be only one in each instance)
	public static final String NAME = "Filterd Visualizer"; // displayed name of this plug-in 
	public static final String HELP = "Help text goes here."; // help text for this plug-in

	/**
	 * Method called by ProM when the user requests to use the Filterd notebook.
	 * It creates an instance of the notebook along with its UI and passes it to
	 * ProM.
	 * 
	 * @param context
	 *            plug-in context used for instantiating other visualizer inside
	 *            the notebook and exporting to workspace
	 * @param log
	 *            initial input log for the notebook
	 * @param canceller
	 *            ???
	 * @return initialized Filterd notebook instance
	 */
	@Plugin(name = NAME, level = PluginLevel.PeerReviewed, parameterLabels = { "Log",
			"Canceller" }, returnTypes = JComponent.class, returnLabels = "Filterd Notebook Visualizer", userAccessible = true, mostSignificantResult = 1, help = HELP)
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
		mainPanel = new JPanel(new GridLayout(1, 1));
		mainPanel.add(filterdPanel);

		return mainPanel;
	}

	/**
	 * Method representing the Filterd notebook import / export plug-in. This
	 * plug-in is used to save the instance of a notebook as an XML file to cold
	 * storage (i.e. file system), that can be shared and / or instantiated
	 * again when ProM is restarted.
	 * 
	 * @param context
	 *            plug-in context used for instantiating other visualizer inside
	 *            the notebook and exporting to workspace
	 * @param adaptedModel
	 *            deserialized notebook model
	 * @param canceller
	 *            ???
	 * @return
	 */
	@Plugin(name = NAME, level = PluginLevel.PeerReviewed, parameterLabels = { "NotebookModelAdapted",
			"Canceller" }, returnTypes = JComponent.class, returnLabels = "Filterd Notebook Visualizer", userAccessible = true, mostSignificantResult = 1, help = HELP)
	@Visualizer(name = "Filterd Visualizer", pack = "Filterd")
	public JComponent visualize(final UIPluginContext context, final NotebookModelAdapted adaptedModel,
			final ProMCanceller canceller) {

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
				// set input/output logs of all computation cells
				for (CellModel cellModel : model.getCells()) {
					if (cellModel instanceof ComputationCellModel) {
						ComputationCellModel computationCellModel = (ComputationCellModel) cellModel;
						if (computationCellModel.indexOfInputOwner == -1) {
							System.out.println(
									"[*] Setting " + cellModel.getCellName() + " to initial input. Index of owner is "
											+ Integer.toString(computationCellModel.indexOfInputOwner));
							computationCellModel.setInputLog(model.getInitialInput());
						} else {
							CellModel inputOwner = model.getCells().get(computationCellModel.indexOfInputOwner);
							System.out.println("Setting " + cellModel.getCellName() + " to " + inputOwner.getCellName()
									+ "'s output");
							computationCellModel
									.setInputLog(((ComputationCellModel) inputOwner).getOutputLogs().get(0));
						}
					}
				}

			}
		});

		// Initialize JPanel to be returned
		mainPanel = new JPanel(new GridLayout(1, 1));
		mainPanel.add(filterdPanel);

		return mainPanel;
	}

	/**
	 * Method used to instantiate the Filterd notebook inside the given JFXPanel
	 * 
	 * @param fxPanel
	 *            panel in which the notebook should be instantiated
	 */
	private void initGUI(final JFXPanel fxPanel) {
		// Prevents the JavaFX Platform from automatically exiting
		// in case the components are no longer visible (i.e. by changing visualization)
		Platform.setImplicitExit(false);
		// This method is invoked on JavaFX thread
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					FXMLLoader loader = new FXMLLoader(
							getClass().getResource("/org/processmining/filterd/gui/fxml/Notebook.fxml"));
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

	/**
	 * Change the contents of the root component (mainPanel) to the given
	 * component. Note that this removes all elements that were previously part
	 * of the mainPanel.
	 * 
	 * @param component
	 *            UI component that should be placed in the root element
	 */
	public static void changeView(JComponent component) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					mainPanel.remove(0);
					mainPanel.add(component);
					mainPanel.revalidate(); // useless
					mainPanel.repaint(); // useless
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Change the contents of the root component (mainPanel) to the Filterd
	 * notebook. Note that this removes all elements that were previously part
	 * of the mainPanel.
	 */
	public static void revertView() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					mainPanel.remove(0);
					mainPanel.add(filterdPanel);
					mainPanel.revalidate(); // useless
					mainPanel.repaint(); // useless
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}