package org.processmining.filterd.gui.adapters;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.parameters.Parameter;

@XmlRootElement
public class FilterdAbstractConfigAdapted {

	String className;
	List<Parameter> parameters;
	
	// following variables are not exported but are needed for reconstruction
	XLog log;
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public List<Parameter> getParameters() {
		return parameters;
	}
	
	@XmlElementWrapper(name = "parameters") // to put the paramters from the list in their own xml section.
	@XmlElement(name = "parameter") // to name individual parameters 'parameter' instead of 'parameters'
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	
	@XmlTransient // makes sure the log does not get saved.
	public XLog getLog() {
		return log;
	}
	
	public void setLog(XLog log) {
		this.log = log;
	}
}
