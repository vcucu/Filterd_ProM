package org.processmining.tests.gui;

import org.junit.Test;
import org.processmining.filterd.gui.TextCellModel;

import junit.framework.TestCase;

public class TextCellModelTest extends TestCase {
	
	@Test
	public void testNewTextCellModel() {
		// Create new text cell model
		TextCellModel cell = new TextCellModel(null);
		// Check that the new text cell model was created
		assertTrue(cell != null);
		assertTrue(cell instanceof TextCellModel);
	}
	
	@Test
	public void testCommentProperty() {
		// Create new text cell model
		TextCellModel cell = new TextCellModel(null);
		// Set the new text cell model comment
		cell.setComment("Filterd");
		// Get the cell model comment
		String comment = cell.getComment();
		// Check the cell model comment
		assertTrue(comment.equals("Filterd"));
	}
}
