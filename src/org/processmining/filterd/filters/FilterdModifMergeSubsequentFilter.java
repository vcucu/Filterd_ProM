package org.processmining.filterd.filters;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XLifecycleExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.log.utils.XUtils;

public class FilterdModifMergeSubsequentFilter extends Filter {
	
	
	private static final int TIME_DIFFERENCE = 1000 * 60;

	public enum MergeFilter {SAME_CLASS("Compare event class") {
		public boolean sameEvent(XEventClassifier classifier, Set<String> relevantAttributes, XEvent eventA,
				XEvent eventB) {
			return classifier.sameEventClass(eventA, eventB);
		}
	},
	SHORT_TIME("Compare event timestamps") {
		public boolean sameEvent(XEventClassifier classifier, Set<String> relevantAttributes, XEvent eventA,
				XEvent eventB) {
			Date timeA = XTimeExtension.instance().extractTimestamp(eventA);
			Date timeB = XTimeExtension.instance().extractTimestamp(eventB);
			if (timeA != null && timeB != null) {
				return Math.abs(timeB.getTime() - timeA.getTime()) < TIME_DIFFERENCE;
			} else {
				return false;
			}
		}
	},
	SAME_DATA("Compare event class & attributes") {
			public boolean sameEvent(XEventClassifier classifier, Set<String> relevantAttributes, XEvent eventA,
					XEvent eventB) {
				return classifier.sameEventClass(eventA, eventB);
			}
		};

		private final String label;

		private MergeFilter(String label) {
			this.label = label;
		}

		public String toString() {
			return label;
		}

		public abstract boolean sameEvent(XEventClassifier classifier, Set<String> relevantAttributes,
				XEvent subsequentEvent, XEvent currentEvent);
	}
	
	
	public enum MergeType {
		PLAIN_FIRST("Merge taking first event") {
			public long merge(XEventClasses eventClasses, XFactory factory, long instance, XTrace newTrace,
					ListIterator<XEvent> iterator, XEvent currentEvent, MergeFilter filter,
					Set<String> relevantAttributes) {
				// Just add first
				newTrace.add(factory.createEvent((XAttributeMap) currentEvent.getAttributes().clone()));

				// Look forward for more event classes of the same type
				while (iterator.hasNext()) {
					XEvent subsequentEvent = iterator.next();
					if (!filter.sameEvent(eventClasses.getClassifier(), relevantAttributes, subsequentEvent,
							currentEvent)) {
						// Go back and stop merging
						iterator.previous();
						break;
					}
				}
				return ++instance;
			}
		},
		PLAIN_LAST("Merge taking last event") {
			public long merge(XEventClasses eventClasses, XFactory factory, long instance, XTrace newTrace,
					ListIterator<XEvent> iterator, XEvent currentEvent, MergeFilter filter,
					Set<String> relevantAttributes) {
				XEvent lastEvent = currentEvent;

				// Look forward for more event classes of the same type
				while (iterator.hasNext()) {
					lastEvent = iterator.next();
					if (!filter.sameEvent(eventClasses.getClassifier(), relevantAttributes, lastEvent, currentEvent)) {
						// Go back and stop merging
						iterator.previous();
						// Go back to the last event with the same event class
						lastEvent = iterator.previous();
						iterator.next();
						break;
					}
				}

				// Add last event
				newTrace.add(factory.createEvent((XAttributeMap) lastEvent.getAttributes().clone()));
				return ++instance;
			}
		},
		LIFECYCLE("Merge taking first as 'start' and last as 'complete' life-cycle transitions") {

			public long merge(XEventClasses eventClasses, XFactory factory, long instance, XTrace newTrace,
					ListIterator<XEvent> iterator, XEvent currentEvent, MergeFilter filter,
					Set<String> relevantAttributes) {
				XEvent newStartEvent = factory.createEvent((XAttributeMap) currentEvent.getAttributes().clone());
				XLifecycleExtension.instance().assignStandardTransition(newStartEvent,
						XLifecycleExtension.StandardModel.START);
				XConceptExtension.instance().assignInstance(newStartEvent, String.valueOf(instance));

				newTrace.add(newStartEvent);

				XEvent lastEvent = currentEvent;

				// Look forward for more event classes of the same type
				while (iterator.hasNext()) {
					lastEvent = iterator.next();
					if (!filter.sameEvent(eventClasses.getClassifier(), relevantAttributes, lastEvent, currentEvent)) {
						// Stop merging, go back so this event is being picked-up by the main iteration
						iterator.previous();
						// Go back once again and remember last time stamp
						lastEvent = iterator.previous();
						// Go forward again :)
						iterator.next();
						break;
					}
				}

				// Copy last found event
				lastEvent = factory.createEvent((XAttributeMap) lastEvent.getAttributes().clone());

				XLifecycleExtension.instance().assignStandardTransition(lastEvent,
						XLifecycleExtension.StandardModel.COMPLETE);
				XConceptExtension.instance().assignInstance(lastEvent, String.valueOf(instance));
				newTrace.add(lastEvent);

				return ++instance;
			}

		};

