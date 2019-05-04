package org.processmining.filterd.parameters;

import javax.swing.JComponent;

import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;

public abstract class FilterdParameters {

	@Override
	public abstract boolean equals(Object object);
	
	@Override
	public abstract int hashCode();
	
	public abstract FilterdParameters apply(JComponent component);

	public abstract boolean canApply(JComponent component);

	public abstract ProMPropertiesPanel getPropertiesPanel();
}