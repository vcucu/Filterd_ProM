package org.processmining.filterd.wizard;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import org.processmining.filterd.parameters.ActionsParameters;
import org.processmining.framework.util.ui.widgets.ProMList;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;

public class FilterdFilterWizardStep<T extends ActionsParameters> implements ProMWizardStep<T> {

	private static final long serialVersionUID = 2026151392881260011L;
	
	private JPanel panel;
	private JProgressBar progressBar;
	private ProMList<String> list;

	public FilterdFilterWizardStep() {
		this("Select the filter you would like to use.", new DefaultListModel<String>());
	}

	public FilterdFilterWizardStep(String title, ListModel<String> model) {
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(30);
		progressBar.setVisible(false);
		panel.add(progressBar);
		
		list = new ProMList<String>(title, model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel.add(list);
	}

	public T apply(T model, JComponent component) {
		if(component instanceof JPanel) {
			String filter = "";
			Component[] components = component.getComponents();
			for(Component comp : components) {
				if(comp instanceof ProMList<?>) {
					filter = ((ProMList<String>) comp).getSelectedValuesList().get(0);
				}
			}
			if(filter.equals("Event Attributes") || filter.equals("Event Attributes (dropdown)")) {
				progressBar.setVisible(true);
			}
			model.setFilter(filter);
			return model;
		} else {
			return null;
		}
	}

	public boolean canApply(T model, JComponent component) {
		if(component instanceof JPanel) {
			Component[] components = component.getComponents();
			for(Component comp : components) {
				if(comp instanceof ProMList<?>) {
					// if there is nothing selected in the list canApply == false
					return !(((ProMList<String>) comp).getSelectedValuesList().size() == 0);
				}
			}
			return false;
		} else {
			return false;
		}
	}

	public JComponent getComponent(T model) {
		DefaultListModel<String> listModel = new DefaultListModel<>();
		listModel.addElement("Event Attributes");
		listModel.addElement("Event Attributes (dropdown)");
		listModel.addElement("Concrete Filter");
		listModel.addElement("Timeframe Filter");
		return new FilterdFilterWizardStep<T>("Select the filter you would like to use.", listModel).getPanel();
	}

	public String getTitle() {
		return "Select filter";
	}
	
	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

}
