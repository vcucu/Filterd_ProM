package org.processmining.filterd.tools;

/**
 * Exception thrown when the filter (configuration) is given an empty log.
 * These components are not designed to account for empty logs and this
 * exception reflects that.
 */
public class EmptyLogException extends RuntimeException{

	private static final long serialVersionUID = -1827039114403517818L;

	public EmptyLogException(String errorMessage) {
		super(errorMessage);
	}
	
}
