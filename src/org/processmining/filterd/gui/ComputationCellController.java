package org.processmining.filterd.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.deckfour.uitopia.api.model.ViewType;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.configurations.FilterdTraceStartEventConfig;
import org.processmining.filterd.filters.FilterdTraceStartEventFilter;
import org.processmining.filterd.gui.ConfigurationModalController.ConfigurationStep;
import org.processmining.filterd.models.YLog;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

public class ComputationCellController extends CellController {

	//TODO: add other FXML attributes

	private boolean isExpanded;
	private boolean isFullScreen;
	private VBox notebookVisualiser;
	private HBox notebookToolbar;

	@FXML
	private VBox fullScreen;

	@FXML
	private VBox panelLayout;
	@FXML
	private ComboBox<YLog> cmbEventLog;
	@FXML
	private ComboBox<ViewType> cmbVisualizers;

	private ConfigurationModalController configurationModal;
	private boolean isConfigurationModalShown;
	private ScrollPane scrollPane;


	@FXML
	private Rectangle expandButton;
	@FXML
	private ScrollPane filterPanelScroll;
	@FXML
	private VBox cell;
	@FXML
	private Rectangle fullScreenButton;
	@FXML
	private Circle playButton;
	@FXML
	private MenuButton CellSettings;
	@FXML
	private Circle prependCellButton;

	@FXML
	private HBox fullToolbar;
	@FXML
	private HBox cellBody;
	
