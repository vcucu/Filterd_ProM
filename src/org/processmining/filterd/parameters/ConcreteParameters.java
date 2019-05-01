package org.processmining.filterd.parameters;

import javax.swing.JComponent;

import org.processmining.filterd.dialogs.ConcretePropertiesPanel;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;

public class ConcreteParameters extends FilterdParameters {
	
	private int someInt;
	private boolean someBool;
	private double someDouble;
	
	public ConcreteParameters() {
		someInt = 5;
		someDouble = 3.14;
		someBool = true;
	}

	public int getSomeInt() {
		return someInt;
	}

	public void setSomeInt(int someInt) {
		this.someInt = someInt;
	}

	public boolean isSomeBool() {
		return someBool;
	}

	public void setSomeBool(boolean someBool) {
		this.someBool = someBool;
	}

	public double getSomeDouble() {
		return someDouble;
	}

	public void setSomeDouble(double someDouble) {
		this.someDouble = someDouble;
	}

	public boolean equals(Object object) {
		if(object instanceof ConcreteParameters) {
			ConcreteParameters concreteParameters = (ConcreteParameters) object;
			return concreteParameters.getSomeInt() == someInt && 
					concreteParameters.getSomeDouble() == someDouble &&
					concreteParameters.isSomeBool() == someBool;
		} else {
			return false;
		}
	}

	public int hashCode() {
		return 0;
	}

	public FilterdParameters apply(JComponent component) {
		ConcretePropertiesPanel panel = (ConcretePropertiesPanel) component;
		someInt = panel.getSomeInt();
		someDouble = panel.getSomeDouble();
		someBool = panel.getSomeBool();
		return this;
	}

	public boolean canApply(JComponent component) {
		if(component instanceof ConcretePropertiesPanel) {
			return true;
		} else {
			return false;
		}
	}

	public ProMPropertiesPanel getPropertiesPanel() {
		return new ConcretePropertiesPanel(someInt, someDouble, someBool);
	}

}