		private final String label;

		private MergeType(String label) {
			this.label = label;
		}

		public String toString() {
			return label;
		}

		public abstract long merge(XEventClasses eventClasses, XFactory factory, long instance, XTrace newTrace,
				ListIterator<XEvent> iterator, XEvent currentEvent, MergeFilter mergeFilter,
				Set<String> relevantAttributes);

	}
	
	
	public XLog filter(XLog log, List<Parameter> parameters) {
		
		ParameterOneFromSet classifierParam = (ParameterOneFromSet)this
				.getParameter(parameters, "classifier");
		ParameterMultipleFromSet desiredEventsParam = (ParameterMultipleFromSet)this
				.getParameter(parameters, "desiredEvents");
		ParameterOneFromSet comparisonTypeParam = (ParameterOneFromSet)this
				.getParameter(parameters, "comparisonType");
		ParameterOneFromSet mergeTypeParam = (ParameterOneFromSet)this
				.getParameter(parameters, "mergeType");
		ParameterMultipleFromSet relevantAttributesParam = (ParameterMultipleFromSet)this
				.getParameter(parameters, "relevantAttributes");
		
		
		
		// get classifier and the corresponding event classes as XEventClasses - eventClasses
		XEventClassifier classifier = Toolbox.computeClassifier(log, classifierParam.getChosen());
		XEventClasses eventClasses = XLogInfoImpl.create(log).getEventClasses(classifier);
		
		// create a clone of the log
		XFactory f = XFactoryRegistry.instance().currentDefault();

		// get the desiredEvents as Set<XEventClass> - relevantClasses
		 Set<XEventClass> desiredEvents = new HashSet<>();
		 List<String> desiredEventsNames = desiredEventsParam.getChosen();
		 
		 for (XEventClass e : eventClasses.getClasses()) {
			 if (desiredEventsNames.contains(e.toString())) {
				 desiredEvents.add(e);
			 }
		 }
		
		// get the comparisonType as MergeFilter - mergeFilter
		 MergeFilter mergeFilter;
		 switch(comparisonTypeParam.getChosen()) {
				case "Compare event class" : mergeFilter = MergeFilter.SAME_CLASS;
				break;
				case "Compare event timestamps" : mergeFilter = MergeFilter.SHORT_TIME;
				break;
				case "Compare event class & attributes" : mergeFilter = MergeFilter.SAME_DATA;
				break;
				default: mergeFilter = MergeFilter.SAME_CLASS;
				break;
			}		
		// get the relevantAttributes as Set<String> - relevantAttributes
		 Set<String> relevantAttributes = new HashSet<>(relevantAttributesParam.getChosen());
		
		// get the mergeType as MergeType - mergeType
		MergeType mergeType;
		switch(mergeTypeParam.getChosen()) {
			case "Merge taking first event" : 
				mergeType = MergeType.PLAIN_FIRST;
			break;
			case "Merge taking last event" : 
				mergeType = MergeType.PLAIN_LAST;
			break;
			case "Merge taking first as 'start' and last as 'complete' life-cycle transitions" : 
				mergeType = MergeType.LIFECYCLE;
			break;	
			default: 
				mergeType = MergeType.PLAIN_FIRST;
			break;
		}
		
		
		return doMergeSubsequentEvents(log, eventClasses, f, desiredEvents, relevantAttributes, mergeFilter,
				mergeType);
	}
	
	public XLog doMergeSubsequentEvents(XLog log, XEventClasses eventClasses, XFactory factory,
			Set<XEventClass> consideredClasses, Set<String> relevantAttributes, MergeFilter mergeFilter,
			MergeType mergeType) {
		XLog newLog = XUtils.createLogFrom(log, factory);
		long instance = 0;

		for (XTrace t : log) {
			XTrace newTrace = factory.createTrace((XAttributeMap) t.getAttributes().clone());

			for (ListIterator<XEvent> iterator = t.listIterator(); iterator.hasNext();) {
				XEvent currentEvent = iterator.next();
				XEventClass eventClass = eventClasses.getClassOf(currentEvent);

				// Check for subsequent events with same class
				if (consideredClasses.contains(eventClass)) {
					instance = mergeType.merge(eventClasses, factory, instance, newTrace, iterator, currentEvent,
							mergeFilter, relevantAttributes);

				} else {
					// Just copy existing
					newTrace.add(factory.createEvent((XAttributeMap) currentEvent.getAttributes().clone()));
				}

			}
			newLog.add(newTrace);
		}

		return newLog;
	}
 
}
