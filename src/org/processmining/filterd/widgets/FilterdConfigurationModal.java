package org.processmining.filterd.widgets;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FilterdConfigurationModal extends JPanel {
	
	private static final long serialVersionUID = -7875037424683057414L;
	private JPanel owner;

	public FilterdConfigurationModal(String title, JPanel modalPanel) {
		super();
		this.owner = modalPanel;
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createLineBorder(Color.blue));
		
		// setup default ui
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		// title label
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		add(new JLabel(title), gbc);
		
		// content
		// TODO: add content

		// cancel button
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(e -> hideModal());
		add(cancelButton, gbc);
		// ok button
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		JButton okButton = new JButton("OK");
		okButton.addActionListener(e -> showModal());
		add(okButton, gbc);
	}
	
	public void hideModal() {
		owner.setVisible(false);
		setVisible(false);
	}
	
	public void showModal() {
		owner.setVisible(true);
		setVisible(true);
	}
	
	public void toggle() {
		if(isVisible()) {
			hideModal();
		} else {
			showModal();
		}
	}
}
