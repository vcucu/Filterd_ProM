package org.processmining.filterd.gui;

import org.deckfour.uitopia.api.model.Author;
import org.deckfour.uitopia.api.model.Resource;
import org.deckfour.uitopia.api.model.ResourceType;
import org.deckfour.uitopia.api.model.View;
import org.deckfour.uitopia.api.model.ViewType;

import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Control;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;

public class Utilities {
	
	static ViewType dummyViewType = new ViewType() {

		@Override
		public View createView(Resource arg0) throws IllegalArgumentException {
			return null;
		}

		@Override
		public Author getAuthor() {
			return null;
		}

		@Override
		public Class<?> getPrimaryType() {
			return null;
		}

		@Override
		public String getTypeName() {
			return null;
		}
		
		@Override
		public String toString() {
			return "None";
		}

		@Override
		public ResourceType getViewableType() {
			return null;
		}
		
	}; 

	/**
	 * Workaround for JavaFX ComboBoxes and MenuButtons when a SwingNode is present.
	 * 
	 * @param parent, the container that has the SwingNode and the control
	 * @param control, the control to be fixed
	 * @param swNode, the SwingNode
	 * 
	 */
	public static void JFXSwingFix(Pane parent, Control control, SwingNode swgNode) {
		final ImageView swgNodePlaceholder = new ImageView();
		
		EventHandler<javafx.scene.input.MouseEvent> pressedHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
			@Override
			public void handle(javafx.scene.input.MouseEvent e) {
				if (parent.getChildren().contains(swgNode)) {
					// Snapshot the SwingNode and load it in the parent
					WritableImage snapshot = swgNode.snapshot(new SnapshotParameters(), null);
					swgNodePlaceholder.setImage(snapshot);
					parent.getChildren().add(swgNodePlaceholder);
					// Unload visualizer
					parent.getChildren().remove(swgNode);
				}
				
			}
		};
		
		EventHandler<javafx.scene.input.MouseEvent> releasedHandler = new EventHandler<javafx.scene.input.MouseEvent>() {
			@Override
			public void handle(javafx.scene.input.MouseEvent e) {
				if (parent.getChildren().contains(swgNodePlaceholder)) {
					// Reload visualizer
					parent.getChildren().add(swgNode);
					// Remove snapshot
					parent.getChildren().remove(swgNodePlaceholder);
				}
			}
		};
		
		// Adding the event handlers
		control.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED, pressedHandler);
		control.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_RELEASED, releasedHandler);
	}
	
}
