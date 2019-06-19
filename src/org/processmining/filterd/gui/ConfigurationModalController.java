package org.processmining.filterd.gui;

import java.io.IOException;
import java.util.List;

import org.processmining.filterd.configurations.FilterdAbstractConfig;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;

/**
 * Controller for the modal that displays the configuration dialog for filters.
 * This UI component is meant to be used within the computation cell! It is
 * shown in the computation cell's visualizer pane, and displays two types of
 * dialogs: - list of available filters which can be initialized by the user, -
 * configuration panel for individual filters. Note: between each configuration
 * step, the modal is reset i.e. all its relevant variables are cleared.
 * 
 * @author Filip Davidovic
 *
 */
public class ConfigurationModalController {

	@FXML private Button cancel; // cancel button
	@FXML private Button apply; // next / apply button
	@FXML private HBox contentPane; // pane that stores the configuration dialog / panel
	
	private VBox root; // root component of this controller's view
	private FilterdAbstractConfig filterConfig; // filter configuration whose configuration panel is currently being displayed 
	private FilterButtonController filterButtonController; // controller of the filter button (used to disable / enable the edit button)s
	private AbstractFilterConfigPanelController currentContentsController; // filter configuration panel that is currently being displayed
	private FilterListController filterListController; // filter list panel that is currently being displayed
	private ComputationCellController parent; // computation cell in which this modal is contained
	private ConfigurationStep configurationStep; // current configuration step
	private Callback<String, FilterdAbstractConfig> filterSelectionCallback; // callback to call when the user selects a filter from the list of available filters

