package org.processmining.filterd.models;

import org.deckfour.xes.model.XLog;

public class YLog {
	
	private XLog log;
	private String name;	// Name of the XLog (for visualization
	private boolean isOutput;	// Is the XLog the output of some cell or filter?
	private int outputOf;	// Number of the cell/filter that generated the XLog
	private int id;
	
//	public YLog(org.deckfour.xes.model.XLog log, String name) {
//		this.log = log;
//		this.name = name;
//		this.isOutput = false;
//	}
//	
//	public YLog(org.deckfour.xes.model.XLog log, String name, int outputOf) {
//		this.log = log;
//		this.name = name;
//		this.isOutput = true;
//		this.outputOf = outputOf;
//	}
	
	public YLog(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public YLog(int id, String name, XLog log) {
		this.id = id;
		this.name = name;
		this.log = log;
	}

	public org.deckfour.xes.model.XLog get() {
		return log;
	}
	
	public void setLog(XLog log) {
		this.log = log;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
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
	
}
