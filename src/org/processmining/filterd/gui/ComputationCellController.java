package org.processmining.filterd.gui;

import java.io.IOException;
import java.util.Collections;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.deckfour.uitopia.api.model.ViewType;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.models.YLog;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import prefuse.data.util.Index;

public class ComputationCellController extends CellController {

	//TODO: add other FXML attributes

	@FXML
	private VBox panelLayout;
	@FXML
	private AnchorPane visualizerPane;
	@FXML
	private ComboBox<YLog> cmbEventLog;
	@FXML
	private ComboBox<ViewType> cmbVisualizers;
	private SwingNode visualizerSwgNode;
	private ConfigurationModalController configurationModal;
	private boolean configurationModalShown;
	

	/**
	 * Gets executed after the constructor. Has access to the @FXML annotated
	 * fields, thus UI elements can be manipulated here.
	 */
	public void initialize() {
		ComputationCellModel model = this.getCellModel();
		// TODO: load event logs in cmbEventLog
		cmbEventLog.getItems().addAll(model.getXLogs());
		cellModel.getProperty().addPropertyChangeListener(new CellModelListeners(this));
		
		getCellModel().getFilters().addListener(new ListChangeListener<FilterButtonModel>() {
			@Override
			public synchronized void onChanged(Change<? extends FilterButtonModel> change) {
				while (change.next()) {
					if (change.wasAdded() && change.wasRemoved()) {
						for (int i = change.getFrom(); i < change.getTo(); i++) {
							int index1 = i;
							Node filter1 = panelLayout.getChildrenUnmodifiable().get(index1);
							int index2 = i+1;
							Node filter2 = panelLayout.getChildrenUnmodifiable().get(index2);
							panelLayout.getChildren().remove(filter1);
							panelLayout.getChildren().add(index2, filter1);
							panelLayout.getChildren().remove(filter2);
							panelLayout.getChildren().add(index1, filter2);
						}
					} else if (change.wasRemoved() && !change.wasAdded()) {
						for (FilterButtonModel removedFilter : change.getRemoved()) {
							System.out.printf("ID: %d ----------\n", change.getFrom());
							System.out.println("Removed: " + removedFilter);
							int index = model.getFilters().indexOf(removedFilter);
							panelLayout.getChildren().remove(index);
						}
					} else if (change.wasAdded() && !change.wasRemoved()) {
						for (FilterButtonModel addedFilter : change.getAddedSubList()) {
							System.out.printf("ID: %d ----------\n", change.getFrom());
							System.out.println("Added: " + addedFilter);
							int index = model.getFilters().indexOf(addedFilter);
							FilterButtonModel filter = model.getFilters().get(index);
							loadFilter(filter, index);
						}
					}
				}
			}
		});
	}

	//TODO: add controller methods

	public ComputationCellController(NotebookController controller, ComputationCellModel model) {
		super(controller, model);
		
		configurationModal = new ConfigurationModalController(this);
		configurationModalShown = false;
	}

	@FXML
	public void addFilter() {
		int index = getCellModel().getFilters().size(); // Index of the new cell, so that we can compute which XLogs are available
		FilterButtonModel filterModel = new FilterButtonModel();
		getCellModel().addFilter(filterModel);
	}
	
	public void loadFilter(FilterButtonModel model, int index) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/gui/fxml/FilterButton.fxml"));
		FilterButtonController newController = new FilterButtonController(this, model);
		loader.setController(newController);
		try {
			HBox newPanelLayout = (HBox) loader.load();
			panelLayout.getChildren().add(index, newPanelLayout);
			newController.setCellLayout(newPanelLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		visualizerSwgNode = new SwingNode();
		visualizerPane.getChildren().add(visualizerSwgNode);
		// We set the anchors for each side of the swingNode to 0 so it fits itself to the anchorPane and gets resized with the cell.
		visualizerPane.setTopAnchor(visualizerSwgNode, 0.0);
		visualizerPane.setBottomAnchor(visualizerSwgNode, 0.0);
		visualizerPane.setLeftAnchor(visualizerSwgNode, 0.0);
		visualizerPane.setRightAnchor(visualizerSwgNode, 0.0);
		// Load Visualizer
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				visualizerSwgNode.setContent(visualizer);
			}
		});
	}
	
	@FXML
	private void toggleConfigurationModal() {
		if(configurationModalShown) {
			hideConfigurationModal();
		} else {
			showConfigurationModal();
		}
	}
	
	public void hideConfigurationModal() {
		// clear the configuration controller (graceful shutdown) 
		configurationModal.clear();
		visualizerPane.getChildren().clear();
		// set visualizer as the content
		visualizerPane.getChildren().add(visualizerSwgNode);
		// set properties w.r.t. parent node (AnchorPane)
		visualizerPane.setTopAnchor(visualizerSwgNode, 0.0);
		visualizerPane.setBottomAnchor(visualizerSwgNode, 0.0);
		visualizerPane.setLeftAnchor(visualizerSwgNode, 0.0);
		visualizerPane.setRightAnchor(visualizerSwgNode, 0.0);
		configurationModalShown = false;
	}
	
	private void showConfigurationModal() {
		// save current visualizer (TODO: is this needed?)
		for(int i = 0; i < visualizerPane.getChildren().size(); i++) {
			if(visualizerPane.getChildren().get(i) instanceof SwingNode) {
				visualizerSwgNode = (SwingNode) visualizerPane.getChildren().get(i);
				break;
			}
		}
		visualizerPane.getChildren().clear();
		// get root component of the configuration modal
		VBox configurationModalRoot = configurationModal.getRoot();
		visualizerPane.getChildren().add(configurationModalRoot);
		// set properties w.r.t. parent node (AnchorPane)
		visualizerPane.setTopAnchor(configurationModalRoot, 0.0);
		visualizerPane.setBottomAnchor(configurationModalRoot, 0.0);
		visualizerPane.setLeftAnchor(configurationModalRoot, 0.0);
		visualizerPane.setRightAnchor(configurationModalRoot, 0.0);
		configurationModalShown = true;
	}
}