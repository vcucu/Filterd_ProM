package org.processmining.filterd.plugins;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;

public class BasicVisualizer extends JPanel {

	
	/**
	 * We need a serial ID to allow serializing.
	 */
	private static final long serialVersionUID = 9104736085046064684L;

	@Plugin(name = "Basic visualizer", level = PluginLevel.PeerReviewed, parameterLabels = "Parameter labels go here",
			returnTypes = JComponent.class, returnLabels = "Return label goes here", userAccessible = true,
			mostSignificantResult = 1, help = "Help text goes here")
	@Visualizer(name = "Name of the visualizer", pack = "Filterd")
	public static JComponent visualize(final PluginContext context, final XLog log) {
		return new JLabel("Hello world");
	}
	
}
