package org.processmining.filterd.gui.adapters;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FilterdAbstractConfigKeyAdapted extends FilterdAbstractConfigAdapted {

	String key;
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
}
