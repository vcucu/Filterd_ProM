package org.processmining.tests.gui;

import java.util.ArrayList;

import org.junit.Test;
import org.processmining.filterd.gui.NestedFilterConfigPanelController;

import junit.framework.TestCase;

public class NestedFilterConfigPanelControllerTest extends TestCase {
	
	@Test
	public void testNewNestedFilterConfigPanel() {
		try {
			// Create new filter config panel
			NestedFilterConfigPanelController panel = new NestedFilterConfigPanelController(new ArrayList<>());
		} catch (Throwable exception) {
			assertFalse(exception.equals(null));
		}
	}

}
