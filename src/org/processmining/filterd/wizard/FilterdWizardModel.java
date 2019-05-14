package org.processmining.filterd.wizard;

import org.processmining.filterd.configurations.ActionsParameters;

public class FilterdWizardModel<T extends ActionsParameters> {
	
	T parameters;
	
	FilterdWizardModel(T parameters) {
		this.parameters = parameters;
	}

	public T getParameters() {
		return parameters;
	}

	public void setParameters(T parameters) {
		this.parameters = parameters;
	}
}
