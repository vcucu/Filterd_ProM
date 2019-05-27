package org.processmining.filterd.gui;

import org.processmining.contexts.uitopia.UIPluginContext;

public class TextCellModel extends CellModel{
	
	private String comment; 

	public TextCellModel(UIPluginContext context) {
		super(context);
	}
	
	/**
	 * If comment is set fire a property change to update the corresponding UI component
	 * @param comment comment displayed in the text area of the text cell
	 */
	public void setComment(String comment) {
		String oldState = this.comment;
		this.comment = comment;
		property.firePropertyChange("setComment", oldState, comment);
	}
	
	public String getComment() {
		return comment;
	}
	
}