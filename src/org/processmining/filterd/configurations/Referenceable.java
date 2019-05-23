package org.processmining.filterd.configurations;

import org.processmining.filterd.widgets.ParameterOneFromSetExtendedController;

public interface Referenceable {
	public FilterdAbstractConfig changeReference(ParameterOneFromSetExtendedController controller);
	
	public FilterdAbstractGreenConfig getConcreteReference();
}
