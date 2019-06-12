package org.processmining.filterd.gui;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;

public class SwingBubble extends AnchorPane {

	private SwingNode swgNode;
	private ImageView imgView;
	
	/**
	 * Allocates a new SwingWrap object.
	 * 
	 * @param parent, the Pane containing the SwingWrap
	 */
	public SwingBubble() {
		super();
		// Initialize SwingNode and ImageView
		swgNode = new SwingNode();
		imgView = new ImageView();
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
        Platform.runLater(new Runnable() {
            @Override
            public synchronized void run() {
        		if (getChildren().contains(imgView)) {
        			// Reload visualizer
        			getChildren().add(swgNode);
        			// Remove snapshot
        			getChildren().remove(imgView);
        		}
            }
        });
	}
	
	public void setContent(JComponent content) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public synchronized void run() {
				if (content != null) {
					Dimension dimension = new Dimension();
					dimension.setSize(getMaxWidth(), getMaxHeight());
					content.setMaximumSize(dimension);
					content.setPreferredSize(dimension);
					swgNode.setContent(content);
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
