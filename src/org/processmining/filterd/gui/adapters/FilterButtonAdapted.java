package org.processmining.filterd.gui.adapters;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.processmining.filterd.configurations.FilterdAbstractConfig;

/**
 * Class representing a deserialized filter button model. It is used by JAXB to
 * save the filter button model in XML format. All attributes of this class have
 * to be either primitives or enumerations. Exception to this are the filter
 * configurations which have their own adapted/adapted classes.
 */
@XmlRootElement
public class FilterButtonAdapted {

	private int index; // index of the filter button in the filters array of the computation cell model
	private String name; // name of the filter button
	private FilterdAbstractConfig filterConfig; // filter configuration associated with this object

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

	/**
	 * Getter for the filter configuration. Annotation is there to specify which
	 * adapted should be used by JAXB when deserializing this object.
	 * 
	 * @return filter configuration associated with this object
	 */
	@XmlElement(name = "filter") // to name individual cells 'cell' instead of 'cells'
	@XmlJavaTypeAdapter(FilterdAbstractConfigAdapter.class)
	public FilterdAbstractConfig getFilterConfig() {
		return filterConfig;
	}

	/**
	 * Setter for the filter configuration.
	 * 
	 * @param filterConfig
	 *            filter configuration to be set
	 */
	public void setFilterConfig(FilterdAbstractConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

}
