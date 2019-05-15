package org.processmining.tests.filterdpackage;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import org.deckfour.xes.in.XUniversalParser;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.junit.Test;

import junit.framework.TestCase;

/* Use this class to create new tests. 
 * The original log: test_event_log_orders.xes
 * Each filter is supposed to be tested on this log. 
 * 
 * When you construct a new filter, check the document text_specification.xlsx provided by Dirk.
 * Identify the feature that you are testing.
 * (first test is identified by test case 2 since the header of the file is on row 1)
 * Identify the method testing this feature. 
 * Replace "XLog computed = null;" by the result of your filter method called on "test_event_log_orders.xes". 
 * 
 * Each expected result is already computed and can be found in Filterd/tests/testfiles.
 * 
 */


public class FilterdPackageTest extends TestCase {
	XUniversalParser parser;
	XLog originalLog; // original log file;

	public FilterdPackageTest() throws Exception {
		this.parser = new XUniversalParser();
		this.originalLog = parseLog("test_event_log_orders.xes");
	}

	/* Parses a file from the project to an OpenXES log 
	 * @param name - name of file, e.g. "example.xes"
	 */
	public XLog parseLog(String name) throws Exception {
		String testFileRoot = System.getProperty("test.testFileRoot", "././tests/testfiles");
		File file = new File(testFileRoot + "/" + name);
		Collection<XLog> logs = parser.parse(file);

		return logs.iterator().next();
	}

	/* Check whether two XLogs are identical. 
	 * Assumes that both logs are sorted in ascending order by trace ID.
	 * Events inside each trace are sorted chronologically.
	 * 
	 * @param log1 - first log to be compared.
	 * @param log2 - second log to be compared.
	 * @result true - if logs are the same
	 * 		   false - otherwise.
	 */
	public boolean equalLog(XLog log1, XLog log2) {
		XLogInfoFactory factory = new XLogInfoFactory();
		XLogInfo infoExpected = factory.createLogInfo(log1);
		XLogInfo infoComputed = factory.createLogInfo(log2);

		int tracesExpected = infoExpected.getNumberOfTraces();
		int tracesComputed = infoComputed.getNumberOfTraces();
		int eventsExpected = infoExpected.getNumberOfEvents();
		int eventsComputed = infoComputed.getNumberOfEvents();

		/* the logs must have the same number of traces and events */
		if (tracesExpected != tracesComputed) return false;
		if (eventsExpected != eventsComputed) return false;

		for (int i = 0; i < log1.size(); i++) {
			XTrace trace1 = log1.get(i);
			XTrace trace2 = log2.get(i);

			if(trace1.getAttributes().get("concept:name").equals(trace2.getAttributes().get("concept:name")) 
					&& trace1.getAttributes().get("customer").equals(trace2.getAttributes().get("customer"))){
				for (int j = 0; j < trace1.size(); j++) {
					XEvent event1 = trace1.get(j);
					XEvent event2 = trace2.get(j);
					/* check whether the traces have the same events */
					if (!event1.getAttributes().equals(event2.getAttributes())) {
						System.out.println("Events do not have the same attr");
						return false;
					}
				}
			} else {
				System.out.println("Not the same traces");
				return false;
			}

		}
		return true;
	}

