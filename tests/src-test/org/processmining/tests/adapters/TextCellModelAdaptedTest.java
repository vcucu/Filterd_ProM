package org.processmining.tests.adapters;

import org.junit.Test;
import org.processmining.filterd.gui.adapters.TextCellModelAdapted;

import junit.framework.TestCase;

public class TextCellModelAdaptedTest extends TestCase {

	TextCellModelAdapted model = new TextCellModelAdapted();
	
	@Test
	public void testCommentMethods() {
		// Set new comment for the cell model
		model.setComment("Filterd");
		// Get the comment of the cell model
		String name = model.getComment();
		// Check that the comment was properly set
		assertTrue(name.equals("Filterd"));
	}
}
