package org.processmining.filterd.gui;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIContext;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.hub.ProMResourceManager;
import org.processmining.contexts.uitopia.hub.ProMViewManager;
import org.processmining.filterd.gui.adapters.ComputationCellModelAdapted;
import org.processmining.filterd.gui.adapters.FilterButtonAdapted;
import org.processmining.filterd.gui.adapters.FilterdAbstractConfigAdapted;
import org.processmining.filterd.gui.adapters.FilterdAbstractConfigReferencingAdapted;
import org.processmining.filterd.gui.adapters.NotebookModelAdapted;
import org.processmining.filterd.gui.adapters.NotebookModelAdapter;
import org.processmining.filterd.gui.adapters.TextCellModelAdapted;
import org.processmining.filterd.models.YLog;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.parameters.ParameterText;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.framework.plugin.ProMCanceller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 * This class contains the model for the notebook.
 * 
 * @author Ewoud
 *
 */
@XmlJavaTypeAdapter(NotebookModelAdapter.class)
public class NotebookModel {

	/**
	 * TODO: IF YOU ADD A NEW VARIABLE, MAKE SURE TO UPDATE THE clone()
	 * METHOD!!!
	 */
	// objects from ProM
	private UIPluginContext promContext; // The ProM context to communicate with the ProM framework.
	private ProMViewManager viewManager; // Current view manager.
	private ProMResourceManager resourceManager; // Current resource manager.

	private YLog initialInput; // the event log the notebook was initialized with.
	// ObservableList allows for action listeners. ObeservableLists are provided by JavaFX
	private ProMCanceller promCanceller;
	private ObservableList<CellModel> cells; // the list of all cells currently in the notebook.
	private ComputationMode computationMode; // the computation mode the notebook is currently in.
	private Task<Void> computeTask; // javafx task which is initialized every time a computation is requested (not anonymous variable so that the user can cancel the task)
	private SimpleBooleanProperty isComputing; // boolean property stating whether the notebook is currently being computed

