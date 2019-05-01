package org.processmining.filterd.dialogs;

import javax.swing.JCheckBox;

import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;
import org.processmining.framework.util.ui.widgets.ProMTextField;

public class ConcretePropertiesPanel extends ProMPropertiesPanel {

	private static final long serialVersionUID = -7979473315436318888L;
	
	private ProMTextField someIntTextField;
	private ProMTextField someDoubleTextField;
	private JCheckBox someBoolCheckBox;

	public ConcretePropertiesPanel(int someIntInit, double someDoubleInit, boolean someBoolInit) {
		super("Concrete implementation of a filter, configuration panel");
		someIntTextField = addTextField("Some integer", Integer.toString(someIntInit));
		someDoubleTextField = addTextField("Some double", Double.toString(someDoubleInit));
		someBoolCheckBox = addCheckBox("Some boolean", someBoolInit);
	}
	
	public int getSomeInt() {
		return Integer.parseInt(someIntTextField.getText());
	}
	
	public double getSomeDouble() {
		return Double.parseDouble(someDoubleTextField.getText());
	}
	
	public boolean getSomeBool() {
		return someBoolCheckBox.isSelected();
	}

}
