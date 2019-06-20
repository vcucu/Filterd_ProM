package org.processmining.filterd.widgets;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

/**
 * Multiple from set parameter UI counterpart.
 *
 * @author Filip Davidovic
 */
public class ParameterMultipleFromSetController extends ParameterController {
	@FXML
	private ListView<String> list; // multiple from set UI component i.e. list view
	@FXML
	private Label label; // description of the parameter

	/**
	 * Default constructor which should be used in all actual code.
	 *
	 * @param nameDisplayed
	 *            description of the parameter
	 * @param name
	 *            unique identified of the parameter (used to map UI parameter
	 *            to actual parameter in populate method of the filter
	 *            configuration)
	 * @param defaultValues
	 *            values in the list view which are selected by default
	 * @param options
	 *            list of options for the list view
	 */
	public ParameterMultipleFromSetController(String nameDisplayed, String name, List<String> defaultValues,
			List<String> options) {
		super(name);
		// load contents
		this.loadFXMLContents(this, "/org/processmining/filterd/widgets/fxml/ParameterMultipleFromSet.fxml");
		// set specifics
		label.setText(nameDisplayed);
		list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		ObservableList<String> observableList = FXCollections.observableList(options);
		list.setItems(observableList);
		for (String option : defaultValues) {
			list.getSelectionModel().select(option);
		}
		if (defaultValues.size() > 0) {
			list.scrollTo(defaultValues.get(0));
		}
	}

	/**
	 * Getter for the value of the list view.
	 *
	 * @return current value of the list view
	 */
	public List<String> getValue() {
		return list.getSelectionModel().getSelectedItems();
	}

	/**
	 * Method which sets the options which are selected in the list view.
	 *
	 * @param selection
	 *            list of options that should be selected
	 */
	public void setSelected(List<String> selection) {
		list.getSelectionModel().clearSelection();
		for (String option : selection) {
			list.getSelectionModel().select(option);
		}
	}

	/**
	 * Method which sets the options of the list view and selects the first
	 * option.
	 *
	 * @param options
	 *            options for the list view
	 */
	public void changeOptions(List<String> options) {
		ObservableList<String> observableList = FXCollections.observableList(options);
		list.setItems(observableList);
		list.getSelectionModel().selectFirst();
	}
}
