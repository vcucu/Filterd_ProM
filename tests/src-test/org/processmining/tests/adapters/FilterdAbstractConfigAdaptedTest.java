package org.processmining.tests.adapters;

import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.gui.adapters.FilterdAbstractConfigAdapted;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.tests.filters.FilterdPackageTest;

public class FilterdAbstractConfigAdaptedTest extends FilterdPackageTest {
	
	FilterdAbstractConfigAdapted filter = new FilterdAbstractConfigAdapted();
	
	public FilterdAbstractConfigAdaptedTest() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Test
	public void testClassNameMethods() {
		// Set the name of the filter model
		filter.setClassName("Filterd");
		// Get the name of the filter model
		String name = filter.getClassName();
		// Check that the name was properly set
		assertTrue(name.equals("Filterd"));
	}
	
	@Test
	public void testFilterNameMethods() {
		// Set the name of the filter model
		filter.setfilterTypeName("Filterd");
		// Get the name of the filter model
		String name = filter.getfilterTypeName();
		// Check that the name was properly set
		assertTrue(name.equals("Filterd"));
	}
	
	@Test
	public void testParameterMethods() {
		// Create new list to store the parameters
		List<Parameter> params = new ArrayList<>();
		// Create new parameter instance
		ParameterYesNo paramYesNo = new ParameterYesNo("Name", "Display Name", true);
		// Add new parameter to the list
		params.add(paramYesNo);
		// Set the parameters list of the filter model
		filter.setParameters(params);
		// Get the parameters list of the filter model
		List<Parameter> newParams = filter.getParameters();
		// Check that the parameters list was properly list
		assertEquals(newParams.size(), 1);
		assertTrue(newParams.equals(params));
	}
	
	@Test
	public void testLogMethods() {
		// Set a new log for the filter model
		filter.setLog(originalLog);
		// Get the log of the filter model
		XLog log = filter.getLog();
		// Check that the log was properly set
		assertTrue(equalLog(log, originalLog));
	}

}
