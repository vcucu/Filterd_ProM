package org.processmining.filterd.parameters;

import javax.swing.JComponent;

import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;

public class ActionsParameters extends FilterdParameters {
	
	protected String filter;
	protected FilterdParameters parameters;

	public ActionsParameters() {
		filter = "";
	}
	
	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	public FilterdParameters getParameters() {
		return parameters;
	}

	public void setParameters(FilterdParameters parameters) {
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

	public FilterdParameters apply(JComponent component) {
		return null;
	}

	public boolean canApply(JComponent component) {
		return false;
	}

	public ProMPropertiesPanel getPropertiesPanel() {
		return null;
	}

}
