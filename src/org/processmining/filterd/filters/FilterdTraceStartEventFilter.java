package org.processmining.filterd.filters;
import java.util.List;

import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.framework.plugin.PluginContext;

public class FilterdTraceStartEventFilter extends Filter {

	XLog filteredLog;

	public XLog filter(PluginContext context, XLog log, List<Parameter> parameters) {

		//attribute to be filtered on
		ParameterOneFromSet attribute = (ParameterOneFromSet)this
				.getParameter(parameters, "attribute");
		//filter in/ filter out
		ParameterOneFromSet selectionType = (ParameterOneFromSet)this
				.getParameter(parameters, "selectionType");
		//which values for the attribute were selected
		ParameterMultipleFromSet desiredEvents = (ParameterMultipleFromSet)this
				.getParameter(parameters, "desiredEvents");
		ParameterYesNo traceHandling = (ParameterYesNo) this
				.getParameter(parameters, "traceHandling");


		//initialize the log that will be output
		filteredLog = Toolbox.initializeLog(log);

		/** alternative **/  
		boolean choice = selectionType.getChosen().equals("Filter in");
		boolean keepTraces = traceHandling.getChosen();
		boolean isAttribute = true;
		String chosen = attribute.getChosen();
		List<XEventClassifier> classifiers = Toolbox.computeComplexClassifiers(log);
		XEventClasses classes = null;

		/* check if the chosen value is an attribute or a classifier */
		if (Toolbox.getClassifiersName(classifiers).contains(chosen)) {
			isAttribute = false;
			/* initialize classifiers and classes */
			XEventClassifier c = Toolbox.computeClassifier(log, chosen);
			classes = XLogInfoImpl.create(log).getEventClasses(c);
		}
		

		for (XTrace trace: log) {
			XTrace filteredTrace = trace;
			boolean add = !choice;

			/* should you keep empty traces */
			if (trace.isEmpty()) {
				if (keepTraces) filteredLog.add(filteredTrace);
				continue;
			}

			/* change behavior based on whether the chosen value is an attribute or a classifier */
			if (isAttribute) {
				//whether to remove events if no value provided
				ParameterYesNo eventHandling = (ParameterYesNo) this
						.getParameter(parameters, "eventHandling");
				boolean keepEvents = eventHandling.getChosen();

				XAttributeMap eventAttributes = trace.get(0).getAttributes();
				/* check if the event has the given attribute */
				if (!eventAttributes.containsKey(chosen)) {
					if (keepEvents) add = choice; 
				} else {
					String value = eventAttributes.get(chosen).toString();
					if (desiredEvents.getChosen().contains(value)) add = choice;
				}

				if (add) filteredLog.add(filteredTrace);
			}
			else {
				/* get the class of this event */
				String eventClass = classes.getClassOf(trace.get(0)).toString();
				/* check if it is part of the selected classes */
				if (desiredEvents.getChosen().contains(eventClass)) add = choice;

				if (add) filteredLog.add(filteredTrace);
			}
		}

		return filteredLog;
	}

}
