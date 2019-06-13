package org.processmining.filterd.gui;

import java.awt.BorderLayout;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.deckfour.uitopia.api.model.ViewType;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.configurations.FilterdEventAttrConfig;
import org.processmining.filterd.configurations.FilterdEventRateConfig;
import org.processmining.filterd.configurations.FilterdModifMergeSubsequentConfig;
import org.processmining.filterd.configurations.FilterdTraceAttrConfig;
import org.processmining.filterd.configurations.FilterdTraceEndEventConfig;
import org.processmining.filterd.configurations.FilterdTraceFollowerConfig;
import org.processmining.filterd.configurations.FilterdTraceFrequencyConfig;
import org.processmining.filterd.configurations.FilterdTracePerformanceConfig;
import org.processmining.filterd.configurations.FilterdTraceSampleConfig;
import org.processmining.filterd.configurations.FilterdTraceStartEventConfig;
import org.processmining.filterd.configurations.FilterdTraceTimeframeConfig;
import org.processmining.filterd.configurations.FilterdTraceTrimConfig;
import org.processmining.filterd.configurations.FilterdTracesHavingEventConfig;
import org.processmining.filterd.filters.FilterdEventAttrFilter;
import org.processmining.filterd.filters.FilterdEventRateFilter;
import org.processmining.filterd.filters.FilterdModifMergeSubsequentFilter;
import org.processmining.filterd.filters.FilterdTraceAttrFilter;
import org.processmining.filterd.filters.FilterdTraceEndEventFilter;
import org.processmining.filterd.filters.FilterdTraceFollowerFilter;
import org.processmining.filterd.filters.FilterdTraceFrequencyFilter;
import org.processmining.filterd.filters.FilterdTracePerformanceFilter;
import org.processmining.filterd.filters.FilterdTraceSampleFilter;
import org.processmining.filterd.filters.FilterdTraceStartEventFilter;
import org.processmining.filterd.filters.FilterdTraceTimeframeFilter;
import org.processmining.filterd.filters.FilterdTraceTrimFilter;
import org.processmining.filterd.filters.FilterdTracesHavingEvent;
import org.processmining.filterd.gui.ConfigurationModalController.ConfigurationStep;
import org.processmining.filterd.models.YLog;
import org.processmining.filterd.plugins.FilterdVisualizer;
import org.processmining.filterd.tools.EmptyLogException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

public class ComputationCellController extends CellController {

	private boolean isExpanded;
	private boolean isFullScreen;
	private boolean isConfigurationModalShown;

	private JPanel fullScreenPanel;
	private VBox notebookLayout;
	private HBox notebookToolbar;
	private SwingBubble visualizerSwgWrap;
	private ConfigurationModalController configurationModal;

	@FXML private VBox panelLayout;
	@FXML private AnchorPane visualizerPane;
	@FXML private ComboBox<YLog> cmbEventLog;
	@FXML private ComboBox<ViewType> cmbVisualizers;
	@FXML private Label expandButton;
	@FXML private ScrollPane filterPanelScroll;
	@FXML private VBox cell;
	@FXML private HBox cellBody;
	@FXML private Label fullScreenButton;
	@FXML private Label playButton;
	@FXML private Label computeButton;
	@FXML private MenuButton menuBtnCellSettings;
	@FXML private Label prependCellButton;
	@FXML private HBox fullToolbar;
	@FXML private HBox cellToolbar;
	@FXML private Label lblNumEventLogs;

	public ComputationCellController(ComputationCellModel model) {
		super(null, model);
	}