	public ConfigurationModalController(ComputationCellController parent) {
		this.parent = parent;
		this.configurationStep = ConfigurationStep.ADD_FILTER; // the controller first has to be called as a filter list dialog (no filters have been created yet)
		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("/org/processmining/filterd/gui/fxml/ConfigurationModal.fxml"));
		fxmlLoader.setController(this);
		try {
			root = (VBox) fxmlLoader.load();
			HBox.setHgrow(root, Priority.ALWAYS); // make full width of the visualizer pane
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Method called by the computation cell to show the list of available
	 * filters.
	 * 
	 * @param options
	 *            List of filter names to be displayed as options
	 * @param filterButtonController
	 *            Controller for the filter button for which the user is picking
	 *            the filter
	 * @param callback
	 *            Callback to call when the user is done with picking (she
	 *            clicks next / apply)
	 */
	public void showFilterList(List<String> options, FilterButtonController filterButtonController,
			Callback<String, FilterdAbstractConfig> callback) {
		if (callback == null) {
			throw new IllegalArgumentException("Callback cannot be null");
		}
		resetModal(); // reset the modal before populating it 
		// set internal variables
		this.filterButtonController = filterButtonController;
		this.filterSelectionCallback = callback; // set the callback which will be invoked when the user clicks Next/Apply
		// create filter list panel
		this.filterListController = new FilterListController(options);
		// populate contents pane
		HBox.setHgrow(this.filterListController.getRoot(), Priority.ALWAYS);
		contentPane.getChildren().clear();
		contentPane.getChildren().add(this.filterListController.getRoot());
		apply.setText("Next"); // act like a wizard 
		apply.disableProperty().bind(this.filterListController.isApplyDisabledProperty()); // disable the button if nothing is selected
		this.configurationStep = ConfigurationStep.ADD_FILTER;
	}
	
	private void showParsingScreen() {
		resetModal();
		Label label = new Label("Configuration you selected is being parsed...");
		label.setFont(new Font(18.0));
		// set size properties of the label
		HBox.setHgrow(label, Priority.ALWAYS);
		label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		label.setAlignment(Pos.CENTER);
		// add label to the content pane
		contentPane.getChildren().clear();
		contentPane.getChildren().add(label);
		// disable the buttons
		this.apply.disableProperty().set(true);
		this.cancel.disableProperty().set(true);
		configurationStep = ConfigurationStep.PARSING;
	}

	/**
	 * Method called by the computation cell to show the filter configuration
	 * panel.
	 * 
	 * @param filterConfig
	 *            Filter configuration whose configuration panel will be
	 *            displayed
	 * @param filterButtonController
	 *            Controller for the filter button for which the user is
	 *            configuring the filter
	 */
	public void showFilterConfiguration(FilterdAbstractConfig filterConfig,
			FilterButtonController filterButtonController) {
		if (filterConfig == null) {
			// if the filter config. is null the error was shown by the invoker -> gracefully cancel
			cancel();
			return;
		}
		resetModal(); // reset the modal before populating it
		// set internal variables
		this.filterConfig = filterConfig;
		this.filterButtonController = filterButtonController;
		if(this.filterListController != null) {
			this.filterListController.setStatusLabelText("Creating the configuration panel");
		}
		currentContentsController = filterConfig.getConfigPanel(); // get the configuration panel from the filter configuration
		if (currentContentsController instanceof NestedFilterConfigPanelController) { // nested configuration panels are handled inside the general configuration panels
			throw new IllegalArgumentException(
					"Filter configuration panel controller is nested. Is this filter config. nested?");
		}
		// populate contents pane
		HBox.setHgrow(currentContentsController.getRoot(), Priority.ALWAYS); // make the content 100% of the available width
		contentPane.getChildren().clear(); // remove anything that may be left over by previous contents
		contentPane.getChildren().add(currentContentsController.getRoot());
		configurationStep = ConfigurationStep.CONFIGURE_FILTER;
	}

	/**
	 * Handler for the cancel button. Tells the parent to hide the configuration
	 * modal and resets the configuration modal.
	 */
	@FXML
	private void cancel() {
		parent.hideConfigurationModal(true); // tell the parent to hide the configuration modal (and show the visualizer among other stuff)
		if (this.filterButtonController != null) {
			filterButtonController.enableEditFilterHandler(); // enable the edit button in the filter button if applicable
		}
		resetModal(); // reset the configuration modal
	}

	/**
	 * Handler for the apply / next button. Has two modes of operation, based on
	 * the configuration step in which the configuration modal is in: -
	 * ADD_FILTER - get the selected filter configuration by invoking the
	 * callback, then show the filter configuration dialog - CONFIGURE_STEP -
	 * populate the filter configuration with the selected parameters
	 */
	@FXML
	private void apply() {
		System.out.println("Applying filter");
		if (this.configurationStep == ConfigurationStep.ADD_FILTER) {
			// user is picking a filter to use -> move on to the configuration screen
			if (filterSelectionCallback == null || this.filterListController == null) {
				throw new IllegalStateException("apply() was called before showFilterList()");
			}
			String userSelection = this.filterListController.getSelection();
			
			// Update FilterButton name
			filterButtonController.getModel().setName(userSelection);
			
			Platform.runLater(() -> showParsingScreen());
			new Thread(new Task<Void>() {

				protected Void call() throws Exception {
					FilterdAbstractConfig conf = filterSelectionCallback.call(userSelection);
					Platform.runLater(() -> showFilterConfiguration(conf, filterButtonController));
					return null;
				}
				
			}).start();
		} else if (this.configurationStep == ConfigurationStep.CONFIGURE_FILTER) {
			if (currentContentsController instanceof NestedFilterConfigPanelController) {
				throw new IllegalStateException("Filter configuration panel controller is nested");
			}
			// user is configuring a filter config. -> apply changes
			FilterConfigPanelController casted = (FilterConfigPanelController) currentContentsController;
			this.filterConfig.populate(casted); // populate filter configuration
			parent.hideConfigurationModal(false);
			if (this.filterButtonController != null) {
				filterButtonController.enableEditFilterHandler();
			}
			resetModal();
		}
		System.out.println("Setting the status bar to out of date in apply");
		//We have applied a filter but have not yet computed the cell with this updated preset so cell is out of date
		parent.getCellModel().setStatusBar(CellStatus.OUT_OF_DATE);
	}

	/**
	 * Clear all the variables and bindings that may have been set up in various
	 * steps.
	 */
	private void resetModal() {
		this.contentPane.getChildren().clear(); // remove any content from the configuration modal
		this.apply.setText("Apply"); // apply is the default over next
		this.apply.disableProperty().unbind(); // remove disabled binding that may have been added to the apply button
		this.apply.disableProperty().set(false);
		this.cancel.disableProperty().set(false);
		this.filterListController = null;
		if (this.configurationStep != ConfigurationStep.ADD_FILTER) {
			this.filterSelectionCallback = null;
			this.filterConfig = null;
			this.filterButtonController = null;
			this.currentContentsController = null;
		}
	}

	public VBox getRoot() {
		return root;
	}

	public ConfigurationStep getConfigurationStep() {
		return this.configurationStep;
	}

	enum ConfigurationStep {
		ADD_FILTER, CONFIGURE_FILTER, PARSING;
	}
}
