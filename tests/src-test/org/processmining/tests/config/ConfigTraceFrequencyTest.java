package org.processmining.tests.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.configurations.FilterdTraceFrequencyConfig;
import org.processmining.filterd.filters.FilterdTraceFrequencyFilter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.tests.filters.FilterdPackageTest;

public class ConfigTraceFrequencyTest extends FilterdPackageTest {

	public ConfigTraceFrequencyTest() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * Setting an occurrence value bigger than the number of traces returns false
	 * Chosen value: 10.0
	 */
	@Test
	public void testGreaterOccurrence() throws Throwable {
		XLog typed = originalLog;	
		XLog typed2 = parseLog("start-events", "test_check_validity_invalid.xes");
		List<Double> pair = new ArrayList<>(Arrays.asList(0.0, 10.0));
		FilterdTraceFrequencyConfig config = new FilterdTraceFrequencyConfig(typed,
				new FilterdTraceFrequencyFilter());
		ParameterOneFromSet thresholdType = (ParameterOneFromSet) config.getParameter("FreqOcc");
		thresholdType.setChosen("occurrence");
		ParameterRangeFromRange<Double> threshold = (ParameterRangeFromRange<Double>) config.getParameter("threshold");
		threshold.setChosenPair(pair);
		assert(!config.checkValidity(typed2));
		
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(ConfigTraceFrequencyTest.class);
	}

}
