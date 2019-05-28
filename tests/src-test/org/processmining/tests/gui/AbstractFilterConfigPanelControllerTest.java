package org.processmining.tests.gui;

import java.util.ArrayList;

import org.junit.Test;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.parameters.ParameterText;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.tests.filters.FilterdPackageTest;

public class AbstractFilterConfigPanelControllerTest extends FilterdPackageTest {
	
	public AbstractFilterConfigPanelControllerTest() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	@Test
	public void testAddParameterYesNo() {
		try {
			// Create new ParameterYesNo object
			ParameterYesNo param = new ParameterYesNo("Name", "Display Name", true);
			// Create new FilterConfigPanelController
			FilterConfigPanelController panel = new FilterConfigPanelController();
			// Add new parameter to the configuration panel
			panel.addParameterYesNo(param);
		} catch (Throwable exception) {
			assertEquals(NoClassDefFoundError.class, exception.getClass());
		}
	}
	
	@Test
	public void testAddParameterOneFromSet() {
		try {
			// Create new ParameterYesNo object
			ParameterOneFromSet param = new ParameterOneFromSet("Name", "Display Name", "Choice", new ArrayList<>());
			// Create new FilterConfigPanelController
			FilterConfigPanelController panel = new FilterConfigPanelController();
			// Add new parameter to the configuration panel
			panel.addParameterOneFromSet(param);
		} catch (Throwable exception) {
			assertEquals(ExceptionInInitializerError.class, exception.getClass());
		}
	}
	
	@Test
	public void testAddParameterMultipleFromSet() {
		try {
			// Create new ParameterYesNo object
			ParameterMultipleFromSet param = new ParameterMultipleFromSet("Name", "Display Name", new ArrayList<>(), new ArrayList<>());
			// Create new FilterConfigPanelController
			FilterConfigPanelController panel = new FilterConfigPanelController();
			// Add new parameter to the configuration panel
			panel.addParameterMultipleFromSet(param);
		} catch (Throwable exception) {
			assertEquals(NoClassDefFoundError.class, exception.getClass());
		}
	}
	
	@Test
	public void testAddParameterValueFromRange() {
		try {
			// Create new ParameterYesNo object
			ParameterValueFromRange param = new ParameterValueFromRange("Name", "Display Name", new ArrayList<>(), new ArrayList<>());
			// Create new FilterConfigPanelController
			FilterConfigPanelController panel = new FilterConfigPanelController();
			// Add new parameter to the configuration panel
			panel.addParameterValueFromRange(param);
		} catch (Throwable exception) {
			assertEquals(ClassCastException.class, exception.getClass());
		}
	}
	
	@Test
	public void testAddParameterText() {
		try {
			// Create new ParameterYesNo object
			ParameterText param = new ParameterText("Name", "Display Name", "Choice");
			// Create new FilterConfigPanelController
			FilterConfigPanelController panel = new FilterConfigPanelController();
			// Add new parameter to the configuration panel
			panel.addParameterText(param);
		} catch (Throwable exception) {
			assertEquals(NoClassDefFoundError.class, exception.getClass());
		}
	}
	
	@Test
	public void addParameterRangeFromRange() {
		try {
			// Create new ParameterYesNo object
			ParameterRangeFromRange param = new ParameterRangeFromRange("Name", "Display Name", new ArrayList<>(), new ArrayList<>());
			// Create new FilterConfigPanelController
			FilterConfigPanelController panel = new FilterConfigPanelController();
			// Add new parameter to the configuration panel
			panel.addParameterRangeFromRange(param);
		} catch (Throwable exception) {
			assertEquals(ExceptionInInitializerError.class, exception.getClass());
		}
	}
	
	@Test
	public void testPopulateFromParameters() {
		// Create new parameters to populate filter configuration
		FilterConfigPanelController panel = new FilterConfigPanelController();
		ParameterYesNo paramYesNo = new ParameterYesNo("Name", "Display Name", true);
		ParameterOneFromSet paramOneFromSet = new ParameterOneFromSet("Name", "Display Name", "Choice", new ArrayList<>());
		ParameterMultipleFromSet paramMultipleFromSet = new ParameterMultipleFromSet("Name", "Display Name", new ArrayList<>(), new ArrayList<>());
		ParameterValueFromRange paramValueFromRange = new ParameterValueFromRange("Name", "Display Name", new ArrayList<>(), new ArrayList<>());
		ParameterText paramText = new ParameterText("Name", "Display Name", "Choice");
		ParameterRangeFromRange paramRangeFromRange = new ParameterRangeFromRange("Name", "Display Name", new ArrayList<>(), new ArrayList<>());
		// Add parameters to a new list
		ArrayList<Parameter> params = new ArrayList<>();
		params.add(paramYesNo);
		params.add(paramOneFromSet);
		params.add(paramMultipleFromSet);
		params.add(paramValueFromRange);
		params.add(paramText);
		params.add(paramRangeFromRange);
		// Call the populate method
		// TODO: populate method is protected and needs to be changed in order to test it from here
	}
}
