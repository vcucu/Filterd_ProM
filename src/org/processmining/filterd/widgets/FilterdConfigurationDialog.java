package org.processmining.filterd.widgets;

import javax.swing.JComponent;
import javax.swing.JDialog;

public class FilterdConfigurationDialog extends JDialog {

	private static final long serialVersionUID = 1911075376227866445L;
	
	public FilterdConfigurationDialog(String title, JComponent content) {
		super();
		setTitle(title);
		getContentPane().add(content);
		pack();
		setVisible(true);
	}

}
