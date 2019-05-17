package org.processmining.filterd.gui;

import java.util.List;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIContext;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.hub.ProMResourceManager;
import org.processmining.contexts.uitopia.hub.ProMViewManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class contains the model for the notebook.
 * 
 * @author Ewoud
 *
 */
public class NotebookModel {
	// objects from ProM
	private UIPluginContext promContext; // The ProM context to communicate with the ProM framework.
	private ProMViewManager viewManager; // Current view manager.
	private ProMResourceManager resourceManager; // Current resource manager.

	private XLog initialInput; // the event log the notebook was initialized with.
	// ObservableList allows for action listeners. ObeservableLists are provided by JavaFX
	private ObservableList<CellModel> cells; // the list of all cells currently in the notebook.
	private ComputationMode computationMode; // the computation mode the notebook is currently in.


	public NotebookModel() {
		cells = FXCollections.emptyObservableList();
	}
	/**
	 * The constructor which sets the initial input event log. Note that the
	 * constructor does not have access to the @FXML annotated fields as @FXML
	 * annotated fields are populated after the execution of the constructor.
	 * 
	 * @param context
	 *            The context from ProM.
	 * @param log
	 *            The event log to initialize the notebook with.
	 */
	public NotebookModel(UIPluginContext context, XLog log) {
		this.promContext = context;
		this.initialInput = log;
		cells = FXCollections.emptyObservableList(); // A different type of observablelist might be more efficient (for example ObservableArrayList)

		// Get current view manager and resource manager.
		UIContext globalContext = context.getGlobalContext();
		viewManager = ProMViewManager.initialize(globalContext);
		resourceManager = ProMResourceManager.initialize(globalContext);
	}

	/**
	 * Returns the ProM context.
	 * 
	 * @return The ProM context.
	 */
	public UIPluginContext getPromContext() {
		return promContext;
	}

	/**
	 * Returns the ProM view manager.
	 * 
	 * @return The ProM view manager.
	 */
	public ProMViewManager getPromViewManager() {
		return viewManager;
	}

	/**
	 * Returns the ProM resource manager.
	 * 
	 * @return The ProM resource manager.
	 */
	public ProMResourceManager getPromResourceManager() {
		return resourceManager;
	}

	/**
	 * Returns the initial event log the notebook was initialized with.
	 * 
	 * @return The event log the notebook was initialized with.
	 */
	public XLog getInitialInput() {
		return initialInput;
	}

	/**
	 * Returns the list of all cells currently in the notebook.
	 * 
	 * @return A list of all cells currently in the notebook.
	 */
	public ObservableList<CellModel> getCells() {
		return cells;
	}

	/**
	 * Adds a single cell to the list of cells in this model.
	 * 
	 * @param cell
	 *            The cell to add to this model
	 */
	public void addCell(CellModel cell) {
		cells.add(cell);
	}

	/**
	 * Appends a list of cells to the list of cells in this model.
	 * 
	 * @param cells
	 *            The list of cells to append to the cells in this model.
	 */
	public void addCells(List<CellModel> cells) {
		this.cells.addAll(cells);
	}

	/**
	 * Removes a cell from the list of cells in this model.
	 * 
	 * @param cell
	 *            The cell to remove from this model.
	 */
	public void removeCell(CellModel cell) {
		cells.remove(cell);
	}

	/**
	 * Removes all cells in a list from the list of cells in this model.
	 * 
	 * @param cells
	 *            The list containing the cells to remove from this model.
	 */
	public void removeCells(List<CellModel> cells) {
		this.cells.removeAll(cells);
	}

	/**
	 * Returns the current computation mode of the notebook.
	 * 
	 * @return The current computation mode of the notebook.
	 */
	public ComputationMode getComputationMode() {
		return computationMode;
	}

	/**
	 * Sets the computation mode of the notebook to the input computation mode.
	 * 
	 * @param computationMode
	 *            The computation mode to set the notebook to.
	 */
	public void setComputationMode(ComputationMode computationMode) {
		this.computationMode = computationMode;
	}

	/**
	 * Recomputes all cells in the notebook that are a descendant of the input
	 * cell.
	 * 
	 * @param cell
	 *            The cell whose descendants to recompute.
	 */
	public void recomputeFrom(CellModel cell) {
		//shouldn't this method belong to the NotebookController?
		//or is this invoked by an action listener on the cells?
		//TODO: implement
	}

	/**
	 * Saves the current notebook to the workspace.
	 */
	public void saveNotebook() {
		//NOTE: shouldn't we give the notebook a name? 

		NotebookModel newNotebook = new NotebookModel(this.getPromContext(), this.getInitialInput());
		newNotebook.addCells(this.getCells());

		promContext.getProvidedObjectManager().createProvidedObject("Notebook File", newNotebook, NotebookModel.class, promContext);
		promContext.getGlobalContext().getResourceManager().getResourceForInstance(newNotebook).setFavorite(true);
	}

	/**
	 * Loads a notebook from the workspace into the current notebook.
	 * 
	 * @param name
	 *            The name of the notebook to load from the workspace.
	 */
	public void loadNotebook(String name) {
		//TODO: implement
	}
}