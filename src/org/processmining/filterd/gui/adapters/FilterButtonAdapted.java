package org.processmining.filterd.gui.adapters;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.processmining.filterd.configurations.FilterdAbstractConfig;

@XmlRootElement
public class FilterButtonAdapted {
	
	private int index;
	private String name;
	private FilterdAbstractConfig filterConfig;
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name = "filter") // to name individual cells 'cell' instead of 'cells'
	@XmlJavaTypeAdapter(FilterdAbstractConfigAdapter.class)
	public FilterdAbstractConfig getFilterConfig() {
		return filterConfig;
	}
	
	public void setFilterConfig(FilterdAbstractConfig filterConfig) {
		this.filterConfig = filterConfig;
	}
	
}