	/**
	 * Constructor for importing/exporting. This constructor needs to exist
	 * because JAXB needs a no-argument constructor for unmarshalling.
	 * Properties set here could be overwritten during loading.
	 */
	public NotebookModel() {
		this.cells = FXCollections.observableArrayList();
		setComputationMode(ComputationMode.MANUAL);
		this.isComputing = new SimpleBooleanProperty();
		this.isComputing.set(false);
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
	public NotebookModel(UIPluginContext context, XLog log, ProMCanceller canceller) {
		this.promContext = context;
		this.initialInput = new YLog(Toolbox.getNextId(), "Initial input", log, -1);
		this.promCanceller = canceller;
		this.cells = FXCollections.observableArrayList();
		// set the computation mode to manual
		setComputationMode(ComputationMode.MANUAL);

		// Get current view manager and resource manager.
		UIContext globalContext = context.getGlobalContext();
		viewManager = ProMViewManager.initialize(globalContext);
		resourceManager = ProMResourceManager.initialize(globalContext);
		this.isComputing = new SimpleBooleanProperty();
		this.isComputing.set(false);
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
	 * Returns the ProM canceller.
	 * 
	 * @return The ProM canceller.
	 */
	public ProMCanceller getPromCanceller() {
		return promCanceller;
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
	public YLog getInitialInput() {
		return initialInput;
	}

	/**
	 * Set the initial event log for the notebook.
	 * 
	 * @param log
	 *            The event log the notebook is initialized with.
	 */
	public void setInitialInput(YLog log) {
		this.initialInput = log;
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
		this.cells.add(cell);
	}

	/**
	 * Appends a list of cells to the list of cells in this model. Ignores empty
	 * cells and null.
	 * 
	 * @param cells
	 *            The list of cells to append to the cells in this model.
	 */
	public void addCells(List<CellModel> cells) {
		if (cells != null && !cells.isEmpty()) {
			// if cells is not null and is not empty
			this.cells.addAll(cells);
		}
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
	 * Adds a single cell to the list of cells in this model.
	 * 
	 * @param index
	 *            The position in the list which the cell should be added
	 * @param cell
	 *            The cell to add to this model
	 */
	public void addCell(int index, CellModel cell) {
		this.cells.add(index, cell);
	}

	/**
	 * Appends a list of cells to the list of cells in this model.
	 * 
	 * @param index
	 *            The position in the list in which the cells should be added
	 * @param cells
	 *            The list of cells to append to the cells in this model.
	 */
	public void addCells(int index, List<CellModel> cells) {
		this.cells.addAll(index, cells);
	}

	/**
	 * Removes a cell from the list of cells in this model.
	 * 
	 * @param index
	 *            The index of the cell to be removed.
	 */
	public void removeCell(int index) {
		cells.remove(index);
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
		try {
			String xml = getXML(); // get the XML.
			promContext.getProvidedObjectManager().createProvidedObject("Notebook File", xml, String.class,
					promContext);
			promContext.getGlobalContext().getResourceManager().getResourceForInstance(xml).setFavorite(true);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
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

	/**
	 * Updates input logs for all computation cells within the notebook model
	 * This happens when a computation cell is removed in order to update the
	 * output logs of downstream cells
	 * 
	 * @param removedCell
	 *            the computation cell that was removed from the notebook model
	 */
	public void removeCellsInputLogs(ComputationCellModel removedCell) {
		YLog removedLog = removedCell.getOutputLogs().get(0);
		for (int i = 0; i < this.getCells().size(); i++) {
			CellModel cell = this.getCells().get(i);
			if (cell instanceof ComputationCellModel) {
				((ComputationCellModel) cell).getInputLogs().remove(removedLog);
			}
		}
	}

	/**
	 * Updates input logs for all computation cells within the notebook model
	 * This happens when a computation cell is added in order to update the
	 * output logs of downstream cells
	 * 
	 * @param addedCell
	 *            the computation cell that was added to the notebook model
	 */
	public void addCellsInputLogs(ComputationCellModel addedCell) {
		for (int i = 0; i < this.getCells().size(); i++) {
			CellModel cell = this.getCells().get(i);
			if (cell instanceof ComputationCellModel) {
				((ComputationCellModel) cell).setInputLogs(FXCollections.observableArrayList(getOutputLogsTill(i)));
			}
		}
	}

	public List<YLog> getOutputLogsTill(int index) {
		List<YLog> logs = new ArrayList<>();
		logs.add(initialInput);
		if (index >= 0 && index < getCells().size() + 1) {
			for (int i = 0; i < index; i++) {
				CellModel gCell = getCells().get(i);
				if (getCells().get(i) instanceof ComputationCellModel) {
					ComputationCellModel cell = (ComputationCellModel) gCell;
					// add all output logs (except the initial input log since we don't want it to be duplicated)
					logs.addAll(cell.getOutputLogs().stream() // parse as a stream
							.filter(l -> !l.getName().equals(initialInput.getName())) // filter all logs whose name is not the same as input log's
							.collect(Collectors.toList())); // convert to a list
				}
			}
		} else {
			return logs;
		}
		return logs;
	}

	/**
	 * Method that computes the whole notebook in a separate thread. Notebook is
	 * computed by computing each individual computation cell while respecting
	 * their order in the list. Separate thread is used so that the UI thread is
	 * not blocked in long computation.
	 */
	public void compute() {
		// computation is executed in a task which is passed to a thread
		this.computeTask = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				isComputing.set(true); // let the controller know that the computation is starting
				// transform the cells list into a new computation cells list (ordering is preserved)
				List<ComputationCellModel> computeList = cells
						.stream()
						.filter(c -> c instanceof ComputationCellModel) // use only computation cells
						.map(c -> (ComputationCellModel) c) // cast to computation cell model
						.collect(Collectors.toList()); // transform steam to list
				// compute cells in their order in the list
				for (ComputationCellModel cellModel : computeList) {
					// check if the computation was cancelled by the user (if so, break out of the loop) 
					if (isCancelled()) {
						break;
					}
					cellModel.computeWithTask(this); // compute 
				}
				isComputing.set(false); // let the controller know the computation is done
				return null;
			}
		};
		// javafx tasks do not report exceptions to the console (this is a feature)
		// add a change listener to print the exception (needed for debugging)
		this.computeTask.exceptionProperty().addListener(new ChangeListener<Throwable>() {

			public void changed(ObservableValue<? extends Throwable> observable, Throwable oldValue,
					Throwable newValue) {
				if (newValue != null) {
					Exception exception = (Exception) newValue;
					exception.printStackTrace();
				}
			}
		});
		// create and start a thread
		Thread thread = new Thread(this.computeTask);
		thread.start();
	}

	public void cancelCompute() {
		if (this.computeTask != null) {
			this.computeTask.cancel();
		}
	}

	public boolean isComputing() {
		return this.isComputing.get();
	}

	public void setComputing(boolean value) {
		this.isComputing.setValue(value);
	}

	public BooleanProperty isComputingProperty() {
		return this.isComputing;
	}

	@Override
	public NotebookModel clone() {
		NotebookModel newNotebook = new NotebookModel(promContext, initialInput.get(), promCanceller);
		newNotebook.addCells(cells);
		newNotebook.setComputationMode(computationMode);

		return newNotebook;
	}

	/**
	 * Prints an XML of the notebook generated by JAXB.
	 * 
	 * @Return The XML of the notebook.
	 */
	public String getXML() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(NotebookModelAdapted.class, TextCellModelAdapted.class,
				ComputationCellModelAdapted.class, FilterButtonAdapted.class, FilterdAbstractConfigAdapted.class,
				Parameter.class, ParameterMultipleFromSet.class, ParameterOneFromSet.class,
				ParameterRangeFromRange.class, ParameterText.class, ParameterValueFromRange.class,
				ParameterYesNo.class, FilterdAbstractConfigReferencingAdapted.class, FilterdAbstractConfigAdapted.class); // Create JAXB Context.
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller(); // Create Marshaller.
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // Format XML (otherwise it wil be a single line without spaces)

		NotebookModelAdapted adaptedModel = new NotebookModelAdapted(); // Create adapted notebook.
		adaptedModel.setCells(getCells());
		adaptedModel.setComputationMode(getComputationMode());

		StringWriter sw = new StringWriter();
		jaxbMarshaller.marshal(adaptedModel, sw); // write XML to stringwriter.
		String xmlContent = sw.toString();
		System.out.println(xmlContent); //print xml to console
		return xmlContent;

	}

}