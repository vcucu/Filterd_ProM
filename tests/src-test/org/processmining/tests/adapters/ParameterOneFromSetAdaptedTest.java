package org.processmining.tests.adapters;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.processmining.filterd.gui.adapters.ParameterOneFromSetAdapted;

import junit.framework.TestCase;

public class ParameterOneFromSetAdaptedTest extends TestCase {

	ParameterOneFromSetAdapted parameter = new ParameterOneFromSetAdapted();
	
	@Test
	public void testChoiceMethods() {
		// Set new default choice for the parameter
		parameter.setDefaultChoice("Filterd");
		// Get the default choice of the parameter
		String name = parameter.getDefaultChoice();
		// Check that the default choice was properly set
		assertTrue(name.equals("Filterd"));
	}
	
	@Test
	public void testChosenMethods() {
		// Set new choice for the parameter
		parameter.setChosen("Filterd");
		// Get the choice of the parameter
		String name = parameter.getChosen();
		// Check that the choice was properly set
		assertTrue(name.equals("Filterd"));
	}
	
	@Test
	public void testOptionsMethods() {
		// Create new list of choices
		List<String> choices = new ArrayList<>();
		// Add new item to the list
		choices.add("Filterd");
		// Set new options for the parameter
		parameter.setOptions(choices);
		// Get the options of the parameter
		List<String> newOptions = parameter.getOptions();
		// Check that the options were properly set
		assertTrue(newOptions.equals(choices));
	}
	
	@Test
	public void testReferenceMethods() {
		// Set new reference value for the parameter
		parameter.setCreatesReference(true);
		// Get the reference value of the parameter
		boolean ref = parameter.isCreatesReference();
		// Check that the reference value was properly set
		assertTrue(ref);
	}
}
