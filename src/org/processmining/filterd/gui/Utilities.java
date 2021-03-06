package org.processmining.filterd.gui;

import org.deckfour.uitopia.api.model.Author;
import org.deckfour.uitopia.api.model.Resource;
import org.deckfour.uitopia.api.model.ResourceType;
import org.deckfour.uitopia.api.model.View;
import org.deckfour.uitopia.api.model.ViewType;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

public class Utilities {
	
	/**	Dummy view type for not showing any visualizer in the dropdown  */
	public static ViewType dummyViewType = new ViewType() {

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
	 * Sets the anchors for each side of a node to some value.
	 */
	public static void setAnchors(Node node, double value) {
		AnchorPane.setTopAnchor(node, value);
		AnchorPane.setBottomAnchor(node, value);
		AnchorPane.setLeftAnchor(node, value);
		AnchorPane.setRightAnchor(node, value);
	}
	
	/**
	 * Updates the icon of a Label button.
	 * 
	 * @pre The Region of the icon should be the first child of the label. 
	 */
	public static void changeIcon(Label button, String oldIcon, String newIcon) {
		// Get icon
		Region icon = (Region) button.getChildrenUnmodifiable().get(0);
		// Update icon
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				icon.getStyleClass().remove(oldIcon);
				icon.getStyleClass().add(newIcon);
			}
		});
	}
	
}
