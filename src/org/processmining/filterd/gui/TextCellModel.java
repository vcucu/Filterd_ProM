package org.processmining.filterd.gui;

import org.processmining.contexts.uitopia.UIPluginContext;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TextCellModel extends CellModel{
	
	private StringProperty comment; // StringProperty is used because it can be bound.

	public TextCellModel(UIPluginContext context, int index) {
		super(context, index);
		comment = new SimpleStringProperty();
	}
	
	/**
	 * If comment is set fire a property change to update the corresponding UI component
	 * @param comment comment displayed in the text area of the text cell
	 */
	public void setComment(String comment) {
		String oldState = this.comment.toString();
		this.comment.set(comment);
		property.firePropertyChange("setComment", oldState, comment);
	}
	
	public String getComment() {
		return comment.toString();
	}
	
	/**
	 * Returns the modifiable StringProperty
	 * @return The StringProperty
	 */
	public StringProperty getCommentProperty() {
		return comment;
	}
	
}