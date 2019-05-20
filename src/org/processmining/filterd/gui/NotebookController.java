package org.processmining.filterd.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterText;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * This class contains the controller for the notebook.
 * 
 * @author Ewoud
 *
 */
public class NotebookController {

	private NotebookModel model; // the model that is paired with this controller

	/**
	 * variables containing the (important) UI elements so they can be
	 * interacted with in the code.
	 */
	@FXML
	private Button autoButton;
	@FXML
	private Button manualButton;
	@FXML
	private Button computeButton;
	@FXML
	private Button exportButton;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private VBox notebookLayout;
	@FXML
	private Button addCellButton;
	@FXML
	private HBox addCellModal;
	@FXML
	private Button addComputationCellButton;
	@FXML
	private Button addTextCellButton;

	// I'm not sure where this configuraitonModal is for?
	@FXML
	private Pane configurationModal;

	/**
	 * The constructor which sets the model. Note that the constructor does not
	 * have access to the @FXML annotated fields as @FXML annotated fields are
	 * populated after the execution of the constructor.
	 * 
	 * @param model
	 *            the model that is to be paired with this controller
	 */
	public NotebookController(NotebookModel model) {
		this.model = model;
	}

	/**
	 * Gets executed after the constructor. Has access to the @FXML annotated
	 * fields, thus UI elements can be manipulated here.
	 */
	public void initialize() {
		// create parameters
		// yes no
		List<Parameter> params = new ArrayList<>();
    	params.add(new ParameterYesNo("yesNo", "Yes/No Label", true));
    	// one from set
    	List<String> oneFromSet = new ArrayList<>();
    	oneFromSet.add("Option 1");
    	oneFromSet.add("Option 2");
    	oneFromSet.add("Option 3");
    	oneFromSet.add("Option 4");
    	oneFromSet.add("Option 5");
    	oneFromSet.add("Option 6");
    	oneFromSet.add("Option 7");
    	params.add(new ParameterOneFromSet("oneFromSet", "One From Set Label", "Option 1", oneFromSet));
    	// multiple from set
    	List<String> multipleFromSet = new ArrayList<>();
    	multipleFromSet.add("Option 3");
    	multipleFromSet.add("Option 6");
    	multipleFromSet.add("Option 7");
    	params.add(new ParameterMultipleFromSet("multipleFromSet", "Multiple From Set Label", multipleFromSet, oneFromSet));
    	// value from range
    	List<Double> optionsPair = new ArrayList<>();
    	optionsPair.add(5.0);
    	optionsPair.add(15.0);
    	params.add(new ParameterValueFromRange<Double>("valueFromRange", "Value From Range Label", 13.2, optionsPair));
    	// text
    	params.add(new ParameterText("text", "Text", "Some value"));
    	// create controller and add contents to the view
    	FilterConfigPanelController ctrl = new FilterConfigPanelController("Some random filter configuration panel", params);
    	configurationModal.getChildren().add(ctrl.getContents());
	}



	/**
	 * Handler for the auto button. Sets the computation mode of the notebook to
	 * Automatic.
	 */
	@FXML
	private void autoButtonHandler() {
		setComputationMode(ComputationMode.AUTOMATIC);
	}

	/**
	 * Handler for the manual button. Sets the computation mode of the notebook
	 * to Manual.
	 */
	@FXML
	private void manualButtonHandler() {
		setComputationMode(ComputationMode.MANUAL);
	}

	/**
	 * Handler for the compute button. (Re)computes the entire notebook.
	 */
	@FXML
	private void computeButtonHandler() {
		compute();
	}

	/**
	 * Handler for the export button. Exports the notebook to the workspace.
	 */
	@FXML
	private void exportButtonHandler() {
		export();
	}

	/**
	 * Handler for the add cell button. Toggles the visibility of the add cell
	 * button modal.
	 */
	@FXML
	private void addCellButtonHandler() {
		toggleAddCellModalVisibilty();
	}

	/**
	 * Handler for the add computation cell button. Adds a new computation cell
	 * to the notebook and hides the add cell button modal.
	 */
	@FXML
	private void addComputationCellButtonHandler() {
		addComputationCell();
		setAddCellModalInvisible();
	}

