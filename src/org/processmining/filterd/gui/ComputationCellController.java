package org.processmining.filterd.gui;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.deckfour.uitopia.api.model.ViewType;
import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.configurations.FilterdEventAttrConfig;
import org.processmining.filterd.configurations.FilterdEventRateConfig;
import org.processmining.filterd.configurations.FilterdTraceFrequencyConfig;
import org.processmining.filterd.configurations.FilterdTracePerformanceConfig;
import org.processmining.filterd.configurations.FilterdTraceSampleConfig;
import org.processmining.filterd.configurations.FilterdTraceStartEventConfig;
import org.processmining.filterd.filters.FilterdEventAttrFilter;
import org.processmining.filterd.filters.FilterdEventRateFilter;
import org.processmining.filterd.filters.FilterdTraceFrequencyFilter;
import org.processmining.filterd.filters.FilterdTracePerformanceFilter;
import org.processmining.filterd.filters.FilterdTraceSampleFilter;
import org.processmining.filterd.filters.FilterdTraceStartEventFilter;
import org.processmining.filterd.gui.ConfigurationModalController.ConfigurationStep;
import org.processmining.filterd.models.YLog;
import org.processmining.filterd.tools.EmptyLogException;

import javafx.collections.ListChangeListener;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

public class ComputationCellController extends CellController {

	private boolean isExpanded;
	private boolean isFullScreen;
	private boolean isConfigurationModalShown;

	private VBox notebookVisualiser;
	private HBox notebookToolbar;
	private SwingNode visualizerSwgNode;
	private ConfigurationModalController configurationModal;

	private ScrollPane scrollPane;

	@FXML private VBox panelLayout;
	@FXML private AnchorPane visualizerPane;
	@FXML private ComboBox<YLog> cmbEventLog;
	@FXML private ComboBox<ViewType> cmbVisualizers;
	@FXML private Rectangle expandButton;
	@FXML private ScrollPane filterPanelScroll;
	@FXML private VBox cell;
	@FXML private HBox cellBody;
	@FXML private Rectangle fullScreenButton;
	@FXML private Circle playButton;
	@FXML private MenuButton menuBtnCellSettings;
	@FXML private Circle prependCellButton;
	@FXML private HBox fullToolbar;
	@FXML private HBox cellToolbar;

	/**
	 * Gets executed after the constructor. Has access to the @FXML annotated
	 * fields, thus UI elements can be manipulated here.
	 */
	public void initialize() {
		ComputationCellModel model = this.getCellModel();
		// Load event logs in cmbEventLog and select "Initial input"
		cmbEventLog.getItems().addAll(model.getInputLogs());
		cmbEventLog.getSelectionModel().selectFirst();
		setXLog();

		// Add listeners to the basic model components
		cellModel.getProperty().addPropertyChangeListener(new CellModelListeners(this));
		// Add listeners for filter buttons
		addFilterButtonListeners();

		// Initialize the visualizer
		visualizerSwgNode = new SwingNode();
		// Add listener for the ComboBoxes and the MenuButton (workaround JavaFX - SwingNode)
		Utilities.JFXSwingFix(visualizerPane, cmbEventLog, visualizerSwgNode);
		Utilities.JFXSwingFix(visualizerPane, cmbVisualizers, visualizerSwgNode);
		Utilities.JFXSwingFix(visualizerPane, menuBtnCellSettings, visualizerSwgNode);
		// bind cellBody width to cellContent width so the visualizations scale properly
		cellBody.maxWidthProperty().bind(controller.getScene().widthProperty().subtract(64));
	}




	public ComputationCellController(NotebookController controller, ComputationCellModel model) {
		super(controller, model);

		configurationModal = new ConfigurationModalController(this);
		this.isConfigurationModalShown = false;

		isExpanded = false;
		isFullScreen = false;
		notebookVisualiser = controller.getNotebookLayout();
		notebookToolbar = controller.getToolbarLayout();
		scrollPane = controller.getScrollPane();
	}

