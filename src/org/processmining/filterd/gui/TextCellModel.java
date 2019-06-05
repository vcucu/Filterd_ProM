package org.processmining.filterd.gui;

import javax.xml.bind.annotation.XmlElement;

import org.processmining.contexts.uitopia.UIPluginContext;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TextCellModel extends CellModel{
	
	// Annotated at the getter method because a conversion is needed.
	private StringProperty comment; // StringProperty is used because it can be bound.

	/**
	 * Constructor for importing/exporting. This constructor needs to exist because JAXB needs a no-argument constructor for unmarshalling.
	 * Properties set here could be overwritten during loading.
	 */
	public TextCellModel() {
	}
	
	public TextCellModel(UIPluginContext context, int index) {
		super(context, index);
		comment = new SimpleStringProperty();
	}
	
	/**
	 * Binds StringProperty to the comment so they will always contain the same value.
	 * @param stringProperty The variable to bind to the comment.
	 */
	public void bindComment(Property<String> stringProperty) {
		comment.bindBidirectional(stringProperty);
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
	
	/**
	 * Returns the string value contained in the StringProperty. Corresponds to the text in the TextArea of the TextCell.
	 * @return THe string value contained in the StringProperty.
	 */
	@XmlElement
	public String getComment() {
		return comment.getValue();
	}
	
	/**
	 * Returns the modifiable StringProperty
	 * @return The StringProperty
	 */
	public StringProperty getCommentProperty() {
		return comment;
	}
	
}