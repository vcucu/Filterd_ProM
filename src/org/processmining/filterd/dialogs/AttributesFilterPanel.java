package org.processmining.filterd.dialogs;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;

import org.processmining.filterd.parameters.AttributeFilterParameters;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.util.collection.AlphanumComparator;
import org.processmining.framework.util.ui.widgets.BorderPanel;
import org.processmining.framework.util.ui.widgets.ProMList;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;
import org.processmining.framework.util.ui.widgets.ProMTextField;

import com.fluxicon.slickerbox.factory.SlickerFactory;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

public class AttributesFilterPanel extends ProMPropertiesPanel {
	
	private static final long serialVersionUID = 2212748609374847593L;
	
	private HashMap<String, ProMList<String>> lists;
	private HashMap<String, JCheckBox> removeList;
	private ProMTextField removeEmptyTracesLabel;
	private JCheckBox removeEmptyTracesComponent;
	AttributeFilterParameters parameters;

	public AttributesFilterPanel(PluginContext context, AttributeFilterParameters parameters) {
		super("Filter on event attributes configuration panel");
		lists = new HashMap<>();
		globalAttributes = new HashMap<>();
		this.parameters = parameters;
		
		// build a hash map for storing the sorted attributes values (for each key)
		Map<String, List<String>> values = new HashMap<String, List<String>>();
		for (String key : parameters.getLogMap().keySet()) {
			values.put(key, new ArrayList<String>());
			values.get(key).addAll(parameters.getLogMap().get(key));
			Collections.sort(values.get(key), new AlphanumComparator());
			context.getProgress().inc();
		}
		
		double size[][] = { { 80, TableLayoutConstants.FILL }, { TableLayoutConstants.FILL, 30, 30 } };
		setLayout(new TableLayout(size));
		setOpaque(false);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		List<String> sortedKeys = new ArrayList<String>();
		sortedKeys.addAll(values.keySet());
		Collections.sort(sortedKeys, new AlphanumComparator());
		for (String key : sortedKeys) {
			
			DefaultListModel<String> listModel = new DefaultListModel<>();
			int[] selected = new int[values.get(key).size()];
			int i = 0;
			for (String value: values.get(key)) {
				listModel.addElement(value);
				selected[i] = i;
				i++;
			}
			
			context.getProgress().inc();
			ProMList<String> list = new ProMList<String>("Select values", listModel);
			lists.put(key, list);
			list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			list.setSelectedIndices(selected);
			list.setPreferredSize(new Dimension(100, 100));
			context.getProgress().inc();
			
			JCheckBox checkBox = SlickerFactory.instance().createCheckBox("Remove if no value provided", false);
			checkBox.setSelected(parameters.getGlobalAttributes().contains(key));
			removeList.put(key, checkBox);
			
			JPanel panel = new BorderPanel(5, 2);
			double panelSize[][] = { { TableLayoutConstants.FILL }, { TableLayoutConstants.FILL, 30 } };
			panel.setLayout(new TableLayout(panelSize));
			panel.add(lists.get(key), "0, 0");
			panel.add(removeList.get(key), "0, 1");
			
			tabbedPane.add(key, panel);
		}
		this.add(tabbedPane, "0, 0, 1, 0");
		
		removeEmptyTracesLabel = new ProMTextField();
		removeEmptyTracesLabel.setText(parameters.getName() + " (filter attributes)");
		add(removeEmptyTracesLabel, "1, 1");
		removeEmptyTracesLabel.setPreferredSize(new Dimension(100, 25));
		add(new JLabel("Log name:"), "0, 1");

		removeEmptyTracesComponent = SlickerFactory.instance().createCheckBox("Remove trace if all events were removed", parameters.isRemoveEmptyTraces()); 
		add(removeEmptyTracesComponent, "0, 2, 1, 2");
	}

	public HashMap<String, ProMList<String>> getLists() {
		return lists;
	}

	public void setLists(HashMap<String, ProMList<String>> lists) {
		this.lists = lists;
	}

	public HashMap<String, JCheckBox> getRemoveList() {
		return removeList;
	}

	public void setRemoveList(HashMap<String, JCheckBox> globalAttributes) {
		this.removeList = globalAttributes;
	}

	public ProMTextField getRemoveEmptyTracesLabel() {
		return removeEmptyTracesLabel;
	}

	public void setRemoveEmptyTracesLabel(ProMTextField removeEmptyTracesLabel) {
		this.removeEmptyTracesLabel = removeEmptyTracesLabel;
	}

	public JCheckBox getRemoveEmptyTracesComponent() {
		return removeEmptyTracesComponent;
	}

	public void setRemoveEmptyTracesComponent(JCheckBox removeEmptyTracesComponent) {
		this.removeEmptyTracesComponent = removeEmptyTracesComponent;
	}

	public AttributeFilterParameters getParameters() {
		return parameters;
	}

	public void setParameters(AttributeFilterParameters parameters) {
		this.parameters = parameters;
	}
}
