package org.processmining.filterd.gui;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class VisualizerPanelController {

	@FXML
	private AnchorPane visualizerPane;

	private SwingNode swingNode;
	private ObservableList<Node> previousContent;

	/**
	 * Note that the constructor does not have access to the @FXML annotated
	 * fields as @FXML annotated fields are populated after the execution of the
	 * constructor.
	 * 
	 */
	public VisualizerPanelController() {
	}

	/**
	 * Gets executed after the constructor. Has access to the @FXML annotated
	 * fields, thus UI elements can be manipulated here.
	 */
	public void initialize() {
		previousContent = visualizerPane.getChildrenUnmodifiable();
	}

	/**
	 * Sets a new visualizer to the visualizer pane.
	 * @param visualizer the visualizer to set to the visualizer pane.
	 */
	public void setVisualizer(JComponent visualizer) {
		// Initialize Swing node.
		swingNode = new SwingNode();
		// clear the old content from the visualization pane and replace with the visualizer.
		replaceContent(swingNode);
		// fit the swingNode to fill the entire visualizer pane.
		setAnchors(swingNode);
		// set the content of the Swing node
		setSwingNode(visualizer);
	}
	
	/**
	 * Shows a modal (or any arbitrary node) in the visualizer pane.
	 * @param node the modal to show.
	 */
	public void showModal(Node node) {
		previousContent = replaceContent(node);
		setAnchors(node);
	}
	
	/**
	 * Hides the currently shown modal.
	 */
	public void hideModal() {
		replaceContent(swingNode);
		setAnchors(swingNode);
	}
	
	/**
	 * Returns the visualizerPane
	 */
	public AnchorPane getVisualizerPanel() {
		return visualizerPane;
	}

	/**
	 * Helper methods
	 */

	/**
	 * Sets the input content to the swing node.
	 * 
	 * @param content
	 *            the content to set to the swing node.
	 */
	private void setSwingNode(JComponent content) {
		// The invokeLater method is required so the swing content is set using the Swing AWT event queue instead of the JavaFX Platform dispatch loop.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// sets the content of the swing node.
				swingNode.setContent(content);
			}
		});
	}

	/**
	 * Replaces the content of the visualization pane with the input node.
	 * Returns the old content of the visualization pane.
	 * 
	 * @param node
	 *            The new content of the visualization pane.
	 * @return The old content of the visualization pane.
	 */
	private ObservableList<Node> replaceContent(Node node) {
		ObservableList<Node> oldContent = visualizerPane.getChildrenUnmodifiable();
		visualizerPane.getChildren().clear();
		visualizerPane.getChildren().add(node);
		return oldContent;
	}

	/**
	 * Replaces the content of the visualization pane with the input list of
	 * nodes. Returns the old content of the visualization pane.
	 * 
	 * @param nodes
	 *            The new content of the visualization pane.
	 * @return The old content of the visualization pane.
	 */
	private ObservableList<Node> replaceContent(ObservableList<Node> nodes) {
		ObservableList<Node> oldContent = visualizerPane.getChildren();
		visualizerPane.getChildren().clear();
		visualizerPane.getChildren().addAll(nodes);
		return oldContent;
	}

	/**
	 * Sets the anchors of the input node in the visualization pane to 0.0 so
	 * the node is fit to the visualization pane.
	 * 
	 * @param node
	 *            The node to fit to the visualization pane.
	 * @exception IllegalArgumentException
	 *                if node is not a child of the visualization pane.
	 */
	private void setAnchors(Node node) throws IllegalArgumentException {
		if (!visualizerPane.getChildren().contains(node)) {
			//node is not in the visualizationPane
			throw new IllegalArgumentException(
					"org.processmining.filterd.gui.VisualizationPanelController.setAnchors: node was not a child of the visualization pane.");
		}
		visualizerPane.setTopAnchor(node, 0.0);
		visualizerPane.setBottomAnchor(node, 0.0);
		visualizerPane.setLeftAnchor(node, 0.0);
		visualizerPane.setRightAnchor(node, 0.0);
	}

}
