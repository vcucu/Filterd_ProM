package org.processmining.filterd.configurations;
import java.util.List;

import javax.swing.JComponent;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.parameters.Parameter;
public abstract class FilterdAbstractConfig {
	
	private Filter filterType;
	private XLog log;
	private List<Parameter> parameters;
	private boolean isValid;
	private XEventClassifier classifier;
	
	public XEventClassifier getClassifier() {
		return classifier;
	}

	public void setClassifier(XEventClassifier classifier) {
		this.classifier = classifier;
	}

	public Filter getFilterType() {
		return filterType;
	}

	public void setFilterType(Filter filterType) {
		this.filterType = filterType;
	}

	public XLog getLog() {
		return log;
	}

	public void setLog(XLog log) {
		
		if (this.checkValidity(log)) {
			this.log = log;
		} else {
			// raise error
		}
		
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public boolean isValid() {
		return isValid;
	}
	
	public Parameter getParameter(String whichParameter) {
		
		for(Parameter parameter :parameters ) {
			if (parameter.getName().equals(whichParameter)) {	
				return parameter;
			}
		}
		return null;
	}
 
	public abstract boolean checkValidity(XLog log);

	public abstract FilterdAbstractConfig populate(JComponent component);

	public abstract boolean canPopulate(JComponent component);

	public abstract JComponent getPropertiesPanel();
	
	public abstract void filter();
}
