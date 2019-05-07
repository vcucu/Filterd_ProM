package org.processmining.filterd.dialogs;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.ListSelectionModel;

import org.processmining.filterd.parameters.AttributeFilterParametersDropdown;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.util.collection.AlphanumComparator;
import org.processmining.framework.util.ui.widgets.ProMComboBox;
import org.processmining.framework.util.ui.widgets.ProMList;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;
import org.processmining.framework.util.ui.widgets.ProMTextField;

public class AttributeFilterPanelDropdown extends ProMPropertiesPanel {

	private static final long serialVersionUID = -7979473315436318888L;
	
	private HashMap<String, Boolean> removeList;
	private ProMTextField nameLabel;
	private JCheckBox removeEmptyTracesComponent;
	private JCheckBox removeEmptyEventsComponent;
	AttributeFilterParametersDropdown parameters;
	
	private ProMComboBox<String> dropdown;
	private ProMList<String> list;
	private HashMap<String, int[]> listSelected = new HashMap<>();
	private HashMap<String, DefaultListModel<String>> listModels = new HashMap<>();
	
	List<String> sortedKeys = new ArrayList<String>();
	Map<String, List<String>> values = new HashMap<String, List<String>>();

	public AttributeFilterPanelDropdown(PluginContext context, AttributeFilterParametersDropdown parameters) {
		super("Filter on event attributes configuration panel");
		removeList = new HashMap<>();
		this.parameters = parameters;
		
		// build a hash map for storing the sorted attributes values (for each key)
		//Map<String, List<String>> values = new HashMap<String, List<String>>();
		for (String key : parameters.getLogMap().keySet()) {
			values.put(key, new ArrayList<String>());
			values.get(key).addAll(parameters.getLogMap().get(key));
			Collections.sort(values.get(key), new AlphanumComparator());
			context.getProgress().inc();
		}
		
		// add attribute keys to the drop down
		//List<String> sortedKeys = new ArrayList<String>();
		sortedKeys.addAll(values.keySet());
		Collections.sort(sortedKeys, new AlphanumComparator());
		dropdown = addComboBox("Filter by", sortedKeys.toArray(new String[sortedKeys.size()]));
		dropdown.addActionListener(e -> attributeDropdownItemStateChanged(e));
		
		// add attribute values to lists (one list for each attribute key)
		for (String key : sortedKeys) {
			
			// build new list model for holding attribute values
			DefaultListModel<String> listModel = new DefaultListModel<>();
			int[] selected = new int[values.get(key).size()];
			// build array to mark all values in the list as being selected
			int index = 0;
			for (String value: values.get(key)) {
				listModel.addElement(value);
				selected[index] = index;
				index += 1;
			}
			context.getProgress().inc();
			
			listModels.put(key, listModel);
			listSelected.put(key, selected);
			removeList.put(key, parameters.getGlobalAttributes().contains(key));
			context.getProgress().inc();
		}
		
		// add initial attribute value list
		String KEY = sortedKeys.get(0);
		list = new ProMList<String>("Select values", listModels.get(KEY));
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setSelectedIndices(listSelected.get(KEY));
		this.addProperty("Event values", list);
		
		// removeEmptyEventsComponent = addCheckBox("Remove if no value provided", removeList.get(KEY));
		removeEmptyEventsComponent = addCheckBox("Remove empty events", removeList.get(KEY));
		
		String newName = parameters.getName() + " (filter attributes)";
		nameLabel = addTextField("Log name", newName);
		
		// removeEmptyTracesComponent = addCheckBox("Remove trace if all events are removed", parameters.isRemoveEmptyTraces());		
		removeEmptyTracesComponent = addCheckBox("Remove empty traces", parameters.isRemoveEmptyTraces());		
	}

	private Object attributeDropdownItemStateChanged(ActionEvent e) {
		String newItemKey = dropdown.getSelectedItem().toString();
		list.getList().setModel(listModels.get(newItemKey));
		list.setSelectedIndices(listSelected.get(newItemKey));
		for(String key: sortedKeys) {
			if(!key.equals(newItemKey)) {
				int[] selected = new int[values.get(key).size()];
				int index = 0;
				for (String value: values.get(key)) {
					selected[index] = index;
					index += 1;
				}
				listSelected.put(key, selected);
			}
		}
		
		return null;
	}

	public HashMap<String, Boolean> getRemoveList() {
		return removeList;
	}

	public void setRemoveList(HashMap<String, Boolean> removeList) {
		this.removeList = removeList;
	}

	public ProMTextField getNameLabel() {
		return nameLabel;
	}

	public void setNameLabel(ProMTextField nameLabel) {
		this.nameLabel = nameLabel;
	}

	public JCheckBox getRemoveEmptyTracesComponent() {
		return removeEmptyTracesComponent;
	}

	public void setRemoveEmptyTracesComponent(JCheckBox removeEmptyTracesComponent) {
		this.removeEmptyTracesComponent = removeEmptyTracesComponent;
	}

	public JCheckBox getRemoveEmptyEventsComponent() {
		return removeEmptyEventsComponent;
	}

	public void setRemoveEmptyEventsComponent(JCheckBox removeEmptyEventsComponent) {
		this.removeEmptyEventsComponent = removeEmptyEventsComponent;
	}

	public ProMComboBox<String> getDropdown() {
		return dropdown;
	}

	public void setDropdown(ProMComboBox<String> dropdown) {
		this.dropdown = dropdown;
	}

	public ProMList<String> getList() {
		return list;
	}

	public void setList(ProMList<String> list) {
		this.list = list;
	}

	public HashMap<String, int[]> getListSelected() {
		return listSelected;
	}

	public void setListSelected(HashMap<String, int[]> listSelected) {
		this.listSelected = listSelected;
	}

	public HashMap<String, DefaultListModel<String>> getListModels() {
		return listModels;
	}

	public void setListModels(HashMap<String, DefaultListModel<String>> listModels) {
		this.listModels = listModels;
	}
}
