package org.processmining.filterd.gui.adapters;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.processmining.filterd.gui.CellModel;
import org.processmining.filterd.gui.ComputationMode;

/**
 * NotebookModelAdapted can contain the data from the NotebookModel that
 * needs to be saved to XML. When saving first a NotebookModelAdapted gets
 * created which then gets marshalled to XML.
 *
 */
@XmlRootElement(namespace = "org.processmining.filterd.gui") // set this class as the root of the XML file for this package.
public class NotebookModelAdapted {
	

	private List<CellModel> cells; // the list of all cells currently in the notebook.
	private ComputationMode computationMode;

	public NotebookModelAdapted() {
		
	}
	
	@XmlElementWrapper(name = "cells") // to put the cells from the list in their own xml section.
	@XmlElement(name = "cell") // to name individual cells 'cell' instead of 'cells'
	@XmlJavaTypeAdapter(CellModelAdapter.class) // tell JAXB to use the adapter.
	public List<CellModel> getCells() {
		return cells;
	}
	
	public void setCells(List<CellModel> cells) {
		this.cells = cells;
	}
	
	public ComputationMode getComputationMode() {
		return computationMode;
	}
	
	public void setComputationMode(ComputationMode computationMode) {
		this.computationMode = computationMode;
	}

}

