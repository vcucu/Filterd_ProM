package org.processmining.filterd.wizard;

import javax.swing.JComponent;

import org.processmining.filterd.parameters.ActionsParameters;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;

public class FilterdConfigurationWizardStep<T extends ActionsParameters> implements ProMWizardStep<T>  {

	public T apply(T model, JComponent component) {
		model.getParameters().apply(component);
		return model;
	}

	public boolean canApply(T model, JComponent component) {
		return model.getParameters().canApply(component);
	}

	public JComponent getComponent(T model) {
		return model.getParameters().getPropertiesPanel();
	}

	public String getTitle() {
		return "Configure the filter you selected in the previous step";
	}

}
