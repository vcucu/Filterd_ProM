package org.processmining.filterd.configurations;

import javax.swing.JComponent;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;

/*
 * obsolete
 */
public class ActionsParameters extends FilterdAbstractConfig {
	
	protected String filter;
	protected FilterdAbstractConfig parameters;

	public ActionsParameters() {
		filter = "";
	}
	
	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	public FilterdAbstractConfig getConfiguration() {
		return parameters;
	}

	public void setParameters(FilterdAbstractConfig parameters) {
		this.parameters = parameters;
	}

	public boolean equals(Object object) {
		if(object instanceof ActionsParameters) {
			ActionsParameters actionParameters = (ActionsParameters) object;
			return actionParameters.getFilter().equals(filter);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return 0;
	}

	public FilterdAbstractConfig populate(JComponent component) {
		return null;
	}

	public boolean canPopulate(JComponent component) {
		return false;
	}

	public ProMPropertiesPanel getConfigPanel() {
		return null;
	}

	public boolean checkValidity(XLog log) {
		// TODO Auto-generated method stub
		return false;
	}

	public void filter() {
		// TODO Auto-generated method stub
		
	}

}
