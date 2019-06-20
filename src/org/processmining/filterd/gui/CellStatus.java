package org.processmining.filterd.gui;

/**
 * Enumeration containing all the states in which a (computation) cell can be
 */
public enum CellStatus {
	IDLE, // everything is valid and the cells are computed
	IN_PROGRESS, // computation is in progress
	OUT_OF_DATE, // something regarding the cell has changed so it has to be recomputed
	INVALID; // something regarding the cell is invalid (in this case error messages are shown to specify what exactly)
}
