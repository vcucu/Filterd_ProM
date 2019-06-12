package org.processmining.filterd.gui.adapters;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.processmining.filterd.parameters.Parameter;

@XmlRootElement
public class FilterdAbstractConfigAdapted {

	List<Parameter> parameters;
		
	public List<Parameter> getParameters() {
		return parameters;
	}
	
	@XmlElementWrapper(name = "parameters") // to put the paramters from the list in their own xml section.
	@XmlElement(name = "parameter") // to name individual parameters 'parameter' instead of 'parameters'
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
}
