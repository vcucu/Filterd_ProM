package org.processmining.filterd.gui.adapters;

import java.beans.Transient;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.processmining.filterd.gui.FilterButtonModel;

@XmlRootElement
public class ComputationCellModelAdapted extends CellModelAdapted {
	
	private List<FilterButtonModel> filters;
	
	@Transient
	public List<FilterButtonModel> getFilters() {
		return filters;
	}
	
	public void setFilters(List<FilterButtonModel> filters) {
		this.filters = filters;
	}
}
