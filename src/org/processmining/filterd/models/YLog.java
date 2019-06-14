package org.processmining.filterd.models;

import org.deckfour.xes.model.XLog;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class YLog {
	
	private XLog log;
	private boolean isOutput;	// Is the XLog the output of some cell or filter?
	private int outputOf;	// Number of the cell/filter that generated the XLog
	private int id;
	private int indexOfOwner;
	private StringProperty name;

	public YLog(int id, String name, int indexOfOwner) {
		this.id = id;
		this.name = new SimpleStringProperty(name);
		this.indexOfOwner = indexOfOwner;
	}
	
	public YLog(int id, String name, XLog log, int indexOfOwner) {
		this.id = id;
		this.name = new SimpleStringProperty(name);
		this.log = log;
		this.indexOfOwner = indexOfOwner;
	}

	public org.deckfour.xes.model.XLog get() {
		return log;
	}
	
	public void setLog(XLog log) {
		this.log = log;
	}
	
	public void setName(String name) {
		this.name.setValue(name);
	}
	
	public String getName() {
		return name.getValue();
	}
	
	public StringProperty getNameProperty() {
		return name;
	}
	
	@Override
	public String toString() {
		return name.getValue().toString();
	}
	
	public boolean isEmpty() {
		return log.isEmpty();
	}

	public int getCellOutput() {
		if (isOutput) {
			return outputOf;			
		} else {
			return Integer.MIN_VALUE;	// Or something like -1
		}
	}

	public boolean isOutput() {
		return isOutput;
	}
	
	public int getIndexOfOwner() {
		return indexOfOwner;
	}

	public void setIndexOfOwner(int indexOfOwner) {
		this.indexOfOwner = indexOfOwner;
	}
	
}
