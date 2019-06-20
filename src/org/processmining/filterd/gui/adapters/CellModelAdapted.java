package org.processmining.filterd.gui.adapters;

import org.processmining.filterd.gui.CellStatus;

/**
 * Class representing a deserialized cell model. It is used by JAXB to save the
 * cell model in XML format. All attributes of this class have to be either
 * primitives or enumerations.
 */
public class CellModelAdapted {

	private int index; // index of the cell in the cells array of the notebook model
	private String cellName; // name of the cell
	private CellStatus statusBar; // current status of the cell
	private boolean isHidden; // boolean stating whether the cell is currently hidden

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	/**
	 * Getter for the current status of the cell.
	 * 
	 * @return current status of the cell
	 */
	public CellStatus getStatusBar() {
		return statusBar;
	}

	/**
	 * Setter for the current status of the cell.
	 * 
	 * @param statusBar
	 *            current status of the cell
	 */
	public void setStatusBar(CellStatus statusBar) {
		this.statusBar = statusBar;
	}

	public boolean getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}
}
