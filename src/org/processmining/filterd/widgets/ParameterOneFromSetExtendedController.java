package org.processmining.filterd.widgets;

import java.io.IOException;
import java.util.List;

import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.configurations.FilterdAbstractReferencingConfig;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ParameterOneFromSetExtendedController extends ParameterController {
	@FXML private ComboBox<String> combobox;
	@FXML private Label label;
	@FXML private VBox nestedPanel;
	private AbstractFilterConfigPanelController nestedConfigPanel;
	private FilterdAbstractReferencingConfig owner;
	
	public ParameterOneFromSetExtendedController(String nameDisplayed, String name, String defaultValue, List<String> list, FilterdAbstractReferencingConfig owner) {
		super(name);
		this.owner = owner;
		// load contents
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/widgets/fxml/ParameterOneFromSetExtended.fxml"));
        fxmlLoader.setController(this);
        try {
            contents = (VBox) fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // set specifics
        label.setText(nameDisplayed);
        ObservableList<String> observableList = FXCollections.observableList(list);
        combobox.setItems(observableList);
        combobox.getSelectionModel().select(defaultValue);
        setNestedContent(owner.getConcreteReference());
	}
	
	public String getValue() {
		return combobox.getSelectionModel().getSelectedItem();
	}
	
	public void setNestedContent(FilterdAbstractConfig nestedFilterConfig) {
		nestedConfigPanel = nestedFilterConfig.getConfigPanel();
		VBox nestedConfigPanelRoot = nestedConfigPanel.getRoot();
		nestedPanel.getChildren().clear();
		VBox.setVgrow(nestedPanel, Priority.ALWAYS);
		nestedPanel.getChildren().add(nestedConfigPanelRoot);
	}
	
	public AbstractFilterConfigPanelController getNestedConfigPanel() {
		return nestedConfigPanel;
	}
	
	@FXML
	public void selectionChanged() {
		setNestedContent(owner.changeReference(this)); 
	}
}
