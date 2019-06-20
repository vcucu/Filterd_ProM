package org.processmining.filterd.gui;

/**
 * Exception thrown by filters and filter configurations. This exception is
 * thrown when the configuration and the input log for that filter are not
 * compatible. For example, if a Trace Sample filter is configured to sample 5
 * traces, but the input log contains 4 or less.
 */
public class InvalidConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 9180377105385259155L;
	private final FilterButtonModel filterButtonModel; // filter button model which contains the filter configuration that is throwing this exception

	public InvalidConfigurationException(String message, FilterButtonModel filterButtonModel) {
		super(message);
		this.filterButtonModel = filterButtonModel;
	}

	/**
	 * Getter for the filter button model reference. This reference is used to
	 * help UI identify which filter button is causing a problem and highlight
	 * it.
	 * 
	 * @return associated filter button model
	 */
	public FilterButtonModel getFilterButtonModel() {
		return this.filterButtonModel;
	}
}
