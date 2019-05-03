package org.processmining.filterd.plugins;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.filterd.widgets.FilterdConfigurationDialog;
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

		FilterdConfigurationDialog modal = new FilterdConfigurationDialog("Test modal", new JLabel("Hello world!"));
		
		return main;
	}
	
	private void initComponents() {
		
		main = new JLayeredPane();
		main.setPreferredSize(new Dimension(300, 300)); 
		
		// filterd panel
		defaultPanel = new JPanel();
		defaultPanel.setBorder(BorderFactory.createLineBorder(Color.green));
		defaultPanel.setSize(200, 200);
		defaultPanel.add(new JLabel("hello world from filterd panel!"));
		toggleButton = new JButton("Toggle modal");
		toggleButton.addActionListener(e -> toggleActionPerformed(e));
		defaultPanel.add(toggleButton);
		main.add(defaultPanel, JLayeredPane.DEFAULT_LAYER);
		
		// modal panel
		modalPanel = new JPanel();
		modalPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		modalPanel.setSize(200, 200);
		modalPanel.setLayout(new BorderLayout());
		configurationModal = new FilterdConfigurationModal("Configure some filter", modalPanel);
		modalPanel.add(configurationModal, BorderLayout.CENTER);
		main.add(modalPanel, JLayeredPane.MODAL_LAYER);
	}

	private void toggleActionPerformed(ActionEvent e) {
//		filterdPanel.setOpaque(false);
//		modalPanel.setOpaque(true);
		configurationModal.toggle();
	}
}
