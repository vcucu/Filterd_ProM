package org.processmining.filterd.gui.adapters;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.processmining.filterd.gui.CellModel;
import org.processmining.filterd.gui.ComputationMode;

import javafx.collections.ObservableList;

/**
 * NotebookModelAdapted can contain the data from the NotebookModel that
 * needs to be saved to XML. When saving first a NotebookModelAdapted gets
 * created which then gets marshalled to XML.
 *
 */
@XmlRootElement(namespace = "org.processmining.filterd.gui") // set this class as the root of the XML file for this package.
@XmlAccessorType(XmlAccessType.NONE) // Makes sure only explicitly named elements get added to the XML.
public class NotebookModelAdapted {
	
	@XmlElementWrapper(name = "cells") // to put the cells from the list in their own xml section.
	@XmlElement(name = "cell") // to name individual cells 'cell' instead of 'cells'
	private ObservableList<CellModel> cells; // the list of all cells currently in the notebook.
	@XmlElement
	private ComputationMode computationMode;

	public NotebookModelAdapted() {
		
	}
	
	public ObservableList<CellModel> getCells() {
		return cells;
	}
	
	public void setCells(ObservableList<CellModel> cells) {
		this.cells = cells;
	}
	
	public ComputationMode getComputationMode() {
		return computationMode;
	}
	
	public void setComputationMode(ComputationMode computationMode) {
		this.computationMode = computationMode;
	}

}

