package org.processmining.filterd.gui.adapters;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.processmining.filterd.gui.FilterButtonModel;

/**
 * Class representing a deserialized computation cell model. It is used by JAXB
 * to save the computation cell model in XML format. All attributes of this
 * class have to be either primitives or enumerations. Exception to this are the
 * filter button models which have their own adapted/adapted classes.
 */
@XmlRootElement(name = "Computation Cell")
public class ComputationCellModelAdapted extends CellModelAdapted {

	private List<FilterButtonModel> filters; // list of filters in this computation cell (can be empty)
	private int indexOfInputOwner; // index of the cell whose output this cell is using as input (if it is using initial input, this value will be -1)

	public int getIndexOfInputOwner() {
		return indexOfInputOwner;
	}

	public void setIndexOfInputOwner(int indexOfInputOwner) {
		this.indexOfInputOwner = indexOfInputOwner;
	}

	@XmlElementWrapper(name = "filterButtons") // to put the buttons from the list in their own xml section.
	@XmlElement(name = "filterButton") // to name individual buttons 'filterButton' instead of 'FilterButtons'
	@XmlJavaTypeAdapter(FilterButtonAdapter.class) // tell JAXB to use the adapter.
	public List<FilterButtonModel> getFilters() {
		return filters;
	}

	/**
	 * Setter for the filters of a computation cell.
	 * 
	 * @param filters
	 *            filters of a computation cell
	 */
	public void setFilters(List<FilterButtonModel> filters) {
		this.filters = filters;
	}
}
