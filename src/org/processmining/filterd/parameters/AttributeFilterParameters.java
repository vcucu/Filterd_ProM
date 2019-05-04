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
import org.processmining.filterd.dialogs.AttributesFilterPanel;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;

public class AttributeFilterParameters extends FilterdParameters {
	
	protected HashMap<String, Set<String>> logMap;
	protected Set<String> globalAttributes;
	protected String name;
	private boolean removeEmptyTraces;
	private XLog log;
	private UIPluginContext context;
	
	public AttributeFilterParameters() {
		logMap = new HashMap<>();
		globalAttributes = new HashSet<>();
		name = "";
		removeEmptyTraces = false;
	}
	
	public AttributeFilterParameters(UIPluginContext context, XLog log) {
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
		AttributesFilterPanel panel = (AttributesFilterPanel) component;
		
		Set<String> attributes = new HashSet<>();
		for (String key : panel.getLists().keySet()) {
			this.getLogMap().get(key).clear();
			this.getLogMap().get(key).addAll(panel.getLists().get(key).getSelectedValuesList());
			if (panel.getRemoveList().get(key).isSelected()) {
				attributes.add(key);
			}
		}
		this.setGlobalAttributes(attributes);
		this.setName(panel.getRemoveEmptyTracesLabel().getText());
		this.setRemoveEmptyTraces(panel.getRemoveEmptyTracesComponent().isSelected());
		
		return this;
	}

	public boolean canApply(JComponent component) {
		if(component instanceof AttributesFilterPanel) {
			return true;
		} else {
			return false;
		}
	}

	public ProMPropertiesPanel getPropertiesPanel() {
		return new AttributesFilterPanel(context, this);
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
}
