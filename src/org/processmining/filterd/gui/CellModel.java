package org.processmining.filterd.gui;

import org.processmining.contexts.uitopia.UIPluginContext;

public class CellModel {

	private boolean isHidden;
	private UIPluginContext context;	
	private CellStatus statusBar;	
	private String cellName;
	
	public CellModel() {
	}
	
	
	
	public void setContext(UIPluginContext context) {
		this.context = context;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public CellStatus getStatusBar() {
		return statusBar;
	}

	public void setStatusBar(CellStatus statusBar) {
		this.statusBar = statusBar;
	}

	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	public UIPluginContext getContext() {
		return context;
	}
}