	/* Check whether a log is contained into another one. 
	 * Assumes that the target log (log1) is sorted in ascending order by trace ID.
	 * Events inside each trace are sorted chronologically.
	 * Log2 is contained in log1 if it contains a subset of log1's trace (with the original events).
	 * 
	 * @param log1 - target log.
	 * @param log2 - log to be checked if it is contained in log1.
	 * @result true - if log2 is contained in log1.
	 * 		   false - otherwise.
	 */
	public boolean containsLog(XLog log1, XLog log2) {
		XLogInfoFactory factory = new XLogInfoFactory();
		XLogInfo infoExpected = factory.createLogInfo(log1);
		XLogInfo infoComputed = factory.createLogInfo(log2);

		int tracesExpected = infoExpected.getNumberOfTraces();
		int tracesComputed = infoComputed.getNumberOfTraces();
		int eventsExpected = infoExpected.getNumberOfEvents();
		int eventsComputed = infoComputed.getNumberOfEvents();

		/* the second log cannot have more traces or events than the target one */
		if (tracesExpected < tracesComputed) return false;
		if (eventsExpected < eventsComputed) return false;

		/* sort log2 by concept:name */
		Collections.sort(log2, new Comparator<XTrace>() {
			@Override
			public int compare(XTrace t1, XTrace t2) {
				return t1.getAttributes().get("concept:name").compareTo(t2.getAttributes().get("concept:name"));
			}
		});

		int k = 0; // log2 index counter

		for (int i = 0; i < log1.size() && k < log2.size(); i++) {
			XTrace trace1 = log1.get(i);
			XTrace trace2 = log2.get(k);

			/* look for the trace in log1 corresponding to each trace in log2 */
			if(trace1.getAttributes().get("concept:name").equals(trace2.getAttributes().get("concept:name")) 
					&& trace1.getAttributes().get("customer").equals(trace2.getAttributes().get("customer"))){
				k++;
				/* check if the traces have the same events */
				for (int j = 0; j < trace1.size(); j++) {
					XEvent event1 = trace1.get(j);
					XEvent event2 = trace2.get(j);
					if (!event1.getAttributes().equals(event2.getAttributes())) {
						return false;
					}
				}
			}
		}

		if (k != log2.size()) return false; // check whether all of log2 has been verified.
		return true;
	}

