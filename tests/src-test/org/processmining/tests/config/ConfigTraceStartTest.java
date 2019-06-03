package org.processmining.tests.config;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.configurations.FilterdTraceStartEventCategoricalConfig;
import org.processmining.filterd.configurations.FilterdTraceStartEventConfig;
import org.processmining.filterd.filters.FilterdTraceStartEventFilter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.tests.filters.FilterdPackageTest;

public class ConfigTraceStartTest extends FilterdPackageTest{
	
	public ConfigTraceStartTest() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	/*
	 * Check whether an invalid log is detected to be invalid
	 * Chosen attribute : "invalid"  
	 * Result: false since "invalid" is not one of the attributes 
	 * of the original log
	 */
	@Test
	public void testInvalid() throws Throwable {
		XLog typed = parseLog("start-events", "test_check_validity_invalid.xes");
		FilterdTraceStartEventConfig config = new FilterdTraceStartEventConfig(typed, new FilterdTraceStartEventFilter());
		
		ParameterOneFromSet attr = (ParameterOneFromSet) config.getParameter("attribute");
		attr.setChosen("invalid");
		
		assert(!config.checkValidity(originalLog));
		
	}
	
	/*
	 * Check validity when a complex classifier is selected from the candidate log
	 * Chosen classifier: "MXML Legacy Classifier"
	 * Result: true, since the candidate log has this classifier too.
	 */
	@Test
	public void testValidWithClassifier() throws Throwable {
		XLog typed = parseLog("start-events", "test_check_validity_invalid.xes");
		FilterdTraceStartEventCategoricalConfig config = new FilterdTraceStartEventCategoricalConfig
				(typed, new FilterdTraceStartEventFilter(), "MXML Legacy Classifier", Toolbox.computeComplexClassifiers(typed));
	
		assert(config.checkValidity(originalLog));
	}
	
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(ConfigTraceStartTest.class);
	}
}
