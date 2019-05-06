package org.processmining.filterd.plugins;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.filterd.widgets.FilterdConfigurationModal;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;

public class FilterdVisualizer {

	private static final long serialVersionUID = 9104736085046064684L;
	private JLayeredPane main;
	private JPanel defaultPanel;
	private JButton toggleButton;
	private JPanel modalPanel;
	private FilterdConfigurationModal configurationModal;

	@Plugin(name = "Filterd Visualizer", level = PluginLevel.PeerReviewed, parameterLabels = "Filter",
			returnTypes = JComponent.class, returnLabels = "Filterd Notebook Visualizer", userAccessible = true,
			mostSignificantResult = 1, help = "Help text goes here")
	@Visualizer(name = "Filterd Visualizer", pack = "Filterd")
	public JComponent visualize(final PluginContext context, final XLog log) {
		initComponents();
		
		return main;
	}

	private void initComponents() {
		main = new JLayeredPane();
		main.setLayout(new OverlayLayout(main)); // make panels on all layers as big as possible
		
		initDefaultPanel();
		
		initModalPanel();
	}
	
	private void initDefaultPanel() {
		defaultPanel = new JPanel();
		defaultPanel.setBackground(new Color(204, 255, 255));
		defaultPanel.setAlignmentX(0.5f); // align horizontally 
		defaultPanel.setAlignmentY(0.5f); // align vertically
		defaultPanel.add(new JLabel("Hello world from filterd panel!"));
		toggleButton = new JButton("Toggle modal");
		toggleButton.addActionListener(e -> toggleActionPerformed(e));
		defaultPanel.add(toggleButton);
		main.add(defaultPanel, JLayeredPane.DEFAULT_LAYER);
	}
	
	private void initModalPanel() {
		modalPanel = new JPanel();
		modalPanel.setOpaque(false); // make the modal panel transparent
		modalPanel.setAlignmentX(0.5f); // align horizontally 
		modalPanel.setAlignmentY(0.5f); // align vertically
		modalPanel.setLayout(new GridBagLayout()); 
		configurationModal = new FilterdConfigurationModal("Configure some filter via a modal", modalPanel);
		modalPanel.add(configurationModal, new GridBagConstraints()); // place modal in center and pack it (set to correct size)
		main.add(modalPanel, JLayeredPane.MODAL_LAYER);
	}

	private void toggleActionPerformed(ActionEvent e) {
		configurationModal.toggle();
	}
}
