package org.processmining.filterd.widgets;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;

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
		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("/org/processmining/filterd/widgets/fxml/ParameterMultipleFromSet.fxml"));
		fxmlLoader.setController(this);
		try {
			contents = (VBox) fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		// set specifics
		label.setText(nameDisplayed);
		list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		ObservableList<String> observableList = FXCollections.observableList(options);
		list.setItems(observableList);
		System.out.println(defaultValues.size());
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