	/* Corresponds to test case 2 from test_specification.xlsx.
	 * See ProM - Log on simple Heuristic.
	 * Selects traces with start event "receive order".
	 * 
	 * Result: original log without case 34.
	 */
	@Test
	public void testStartOrder() throws Throwable {
		XLog expected = parseLog("test_start_order.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 3 from test_specification.xlsx.
	 * See ProM - Log on simple Heuristic.
	 * Selects traces with start event "receive payment".
	 * 
	 * Result: only case 34.
	 */
	@Test
	public void testStartPayment() throws Throwable {
		XLog expected = parseLog("test_start_payment.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 4 from test_specification.xlsx.
	 * See ProM - Log on simple Heuristic.
	 * Selects traces with the most popular (frequent) start event.
	 * 
	 * Result: original log without case 34.
	 */
	@Test
	public void testStartFrequent() throws Throwable {
		XLog expected = parseLog("test_start_frequent.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 5 from test_specification.xlsx.
	 * See ProM - Log on simple Heuristic.
	 * Selects traces with end event "archive".
	 * 
	 * Result: original log without case 72.
	 */
	@Test
	public void testEndArchive() throws Throwable {
		XLog expected = parseLog("test_end_archive.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 6 from test_specification.xlsx.
	 * See ProM - Log on simple Heuristic.
	 * Selects traces with end event "ship parcel".
	 * 
	 * Result: only case 72.
	 */
	@Test
	public void testEndParcel() throws Throwable {
		XLog expected = parseLog("test_end_parcel.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 7 from test_specification.xlsx.
	 * See ProM - Log on simple Heuristic.
	 * Selects traces with the most popular (frequent) end event.
	 * 
	 * Result: original log without case 72.
	 */
	@Test
	public void testEndFrequent() throws Throwable {
		XLog expected = parseLog("test_end_frequent.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 8 from test_specification.xlsx.
	 * See ProM - Log on simple Heuristic.
	 * Keep all events of all life-cycle types.
	 * 
	 * Result: original log.
	 */
	@Test
	public void testLifeCycleAll() throws Throwable {
		XLog expected = parseLog("test_lc_all.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 9 from test_specification.xlsx.
	 * See ProM - Log on simple Heuristic.
	 * Remove events of with life-cycle type "start".
	 * 
	 * Result: e.g., case 35 no longer contains "pack order + start", has only 7 events
	 */
	@Test
	public void testLifeCycleRemoveStart() throws Throwable {
		XLog expected = parseLog("test_lc_start.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 10 from test_specification.xlsx.
	 * See ProM - Log on simple Heuristic.
	 * Remove events of with life-cycle type "resume" and "suspend".
	 * 
	 * Result: e.g., case 72 has only 6 events.
	 */
	@Test
	public void testLifeCycleRemoveMultiples() throws Throwable {
		XLog expected = parseLog("test_lc_resume.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 11 from test_specification.xlsx.
	 * See ProM - Filter Log on Event Attribute Values.
	 * Remove events with name "pack order".
	 * 
	 * Result: e.g. case 34 stays the same, case 35 has 6 events.
	 */
	@Test
	public void testNameRemove() throws Throwable {
		XLog expected = parseLog("test_event_name_remove.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 13 from test_specification.xlsx.
	 * See ProM - Filter In High Frequency Traces.
	 * Threshold 50%.
	 * 
	 * Result: cases 41, 73, 56, 74, 75, 76.
	 */
	@Test
	public void testInFrequency1() throws Throwable {
		XLog expected = parseLog("test_fin_50.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 14 from test_specification.xlsx.
	 * See ProM - Filter In High Frequency Traces.
	 * Threshold 25%.
	 * 
	 * Result: cases 56, 74, 75, 76.
	 */
	@Test
	public void testInFrequency2() throws Throwable {
		XLog expected = parseLog("test_fin_25.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 15 from test_specification.xlsx.
	 * See ProM - Filter Out Low Occurence Traces.
	 * Threshold 2%.
	 * 
	 * Result: cases 41, 56, 73, 74, 75, 76.
	 */
	@Test
	public void testOutOccurence1() throws Throwable {
		XLog expected = parseLog("test_oout_2.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 16 from test_specification.xlsx.
	 * See ProM - Filter Out Low Occurence Traces.
	 * Threshold 3%.
	 * 
	 * Result: cases 56, 74, 75, 76.
	 */
	@Test
	public void testOutOccurence2() throws Throwable {
		XLog expected = parseLog("test_oout_3.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 17 from test_specification.xlsx.
	 * See ProM - Filter Out Low Frequency Traces.
	 * Threshold 5%.
	 * 
	 * Result: original log.
	 */
	@Test
	public void testOutFrequency1() throws Throwable {
		XLog expected = parseLog("test_fout_5.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 18 from test_specification.xlsx.
	 * See ProM - Filter Out Low Frequency Traces.
	 * Threshold 25%.
	 * 
	 * Result: cases 41, 56, 73, 74, 75, 76.
	 */
	@Test
	public void testOutFrequency2() throws Throwable {
		XLog expected = parseLog("test_fout_25.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 19 from test_specification.xlsx.
	 * See ProM - Project Log onto Events // Filter log on event attribute value.
	 * Keep events with lifecycle:transition = abort.
	 * 
	 * Result: cases 56, 74, 75, 76 - 1 event.
	 */
	@Test
	public void testEventAttribute1() throws Throwable {
		XLog expected = parseLog("test_event_attribute_abort.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 20 from test_specification.xlsx.
	 * See ProM - Project Log onto Events // Filter log on event attribute value.
	 * Keep events with delivery = 514, 623 and "remove if no value provided = true".
	 * 
	 * Result: case 41 - 4 events
	 * 		 cases 56, 76 - 1 event.
	 */
	@Test
	public void testEventAttribute2() throws Throwable {
		XLog expected = parseLog("test_event_attribute_delivery_true.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 21 from test_specification.xlsx.
	 * See ProM - Project Log onto Events // Filter log on event attribute value.
	 * Keep events with delivery = 514, 623 and "remove if no value provided = false".
	 * 
	 * Result: cases 56, 76, 74, 75, 34, 35, 41, 72, 73.
	 */
	@Test
	public void testEventAttribute3() throws Throwable {
		XLog expected = parseLog("test_event_attribute_delivery_false.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 22 from test_specification.xlsx.
	 * See ProM - Project Log onto Events // Filter log on event attribute value.
	 * Keep events with org:resource=System and "remove if no value provided = false".
	 * 
	 * Result: cases 35, 56, 74, 75, 76 - 2 events
	 * 		 case 34 - 1 event.
	 */
	@Test
	public void testEventAttribute4() throws Throwable {
		XLog expected = parseLog("test_event_attribute_org.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 23 from test_specification.xlsx.
	 * See ProM - Filter log on trace attribute values.
	 * Keep traces with concept:name = 41, 56, 76.
	 * 
	 * Result: cases 41, 56, 76.
	 */
	@Test
	public void testTraceAttribute1() throws Throwable {
		XLog expected = parseLog("test_trace_attribute_name.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 24 from test_specification.xlsx.
	 * See ProM - Filter log on trace attribute values.
	 * Keep traces with customer = X, Y, Z.
	 * 
	 * Result: cases 34, 35, 41, 56, 74, 75.
	 */
	@Test
	public void testTraceAttribute2() throws Throwable {
		XLog expected = parseLog("test_trace_attribute_customer.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}


	/* Corresponds to test case 25 from test_specification.xlsx.
	 * See ProM - Filter log by attributes.
	 * Keep traces with an event having delivery = 623, 514.
	 * 
	 * Result: cases 56, 76 - 6 events
	 * 		 case 41 - 8 events.
	 */
	@Test
	public void testLogAttribute1() throws Throwable {
		XLog expected = parseLog("test_log_attribute_delivery.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}


	/* Corresponds to test case 26 from test_specification.xlsx.
	 * See ProM - Filter log by attributes.
	 * Keep traces with an event having lifecycle:transition = abort.
	 * 
	 * Result: cases 56, 74, 75, 76 - 6 events.
	 */
	@Test
	public void testLogAttribute2() throws Throwable {
		XLog expected = parseLog("test_log_attribute_abort.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* NO TEST FILE!!!!!!
	 * Corresponds to test case 27 from test_specification.xlsx.
	 * See ProM - Filter log by attributes.
	 * Keep traces with minimum number of events = 3 and maximum = 7.
	 * 
	 * Result: cases 56, 74, 75, 76 - 6 events.
	 */
	@Test
	public void testLogAttribute3() throws Throwable {
		XLog expected = parseLog("test_log_attribute_min_max.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 28 from test_specification.xlsx.
	 * See ProM - Merge subsequent events.
	 * Merge by event name, look into all event classes, compare event classes, merge taking last event.
	 * 
	 * Result: case 35 has 7 events with an "add item" event having "item=Gameboy".
	 */
	@Test
	public void testMergeEvents() throws Throwable {
		XLog expected = parseLog("test_merge_events.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 29 from test_specification.xlsx.
	 * Keeps events contained in 01/01/2019 - end of original log.
	 * 
	 * Result: case 76 - 6 events.
	 */
	@Test
	public void testTimeframeContains() throws Throwable {
		XLog expected = parseLog("test_timeframe_1.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 30 from test_specification.xlsx.
	 * Keeps trace intersecting with 24/12/2018 0:00 - 26/12/2018 23:59:59.
	 * 
	 * Result: case 41 - 8 events.
	 */
	@Test
	public void testTimeframeIntersect() throws Throwable {
		XLog expected = parseLog("test_timeframe_2.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}
	
	/* NOT IMPLEMENTED YET!!
	 * Corresponds to test case 31 from test_specification.xlsx.
	 * See Disco Endpoints - discard cases.
	 * 
	 * Result: 
	 */
	@Test
	public void testEndpointsDiscard() throws Throwable {
		XLog expected = parseLog("test_endpoints_1.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 32 from test_specification.xlsx.
	 * See Disco Endpoints - trim longest ("receive order" - "ship parcel").
	 * 
	 * Result: cases 41, 73 - 6 events
	 * 		 case  35 - 5 events
	 * 		 case  72 - 8 events.
	 */
	@Test
	public void testEndpointsLongest() throws Throwable {
		XLog expected = parseLog("test_endpoints_2.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 33 from test_specification.xlsx.
	 * See Disco Endpoints - trim first ("receive order" - "add item").
	 * 
	 * Result: cases 35, 41, 72, 73 - 3 events
	 * 		 cases 56, 74, 75, 76 - 4 events.
	 */
	@Test
	public void testEndpointsFirst() throws Throwable {
		XLog expected = parseLog("test_endpoints_3.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 34 from test_specification.xlsx.
	 * See Disco Performance - duration of min 24-hours - max inf.
	 * 
	 * Result: cases 35, 41, 72.
	 */
	@Test
	public void testPerformanceDuration() throws Throwable {
		XLog expected = parseLog("test_performance_1.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 35 from test_specification.xlsx.
	 * See Disco Endpoints - number of events: min 3 events - max 6 events.
	 * See ProM - Filter log by attributes = "Select traces from log".
	 * 
	 * Result: cases 56, 74, 75, 76.
	 */
	@Test
	public void testPerformanceEvents() throws Throwable {
		XLog expected = parseLog("test_performance_2.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 36 from test_specification.xlsx.
	 * See Disco Attributes - keep selected events with concept:name != "ship parcel"
	 * 
	 * Result: case 34
	 * 		 cases 41, 56, 73, 74, 75, 76 - 6 events
	 * 		 cases 35, 72 - 7 events
	 */
	@Test
	public void testAttributesKeep() throws Throwable {
		XLog expected = parseLog("test_attributes_1.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 37 from test_specification.xlsx.
	 * See Disco Attributes - mandatory traces having at least one event with
	 * "concept:name" != "archive" and "receive payment"
	 * 
	 * Result: each case except case 34.
	 */
	@Test
	public void testAttributesMandatory() throws Throwable {
		XLog expected = parseLog("test_attributes_2.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 38 from test_specification.xlsx.
	 * See Disco Follower - traces with "add item" event directly followed by "add item".
	 * 
	 * Result: case 35.
	 */
	@Test
	public void testFollowerDirectly() throws Throwable {
		XLog expected = parseLog("test_follower_1.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 39 from test_specification.xlsx.
	 * See Disco Follower - traces with "add item" event eventually followed by "add item".
	 * 
	 * Result: cases 35, 41, 72, 73.
	 */
	@Test
	public void testFollowerEventualy() throws Throwable {
		XLog expected = parseLog("test_follower_2.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 40 from test_specification.xlsx.
	 * See Disco Follower - traces with "add item" event eventually followed by "add item" 
	 * after 1hr.
	 * Result: case 72, 73.
	 */
	@Test
	public void testFollowerEventualy2() throws Throwable {
		XLog expected = parseLog("test_follower_3.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 41 from test_specification.xlsx.
	 * See Disco Follower - traces with "add item" event eventually followed by "add item" different resource.
	 * 
	 * Result: no case.
	 */
	@Test
	public void testFollowerEventualy3() throws Throwable {
		XLog expected = parseLog("test_follower_4.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 42 from test_specification.xlsx.
	 * See Disco Follower - traces with "add item" event eventually followed by "add item" 
	 * different delivery.
	 * 
	 * Result: case 41.
	 */
	@Test
	public void testFollowerEventualy4() throws Throwable {
		XLog expected = parseLog("test_follower_5.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 43 from test_specification.xlsx.
	 * See Disco Follower - traces with "add item" event eventually followed by "add item" 
	 * same delivery.
	 * 
	 * Result: case 35, 72, 73.
	 */
	@Test
	public void testFollowerEventualy5() throws Throwable {
		XLog expected = parseLog("test_follower_6.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 44 from test_specification.xlsx.
	 * See ProM - Unroll Loops.
	 * in each trace, each event has a unique name (no two events in a trace have the same name), 
	 * e.g., in cases 41, 73, the 3rd and the 5th event have different names 
	 * (add item, add item_2).
	 * 
	 * Result: renamed events.
	 */
	@Test
	public void testUnroll() throws Throwable {
		XLog expected = parseLog("test_unroll.xes");
		XLog computed = null; // insert filter operation

		assert equalLog(expected, computed);
	}

	/* Corresponds to test case 45 from test_specification.xlsx.
	 * See ProM - Extract sample of random traces with parameter 3 and empty seed.
	 * 
	 * Result: log with 3 random UNIQUE traces (which were part of the original log).
	 */
	@Test
	public void testExtractSample() throws Throwable {
		XLog expected = originalLog;
		XLog computed = null; // insert filter operation

		assert containsLog(expected, computed);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(FilterdPackageTest.class);
	}

}
