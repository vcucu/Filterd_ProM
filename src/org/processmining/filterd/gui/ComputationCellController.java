package org.processmining.filterd.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.deckfour.uitopia.api.model.ViewType;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.models.YLog;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ComputationCellController extends CellController {

	//TODO: add other FXML attributes
	private List<FilterButtonModel> filters;
	private ObservableList<FilterButtonModel> filtersOL;

	@FXML
	private VBox panelLayout;
	@FXML
	private AnchorPane visualizerPane;
	@FXML
	private ComboBox<YLog> cmbEventLog;
	@FXML
	private ComboBox<ViewType> cmbVisualizers;

	/**
	 * Gets executed after the constructor. Has access to the @FXML annotated
	 * fields, thus UI elements can be manipulated here.
	 */
	public void initialize() {
		ComputationCellModel model = this.getCellModel();
		// TODO: load event logs in cmbEventLog
		cmbEventLog.getItems().addAll(model.getXLogs());
		cellModel.getProperty().addPropertyChangeListener(new CellModelListeners(this));
	}

	//TODO: add controller methods

	public ComputationCellController(NotebookController controller, ComputationCellModel model) {
		super(controller, model);
		filters = new ArrayList<>();
		filtersOL = FXCollections.observableList(filters);

		filtersOL.addListener(new ListChangeListener<Object>() {
			@Override
			public void onChanged(ListChangeListener.Change change) {
				System.out.println("Added new filter!");
			}
		});
	}

	@FXML
	public void addFilter() {
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/org/processmining/filterd/gui/fxml/FilterButton.fxml"));
			FilterButtonController newController = new FilterButtonController(this);
			loader.setController(newController);
			HBox newLayout = (HBox) loader.load();
			panelLayout.getChildren().add(newLayout);
			newController.setCellLayout(newLayout);
			filtersOL.add(newController.getModel());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ObservableList<FilterButtonModel> getFiltersOL() {
		return filtersOL;
	}

	public void setFiltersOL(ObservableList<FilterButtonModel> filtersOL) {
		this.filtersOL = filtersOL;
	}

	public VBox getPanelLayout() {
		return panelLayout;
	}

	public void setPanelLayout(VBox panelLayout) {
		this.panelLayout = panelLayout;
	}

	/**
	 * Sets the cell model of the current cell. This method is overridden so it
	 * only takes a ComputationCellModel instead of all subclasses of CellModel.
	 * 
	 * @param cellModel
	 *            The ComputationCellModel to set.
	 * @throws IllegalArgumentException
	 *             Thrown if cellModel is not of type ComputationCellModel.
	 */

	@Override
	public void setCellModel(CellModel cellModel) throws IllegalArgumentException {
		if (!(cellModel instanceof ComputationCellModel)) {
			//CellModel is not of type ComputationCellModel.
			throw new IllegalArgumentException(
					"ComputationCellController.setCellModel: expected object of type ComputationCellModel as input, instead got object of type"
							+ cellModel.getClass().getCanonicalName());
		}
		super.setCellModel(cellModel);
	}

	/**
	 * Gets the cell model of the current cell. This method is overridden so it
	 * returns an object of type ComputationCellModel, this prevents us from
	 * having to cast the returned object to ComputationCellModel every single
	 * time it is called.
	 */
	@Override
	public ComputationCellModel getCellModel() {
		return (ComputationCellModel) super.getCellModel();
	}

	@FXML
	public void prependCellButtonHandler() {
		// TODO Add cell above the one that generated this
	}

	// Set XLog
	@FXML
	public void setXLog(ActionEvent event) {
		ComputationCellModel model = this.getCellModel();
		XLog eventLog = cmbEventLog.getValue().get();
		model.setXLog(eventLog);
		cmbVisualizers.getItems().addAll(model.getVisualizers());
	}

	// Load visualizer
	@FXML
	private synchronized void loadVisualizer(ActionEvent event) {
		ComputationCellModel model = this.getCellModel();
		JComponent visualizer = model.getVisualization(cmbVisualizers.getValue());
		// Add a SwingNode to the Visualizer pane
		SwingNode swgNode = new SwingNode();
		visualizerPane.getChildren().add(swgNode);
		// We set the anchors for each side of the swingNode to 0 so it fits itself to the anchorPane and gets resized with the cell.
		visualizerPane.setTopAnchor(swgNode, 0.0);
		visualizerPane.setBottomAnchor(swgNode, 0.0);
		visualizerPane.setLeftAnchor(swgNode, 0.0);
		visualizerPane.setRightAnchor(swgNode, 0.0);
		// Load Visualizer
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				swgNode.setContent(visualizer);
			}
		});
	}

}