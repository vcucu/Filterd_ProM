package org.processmining.filterd.gui.adapters;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.processmining.filterd.gui.FilterButtonModel;

@XmlRootElement(name = "Computation Cell")
public class ComputationCellModelAdapted extends CellModelAdapted {

	private List<FilterButtonModel> filters;
	private int indexOfInputOwner;

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

	public void setFilters(List<FilterButtonModel> filters) {
		this.filters = filters;
	}
}
