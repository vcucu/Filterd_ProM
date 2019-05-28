package org.processmining.filterd.filters;

import java.util.Collections;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.tools.Toolbox;

public class FilterdTraceSampleFilter extends Filter {
	public FilterdTraceSampleFilter() {}

	@SuppressWarnings("unchecked")
	public XLog filter(XLog log, List<Parameter> parameters) {
	
		// Get value of threshold parameter (i.e. the desired number of 
		// samples).
		ParameterValueFromRange<Integer> nrSamples = 
				(ParameterValueFromRange<Integer>) parameters.get(0);	
		
		// Initialize the log that will be output.
		XLog filteredLog = Toolbox.initializeLog(log);
		
		// Clone input log, since ProM documentation says filters should not 
		// change input logs
		XLog clonedLog = (XLog) log.clone();
		
		//shuffle the copied input log to assure randomness
		Collections.shuffle(clonedLog);
		
		// Add the first nrSamples traces from the copied input log to the 
		// output log.
		clonedLog.stream()
		.limit(nrSamples.getChosen())
		.forEach(filteredLog :: add);
		
		return filteredLog;
	}

}