	private VisualizerPanelController visualizerPanelController;
	
	
	/**
	 * Gets executed after the constructor. Has access to the @FXML annotated
	 * fields, thus UI elements can be manipulated here.
	 */
	public void initialize() {
		ComputationCellModel model = this.getCellModel();
		// TODO: load event logs in cmbEventLog
		cmbEventLog.getItems().addAll(model.getInputLogs());
		//add listeners to the basic model components
		cellModel.getProperty().addPropertyChangeListener(new CellModelListeners(this));

		getCellModel().getFilters().addListener(new ListChangeListener<FilterButtonModel>() {
			@Override
			public void onChanged(Change<? extends FilterButtonModel> change) {
				while (change.next()) {
					if (change.wasPermutated()) {
						for (int i = change.getFrom(); i < change.getTo(); i++) {
							System.out.printf("ID: %d ----------\n", getCellModel().getFilters().get(i).getIndex());
							System.out.println("Permuted: " + i + " " + getCellModel().getFilters().get(i));
						}
					} else if (change.wasUpdated()) {
						for (int i = change.getFrom(); i < change.getTo(); i++) {
							System.out.printf("ID: %d ----------\n", getCellModel().getFilters().get(i).getIndex());
							System.out.println("Updated: " + i + " " + getCellModel().getFilters().get(i));
							System.out.println("SELECTED: " + getCellModel().getFilters().get(i).getSelected());
							getCellModel().getFilterControllers().get(getCellModel().getFilters().get(i).getIndex())
									.updateFilterButtonView();
						}
					} else {
						for (FilterButtonModel removedFilter : change.getRemoved()) {
							System.out.printf("ID: %d ----------\n", removedFilter.getIndex());
							System.out.println("Removed: " + removedFilter);
						}
						for (FilterButtonModel addedFilter : change.getAddedSubList()) {
							System.out.printf("ID: %d ----------\n", addedFilter.getIndex());
							System.out.println("Added: " + addedFilter);
						}
						for (int i = 0; i < getCellModel().getFilters().size(); i++) {
							getCellModel().getFilters().get(i).setIndex(i);
						}
					}
				}
			}
		});
		
		// add the visualizerPanel
		FXMLLoader loader = new FXMLLoader();
		loader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/gui/fxml/VisualizerPanel.fxml"));
		visualizerPanelController = new VisualizerPanelController();
		loader.setController(visualizerPanelController);
		try {
			AnchorPane visualizerPanel = (AnchorPane) loader.load();
			cellBody.getChildren().add(visualizerPanel);
			cellBody.setHgrow(visualizerPanel, Priority.ALWAYS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	//TODO: add controller methods

	public ComputationCellController(NotebookController controller, ComputationCellModel model) {
		super(controller, model);

		configurationModal = new ConfigurationModalController(this);
		this.isConfigurationModalShown = false;

		isExpanded = false;
		isFullScreen = false;
		notebookVisualiser = controller.getNotebookVisualiser();
		notebookToolbar = controller.getNotebookToolbar();
		scrollPane = controller.getScrollPane();
	}

	@FXML
	public void addFilter() {
		if (!this.isConfigurationModalShown
				|| this.configurationModal.getConfigurationStep() != ConfigurationStep.ADD_FILTER) {
			int index = getCellModel().getFilters().size(); // Index of the new cell, so that we can compute which XLogs are available
			FilterButtonModel filterModel = new FilterButtonModel(index);
			getCellModel().addFilterModel(index, filterModel);
			loadFilter(index, filterModel);
		}
	}

	public void loadFilter(int index, FilterButtonModel model) {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/org/processmining/filterd/gui/fxml/FilterButton.fxml"));
		FilterButtonController newController = new FilterButtonController(this, model);
		loader.setController(newController);
		getCellModel().addFilterController(index, newController);
		try {
			HBox newPanelLayout = (HBox) loader.load();
			panelLayout.getChildren().add(index, newPanelLayout);
			newController.setCellLayout(newPanelLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
		newController.selectFilterButton();
		// show the filter list to allow the user to pick which filter she wants to add
		showModalFilterList(model);
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

	/**
	 * Handler added to the expansion button responsible for increasing the cell
	 * size to the window size
	 */
	@FXML
	public void handleExpandVisualiser() {
		//visualizerPane.setStyle("-fx-background-color: #ff0000; ");
		if (isExpanded) {
			//make cell go to default size
			isExpanded = false;
			//unbind from window size
			cell.prefHeightProperty().unbind();
			//set the PrefHeight to what it is by default
			cell.setPrefHeight(cell.USE_COMPUTED_SIZE);
		} else if (!isExpanded && !isConfigurationModalShown) {
			isExpanded = true;
			//set height of cell to be the size of the 'window'
			cell.prefHeightProperty()
					.bind(notebookVisualiser.heightProperty().subtract(notebookToolbar.heightProperty()));
		}
		//extend visualizerPane over the filter pane
		filterPanelScroll.setVisible(!isExpanded);
		filterPanelScroll.setManaged(!isExpanded);
	}

	/**
	 * Handler added to the fullScreenButton responsible for making the cell go
	 * full screen
	 */
	@FXML
	public void handleFullScreen() {
		AnchorPane visualizerPane = visualizerPanelController.getVisualizerPanel();
		if (isFullScreen) {
			//set visualiserPane as child of corresponding enlarged cell
			cellBody.getChildren().add(visualizerPane);
			//remove fullscreen cell child from the notebookVisualiser
			notebookVisualiser.getChildren().remove(fullScreen);
			//make the list of cells visible 
			scrollPane.setVisible(isFullScreen);
			scrollPane.setManaged(isFullScreen);
			isFullScreen = false;
		} else if (!isFullScreen && !isConfigurationModalShown) {
			//notebookVisualiser.setStyle("-fx-background-color: #00ffff; ");
			//hide the scrollpane containing the list of cells in notebook
			scrollPane.setVisible(isFullScreen);
			scrollPane.setManaged(isFullScreen);
			//if the fullscreen hasn't been loaded yet then load
			if (fullScreen == null) {
				//load the fullScreen fxml 
				FXMLLoader loader = new FXMLLoader(
						getClass().getResource("/org/processmining/filterd/gui/fxml/FullScreenCell.fxml"));
				loader.setController(this);
				try {
					fullScreen = (VBox) loader.load();
					//fullScreen.setStyle("-fx-background-color: #0000ff; ");
					//fullToolbar.setStyle("-fx-background-color: ##008080; ");
					fullScreen.setVgrow(visualizerPane, Priority.ALWAYS);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			notebookVisualiser.getChildren().add(fullScreen);
			//add visualiserPane as a child, automatically removed from list of children 
			//of the corresponding Computation cell, must be re-added when undoing fullscreen mode 
			fullScreen.getChildren().add(visualizerPane);
			notebookVisualiser.setVgrow(fullScreen, Priority.ALWAYS);
			isFullScreen = true;
		}
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
		// set the visualizer to the visualizer panel.
		visualizerPanelController.setVisualizer(visualizer);
	}

	public void hideConfigurationModal() {
		if (this.isConfigurationModalShown) {
			ConfigurationStep configurationStep = configurationModal.getConfigurationStep();
			// set visualizer as the content
			visualizerPanelController.hideModal();
			// if filter selection was cancelled, delete the added button
			if (configurationStep == ConfigurationStep.ADD_FILTER) {
				// remove model
				FilterButtonModel buttonToRemove = getCellModel().getFilters()
						.get(getCellModel().getFilters().size() - 1);
				getCellModel().removeFilterModel(buttonToRemove);
				// remove controller
				FilterButtonController controllerToRemove = getCellModel().getFilterControllers()
						.get(getCellModel().getFilterControllers().size() - 1);
				getCellModel().removeFilterController(controllerToRemove);
				getPanelLayout().getChildren().remove(controllerToRemove.getFilterLayout());
			}
		}
		this.isConfigurationModalShown = false;
	}

	public void showModalFilterConfiguration(FilterdAbstractConfig filterConfig) {
		if (filterConfig == null) {
			throw new IllegalArgumentException("Fitler configuration cannot be null");
		}
		// populate filter configuration modal
		configurationModal.showFilterConfiguration(filterConfig);
		// get root component of the configuration modal
		VBox configurationModalRoot = configurationModal.getRoot();
		visualizerPanelController.showModal(configurationModalRoot);
		this.isConfigurationModalShown = true;
	}

	public void showModalFilterList(FilterButtonModel filterButtonModel) {
		List<String> filterOptions = new ArrayList<>();
		filterOptions.add("Filter 1");
		filterOptions.add("Filter 2");
		configurationModal.showFilterList(filterOptions, new Callback<String, FilterdAbstractConfig>() {

			public FilterdAbstractConfig call(String userSelection) {
				// TODO: create a new filter config based on the user's selection
				ComputationCellModel model = (ComputationCellModel) cellModel;
				FilterdAbstractConfig filterConfig = new FilterdTraceStartEventConfig(model.getInputLog(),
						new FilterdTraceStartEventFilter());
				filterButtonModel.setFilterConfig(filterConfig);
				// TODO: set cell status to OUT_OF_DATE
				return filterConfig;
			}

		});
		VBox configurationModalRoot = configurationModal.getRoot();
		visualizerPanelController.showModal(configurationModalRoot);
		this.isConfigurationModalShown = true;
	}

	@Override
	public void show() {
		super.show();
		if (isExpanded) {
			cell.prefHeightProperty()
					.bind(notebookVisualiser.heightProperty().subtract(notebookToolbar.heightProperty()));
		}
	}

	@Override
	public void hide() {
		super.hide();
		if (isExpanded) {
			cell.prefHeightProperty().unbind();
			//set the PrefHeight to what it is by default
			cell.setPrefHeight(cell.USE_COMPUTED_SIZE);
		}
	}

	public ConfigurationModalController getConfigurationModal() {
		return this.configurationModal;
	}
}