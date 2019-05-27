package org.processmining.tests.filters;
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

import junit.framework.TestCase;

/* This class contains some methods for comparin logs. 
 * The original log used for most tests: test_event_log_orders.xes
 * Each filter is supposed to be tested on this log. 
 * 
 * Identify the feature that you are testing.
 * Identify the method testing this feature. 
 * Go to/create the filter test class corresponding to your filter
 * Replace "XLog computed = null;" by the result of your filter method called on originalLog. 
 * Or add a new test. 
 * Each expected result is already computed and can be found in
 * Filterd/tests/testfiles/ + the test files folder found in the header of the class.
 * 
 */


public class FilterdPackageTest extends TestCase {
	public XUniversalParser parser;
	public XLog originalLog; // original log file;

	public FilterdPackageTest() throws Exception {
		this.parser = new XUniversalParser();
		this.originalLog = parseLog("", "test_event_log_orders.xes");
	}

	/* Parses a file from the project to an OpenXES log 
	 * @param name - name of file, e.g. "example.xes"
	 */
	public XLog parseLog(String folder, String name) throws Exception {
		String testFileRoot = System.getProperty("test.testFileRoot", "././tests/testfiles");
		File file = new File(testFileRoot + "/" + folder + "/" + name);
		Collection<XLog> logs = parser.parse(file);

		return logs.iterator().next();
	}


	/* Check whether two XLogs are identical. 
	 * Assumes that both logs are sorted in ascending order by trace ID.
	 * Events inside each trace are sorted chronologically.
	 * 
	 * @param expected - first log to be compared.
	 * @param computed - second log to be compared.
	 * @result true - if logs are the same
	 * 		   false - otherwise.
	 */
	public boolean equalLog(XLog expected, XLog computed) {
		XLogInfoFactory factory = new XLogInfoFactory();
		XLogInfo infoExpected = factory.createLogInfo(expected);
		XLogInfo infoComputed = factory.createLogInfo(computed);


		int tracesExpected = infoExpected.getNumberOfTraces();
		int tracesComputed = infoComputed.getNumberOfTraces();
		int eventsExpected = infoExpected.getNumberOfEvents();
		int eventsComputed = infoComputed.getNumberOfEvents();

		/* the logs must have the same number of traces and events */
		if (tracesExpected != tracesComputed) {
			System.out.println("traces expected " + tracesExpected + " traces computed " + tracesComputed); 
			return false; 
		}
		if (eventsExpected != eventsComputed) {
			System.out.println("traces expected " + tracesExpected + " traces computed " + tracesComputed);
			return false;
		}

		for (int i = 0; i < expected.size(); i++) {
			XTrace trace1 = expected.get(i);
			XTrace trace2 = computed.get(i);

			if (trace1.getAttributes().get("concept:name").equals(trace2.getAttributes().get("concept:name")) 
					&& trace1.getAttributes().get("customer").equals(trace2.getAttributes().get("customer"))){
				for (int j = 0; j < trace1.size(); j++) {
					XEvent event1 = trace1.get(j);
					XEvent event2 = trace2.get(j);
					/* check whether the traces have the same events */
					if (!event1.getAttributes().equals(event2.getAttributes())) {
						System.out.println("Events do not have the same attributes");
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
}