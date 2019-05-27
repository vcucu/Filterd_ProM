package org.processmining.filterd.gui;

public class InvalidConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 9180377105385259155L;
	
	private final FilterButtonModel filterButtonModel;

	InvalidConfigurationException(String message, FilterButtonModel filterButtonModel) {
		super(message);
		this.filterButtonModel = filterButtonModel;
	}
	
	public FilterButtonModel getFilterButtonModel() {
		return this.filterButtonModel;
	}
}
