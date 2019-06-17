package org.processmining.tests.adapters;

import org.junit.Test;
import org.processmining.filterd.gui.adapters.ParameterAdapted;

import junit.framework.TestCase;

public class ParameterAdaptedTest extends TestCase {
	
	ParameterAdapted parameter = new ParameterAdapted();
	
	@Test
	public void testNameMethods() {
		// Set new name for the parameter
		parameter.setName("Filterd");
		// Get the name of the parameter
		String name = parameter.getName();
		// Check that the name was properly set
		assertTrue(name.equals("Filterd"));
	}
	
	@Test
	public void testNameDisplayedMethods() {
		// Set new displayed name for the parameter
		parameter.setNameDisplayed("Filterd");
		// Get the displayed name of the parameter
		String name = parameter.getNameDisplayed();
		// Check that the displayed name was properly set
		assertTrue(name.equals("Filterd"));
	}
	
	@Test
	public void testExplanationMethods() {
		// Set new explanation for the parameter
		parameter.setExplanation("Filterd");
		// Get the explanation of the parameter
		String name = parameter.getExplanation();
		// Check that the explanation was properly set
		assertTrue(name.equals("Filterd"));
	}

}
