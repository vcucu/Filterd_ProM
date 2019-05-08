package org.processmining.filterd.wizard;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import org.processmining.filterd.parameters.ActionsParameters;
import org.processmining.framework.util.ui.widgets.ProMList;

public class FilterdFilterStep extends JPanel {

	private static final long serialVersionUID = 912819088710333646L;
	
	private ProMList<String> list;
	
	public FilterdFilterStep() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		DefaultListModel<String> listModel = new DefaultListModel<>();
		/* list of filters displayed after selecting the FilterD plugin */
		listModel.addElement("Event Attributes");
		listModel.addElement("Event Attributes (dropdown)");
		listModel.addElement("Concrete Filter");
		listModel.addElement("Timeframe Filter");
		list = new ProMList<>("Select the filter you would like to use.", listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		add(list);
	}
	
	public ActionsParameters apply(ActionsParameters model) {
		// fetch list component and get its value
		Component[] components = getComponents();
		for(Component component : components) {
			if(component instanceof ProMList<?>) {
				model.setFilter(((ProMList<String>) component).getSelectedValuesList().get(0));
			}
		}
		return model;
	}
}
