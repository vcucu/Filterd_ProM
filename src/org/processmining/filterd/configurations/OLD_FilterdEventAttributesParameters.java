package org.processmining.filterd.configurations;

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
import org.processmining.filterd.dialogs.FilterdEventAttributesPanel;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.filters.OLD_FilterLogOnEventAttributes;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;


public class OLD_FilterdEventAttributesParameters extends FilterdAbstractConfig {
	
	protected Filter filter;
	protected HashMap<String, Set<String>> logMap; // the filter
	protected Set<String> globalAttributes; // the must haves
	protected String name;
	private boolean removeEmptyTraces;
	private XLog log;
	private UIPluginContext context;
	
	public OLD_FilterdEventAttributesParameters() {
		logMap = new HashMap<>();
		globalAttributes = new HashSet<>();
		name = "";
		removeEmptyTraces = false;
		filter = new OLD_FilterLogOnEventAttributes();
	}
	
	public OLD_FilterdEventAttributesParameters(UIPluginContext context, XLog log) {
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
		if(object instanceof OLD_FilterdEventAttributesParameters) {
			OLD_FilterdEventAttributesParameters attributeParameters = (OLD_FilterdEventAttributesParameters) object;
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

	public FilterdAbstractConfig populate(JComponent component) {
		System.out.println("This is the apply() method from dropdown!");
		FilterdEventAttributesPanel panel = (FilterdEventAttributesPanel) component;
		
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

	public boolean canPopulate(JComponent component) {
		if(component instanceof FilterdEventAttributesPanel) {
			return true;
		} else {
			return false;
		}
	}

	public ProMPropertiesPanel getConfigPanel() {
		return new FilterdEventAttributesPanel(context, this);
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

	public boolean checkValidity(XLog log) {
		// TODO Auto-generated method stub
		return false;
	}

	public XLog filter() {
		// TODO Auto-generated method stub
		return null;
	}
}