	public ComputationCellController(NotebookController controller, ComputationCellModel model) {
		super(controller, model);

		configurationModal = new ConfigurationModalController(this);
		this.isConfigurationModalShown = false;

		isExpanded = false;
		isFullScreen = false;
		notebookLayout = controller.getNotebookLayout();
		notebookToolbar = controller.getToolbarLayout();
	}

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
		cellModel.getProperty().addPropertyChangeListener(new ComputationCellModelListeners(this));
		// binding for cell name
		this.cellName.setText(this.cellModel.getCellName());
		this.cellModel.cellNameProperty().addListener(new ChangeListener<String>() {

			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!cellName.getText().equals(newValue)) {
					cellName.setText(newValue);
				}
			}
		});
		// Add listeners for filter buttons
		addFilterButtonListeners();
		// bind the cell name to the cell name variable.
		getCellModel().bindCellName(cellName.textProperty());
		
		// Change compute button icon (play / pause) when the computation stops / starts
		this.getCellModel().isComputingProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				Utilities.changeIcon(computeButton, "play-solid", "pause-solid");
			} else {
				Utilities.changeIcon(computeButton, "pause-solid", "play-solid");
			}
		});
		
		// Update label showing number of output logs when there's a change
		updateNoOfOutputs(getCellModel().getOutputLogs().size());
		
		this.getCellModel().getOutputLogs().addListener((ListChangeListener<? super YLog>) change -> {
			updateNoOfOutputs(getCellModel().getOutputLogs().size());
		});

		// Initialize the visualizer
		visualizerSwgWrap = new SwingBubble();
		// bind cellBody width to cellContent width so the visualizations scale properly
		cellBody.maxWidthProperty().bind(controller.getScene().widthProperty().subtract(64));
		if (!model.getFilters().isEmpty()) {
			// if there already exist filters inside this cell generate their controllers.
			generateFilterButtonControllers();
			hideConfigurationModal(false); // hides the configuration modal that spawn upon creating a filter controller.
		}
	}

	@FXML
	public void addFilter() {
		if (!this.isConfigurationModalShown
				|| this.configurationModal.getConfigurationStep() != ConfigurationStep.ADD_FILTER) {
			int index = getCellModel().getFilters().size(); // Index of the new cell, so that we can compute which XLogs are available
			FilterButtonModel filterModel = new FilterButtonModel(index);
			// if cell was already computed and is not out-of-date, we can set the input log of the new filter to be the output of the previous one
			if(index > 0 &&
				getCellModel().getFilters().get(index - 1).getOutputLog() != null &&
				getCellModel().getStatusBar() == CellStatus.IDLE) {
				filterModel.setInputLog(getCellModel().getFilters().get(index - 1).getOutputLog());
			} else {
			}
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
		showModalFilterList(newController, model);
	}

	/**
	 * This is to generate a controller for every filter model. Used when importing a notebook from the workspace.
	 */
	public void generateFilterButtonControllers() {
		for (FilterButtonModel filter : getCellModel().getFilters()) {
			loadFilter(filter.getIndex(), filter);
		}
	}

	public void addFilterButtonListeners() {
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
		this.hideConfigurationModal(false);
		int index = filter.getIndex();
		getCellModel().removeFilter(filter); // Removes the cell from the model
		panelLayout.getChildren().remove(index); // Removes the layout
	}

	public VBox getPanelLayout() {
		return panelLayout;
	}

	public void setPanelLayout(VBox panelLayout) {
		this.panelLayout = panelLayout;
	}


	public void changeInputLogsCombo(List <YLog> logs){
		cmbEventLog.setItems((ObservableList<YLog>) logs);
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
			cell.setPrefHeight(Region.USE_COMPUTED_SIZE);
		} else if (!isExpanded && !isConfigurationModalShown) {
			isExpanded = true;
			//set height of cell to be the size of the 'window'
			cell.prefHeightProperty().bind(notebookLayout.heightProperty().subtract(notebookToolbar.heightProperty()));
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
		if (!isFullScreen && !isConfigurationModalShown
				&& cmbVisualizers.getValue() != Utilities.dummyViewType) {
			// Style toolbar for fullscreen mode
			fullToolbar.getStyleClass().remove("bg-gray");
			fullToolbar.getStyleClass().add("bg-black");
			fullToolbar.setPadding(new Insets(5, 20, 5, 10));

			// Remove toolbar
			cellToolbar.getChildren().remove(fullToolbar);

			// Initialize toolbar
			JFXPanel toolbarPanel = new JFXPanel();
			Scene scene = new Scene(fullToolbar);
			scene.getStylesheets().add("org/processmining/filterd/gui/css/Notebook.css");
			scene.setFill(Paint.valueOf("black"));
            toolbarPanel.setScene(scene);

			// Initialize panel
            fullScreenPanel = new JPanel(new BorderLayout());
			fullScreenPanel.add(toolbarPanel, BorderLayout.PAGE_START);
			fullScreenPanel.add(visualizerSwgWrap.getContent(), BorderLayout.CENTER);

			// Change plugin view to fullscreen panel
			FilterdVisualizer.changeView(fullScreenPanel);

			// TODO Change icon
			isFullScreen = true;
		} else if (isFullScreen) {
			// Style toolbar for leaving fullscreen mode
			fullToolbar.getStyleClass().remove("bg-black");
			fullToolbar.getStyleClass().add("bg-gray");
			fullToolbar.setPadding(new Insets(0, 0, 0, 0));

			// Re-add toolbar
			cellToolbar.getChildren().add(cellToolbar.getChildren().size() - 1, fullToolbar);

			// Revert view
			FilterdVisualizer.revertView();
			// TODO Change icon
			isFullScreen = false;
		}
	}

	@FXML
	public void computeHandler() {
		if (getCellModel().isComputing()) {
			getCellModel().cancelCompute();
		} else {
			getCellModel().compute();
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
	private void loadVisualizer() {
		if (cmbVisualizers.getValue() == Utilities.dummyViewType) {
			// Remove visualizer if "None" is selected
			//			visualizerPane.getChildren().remove(visualizerSwgWrap);
			visualizerPane.getChildren().clear();
			visualizerSwgWrap.setContent(null);
			return;
		}
		visualizerPane.getChildren().clear();
		visualizerSwgWrap.setContent(null);
		
		JComponent visualizer = getCellModel().getVisualization(cmbVisualizers.getValue());
		if (!visualizerPane.getChildren().contains(visualizerSwgWrap)) {
			// Add visualizer if not present
			visualizerPane.getChildren().add(visualizerSwgWrap);
		}
		// Make the visualizer resize with the pane
		Utilities.setAnchors(visualizerSwgWrap, 0.0);
		// Load Visualizer
		visualizerSwgWrap.setContent(visualizer);

		if (isFullScreen) {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						fullScreenPanel.remove(1);
						fullScreenPanel.add(visualizer);
					}
				});
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	private void reloadVisualizer() {
		this.loadVisualizer();
	}
	
	private void updateNoOfOutputs(int noOfLogs) {
		if (noOfLogs == 1) {
			lblNumEventLogs.setText("1 output event log");
		} else {
			lblNumEventLogs.setText(noOfLogs + " output event logs");
		}
	}

	/**
	 * Method called when the configuration modal has to be hidden. It replaces
	 * the configuration modal with the visualizer.
	 *
	 * @param removeFilter
	 *            boolean stating whether the last filter button should be
	 *            removed (when the user cancels the configuration)
	 */
	public void hideConfigurationModal(boolean removeFilter) {
		// if configuration modal is not shown at all, do not do anything
		if (this.isConfigurationModalShown) {
			ConfigurationStep configurationStep = configurationModal.getConfigurationStep();
			visualizerPane.getChildren().clear(); // remove configuration modal from the visualizer pane
			visualizerPane.getChildren().add(visualizerSwgWrap); // add the visualizer to the visualizer pane
			cmbVisualizers.setDisable(false); // enable visualizer combobox
			// Make the visualizer resize with the pane
			Utilities.setAnchors(visualizerSwgWrap, 0.0);
			// if filter selection was cancelled, delete the added button
			if (configurationStep == ConfigurationStep.ADD_FILTER && removeFilter) {
				// remove FilterButton (its always the last in the list)
				FilterButtonModel buttonToRemove = getCellModel().getFilters()
						.get(getCellModel().getFilters().size() - 1);
				removeFilter(buttonToRemove);
			}
		}
		this.isConfigurationModalShown = false;
	}

	/**
	 * Method called when the filter configuration dialog should be shown in the
	 * configuration modal.
	 *
	 * @param filterConfig
	 *            Filter configuration whose configuration panel should be shown
	 * @param filterConfigController
	 *            Filter button whose filter is being configured
	 */
	public void showModalFilterConfiguration(FilterdAbstractConfig filterConfig,
			FilterButtonController filterConfigController) {
		if (filterConfig == null) {
			throw new IllegalArgumentException("Filter configuration cannot be null");
		}
		visualizerPane.getChildren().clear(); // Remove visualizer
		cmbVisualizers.setDisable(true); // Disable visualizer combobox
		configurationModal.showFilterConfiguration(filterConfig, filterConfigController); // populate filter configuration modal
		// get and set the root component of the configuration modal
		VBox configurationModalRoot = configurationModal.getRoot();
		visualizerPane.getChildren().add(configurationModalRoot);
		// Make the configuration modal resize with the pane
		Utilities.setAnchors(configurationModalRoot, 0.0);
		this.isConfigurationModalShown = true;
	}

	/**
	 * Method called when the list of available filters should be shown in the
	 * configuration modal.
	 *
	 * @param filterButtonController
	 *            Filter button for which the user wants to pick the filter
	 * @param filterButtonModel
	 *            Filter button model which should be populated with the chosen
	 *            configuration
	 */
	public void showModalFilterList(FilterButtonController filterButtonController,
			FilterButtonModel filterButtonModel) {
		visualizerPane.getChildren().clear(); // Remove visualizer
		cmbVisualizers.setDisable(true); // Disable visualizer combobox
		// populate the options list that is passed to the ConfigurationModalController
		List<String> filterOptions = new ArrayList<>();
		filterOptions.add("Trace Start Event Filter");
		filterOptions.add("Trace End Event Filter");
		filterOptions.add("Trace Frequency");
		filterOptions.add("Trace Sample");
		filterOptions.add("Trace Performance");
		filterOptions.add("Trace Having Event");
		filterOptions.add("Trace Attribute");
		filterOptions.add("Trace Timeframe");
		filterOptions.add("Trace Follower Filter");
		filterOptions.add("Trace Trim Filter");
		filterOptions.add("Event Attributes");
		filterOptions.add("Event Rate");
		filterOptions.add("Merge Subsequent Events");

		// callback is called when the user chooses which filter she wants to use
		// it should map the chosen string to a concrete class which is initialized
		configurationModal.showFilterList(filterOptions, filterButtonController,
				new Callback<String, FilterdAbstractConfig>() {

					public FilterdAbstractConfig call(String userSelection) {
						// set the input log for the filter configuration
						// this is either the input log for the cell if there was no computation done
						// or it can be the output of the previous filter if there was computation done
						XLog inputLog;
						ComputationCellModel model = (ComputationCellModel) cellModel;
						// if the input log was selected, there was definitely no computation
						if (model.getInputLog().get() == null) {
							throw new IllegalStateException("No input log selected");
						}
						FilterButtonModel lastFilterButton = model.getFilters().get(model.getFilters().size() - 1); // this is the filter button that we are currently configuring
						// use the cell input log or the given input log for the filter button
						if (lastFilterButton.getInputLog() != null) {
							inputLog = lastFilterButton.getInputLog(); // input log is set in the addFilter() method in this class
						} else {
							inputLog = model.getInputLog().get(); // if filter input log is null, use the cell input log
						}
						FilterdAbstractConfig filterConfig = null;
						// do not accept empty logs
						if (inputLog.size() == 0) {
							ComputationCellModel.handleError(new EmptyLogException(""));
							getCellModel().getFilters().get(getCellModel().getFilters().size() - 1).isValidProperty()
									.set(false);
							return null;
						}
						// map string to class
						switch (userSelection) {
							case "Trace Start Event Filter" :
								filterConfig = new FilterdTraceStartEventConfig(inputLog,
										new FilterdTraceStartEventFilter());
								break;
							case "Trace End Event Filter" :
								filterConfig = new FilterdTraceEndEventConfig(inputLog,
										new FilterdTraceEndEventFilter());
								break;
							case "Trace Frequency" :
								filterConfig = new FilterdTraceFrequencyConfig(inputLog,
										new FilterdTraceFrequencyFilter());
								break;
							case "Trace Sample" :
								filterConfig = new FilterdTraceSampleConfig(inputLog, new FilterdTraceSampleFilter());
								break;
							case "Trace Performance" :
								filterConfig = new FilterdTracePerformanceConfig(inputLog,
										new FilterdTracePerformanceFilter());
								break;
							case "Event Attributes" :
								filterConfig = new FilterdEventAttrConfig(inputLog, new FilterdEventAttrFilter());
								break;
							case "Event Rate" :
								filterConfig = new FilterdEventRateConfig(inputLog, new FilterdEventRateFilter());
								break;
							case "Trace Having Event" :
								filterConfig = new FilterdTracesHavingEventConfig(inputLog,
										new FilterdTracesHavingEvent());
								break;
							case "Trace Attribute" :
								filterConfig = new FilterdTraceAttrConfig(inputLog, new FilterdTraceAttrFilter());
								break;
							case "Trace Timeframe" :
								filterConfig = new FilterdTraceTimeframeConfig(inputLog,
										new FilterdTraceTimeframeFilter());
								break;
							case "Merge Subsequent Events" :
								filterConfig = new FilterdModifMergeSubsequentConfig(inputLog,
										new FilterdModifMergeSubsequentFilter());
								break;
							case "Trace Follower Filter" :
								filterConfig = new FilterdTraceFollowerConfig(inputLog,
										new FilterdTraceFollowerFilter());
								break;
							case "Trace Trim Filter" :
								filterConfig = new FilterdTraceTrimConfig(inputLog, new FilterdTraceTrimFilter());
								break;
							default :
								throw new IllegalArgumentException("Unsupported filter selected");
						}
						// TODO: set cell status to OUT_OF_DATE
						filterButtonModel.setFilterConfig(filterConfig);
						return filterConfig;
					}

				});
		VBox configurationModalRoot = configurationModal.getRoot();
		visualizerPane.getChildren().add(configurationModalRoot);
		// Make the configuration modal resize with the pane
		Utilities.setAnchors(configurationModalRoot, 0.0);
		this.isConfigurationModalShown = true;
	}

	public void enableAllFilterButtonsBut(int index) {
		for(int i = 0; i < getCellModel().getFilters().size(); i++) {
			if(i != index) {
				getCellModel().getFilters().get(i).setIsEditDisabled(false);
			}
		}
	}

	@Override
	public void show() {
		super.show();
		if (isExpanded) {
			cell.prefHeightProperty().bind(notebookLayout.heightProperty().subtract(notebookToolbar.heightProperty()));
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
