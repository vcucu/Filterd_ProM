package org.processmining.filterd.models;

import org.deckfour.xes.model.XLog;

public class YLog {
	
	private final XLog log;
	private String name;	// Name of the XLog (for visualization
	private final boolean isOutput;	// Is the XLog the output of some cell or filter?
	private int outputOf;	// Number of the cell/filter that generated the XLog
	
	public YLog(org.deckfour.xes.model.XLog log, String name) {
		this.log = log;
		this.name = name;
		this.isOutput = false;
	}
	
	public YLog(org.deckfour.xes.model.XLog log, String name, int outputOf) {
		this.log = log;
		this.name = name;
		this.isOutput = true;
		this.outputOf = outputOf;
	}

	public org.deckfour.xes.model.XLog get() {
		return log;
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
