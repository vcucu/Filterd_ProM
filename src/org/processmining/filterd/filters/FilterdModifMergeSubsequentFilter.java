package org.processmining.filterd.filters;

import java.util.List;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.tools.Toolbox;

public class FilterdModifMergeSubsequentFilter extends Filter {

	XLog filteredLog;
	
	public XLog filter(XLog log, List<Parameter> parameters) {
		
		ParameterOneFromSet classifier = (ParameterOneFromSet)this
				.getParameter(parameters, "classifier");
		ParameterMultipleFromSet desiredEvents = (ParameterMultipleFromSet)this
				.getParameter(parameters, "desiredEvents");
		ParameterOneFromSet comparisonType = (ParameterOneFromSet)this
				.getParameter(parameters, "comparisonType");
		ParameterOneFromSet mergeType = (ParameterOneFromSet)this
				.getParameter(parameters, "mergeType");
		
		//initialize the log that will be output
		filteredLog = Toolbox.initializeLog(log);
		
		return filteredLog;
	}

}
