package org.processmining.filterd.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class NotebookController {

	private NotebookModel model;
//	private UIPluginContext context;
//	private XLog log;

	@FXML private Button autoButton;
	@FXML private Button manualButton;
	@FXML private Button computeButton;
	@FXML private Button exportButton;
	@FXML private ScrollPane scrollPane;
	@FXML private VBox cellLayout;
	@FXML private Button addCellButton;

	@FXML private Pane configurationModal;

	@FXML private Button addComputationCellButton;
	@FXML private Button addTextCellButton;
	
	
	public NotebookController(NotebookModel model) {
		this.model = model;
		computationMode = ComputationMode.MANUAL;
		cells = new ArrayList<>();
	}

	public void initialize() {
    	this.contextProM = model.getContext();
    	this.initialInput = model.getLog();

//    	try {
//    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/gui/fxml/FilterConfigPanel.fxml"));
//
//    		// yes no
//    		List<Parameter> params = new ArrayList<>();
//        	params.add(new ParameterYesNo("yesNo", "Yes/No Label", true));
//        	// one from set
//        	List<String> oneFromSet = new ArrayList<>();
//        	oneFromSet.add("Option 1");
//        	oneFromSet.add("Option 2");
//        	oneFromSet.add("Option 3");
//        	oneFromSet.add("Option 4");
//        	oneFromSet.add("Option 5");
//        	oneFromSet.add("Option 6");
//        	oneFromSet.add("Option 7");
//        	params.add(new ParameterOneFromSet("oneFromSet", "One From Set Label", "Option 1", oneFromSet));
//        	// multiple from set
//        	List<String> multipleFromSet = new ArrayList<>();
//        	multipleFromSet.add("Option 3");
//        	multipleFromSet.add("Option 6");
//        	multipleFromSet.add("Option 7");
//        	params.add(new ParameterMultipleFromSet("multipleFromSet", "Multiple From Set Label", multipleFromSet, oneFromSet));
//        	// value from range
//        	List<Double> optionsPair = new ArrayList<>();
//        	optionsPair.add(5.0);
//        	optionsPair.add(15.0);
//        	params.add(new ParameterValueFromRange<Double>("valueFromRange", "Value From Range Label", 13.2, optionsPair));
//        	FilterConfigPanelController ctrl = new FilterConfigPanelController();
//
//        	loader.setController(ctrl);
//        	VBox newLayout = (VBox) loader.load();
//        	ctrl.setTitle("Some random filter configuration panel");
//        	ctrl.populateContainer(params);
//        	configurationModal.getChildren().add(newLayout);
//    	} catch(IOException e) {
//    		e.printStackTrace();
//    	}
	}

	@FXML
	public void setComputationModeToAutomatic() {
		model.setCompMode(ComputationMode.AUTOMATIC);
	}

	@FXML
	public void setComputationModeToManual() {
		model.setCompMode(ComputationMode.MANUAL);
	}

	@FXML
	public void addCell() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/gui/fxml/AddCell.fxml"));
			AddCellController newController = new AddCellController(this);
			loader.setController(newController);
			HBox newCellLayout = (HBox) loader.load();
			cellLayout.getChildren().add(newCellLayout);
			newController.setCellLayout(newCellLayout);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public NotebookModel getModel() {
		return model;
	}

	public void setModel(NotebookModel model) {
		this.model = model;
	}

	public VBox getLayout() {
		return cellLayout;
	}

	public void setLayout(VBox layout) {
		this.cellLayout = layout;
	}
}