	/**
	 * Handler for the add text cell button. Adds a new text cell to the
	 * notebook and hides the add cell button modal.
	 */
	@FXML
	private void addTextCellButtonHandler() {
		addTextCell();		
		setAddCellModalInvisible();
	}

	/**
	 * Sets the computation mode of the notebook to {@code mode}.
	 * 
	 * @param ComputationMode
	 *            mode the computation mode to set the notebook to.
	 */
	public void setComputationMode(ComputationMode mode) {
		model.setComputationMode(mode);
	}

	/**
	 * (Re)computes the entire notebook.
	 */
	public void compute() {
		//TODO: implement. check out model.recomputeFrom.
	}

	/**
	 * Exports the notebook to the workspace.
	 */
	public void export() {
		model.saveNotebook();
	}

	/**
	 * Creates a new computation cell model and corresponding controller and
	 * adds the computation cell to the notebook UI and model.
	 */
	public void addComputationCell() {
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/org/processmining/filterd/gui/fxml/ComputationCell.fxml"));
			ComputationCellModel cell = new ComputationCellModel();
			ComputationCellController newController = new ComputationCellController(this,cell);			
			loader.setController(newController);
			VBox newCellLayout = (VBox) loader.load();
			notebookLayout.getChildren().add(newCellLayout);
			newController.setCellLayout(newCellLayout);
			
			//add cellmodel in notebook model with corresponnding ui cell componenet controller
			model.addCell(cell);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new text cell model and corresponding controller and adds the
	 * computation cell to the notebook UI and model.
	 */
	public void addTextCell() {
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/org/processmining/filterd/gui/fxml/TextCell.fxml"));
			TextCellModel cell = new TextCellModel();
			TextCellController newController = new TextCellController(this, cell);			
			loader.setController(newController);
			VBox newCellLayout = (VBox) loader.load();
			notebookLayout.getChildren().add(newCellLayout);
			newController.setCellLayout(newCellLayout);
			model.addCell(cell);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes the input {@code cell} from the notebook UI and model.
	 * 
	 * @param cell
	 *            the cell to remove from the notebook.
	 */
//	public void removeCell(CellModel cell) {
//		notebookLayout.getChildren().remove(cell.getCellLayout()); // removes the cell from the UI
//		model.removeCell(cell.getCellModel()); // removes the cell from the model
//	}

	/**
	 * Returns the model of the current notebook.
	 * 
	 * @return the {@code NotebookModel} for the current notebook.
	 */
	public NotebookModel getModel() {
		return model;
	}

	/**
	 * Sets a model for the current notebook.
	 * 
	 * @param model
	 *            the {@code NotebookModel} to set for the current notebook.
	 */
	public void setModel(NotebookModel model) {
		//do we need this method? seems dangerous to me.
		this.model = model;
	}

	/**
	 * Returns the layout that contains the cells in this notebook.
	 * 
	 * @return the {@code VBox} that contains all the cells in this notebook.
	 */
	public VBox getLayout() {
		return notebookLayout;
	}

	/**
	 * Sets the layout that contains the cells in this notebook.
	 * 
	 * @param layout
	 *            the {@code VBox} to set as the layout, that contains cells,
	 *            for the current notebook.
	 */
	public void setLayout(VBox layout) {
		//do we need this method? seems dangerous to me.
		this.notebookLayout = layout;
	}

	/**
	 * Make the add cell modal appear.
	 */
	private void setAddCellModalVisible() {
		addCellModal.setVisible(true); // makes the content of the modal (HBox) visible.
		addCellModal.setManaged(true); // makes the modal (HBox) take up space. This option is note available in the Scene Builder.
	}

	/**
	 * Make the add cell modal disappear.
	 */
	private void setAddCellModalInvisible() {
		addCellModal.setVisible(false); // makes the content of the modal (HBox) invisible.
		addCellModal.setManaged(false); // makes the modal (HBox) take up no space. This option is note available in the Scene Builder.
	}

	/**
	 * Toggle the visibility of the add cell modal
	 */
	private void toggleAddCellModalVisibilty() {
		if (addCellModal.isVisible()) {
			setAddCellModalInvisible();
		} else {
			setAddCellModalVisible();
		}
	}
}
