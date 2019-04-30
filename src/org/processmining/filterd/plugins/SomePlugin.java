package org.processmining.filterd.plugins;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.filterd.help.YourHelp;
import org.processmining.filterd.parameters.SomeParameters;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

@Plugin(name = "Some random plug-in", parameterLabels = { "Log", "Parameters" }, 
		returnLabels = { "Random plug-in results" }, returnTypes = { XLog.class }, help = YourHelp.TEXT)
public class SomePlugin {

	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "F. Davidovic", email = "f.davidovic@student.tue.nl")
	@PluginVariant(variantLabel = "Mine with random plug-in, default configuration", requiredParameterLabels = { 0 })
	public XLog mineDefault(PluginContext context, XLog log) {
		SomeParameters parameters = new SomeParameters();
		return mine(context, log, parameters);
	}

	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "F. Davidovic", email = "f.davidovic@student.tue.nl")
	@PluginVariant(variantLabel = "Mine with random plug-in, parameterized", requiredParameterLabels = { 0, 1 })
	public XLog mineParameters(PluginContext context, XLog log, SomeParameters parameters) {
		// do some setup?
		return mine(context, log, parameters);
	}

	private XLog mine(PluginContext context, XLog log, SomeParameters parameters) {
		return log;
	}
}
