package org.processmining.filterd.gui;

import java.awt.Dimension;
import java.awt.MouseInfo;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * This class is meant as a wrapper for SwingNodes.
 * 
 * It is used to work around compatibility problems 
 * between JavaFX and Swing. 
 * @author Omar Alabbasi
 *
 */

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
	 * @pre The SwingWrap must be in the parent object 
	 * and the mouse must not be in the area of the SwingBubble.
	 */
	private void fake() {
		Platform.runLater(new Runnable() {
			@Override
			public synchronized void run() {
				if (!isFake() && !isMouseIn()) {
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
					if (isFake()) {
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
				unfake();
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
				// Wait 1 second and fake the SwingNode. This is done to prevent the dropdown menus from
				// not working in the future (e.g. if the user doesn't enter the SwingBubble area at all)
				// but still giving some time for SwingNode to update its view.
				fakeAfter(1000);
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
		if (isFake()) {
			unfake();
			fake();
		} else {
			fake();
			unfake();
		}
	}

	/**
	 * Adds mouse event handlers to fake and unfake the SwingNode.
	 * (for the JavaFX - Swing workaround)
	 */
	private void addHandlers() {
		// Restore SwingNode when mouse enters pane
		this.setOnMouseEntered(event -> unfake());

		// Restore SwingNode when mouse moves over ImageView
		imgView.setOnMouseMoved(event -> unfake());

		// Hide SwingNode when mouse exits pane
		this.setOnMouseExited(event -> fake());
	}
	
	/**
	 * Fakes the SwingNode after a given amount of milliseconds.
	 */
	private void fakeAfter(long millis) {
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(millis);
				} catch (InterruptedException e) {
					System.out.println(e.toString());
				}
				fake();
			}
		}).start();
	}
	
	/**
	 * Returns whether the SwingNode is faked (i.e. replaced by a picture) or not.
	 */
	public boolean isFake() {
		return (getChildren().contains(imgView) && !getChildren().contains(swgNode));
	}
	
	/**
	 * Returns whether the mouse is in the area of the SwingBubble.
	 */
	public boolean isMouseIn() {
		// Mouse coordinates
		double mouseX = MouseInfo.getPointerInfo().getLocation().getX();
		double mouseY = MouseInfo.getPointerInfo().getLocation().getY();
		// SwingBubble coordinates
		Bounds bounds = this.getBoundsInLocal();	// Bounds of SwingBubble
		bounds = this.localToScreen(bounds);	// Get screen bounds
		
		// Return false if the node is not in a window
		if (bounds != null) {
			Rectangle area = new Rectangle(
					bounds.getMinX(), bounds.getMinY(),
					bounds.getWidth(), bounds.getHeight());
				
			return area.contains(mouseX, mouseY);
		} else {
			return false;
		}
	}
	
}
