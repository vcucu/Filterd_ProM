package org.processmining.tests.gui;

import org.junit.Test;
import org.processmining.filterd.gui.TextCellModel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import junit.framework.TestCase;

public class TextCellModelTest extends TestCase {
	
	@Test
	public void testNewTextCellModel() {
		// Create new text cell model
		TextCellModel cell = new TextCellModel(null, 0);
		// Check that the new text cell model was created
		assertTrue(cell != null);
		assertTrue(cell instanceof TextCellModel);
	}
	
	@Test
	public void testNewEmptyTextCellModel() {
		// Create new text cell model (empty constructor)
		TextCellModel cell = new TextCellModel();
		// Check that the new text cell model was created
		assertTrue(cell != null);
		assertTrue(cell instanceof TextCellModel);
	}
	
	@Test
	public void testCommentProperty() {
		// Create new text cell model
		TextCellModel cell = new TextCellModel(null, 0);
		// Set the new text cell model comment
		cell.setComment("Filterd");
		// Get the cell model comment
		String comment = cell.getComment();
		// Check the cell model comment
		assertTrue(comment.equals("Filterd"));
	}
	
	@Test
	public void testBindCommentProperty() {
		// Create new text cell model
		TextCellModel cell = new TextCellModel(null, 0);
		// Create new string property
		Property<String> stringProperty = new SimpleStringProperty("Filterd");
		// Set the new string property to the text cell
		cell.bindComment(stringProperty);
		// Get the previously set string property
		Property<String> newStringProperty = cell.getCommentProperty();
		// Check that the property was properly set
		assertTrue(newStringProperty.getValue().equals("Filterd"));
	}
}
