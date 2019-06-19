package org.processmining.filterd.models;

import org.deckfour.xes.model.XLog;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Wrapper for XLog objects. This wrapper is used as input and output for
 * computation cells. It holds a reference to an XLog, as well its associated
 * name.
 * 
 * @author Filip Davidovic
 */
public class YLog {

	private XLog log; // actual XLog that this object is wrapping s
	private boolean isOutput; // Is the XLog the output of some cell or filter?
	private int outputOf; // Number of the cell/filter that generated the XLog
	private int id;
	private int indexOfOwner; // index of the cell that is outputting this YLog 
	private StringProperty name; // name of this object (javafx property so it can binded)

	/**
	 * Constructor for a YLog without an XLog. This is needed due to the fact
	 * that computation cells are created without their input log selected. Note
	 * that this object should never be used without an XLog set. The XLog
	 * should be set as soon as possible.
	 * 
	 * @param id
	 *            unique identifier of this object
	 * @param name
	 *            name of this object
	 * @param indexOfOwner
	 *            index of the cell that is outputting this object
	 */
	public YLog(int id, String name, int indexOfOwner) {
		this.id = id;
		this.name = new SimpleStringProperty(name);
		this.indexOfOwner = indexOfOwner;
	}

	/**
	 * Default constructor for a YLog.
	 * 
	 * @param id
	 *            unique identifier of this object
	 * @param name
	 *            name of this object
	 * @param log
	 *            XLog that this object is wrapping
	 * @param indexOfOwner
	 *            index of the cell that is outputting this object
	 */
	public YLog(int id, String name, XLog log, int indexOfOwner) {
		this.id = id;
		this.name = new SimpleStringProperty(name);
		this.log = log;
		this.indexOfOwner = indexOfOwner;
	}

	/**
	 * Getter for the wrapped XLog object.
	 * 
	 * @return wrapped XLog object
	 */
	public org.deckfour.xes.model.XLog get() {
		return log;
	}

	/**
	 * Setter for the wrapped XLog object.
	 * 
	 * @param log
	 *            wrapped XLog object
	 */
	public void setLog(XLog log) {
		XLog oldValue = this.log;
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

	/**
	 * Method which returns a boolean stating whether the wrapped XLog object is
	 * empty (XLog property)
	 * 
	 * @return whether the wrapped XLog object is empty
	 */
	public boolean isEmpty() {
		if (log == null) {
			return true;
		} else {
			return log.isEmpty();
		}
	}

	public int getCellOutput() {
		if (isOutput) {
			return outputOf;
		} else {
			return Integer.MIN_VALUE; // Or something like -1
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
