package org.processmining.filterd.plugins;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.filterd.parameters.FilterdParameters;
import org.processmining.filterd.wizard.FilterdWizard;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.framework.util.ui.wizard.ProMWizardDisplay;

@Plugin(name = "Filterd plug-in", returnLabels = { "Output log" }, returnTypes = { XLog.class }, parameterLabels = {
		"Log", "Parameters" }, userAccessible = true)
public class Filterd {

//	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "T. Klimovic & F. Davidovic", email = "t.klimovic@student.tue.nl & f.davidovic@student.tue.nl")
	@PluginVariant(variantLabel = "Filterd plug-in, default configuration", requiredParameterLabels = { 0 })
	public XLog mineDefault(PluginContext context, XLog log) {
		return mineParameters(context, log, new FilterdParameters());
	}

	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "T. Klimovic & F. Davidovic", email = "t.klimovic@student.tue.nl & f.davidovic@student.tue.nl")
	@PluginVariant(variantLabel = "Filterd plug-in, parameterized", requiredParameterLabels = { 0, 1 })
	public XLog mineParameters(PluginContext context, XLog log, FilterdParameters parameters) {
		return mine(context, log, parameters);
	}

	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "T. Klimovic & F. Davidovic", email = "t.klimovic@student.tue.nl & f.davidovic@student.tue.nl")
	@PluginVariant(variantLabel = "Filterd plug-in, setup wizard", requiredParameterLabels = { 0 })
	public XLog mineDefault(UIPluginContext context, XLog log) {
		return mineParameters(context, log, populate(context, new FilterdParameters()));
	}

	private FilterdParameters populate(UIPluginContext context, FilterdParameters parameters) {
		FilterdWizard<FilterdParameters> wizard = new FilterdWizard<FilterdParameters>();
		parameters = ProMWizardDisplay.show(context, wizard, parameters);
		if(parameters == null) {
			context.getFutureResult(0).cancel(true);
			return null;
		}
		return parameters;
	}

	private XLog mine(PluginContext context, XLog log, FilterdParameters parameters) {
		return log;
	}

}
