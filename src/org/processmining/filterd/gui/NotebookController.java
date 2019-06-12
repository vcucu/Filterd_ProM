package org.processmining.filterd.gui;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class contains the controller for the notebook.
 * 
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
	private Label computeButton;
	@FXML
	private Region computeButtonImage;
	@FXML
	private Button exportButton;
	@FXML
	private VBox cellsLayout;
	@FXML
	private Button appendCellButton;
	@FXML
	private HBox addCellHBox;
	@FXML
	private Button addComputationCellButton;
	@FXML
	private Button addTextCellButton;
	@FXML
	private VBox notebookLayout;
	@FXML
	private HBox toolbarLayout;


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

	public VBox getNotebookLayout() {
		return notebookLayout;
	}

	public VBox getCellsLayout() {
		return cellsLayout;
	}

	public HBox getToolbarLayout() {
		return toolbarLayout;
	}

	/**
	 * Gets executed after the constructor. Has access to the @FXML annotated
	 * fields, thus UI elements can be manipulated here.
	 */
	public void initialize() {
		// Add cell listener 
		cellListeners();

		// Initialize AddCellModal
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/org/processmining/filterd/gui/fxml/AddCell.fxml"));
			loader.setController(new AddCellController());
			addCellHBox.getChildren().add((HBox) loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// change compute button text (play / pause symbols) when the computation stops / starts
		this.model.isComputingProperty().addListener(new ChangeListener<Boolean>() {

			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							computeButtonImage.getStyleClass().remove("play-solid");
							computeButtonImage.getStyleClass().add("pause-solid");
						}
					});
				} else {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							computeButtonImage.getStyleClass().remove("pause-solid");
							computeButtonImage.getStyleClass().add("play-solid");
						}
					});
				}
			}
		});

	}

	public void cellListeners() {
		model.getCells().addListener(new ListChangeListener<CellModel>() {
			@Override
			public void onChanged(Change<? extends CellModel> change) {
				while (change.next()) {
					if (change.wasUpdated()) {
						for (int i = change.getFrom(); i < change.getTo(); i++) {
							System.out.printf("ID: %d ----------\n", model.getCells().get(i).getIndex());
							System.out.println("Updated: " + i + " " + model.getCells().get(i));
							// Do something
						}
					} else {
						for (CellModel removedCell : change.getRemoved()) {
							System.out.printf("ID: %d ----------\n", removedCell.getIndex());
							System.out.println("Removed: " + removedCell);
							// Do something
						}
						for (CellModel addedCell : change.getAddedSubList()) {
							System.out.printf("ID: %d ----------\n", addedCell.getIndex());
							System.out.println("Added: " + addedCell);
							// Do something
						}
						// Update indices
						for (int i = 0; i < model.getCells().size(); i++) {
							model.getCells().get(i).setIndex(i);
						}
					}
				}
			}
		});
	}

	/**
	 * Handler for the auto button. Sets the computation mode of the notebook to
	 * Automatic.
	 */
	@FXML
	public void autoButtonHandler() {
		setComputationMode(ComputationMode.AUTOMATIC);
	}

	/**
	 * Handler for the manual button. Sets the computation mode of the notebook
	 * to Manual.
	 */
	@FXML
	public void manualButtonHandler() {
		setComputationMode(ComputationMode.MANUAL);
	}

	/**
	 * Handler for the compute button. (Re)computes the entire notebook.
	 */
	@FXML
	private void computeButtonHandler() {
		// TODO: set compute button icon to play / pause w.r.t. this.isComputing
		if (model.isComputing()) {
			model.cancelCompute();
		} else {
			model.compute();
		}
	}

	/**
	 * Handler for the export button. Exports the notebook to the workspace.
	 */
	@FXML
	private void exportButtonHandler() {
		//create pop up to confirm export
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Export notebook");
		//alert.setHeaderText("Look, a Confirmation Dialog");
		alert.setContentText("Are you sure you want to export this notebook to workspace?");
		ButtonType buttonYes = new ButtonType("Yes", ButtonData.OK_DONE);
		ButtonType buttonNo = new ButtonType("No", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonYes, buttonNo);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.setAlwaysOnTop(true);// make sure window always at front when open

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonYes) {
			//user chose Yes so export cell
			export();
		}
		//user chose No or closed the dialog don't export

	}

	/**
	 * Handler for the add cell button. Toggles the visibility of the add cell
	 * button modal.
	 */
	@FXML
	private void appendCellButtonHandler() {
		int index = model.getCells().size();
		toggleAddCellModal(index);
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
	 * Exports the notebook to the workspace.
	 */
	public void export() {
		model.saveNotebook();
	}

	/**
	 * Creates a new ComputationCell model and adds it to the observable list.
	 */
	public void addComputationCell(int index) {
		ComputationCellModel cellModel = new ComputationCellModel(model.getPromContext(), index,
				model.getPromCanceller(), model.getOutputLogsTill(index));
		model.addCell(index, cellModel);
		loadCell(cellModel);
	}

	/**
	 * Creates a new TextCell model and adds it to the observable list.
	 */
	public void addTextCell(int index) {
		TextCellModel cellModel = new TextCellModel(model.getPromContext(), index);
		model.addCell(index, cellModel);
		loadCell(cellModel);
	}

	/**
	 * Given a cell model, this method creates a corresponding controller and
	 * adds it the notebook UI.
	 * @param cell The cell to load into the notebook.
	 */
	private void loadCell(CellModel cell) {
		FXMLLoader loader = new FXMLLoader();
		CellController newController;
		if (cell.getClass().isAssignableFrom(ComputationCellModel.class)) {
			// Cell to be added is a Computation cell
			loader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/gui/fxml/ComputationCell.fxml"));
			newController = new ComputationCellController(this, (ComputationCellModel) cell);
		} else {
			// Cell to be added is a Text cell
			loader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/gui/fxml/TextCell.fxml"));
			newController = new TextCellController(this, (TextCellModel) cell);
		}
		loader.setController(newController);
		VBox newCellLayout;
		try {
			newCellLayout = (VBox) loader.load();
			cellsLayout.getChildren().add(cell.getIndex(), newCellLayout);
			newController.setCellLayout(newCellLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads a list of cells into the notebook. Ignores null or an empty list.
	 * @param cells The list of cells to load into the notebook.
	 */
	public void loadCells(List<CellModel> cells) {
		if (cells != null && !cells.isEmpty()) {
			for (CellModel cell : cells) {
				// TODO: make sure we iterate through the cells in order of their index.
				cell.setContext(model.getPromContext()); // set the context for the cell.
				if (cell.getClass() == ComputationCellModel.class) {
					((ComputationCellModel) cell).setCanceller(model.getPromCanceller()); // set the canceller for this cell.
					((ComputationCellModel) cell).setInputLogs(model.getOutputLogsTill(cell.getIndex())); // set the available input logs for this cell.
					// TODO: set the correct input log for the cell. In order to do this cell need to start keeping track of the index of the cell whoses output event log they are using.
				}
				model.addCell(cell); // add the cell to the list of cells in the NotebookModel.
				loadCell(cell); // Load the UI elements of the Cell.
			}
		}
	}

	/**
	 * Removes the input {@code cell} from the notebook model. Removal from the
	 * UI should happen through an actionListener.
	 * 
	 * @param cell
	 *            the cell to remove from the notebook.
	 */
	public void removeCell(CellModel cell) {
		int index = cell.getIndex();
		model.removeCell(cell); // Removes the cell from the model
		cellsLayout.getChildren().remove(index); // Removes the layout
	}

	/**
	 * Returns the model of the current notebook.
	 * 
	 * @return the {@code NotebookModel} for the current notebook.
	 */
	public NotebookModel getModel() {
		return model;
	}

	/**
	 * Returns the scene of the notebook visualizer.
	 * 
	 * @return The scene of the notebook visualizer.
	 */
	public Scene getScene() {
		return notebookLayout.getScene();
	}

	/**
	 * Make the add cell modal disappear.
	 */
	public void hideAddCellModal() {
		addCellHBox.setVisible(false); // makes the content of the modal (HBox) invisible.
		addCellHBox.setManaged(false); // makes the modal (HBox) take up no space. This option is note available in the
										// Scene Builder.
		cellsLayout.getChildren().remove(addCellHBox);
	}

	/**
	 * Make the add cell modal appear.
	 */
	// Do NOT make this public! - Omar
	private void showAddCellModal(int index) {
		addCellHBox.setVisible(true); // makes the content of the modal (HBox) visible.
		addCellHBox.setManaged(true); // makes the modal (HBox) take up space. This option is note available in the
										// Scene Builder.
		cellsLayout.getChildren().add(index, addCellHBox);
	}

	/**
	 * Toggle the visibility of the add cell modal
	 */
	public void toggleAddCellModal(int index) {
		boolean isVisible = addCellHBox.isVisible();
		boolean sameIndex = (index == cellsLayout.getChildrenUnmodifiable().indexOf(addCellHBox));
		if (!isVisible) {
			showAddCellModal(index);
		} else {
			// It's visible
			if (sameIndex) {
				hideAddCellModal();
			} else {
				hideAddCellModal();
				showAddCellModal(index);
			}
		}
	}

	/**
	 * Prints an XML of the notebook generated by JAXB
	 */
	@FXML
	public void printXML() {
		try {
			//Create JAXB Context
			JAXBContext jaxbContext = JAXBContext.newInstance(NotebookModel.class, TextCellModel.class,
					ComputationCellModel.class, FilterButtonModel.class);

			//Create Marshaller
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			//Required formatting??
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			//Print XML String to Console
			StringWriter sw = new StringWriter();

			//Write XML to StringWriter
			jaxbMarshaller.marshal(model, sw);

			//Store XML to File
			//            File file = new File("test.xml");
			//            jaxbMarshaller.marshal(model, file);

			// Print the xml to the console
			String xmlContent = sw.toString();
			System.out.println(xmlContent);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	class AddCellController {

		/**
		 * Handler for the add computation cell button. Adds a new computation
		 * cell to the notebook and hides the add cell button modal.
		 */
		@FXML
		private void addComputationCellButtonHandler() {
			int index = cellsLayout.getChildrenUnmodifiable().indexOf(addCellHBox);
			hideAddCellModal();
			addComputationCell(index);
		}

		/**
		 * Handler for the add text cell button. Adds a new text cell to the
		 * notebook and hides the add cell button modal.
		 */
		@FXML
		private void addTextCellButtonHandler() {
			int index = cellsLayout.getChildrenUnmodifiable().indexOf(addCellHBox);
			hideAddCellModal();
			addTextCell(index);
		}

	}

}
