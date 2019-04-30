package org.processmining.filterd.parameters;

public class FilterdParameters {
	
	String filter;
	
	public FilterdParameters() {
		filter = "";
	}
	
	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	@Override
	public boolean equals(Object object) {
		return false;
	}
	
	@Override
	public int hashCode() {
		return 0;
	}
}
