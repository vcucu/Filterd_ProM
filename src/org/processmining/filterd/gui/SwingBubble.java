package org.processmining.filterd.gui;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SwingBubble extends AnchorPane {

	private SwingNode swgNode;
	private ImageView imgView;

	/*
	 * HV: Create a backstage for the visualizer. If this visualizer is not on
	 * some stage, things may crash.
	 */
	private VBox backRoot;
	private Scene backScene;
	private Stage backStage;

	/**
	 * Allocates a new SwingWrap object.
	 * 
	 * @param parent,
	 *            the Pane containing the SwingWrap
	 */
	public SwingBubble() {
		super();
		// Initialize SwingNode and ImageView
		swgNode = new SwingNode();
		imgView = new ImageView();

		/*
		 * HV: Initialize the backstage.
		 */
		backRoot = new VBox();
		backRoot.setPrefSize(700, 300);
		backScene = new Scene(backRoot);
		backStage = new Stage();
		backStage.setScene(backScene);

		// Make the nodes resize with the pane 
		Utilities.setAnchors(swgNode, 0.0);
		Utilities.setAnchors(imgView, 0.0);
		// Add imgView to the view
		getChildren().add(imgView);
		// Add listeners for the JavaFX - Swing workaround
		addHandlers();
	}

	/**
	 * Hides the SwingNode and replaces it with an image.
	 * 
	 * @pre The SwingWrap must be in the parent object.
	 */
	private void fake() {
		Platform.runLater(new Runnable() {
			@Override
			public synchronized void run() {
				if (getChildren().contains(swgNode)) {
					// Snapshot the SwingNode
					WritableImage snapshot = swgNode.snapshot(new SnapshotParameters(), null);
					imgView.setImage(snapshot);
					// Load ImageView
					getChildren().add(imgView);
					// Unload visualizer
					getChildren().remove(swgNode);
					/*
					 * HV: Add the visualizer to the backstage.
					 */
					backRoot.getChildren().add(swgNode);
				}
			}
		});
	}

	/**
	 * Hides the SwingNode and replaces it with an image.
	 * 
	 * @pre imgView must be in the parent object.
	 */
	private void unfake() {
		if (swgNode.getContent() != null) {
			Platform.runLater(new Runnable() {
				@Override
				public synchronized void run() {
					if (getChildren().contains(imgView)) {
						/*
						 * HV: Remove the visualizer form the backstage.
						 */
						backRoot.getChildren().remove(swgNode);
						// Reload visualizer
						getChildren().add(swgNode);
						// Remove snapshot
						getChildren().remove(imgView);
					}
				}
			});
		}
	}

	public void setContent(JComponent content) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public synchronized void run() {
				/*
				 * HV: Apparently, putting the visualizer backstage has side-effects
				 * when changing the visualization type. 
				 */
				if (getChildren().contains(imgView)) {
					unfake();
				} else {
					fake();
				}
				if (content != null) {
					Dimension dimension = new Dimension();
					/*
					 * HV: If the visualization is backstage, the max width and max height may be incorrect.
					 */
					dimension.setSize(Math.max(imgView.getFitWidth(), getMaxWidth()),
							Math.max(imgView.getFitHeight(), getMaxHeight()));
					content.setMaximumSize(dimension);
					content.setPreferredSize(dimension);
					swgNode.setContent(content);
				}
				if (getChildren().contains(imgView)) {
					fake();
				} else {
					unfake();
				}
			}
		});
	}

	public JComponent getContent() {
		return swgNode.getContent();
	}

	/**
	 * Forces the view to refresh.
	 */
	public synchronized void refresh() {
		// TODO Find a better way to force the view to refresh
		if (getChildren().contains(imgView)) {
			unfake();
			fake();
		} else {
			fake();
			unfake();
		}

	}

	/**
	 * Adds mouse event handlers for the JavaFX - Swing workaround.
	 */
	private void addHandlers() {
		// Restore SwingNode when mouse enters pane
		this.setOnMouseEntered(event -> unfake());

		// Restore SwingNode when mouse moves over ImageView
		imgView.setOnMouseMoved(event -> unfake());

		// Hide SwingNode when mouse exits pane
		this.setOnMouseExited(event -> fake());

	}

}