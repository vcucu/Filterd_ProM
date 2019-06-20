package org.processmining.filterd.widgets;

import java.util.List;

import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.configurations.FilterdAbstractReferencingConfig;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * One from set parameter which creates a reference. This parameter contains a
 * nested configuration panel which changes based on the selection in the one
 * from set (combo box). This means that once a value is selected in the combo
 * box, the filter configuration is asked what to display.
 * 
 * @author Filip Davidovic
 */
public class ParameterOneFromSetExtendedController extends ParameterController {
	@FXML
	private ComboBox<String> combobox; // one from set combo box
	@FXML
	private Label label; // label describing the parameter
	@FXML
	private VBox nestedPanel; // nested panel which will contain the nested filter configuration panel
	private AbstractFilterConfigPanelController nestedConfigPanel; // nested configuration panel
	private FilterdAbstractReferencingConfig owner; // referencing filter configuration which is being configured with this parameter

	/**
	 * Default constructor which should be used in all actual code.
	 * 
	 * @param nameDisplayed
	 *            description of the parameter
	 * @param name
	 *            unique identified of the parameter (used to map UI parameter
	 *            to actual parameter in populate method of the filter
	 *            configuration)
	 * @param defaultValue
	 *            default value of the combo box
	 * @param list
	 *            list of options for the combo box
	 * @param owner
	 *            referencing filter configuration which is being configured
	 *            with this parameter
	 */
	public ParameterOneFromSetExtendedController(String nameDisplayed, String name, String defaultValue,
			List<String> list, FilterdAbstractReferencingConfig owner) {
		super(name);
		this.owner = owner;
		// load UI contents
		this.loadFXMLContents(this, "/org/processmining/filterd/widgets/fxml/ParameterOneFromSetExtended.fxml");
		// set specifics
		label.setText(nameDisplayed); // set description
		ObservableList<String> observableList = FXCollections.observableList(list); // transform list to an observable list (this is what the combo box expects)
		combobox.setItems(observableList); // set items of the combo box
		combobox.getSelectionModel().select(defaultValue); // set the initial value of the combo box
		setNestedContent(owner.getConcreteReference()); // get the nested configuration panel and set it as contents
	}

	/**
	 * Getter for the value of the combo box
	 * 
	 * @return current value of the combo box
	 */
	public String getValue() {
		return combobox.getSelectionModel().getSelectedItem();
	}

	/**
	 * Set the contents of the nested panel.
	 * 
	 * @param nestedFilterConfig
	 *            referenceable filter configuration whose configuration panel
	 *            should be displayed
	 */
	public void setNestedContent(FilterdAbstractConfig nestedFilterConfig) {
		nestedConfigPanel = nestedFilterConfig.getConfigPanel(); // get the nested configuration panel controller
		VBox nestedConfigPanelRoot = nestedConfigPanel.getRoot(); // get the root component of the nested configuration panel
		nestedPanel.getChildren().clear(); // clear all contents of the nested container
		VBox.setVgrow(nestedPanel, Priority.ALWAYS);
		nestedPanel.getChildren().add(nestedConfigPanelRoot); // add the nested configuration panel to the nested container
	}

	/**
	 * Getter for the nested configuration panel.
	 * 
	 * @return nested configuration panel
	 */
	public AbstractFilterConfigPanelController getNestedConfigPanel() {
		return nestedConfigPanel;
	}

	/**
	 * Handler for the event when the selection in the combo box is changed.
	 */
	@FXML
	public void selectionChanged() {
		setNestedContent(owner.changeReference(this));
	}
}
