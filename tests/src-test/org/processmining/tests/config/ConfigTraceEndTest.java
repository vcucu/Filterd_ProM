package org.processmining.tests.config;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.configurations.FilterdTraceEndEventCategoricalConfig;
import org.processmining.filterd.configurations.FilterdTraceEndEventConfig;
import org.processmining.filterd.filters.FilterdTraceEndEventFilter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.tests.filters.FilterdPackageTest;

public class ConfigTraceEndTest extends FilterdPackageTest{
	
	public ConfigTraceEndTest() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	/*
	 * Check whether an invalid log is detected to be invalid
	 * Chosen attribute : "invalid"  
	 * Result: checkValidity()	returns false since "invalid" is not one of the attributes 
	 * of the original log
	 * 
	 */
	@Test
	public void test() throws Throwable {
		XLog typed = parseLog("start-events", "test_check_validity_invalid.xes");
		FilterdTraceEndEventConfig config = new FilterdTraceEndEventConfig(typed, new FilterdTraceEndEventFilter());
		
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
		FilterdTraceEndEventCategoricalConfig config = new FilterdTraceEndEventCategoricalConfig
				(typed, new FilterdTraceEndEventFilter(), "MXML Legacy Classifier", Toolbox.computeComplexClassifiers(typed));
	
		assert(config.checkValidity(originalLog));
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(ConfigTraceStartTest.class);
	}
}
