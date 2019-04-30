package org.processmining.filterd.wizard;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import org.processmining.filterd.parameters.FilterdParameters;
import org.processmining.framework.util.ui.widgets.ProMList;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;

public class FilterdFilterWizardStep<T extends FilterdParameters> extends ProMList<String> implements ProMWizardStep<T> {

	private static final long serialVersionUID = 2026151392881260011L;
	
	public FilterdFilterWizardStep() {
		this("Select the filter you would like to use.", new DefaultListModel<String>());
	}

	public FilterdFilterWizardStep(String title, ListModel<String> model) {
		super(title, model);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	public FilterdParameters apply(FilterdParameters model, JComponent component) {
		if(component instanceof FilterdFilterWizardStep<?>) {
			model.setFilter(((FilterdFilterWizardStep<T>) component).getSelectedValuesList().get(0));
			return model;
		} else {
			return null;
		}
	}

	public boolean canApply(FilterdParameters model, JComponent component) {
		return !model.getFilter().equals("");
	}

	public JComponent getComponent(FilterdParameters model) {
		DefaultListModel<String> listModel = new DefaultListModel<>();
		listModel.addElement("Filter 1");
		listModel.addElement("Filter 2");
		listModel.addElement("Filter 3");
		return new FilterdFilterWizardStep<T>("Select the filter you would like to use.", listModel);
	}

	public String getTitle() {
		return "Select filter";
	}

}
