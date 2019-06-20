package org.processmining.tests.config;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.configurations.FilterdTracesHavingEventConfig;
import org.processmining.filterd.filters.FilterdTracesHavingEventFilter;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.tests.filters.FilterdPackageTest;


public class ConfigTracesHavingEventTest extends FilterdPackageTest {
	public ConfigTracesHavingEventTest() throws Exception {
		super();
	}
	
	public void testCanPopulate() throws Exception {
		XLog typed = originalLog;	
		FilterdTracesHavingEventConfig config = new FilterdTracesHavingEventConfig(typed,
				new FilterdTracesHavingEventFilter());
		assertTrue(config.canPopulate(new FilterConfigPanelController()));
	}
	
	public void testCheckValidity() throws Exception {
		XLog typed = originalLog;	
		FilterdTracesHavingEventConfig config = new FilterdTracesHavingEventConfig(typed,
				new FilterdTracesHavingEventFilter());
		assertTrue(config.checkValidity(typed));
		XLog empty = parseLog("", "test_empty_event_log.xes");
		assertFalse(!config.checkValidity(empty));
		
	}
	public static void main(String[] args) {
		junit.textui.TestRunner.run(ConfigTracesHavingEventTest.class);
	}
	
}
