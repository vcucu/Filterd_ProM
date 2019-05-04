package org.processmining.filterd.dialogs;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;

import org.processmining.framework.util.ui.widgets.ProMComboBox;
import org.processmining.framework.util.ui.widgets.ProMList;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;

public class AttributeFilterPanelDropdown extends ProMPropertiesPanel {

	private static final long serialVersionUID = -7979473315436318888L;
	
	private ProMComboBox comboBox;
	private ArrayList<String> values = new ArrayList<>();
	private HashMap<Integer, DefaultListModel<String>> listModels = new HashMap<>();
	private ProMList<String> list;

	public AttributeFilterPanelDropdown() {
		super("Filter on event attributes configuration panel");
		
		for (int i = 0; i < 10; i++) {
			String temp = "Item #" + i;
			values.add(temp);
			DefaultListModel<String> listModel = new DefaultListModel<>();
			listModel.addElement(temp);
			listModels.put(i, listModel);
		}
		comboBox = addComboBox("ComboBox", values.toArray(new String[values.size()]));
		comboBox.addActionListener(e -> attributeDropdownItemStateChanged(e));
		
		list = new ProMList<String>("Select values", listModels.get(0));
		this.addProperty("Event values", list);
	}

	private Object attributeDropdownItemStateChanged(ActionEvent e) {
		int newItemIndex = comboBox.getSelectedIndex();
		list.getList().setModel(listModels.get(newItemIndex));
		return null;
	}
}
