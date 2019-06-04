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
import org.processmining.filterd.gui.adapters.NotebookModelAdapted;
import org.processmining.filterd.models.YLog;
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
@XmlJavaTypeAdapter(NotebookModel.NotebookModelAdapter.class)
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
	private Task<Void> computeTask;
	private SimpleBooleanProperty isComputing;

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
		this.initialInput = new YLog(Toolbox.getNextId(), "Initial input", log);
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

		String xml = getXML();

		promContext.getProvidedObjectManager().createProvidedObject("Notebook File", xml, String.class,
				promContext);
		promContext.getGlobalContext().getResourceManager().getResourceForInstance(xml).setFavorite(true);
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

	public List<YLog> getOutputLogsTill(int index) {
		List<YLog> logs = new ArrayList<>();
		logs.add(initialInput);
		if (index >= 0 && index < getCells().size() + 1) {
			for (int i = 0; i < index; i++) {
				CellModel gCell = getCells().get(i);
				if (getCells().get(i) instanceof ComputationCellModel) {
					ComputationCellModel cell = (ComputationCellModel) gCell;
					logs.addAll(cell.getOutputLogs());
				}
			}
		}
		return logs;
	}

	public void compute() {
		this.computeTask = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				isComputing.set(true); // let the controller know that the computation is starting
				List<ComputationCellModel> computeList = cells.stream().filter(c -> c instanceof ComputationCellModel) // use only computation cells
						.map(c -> (ComputationCellModel) c) // cast to computation cell model
						.collect(Collectors.toList()); // transform steam to list
				for (ComputationCellModel cellModel : computeList) {
					if (isCancelled()) {
						break;
					}
					cellModel.compute(this);
				}
				isComputing.set(false); // let the controller know the computation is done
				return null;
			}
		};
		this.computeTask.exceptionProperty().addListener(new ChangeListener<Throwable>() {

			public void changed(ObservableValue<? extends Throwable> observable, Throwable oldValue,
					Throwable newValue) {
				if (newValue != null) {
					Exception exception = (Exception) newValue;
					exception.printStackTrace();
				}
			}
		});
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
	public String getXML(){
		try {
			//Create JAXB Context
			JAXBContext jaxbContext = JAXBContext.newInstance(NotebookModelAdapted.class, TextCellModel.class,
					ComputationCellModel.class, FilterButtonModel.class);
			//Create Marshaller
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			//Format XML (otherwise it wil be a single line without spaces)
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			//Create adapted notebook
			NotebookModelAdapted adaptedModel = new NotebookModelAdapted();
			adaptedModel.setCells(getCells());
			adaptedModel.setComputationMode(getComputationMode());
			//Write XML to StringWriter
			StringWriter sw = new StringWriter();
			jaxbMarshaller.marshal(adaptedModel, sw);
			//print xml to console
			String xmlContent = sw.toString();
			System.out.println(xmlContent);
			return xmlContent;

		} catch (JAXBException e) {
			e.printStackTrace();
			//TODO: Handle this in calling method.
			return "";
		}
	}

	/**
	 * Converts the NotebookModel into a NotebokModelAdapted and vice versa.
	 *
	 */
	public class NotebookModelAdapter extends AbstractJAXBAdapter<NotebookModelAdapted, NotebookModel> {

		/**
		 * Converts the NotebookMode into a NotebookModelAdapted.
		 */
		public NotebookModelAdapted marshal(NotebookModel model) throws Exception {
			NotebookModelAdapted adaptedModel = new NotebookModelAdapted();
			adaptedModel.setCells(model.getCells());
			adaptedModel.setComputationMode(model.getComputationMode());
			return adaptedModel;
		}

		/**
		 * Converts a NotebookModelAdapted to a NotebookModel
		 */
		public NotebookModel unmarshal(NotebookModelAdapted adaptedModel) throws Exception {
			// Parameters come from the static variables in AbstractJAXBAdapter
			NotebookModel model = new NotebookModel(this.staticPromContext, this.staticInitialInput,
					this.staticPromCanceller);
			model.addCells(adaptedModel.getCells());
			model.setComputationMode(adaptedModel.getComputationMode());
			return model;
		}

	}

}