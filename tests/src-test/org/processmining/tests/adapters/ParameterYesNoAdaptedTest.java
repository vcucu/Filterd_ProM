package org.processmining.tests.adapters;

import org.junit.Test;
import org.processmining.filterd.gui.adapters.ParameterYesNoAdapted;

import junit.framework.TestCase;

public class ParameterYesNoAdaptedTest extends TestCase {
	
	ParameterYesNoAdapted parameter = new ParameterYesNoAdapted();
	
	@Test
	public void testDefaultChoiceMethods() {
		// Set new default choice value for the parameter
		parameter.setDefaultChoice(true);
		// Get the default choice value of the parameter
		boolean choice = parameter.getDefaultChoice();
		// Check that the default choice value was properly set
		assertTrue(choice);
	}
	
	@Test
	public void testChosenMethods() {
		// Set new choice value for the parameter
		parameter.setChosen(true);
		// Get the choice value of the parameter
		boolean choice = parameter.getChosen();
		// Check that the choice value was properly set
		assertTrue(choice);
	}

}
