package org.processmining.filterd.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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
		
		// top side of the modal (title)
		// title label
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.weighty = 1.0f;
		gbc.anchor = GridBagConstraints.PAGE_START;
		add(new JLabel(title, SwingConstants.CENTER), gbc);
		
		// content
		// TODO: add content

		// bottom side of the modal (buttons)
		// cancel button
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0.3f;
		gbc.anchor = GridBagConstraints.PAGE_END;
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(e -> hideModal());
		add(cancelButton, gbc);
		// some space between the buttons 
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0.4f;
		add(Box.createGlue(), gbc);
		// ok button
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0.3f;
		gbc.anchor = GridBagConstraints.PAGE_END;
		JButton okButton = new JButton("OK");
		okButton.addActionListener(e -> showModal());
		add(okButton, gbc);
		
		setPreferredSize(new Dimension(600, 400));
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
