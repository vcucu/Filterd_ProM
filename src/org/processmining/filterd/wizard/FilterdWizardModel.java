package org.processmining.filterd.wizard;

public class FilterdWizardModel<FilterdParameters> {
	
	FilterdParameters parameters;
	
	FilterdWizardModel(FilterdParameters parameters) {
		this.parameters = parameters;
	}

	public FilterdParameters getParameters() {
		return parameters;
	}

	public void setParameters(FilterdParameters parameters) {
		this.parameters = parameters;
	}
}
