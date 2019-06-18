package org.processmining.filterd.gui.adapters;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.processmining.filterd.parameters.Parameter;

@XmlRootElement
public class FilterdAbstractConfigAdapted {

	String className;
	String filterTypeName;
	List<Parameter> parameters;
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getfilterTypeName() {
		return filterTypeName;
	}
	
	public void setfilterTypeName(String filterTypeName) {
		this.filterTypeName = filterTypeName;
	}
	
	@XmlElementWrapper(name = "parameters") // to put the paramters from the list in their own xml section.
	@XmlElement(name = "parameter") // to name individual parameters 'parameter' instead of 'parameters'
	public List<Parameter> getParameters() {
		return parameters;
	}
	
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
}
