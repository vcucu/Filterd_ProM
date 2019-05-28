package org.processmining.filterd.filters;

import java.util.List;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;;

public class FilterdTraceFollowerFilter extends Filter {

	public XLog filter(XLog log, List<Parameter> parameters) {
		// clone input log, since ProM documentation says filters should not 
		// change input logs
		XLog clonedLog = (XLog) log.clone();
		String selectedAttribute = ((ParameterOneFromSet)
				parameters.get(0)).getChosen();
		
		String selectionType = ((ParameterOneFromSet)
				parameters.get(1)).getChosen();
		
		boolean valueMatching = ((ParameterYesNo)
				parameters.get(2)).getChosen();
		
		String sameOrDifferent = ((ParameterOneFromSet)
				parameters.get(3)).getChosen();
		
		String valueMatchingAttribute = ((ParameterOneFromSet)
				parameters.get(4)).getChosen();
		
		boolean timeRestriction = ((ParameterYesNo)
				parameters.get(5)).getChosen();
		
		String shorterOrLonger = ((ParameterOneFromSet)
				parameters.get(6)).getChosen();
		
		double lowThreshold = ((ParameterRangeFromRange<Double>) 
				parameters.get(7)).getChosenPair().get(0);
		
		double highThreshold = ((ParameterRangeFromRange<Double>) 
				parameters.get(7)).getChosenPair().get(1);
		
		return null;
	}

}
