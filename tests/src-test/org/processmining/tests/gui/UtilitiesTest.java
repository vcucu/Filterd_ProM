package org.processmining.tests.gui;

import org.deckfour.uitopia.api.model.Author;
import org.deckfour.uitopia.api.model.ResourceType;
import org.deckfour.uitopia.api.model.View;
import org.junit.Test;
import org.processmining.filterd.gui.Utilities;

import junit.framework.TestCase;

public class UtilitiesTest extends TestCase {
	
	@Test
	public void testCreateView() {
		// Create new utilities instance
		Utilities util = new Utilities();
		// Get the view type
		View view = util.dummyViewType.createView(null);
		// Check that the view was properly returned
		assertEquals(view, null);
	}
	
	@Test
	public void testGetAuthor() {
		// Create new utilities instance
		Utilities util = new Utilities();
		// Get the author of the view type
		Author author = util.dummyViewType.getAuthor();
		// Check that the author was properly returned
		assertEquals(author, null);
	}
	
	@Test
	public void testGetPrimaryType() {
		// Create new utilities instance
		Utilities util = new Utilities();
		// Get the author of the view type
		Class<?> type = util.dummyViewType.getPrimaryType();
		// Check that the author was properly returned
		assertEquals(type, null);
	}
	
	@Test
	public void testGetTypeName() {
		// Create new utilities instance
		Utilities util = new Utilities();
		// Get the author of the view type
		String typeName = util.dummyViewType.getTypeName();
		// Check that the author was properly returned
		assertEquals(typeName, null);
	}
	
	@Test
	public void testToString() {
		// Create new utilities instance
		Utilities util = new Utilities();
		// Get the author of the view type
		String string = util.dummyViewType.toString();
		// Check that the author was properly returned
		assertTrue(string.equals("None"));
	}
	
	@Test
	public void testGetViewableType() {
		// Create new utilities instance
		Utilities util = new Utilities();
		// Get the author of the view type
		ResourceType resource = util.dummyViewType.getViewableType();
		// Check that the author was properly returned
		assertEquals(resource, null);
	}

}
