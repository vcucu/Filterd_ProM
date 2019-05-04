package org.processmining.filterd.parameters;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;

public class AttributeFilterParameters extends FilterdParameters {
	
	protected HashMap<String, Set<String>> logMap;
	protected Set<String> globalAttributes;
	protected String name;
	private boolean removeEmptyTraces;
	
	public AttributeFilterParameters() {
		logMap = new HashMap<>();
		globalAttributes = new HashSet<>();
		name = "";
		removeEmptyTraces = false;
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

	public boolean equals(Object object) {
		// TODO Auto-generated method stub
		return false;
	}

	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	public FilterdParameters apply(JComponent component) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean canApply(JComponent component) {
		// TODO Auto-generated method stub
		return false;
	}

	public ProMPropertiesPanel getPropertiesPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	public XLog getLog() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setLog(XLog log) {
		// TODO Auto-generated method stub
	}
}
