package org.processmining.filterd.widgets;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

/**
 * Abstract class for all parameter controllers (UI counterparts of filter parameters). 
 * 
 * @author Filip Davidovic
 */
public abstract class ParameterController {
	protected VBox contents; // UI container for parameter controls 
	protected String name; // unique identifier of the parameter (used to map parameter controllers to actual parameters while populating)
	
	/**
	 * Only constructor which sets the parameter name.
	 * 
	 * @param name  parameter name
	 */
	public ParameterController(String name) {
		this.name = name;
	}
	
	/**
	 * Getter for the contents container.
	 * 
	 * @return  contents container
	 */
	public VBox getContents() {
		return contents;
	}
	
	/**
	 * Getter for the unique identifier.
	 * 
	 * @return  unique identifier
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Loads the FXML file contents for a parameter.
	 * 
	 * @param controller parameter controller
	 * @param path 		 path for loading the parameter component
	 */
	public void loadFXMLContents(ParameterController controller, String path) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
        fxmlLoader.setController(controller);
        try {
            contents = (VBox) fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
}