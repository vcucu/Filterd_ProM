package org.processmining.filterd.gui.adapters;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FilterdTraceStartEventCategoricalConfigAdapted extends FilterdAbstractConfigAdapted {
	
	private String attribute;
	
	public String getAttribute() {
		return attribute;
	}
	
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
}
