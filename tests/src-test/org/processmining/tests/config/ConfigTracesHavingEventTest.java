package org.processmining.tests.config;

import java.util.ArrayList;
import java.util.Arrays;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.configurations.FilterdEventAttrCategoricalConfig;
import org.processmining.filterd.configurations.FilterdEventAttrConfig;
import org.processmining.filterd.configurations.FilterdEventAttrDateConfig;
import org.processmining.filterd.configurations.FilterdEventAttrNumericalConfig;
import org.processmining.filterd.configurations.FilterdTraceFrequencyConfig;
import org.processmining.filterd.configurations.FilterdTracesHavingEventConfig;
import org.processmining.filterd.filters.FilterdEventAttrFilter;
import org.processmining.filterd.filters.FilterdTraceFrequencyFilter;
import org.processmining.filterd.filters.FilterdTracesHavingEvent;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.tests.filters.FilterdPackageTest;

public class ConfigTracesHavingEventTest extends FilterdPackageTest {
	public ConfigTracesHavingEventTest() throws Exception {
		super();
	}
	
	public void testCanPopulate() throws Exception {
		XLog typed = originalLog;	
		FilterdTracesHavingEventConfig config = new FilterdTracesHavingEventConfig(typed,
				new FilterdTracesHavingEvent());
		assertTrue(config.canPopulate(new FilterConfigPanelController()));
	}
	
	public void testCheckValidity() throws Exception {
		XLog typed = originalLog;	
		FilterdTracesHavingEventConfig config = new FilterdTracesHavingEventConfig(typed,
				new FilterdTracesHavingEvent());
		assertTrue(config.checkValidity(typed));
		XLog empty = parseLog("", "test_empty_event_log.xes");
		assertFalse(config.checkValidity(empty));
		
	}
	public static void main(String[] args) {
		junit.textui.TestRunner.run(ConfigTracesHavingEventTest.class);
	}
	
}
