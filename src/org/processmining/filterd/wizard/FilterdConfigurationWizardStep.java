package org.processmining.filterd.wizard;

import javax.swing.JComponent;

import org.processmining.filterd.configurations.ActionsParameters;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;

public class FilterdConfigurationWizardStep<T extends ActionsParameters> implements ProMWizardStep<T>  {

	public T apply(T model, JComponent component) {
		model.getConfiguration().populate(component);
		return model;
	}

	public boolean canApply(T model, JComponent component) {
		return model.getConfiguration().canPopulate(component);
	}

	public JComponent getComponent(T model) {
		return model.getConfiguration().getPropertiesPanel();
	}

	public String getTitle() {
		return "Configure the filter you selected in the previous step";
	}


}
