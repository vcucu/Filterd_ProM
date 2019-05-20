package org.processmining.filterd.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.deckfour.uitopia.api.model.ViewType;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ComputationCellController extends CellController {
	
	//TODO: add other FXML attributes
	private List<FilterButtonModel> filters = new ArrayList<>();
	private ObservableList<FilterButtonModel> filtersOL;
	
	@FXML private VBox panelLayout;
	@FXML private Pane visualizerPane;
	@FXML private ComboBox cmbEventLog;
	@FXML private ComboBox<ViewType> cmbVisualizers;
	
	/**
	 * Gets executed after the constructor. Has access to the @FXML annotated
	 * fields, thus UI elements can be manipulated here.
	 */
	public void initialize() {
		ComputationCellModel model = (ComputationCellModel) this.getCellModel();
		// TODO: load event logs in cmbEventLog
		model.setXLog(NotebookModel.initialInput); // TODO: set logs from combobox
		cmbVisualizers.getItems().addAll(model.getVisualizers());
	}
	
	//TODO: add controller methods
	
	public ComputationCellController(NotebookController controller, ComputationCellModel model) {
		super(controller, model);
		this.setCellModel(model);
		filtersOL = FXCollections.observableList(filters);
		
		filtersOL.addListener(new ListChangeListener<Object>() {
			@Override
            public void onChanged(ListChangeListener.Change change) {
                System.out.println("Detected a change! ");
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
			newController.setIndex(filtersOL.size() - 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void removeCell() {
		getLayout().getChildren().remove(getCellLayout());
	}

	public void show() {
		// TODO Auto-generated method stub
		
	}

	public void hide() {
		// TODO Auto-generated method stub
		
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
	
	// Load visualizer
	@FXML
    private synchronized void loadVisualizer(ActionEvent event) {
		ComputationCellModel model = (ComputationCellModel) this.getCellModel();
		JComponent visualizer = model.getVisualization(cmbVisualizers.getValue());
    	// Add a SwingNode to the Visualizer pane
    	SwingNode swgNode = new SwingNode();
    	visualizerPane.getChildren().add(swgNode);
    	// Load Visualizer
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	swgNode.setContent(visualizer);
            }
        });
    }
    
    
}