package org.processmining.filterd.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Control;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;

public class SwingWrap extends SwingNode {
	
	// List of all the SwingWraps instances. Used for the SwingNode workaround.  
	public static List<SwingWrap> wraps = new ArrayList<>();

	private Pane parent;
	private ImageView imgView;
	private int index;
	
	/**
	 * Allocates a new SwingWrap object.
	 * 
	 * @param parent, the Pane containing the SwingWrap
	 */
	public SwingWrap(Pane parent) {
		super();
		this.parent = parent;
		imgView = new ImageView();
		wraps.add(this);
	}
	
	/**
	 * Hides the SwingNode and replaces it with an image.
	 * 
	 * @pre The SwingWrap must be in the parent object.
	 */
	private void fake() {
		if (parent.getChildren().contains(this)) {
			index = parent.getChildren().indexOf(this);
			// Snapshot the SwingNode and load it in the parent
			WritableImage snapshot = this.snapshot(new SnapshotParameters(), null);
			imgView.setImage(snapshot);
			parent.getChildren().add(index, imgView);
			// Unload visualizer
			parent.getChildren().remove(this);
		}
	}
	
	/**
	 * Hides the SwingNode and replaces it with an image.
	 * 
	 * @pre imgView must be in the parent object.
	 */
	private void unfake() {
		if (parent.getChildren().contains(imgView)) {
			index = parent.getChildren().indexOf(imgView);
			// Reload visualizer
			parent.getChildren().add(index, this);
			// Remove snapshot
			parent.getChildren().remove(imgView);
		}
	}
	
	/**
	 * Forces the view to refresh.
	 */
	public void refresh() {
		fake();
		unfake();
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		// Remove SwingWrap from static list when finalized
		wraps.remove(this);
	}
	
	/**
	 * Workaround for JavaFX ComboBoxes and MenuButtons when a SwingNode is present.
	 * 
	 * This method must be called once for each control that doesn't work when a SwingNode is present.
	 * 
	 * @param control, the control to be fixed
	 * 
	 */
	public static void workaround(Control control) {
		EventHandler<javafx.scene.input.MouseEvent> pressedHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
			@Override
			public synchronized void handle(javafx.scene.input.MouseEvent e) {
				wraps.forEach(wrap -> wrap.fake());
			}
		};
		
		EventHandler<javafx.scene.input.MouseEvent> releasedHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
			@Override
			public synchronized void handle(javafx.scene.input.MouseEvent e) {
				wraps.forEach(wrap -> wrap.unfake());
				// Show the context menu on MenuButtons
				if (control instanceof MenuButton) {
					((MenuButton) control).show();
				}
			}
		};
		
		// Adding the event handlers
		control.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED, pressedHandler);
		control.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_RELEASED, releasedHandler);
	}
}
