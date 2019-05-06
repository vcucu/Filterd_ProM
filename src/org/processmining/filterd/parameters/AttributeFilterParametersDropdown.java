package org.processmining.filterd.parameters;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.filterd.algorithms.Filter;
import org.processmining.filterd.algorithms.FilterLogOnEventAttributes;
import org.processmining.filterd.dialogs.AttributeFilterPanelDropdown;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;

public class AttributeFilterParametersDropdown extends FilterdParameters {
	
	protected Filter filter;
	protected HashMap<String, Set<String>> logMap; // the filter
	protected Set<String> globalAttributes; // the must haves
	protected String name;
	private boolean removeEmptyTraces;
	private XLog log;
	private UIPluginContext context;
	
	public AttributeFilterParametersDropdown() {
		logMap = new HashMap<>();
		globalAttributes = new HashSet<>();
		name = "";
		removeEmptyTraces = false;
		filter = new FilterLogOnEventAttributes();
	}
	
	public AttributeFilterParametersDropdown(UIPluginContext context, XLog log) {
		this();
		this.log = log;
		this.context = context;
		
		// build the hash map for the given log 
		// by adding all attribute key-value pairs of the events to the map
		for (XTrace trace : log) {
			for (XEvent event : trace) {
				for (String key : event.getAttributes().keySet()) {
					if (!logMap.containsKey(key)) {
						logMap.put(key, new HashSet<String>());
					}
					logMap.get(key).add(event.getAttributes().get(key).toString());
				}
			}
			context.getProgress().inc();
		}
		
		// get the global attributes of the log
		for (XAttribute attribute : log.getGlobalEventAttributes()) {
			globalAttributes.add(attribute.getKey());
		}
		name = XConceptExtension.instance().extractName(log);
	}
	
	public boolean equals(Object object) {
		if(object instanceof AttributeFilterParameters) {
			AttributeFilterParameters attributeParameters = (AttributeFilterParameters) object;
			return this.getLogMap().equals(attributeParameters.getLogMap()) && 
					this.getGlobalAttributes().equals(attributeParameters.getGlobalAttributes()) &&
					this.getName() == attributeParameters.getName();
		} else {
			return false;
		}
	}

	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	public FilterdParameters apply(JComponent component) {
		System.out.println("This is the apply() method from dropdown!");
		AttributeFilterPanelDropdown panel = (AttributeFilterPanelDropdown) component;
		
		Set<String> attributes = new HashSet<>();
		for (String key : panel.getListModels().keySet()) {
			if (key.equals(panel.getDropdown().getSelectedItem().toString())) {
				this.getLogMap().get(key).clear();
				this.getLogMap().get(key).addAll(panel.getList().getSelectedValuesList());
				System.out.println("Updated log map in parameters class!");
			}
			if (panel.getRemoveList().get(key)) {
				attributes.add(key);
			}
		}
		this.setGlobalAttributes(attributes);
		this.setName(panel.getNameLabel().getText());
		this.setRemoveEmptyTraces(panel.getRemoveEmptyTracesComponent().isSelected());
		
		System.out.println("Return from dropdown params!");
		return this;
	}

	public boolean canApply(JComponent component) {
		if(component instanceof AttributeFilterPanelDropdown) {
			return true;
		} else {
			return false;
		}
	}

	public ProMPropertiesPanel getPropertiesPanel() {
		return new AttributeFilterPanelDropdown(context, this);
	}

	public HashMap<String, Set<String>> getLogMap() {
		return logMap;
	}

	public void setLogMap(HashMap<String, Set<String>> logMap) {
		this.logMap = logMap;
	}

	public Set<String> getGlobalAttributes() {
		return globalAttributes;
	}

	public void setGlobalAttributes(Set<String> globalAttributes) {
		this.globalAttributes = globalAttributes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRemoveEmptyTraces() {
		return removeEmptyTraces;
	}

	public void setRemoveEmptyTraces(boolean removeEmptyTraces) {
		this.removeEmptyTraces = removeEmptyTraces;
	}
	public Filter getFilter() {
		return this.filter;
	}
}
