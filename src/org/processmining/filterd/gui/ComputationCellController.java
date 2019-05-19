package org.processmining.filterd.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ComputationCellController extends CellController {
	
	//TODO: add other FXML attributes
	private List<FilterButtonModel> filters = new ArrayList<>();
	private ObservableList<FilterButtonModel> filtersOL;
	
	@FXML 
	private VBox panelLayout;
	
	//TODO: add controller methods
	
	public ComputationCellController(NotebookController controller, ComputationCellModel cell) {
		super(controller, cell);
		this.setCellModel(new CellModel());
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
}
