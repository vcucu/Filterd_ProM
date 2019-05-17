package org.processmining.filterd.filters;

import java.util.Collections;
import java.util.List;

import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.framework.plugin.PluginContext;

public class FilterdTraceSampleFilter extends Filter {

	@SuppressWarnings("unchecked")
	public XLog filter(PluginContext context, XLog log, List<Parameter> parameters) {
	
		//get value of threshold parameter (i.e. the desired number of samples)
		ParameterValueFromRange<Long> nrSamples = (ParameterValueFromRange<Long>) this.getParameter(parameters, "threshold");
			
		//initialize the log that will be output
		XLog filteredLog = this.initializeLog(log);
		
		//clone input log, since ProM documentation says filters should not change input logs
		XLog clonedLog = (XLog) log.clone();
		
		//shuffle the copied input log to assure randomness
		Collections.shuffle(clonedLog);
		
		//add the first nrSamples traces from the copied input log to the output log
		clonedLog.stream()
		.limit((long) nrSamples.getChosen())
		.forEach(filteredLog :: add);
		
		return filteredLog;
	}

}