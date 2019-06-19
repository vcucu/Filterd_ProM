package org.processmining.filterd.filters;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.parameters.Parameter;

public abstract class Filter {


	/**
	 * 
	 * @param log to be filtered
	 * @param parameters list containing objects of classes that extend the type Parameter
	 * @pre log != null \and parameters contain all the necessary ones 
	 * @return filtered log
	 */
	public abstract XLog filter(XLog log, List<Parameter> parameters);
	
	/**
	 * 
	 * @param parameters list containing objects of classes that extend the type Parameter
	 * @param whichParameter containing the name of the parameter
	 * @return first retrieved parameter object with the specified name or null
	 * 
	 */
	public Parameter getParameter(List<Parameter> parameters, String whichParameter) {
			
		for(Parameter parameter :parameters ) {
			//check for each parameter whether its name equals the desired value
			if (parameter.getName().equals(whichParameter)) {
				return parameter;
			}
		}
		return null;
	}
}
