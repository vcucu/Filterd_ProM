package org.processmining.filterd.widgets;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JDialog;

public class FilterdConfigurationDialog extends JDialog {

	private static final long serialVersionUID = 1911075376227866445L;
	
	public FilterdConfigurationDialog(String title) {
		super();
		setTitle(title);
		setLayout(new GridBagLayout());
		
		// setup default ui
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		// content
		// TODO: add content

		// cancel button
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(e -> closeDialog());
		getContentPane().add(cancelButton, gbc);
		// ok button
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		JButton okButton = new JButton("OK");
		getContentPane().add(okButton, gbc);
		
		setPreferredSize(new Dimension(600, 400));
		pack();
		setVisible(true);
	}
	
	public void closeDialog() {
		dispose();
	}

}