	@FXML
	public void addFilter() {
		if (!this.isConfigurationModalShown
				|| this.configurationModal.getConfigurationStep() != ConfigurationStep.ADD_FILTER) {
			int index = getCellModel().getFilters().size(); // Index of the new cell, so that we can compute which XLogs are available
			// each filter's input log is the output log of the previous filter (except for the first filter which always gets the cell's input log)
			YLog inputLog;
			if(index == 0) {
				inputLog = getCellModel().getInputLog();
				if(inputLog.get() == null) {
					throw new IllegalStateException("Cannot create filters if the input log for the cell is not selected.");
				}
			} else {
				inputLog = getCellModel().getFilters().get(index - 1).getOutputLog();
			}
			FilterButtonModel filterModel = new FilterButtonModel(index, inputLog);
			// set cell output to be the output of the last filter (the filter we just created)
			List<YLog> outputLogs = getCellModel().getOutputLogs();
			outputLogs.clear();
			YLog outputLog = filterModel.getOutputLog();
			outputLog.setName(getCellModel().getCellName() + " output log");
			outputLogs.add(outputLog);
			getCellModel().addFilterModel(index, filterModel);
			loadFilter(index, filterModel);
		}
	}

	public void loadFilter(int index, FilterButtonModel model) {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/org/processmining/filterd/gui/fxml/FilterButton.fxml"));
		FilterButtonController newController = new FilterButtonController(this, model);
		loader.setController(newController);
		try {
			HBox newPanelLayout = (HBox) loader.load();
			panelLayout.getChildren().add(index, newPanelLayout);
			newController.setFilterLayout(newPanelLayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
		newController.selectFilterButton();
		// show the filter list to allow the user to pick which filter she wants to add
		showModalFilterList(model);
	}

	private void addFilterButtonListeners() {
		getCellModel().getFilters().addListener(new ListChangeListener<FilterButtonModel>() {
			@Override
			public void onChanged(Change<? extends FilterButtonModel> change) {
				while (change.next()) {
					if (change.wasUpdated()) {
						for (int i = change.getFrom(); i < change.getTo(); i++) {
							System.out.printf("ID: %d ----------\n", getCellModel().getFilters().get(i).getIndex());
							System.out.println("Updated: " + i + " " + getCellModel().getFilters().get(i));
							System.out.println("SELECTED: " + getCellModel().getFilters().get(i).getSelected());
							// Do something
						}
					} else {
						for (FilterButtonModel removedFilter : change.getRemoved()) {
							System.out.printf("ID: %d ----------\n", removedFilter.getIndex());
							System.out.println("Removed: " + removedFilter);
							// Do something
						}
						for (FilterButtonModel addedFilter : change.getAddedSubList()) {
							System.out.printf("ID: %d ----------\n", addedFilter.getIndex());
							System.out.println("Added: " + addedFilter);
							// Do something
						}
						// Update indices
						for (int i = 0; i < getCellModel().getFilters().size(); i++) {
							getCellModel().getFilters().get(i).setIndex(i);
						}
					}
				}
			}
		});
	}

	public void removeFilter(FilterButtonModel filter) {
		int index = filter.getIndex();
		getCellModel().removeFilter(filter); // Removes the cell from the model
		panelLayout.getChildren().remove(index);	// Removes the layout
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
		visualizerPane.setStyle("-fx-background-color: #ff0000; ");
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
		if (isFullScreen) {
			//add the toolbar and visualiser to their original cell parent
			cellBody.getChildren().add(visualizerPane);
			cellToolbar.getChildren().add(cellToolbar.getChildren().size() - 1, fullToolbar);
			fullToolbar.setPadding(new Insets(0, 0, 0, 0));

			//make the notebook toolbar and scrollpane visible 
			notebookToolbar.setVisible(isFullScreen);
			notebookToolbar.setManaged(isFullScreen);
			scrollPane.setVisible(isFullScreen);
			scrollPane.setManaged(isFullScreen);
			isFullScreen = false;

		} else if (!isFullScreen && !isConfigurationModalShown) {
			//make the notebook toolbar and scrollpane invisible
			notebookToolbar.setVisible(isFullScreen);
			notebookToolbar.setManaged(isFullScreen);
			scrollPane.setVisible(isFullScreen);
			scrollPane.setManaged(isFullScreen);
			//fullToolbar.setStyle("-fx-background-color: #00ffff; ");
			fullToolbar.setPadding(new Insets(5, 20, 5, 10));

			//add the toolbar and visualiser to the notebook
			notebookVisualiser.getChildren().add(fullToolbar);
			notebookVisualiser.getChildren().add(visualizerPane);
			notebookVisualiser.setVgrow(visualizerPane, Priority.ALWAYS);
			isFullScreen = true;
		}
	}

	// Set XLog
	@FXML
	public void setXLog() {
		ComputationCellModel model = this.getCellModel();
		YLog eventLog = cmbEventLog.getValue();
		model.setInputLog(eventLog);
		cmbVisualizers.getItems().addAll(model.getVisualizers());
		cmbVisualizers.getSelectionModel().selectFirst();
	}

	// Load visualizer
	@FXML
	private synchronized void loadVisualizer() {
		ComputationCellModel model = this.getCellModel();
		if (cmbVisualizers.getValue() == Utilities.dummyViewType) {
			// Remove visualizer if "None" is selected
			visualizerPane.getChildren().remove(visualizerSwgNode);
			return;
		}
		JComponent visualizer = model.getVisualization(cmbVisualizers.getValue());
		if (!visualizerPane.getChildren().contains(visualizerSwgNode)) {
			// Add visualizer if not present 
			visualizerPane.getChildren().add(visualizerSwgNode);			
		}
		// We set the anchors for each side of the swingNode to 0 so it fits itself to the anchorPane and gets resized with the cell.
		visualizerPane.setTopAnchor(visualizerSwgNode, 0.0);
		visualizerPane.setBottomAnchor(visualizerSwgNode, 0.0);
		visualizerPane.setLeftAnchor(visualizerSwgNode, 0.0);
		visualizerPane.setRightAnchor(visualizerSwgNode, 0.0);
		// Load Visualizer
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Dimension dimension = new Dimension();
				dimension.setSize(Double.MAX_VALUE, Double.MAX_VALUE);
				//visualizer.setPreferredSize(new Dimension((int)button.getPreferredSize().getWidth()+10, (int)button.getPreferredSize().getHeight()));
				visualizer.setMaximumSize(dimension);
				visualizer.setPreferredSize(dimension);
				visualizerSwgNode.setContent(visualizer);
			}
		});
	}

	public void hideConfigurationModal() {
		if (this.isConfigurationModalShown) {
			ConfigurationStep configurationStep = configurationModal.getConfigurationStep();
			visualizerPane.getChildren().remove(configurationModal.getRoot());
			// set visualizer as the content
			visualizerPane.getChildren().add(visualizerSwgNode);
			// Enable visualizer combobox
			cmbVisualizers.setDisable(false);
			// set properties w.r.t. parent node (AnchorPane)
			visualizerPane.setTopAnchor(visualizerSwgNode, 0.0);
			visualizerPane.setBottomAnchor(visualizerSwgNode, 0.0);
			visualizerPane.setLeftAnchor(visualizerSwgNode, 0.0);
			visualizerPane.setRightAnchor(visualizerSwgNode, 0.0);
			// if filter selection was cancelled, delete the added button
			if (configurationStep == ConfigurationStep.ADD_FILTER) {
				// remove FilterButton
				FilterButtonModel buttonToRemove = getCellModel().getFilters()
						.get(getCellModel().getFilters().size() - 1);
				removeFilter(buttonToRemove);
			}
		}
		this.isConfigurationModalShown = false;
	}

	public void showModalFilterConfiguration(FilterdAbstractConfig filterConfig) {
		if (filterConfig == null) {
			throw new IllegalArgumentException("Fitler configuration cannot be null");
		}
		// Remove visualizer
		visualizerPane.getChildren().remove(visualizerSwgNode);
		// Disable visualizer combobox
		cmbVisualizers.setDisable(true);
		// populate filter configuration modal
		configurationModal.showFilterConfiguration(filterConfig);
		// get root component of the configuration modal
		VBox configurationModalRoot = configurationModal.getRoot();
		visualizerPane.getChildren().add(configurationModalRoot);
		// set properties w.r.t. parent node (AnchorPane)
		visualizerPane.setTopAnchor(configurationModalRoot, 0.0);
		visualizerPane.setBottomAnchor(configurationModalRoot, 0.0);
		visualizerPane.setLeftAnchor(configurationModalRoot, 0.0);
		visualizerPane.setRightAnchor(configurationModalRoot, 0.0);
		this.isConfigurationModalShown = true;
	}

	public void showModalFilterList(FilterButtonModel filterButtonModel) {
		// Remove visualizer
		visualizerPane.getChildren().remove(visualizerSwgNode);
		// Disable visualizer combobox
		cmbVisualizers.setDisable(true);
		List<String> filterOptions = new ArrayList<>();
		filterOptions.add("Trace Start Event Filter");
		filterOptions.add("Trace Frequency");
		filterOptions.add("Trace Sample");
		filterOptions.add("Trace Performance");
		filterOptions.add("Event Attributes");
		filterOptions.add("Event Rate");

		configurationModal.showFilterList(filterOptions, new Callback<String, FilterdAbstractConfig>() {

			public FilterdAbstractConfig call(String userSelection) {
				ComputationCellModel model = (ComputationCellModel) cellModel;
				if(model.getInputLog().get() == null) {
					throw new IllegalStateException("No input log selected");
				}
				FilterdAbstractConfig filterConfig = null;
				switch(userSelection) {
					case "Trace Start Event Filter":
						try {
							filterConfig = new FilterdTraceStartEventConfig(model.getInputLog().get(),
									new FilterdTraceStartEventFilter());
						} catch (EmptyLogException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case "Trace Frequency":
						try {
							filterConfig = new FilterdTraceFrequencyConfig(model.getInputLog().get(),
									new FilterdTraceFrequencyFilter());
						} catch (EmptyLogException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case "Trace Sample":
						try {
							filterConfig = new FilterdTraceSampleConfig(model.getInputLog().get(),
									new FilterdTraceSampleFilter());
						} catch (EmptyLogException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case "Trace Performance":
						try {
							filterConfig = new FilterdTracePerformanceConfig(model.getInputLog().get(),
									new FilterdTracePerformanceFilter());
						} catch (EmptyLogException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case "Event Attributes":
						try {
							filterConfig = new FilterdEventAttrConfig(model.getInputLog().get(),
									new FilterdEventAttrFilter());
						} catch (EmptyLogException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case "Event Rate":
						try {
							filterConfig = new FilterdEventRateConfig(model.getInputLog().get(),
									new FilterdEventRateFilter());
						} catch (EmptyLogException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					default:
						throw new IllegalArgumentException("Unsupported filter selected");
				}
				// TODO: set cell status to OUT_OF_DATE
				filterButtonModel.setFilterConfig(filterConfig);
				return filterConfig;
			}

		});
		VBox configurationModalRoot = configurationModal.getRoot();
		visualizerPane.getChildren().add(configurationModalRoot);
		visualizerPane.setTopAnchor(configurationModalRoot, 0.0);
		visualizerPane.setBottomAnchor(configurationModalRoot, 0.0);
		visualizerPane.setLeftAnchor(configurationModalRoot, 0.0);
		visualizerPane.setRightAnchor(configurationModalRoot, 0.0);
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
			cell.setPrefHeight(Region.USE_COMPUTED_SIZE);
		}
	}

	public ConfigurationModalController getConfigurationModal() {
		return this.configurationModal;
	}
}
