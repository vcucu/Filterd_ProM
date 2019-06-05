package org.processmining.filterd.gui.adapters;

import org.processmining.filterd.gui.CellStatus;

public class CellModelAdapted {
	
	private int index;
	private String cellName;
	private CellStatus statusBar;
	private boolean isHidden;
	
	
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
	
	public CellStatus getStatusBar() {
		return statusBar;
	}
	
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
