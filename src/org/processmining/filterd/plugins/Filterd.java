package org.processmining.filterd.plugins;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.filterd.parameters.ActionsParameters;
import org.processmining.filterd.parameters.AttributeFilterParametersDropdown;
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
		return mineParameters(context, log, new ActionsParameters());
	}

	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "T. Klimovic & F. Davidovic", email = "t.klimovic@student.tue.nl & f.davidovic@student.tue.nl")
	@PluginVariant(variantLabel = "Filterd plug-in, parameterized", requiredParameterLabels = { 0, 1 })
	public XLog mineParameters(PluginContext context, XLog log, ActionsParameters parameters) {
		return mine(context, log, parameters);
	}

	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "T. Klimovic & F. Davidovic", email = "t.klimovic@student.tue.nl & f.davidovic@student.tue.nl")
	@PluginVariant(variantLabel = "Filterd plug-in, setup wizard", requiredParameterLabels = { 0 })
	public XLog mineDefault(UIPluginContext context, XLog log) {
		return mineParameters(context, log, populate(context, log, new ActionsParameters()));
	}

	private ActionsParameters populate(UIPluginContext context, XLog log, ActionsParameters parameters) {
		context.getProgress().setMaximum(3 * log.size());
		FilterdWizard<ActionsParameters> wizard = new FilterdWizard<ActionsParameters>(context, log);
		parameters = ProMWizardDisplay.show(context, wizard, parameters);
		if(parameters == null) {
			context.getFutureResult(0).cancel(true);
			return null;
		}
		FilterdParameters tmp = parameters.getParameters();
		return parameters;
	}

	private XLog mine(PluginContext context, XLog log, ActionsParameters parameters) {
		XFactory factory = XFactoryRegistry.instance().currentDefault();
		XLog filteredLog = factory.createLog((XAttributeMap) log.getAttributes().clone());
		filteredLog.getClassifiers().addAll(log.getClassifiers());
		filteredLog.getExtensions().addAll(log.getExtensions());
		filteredLog.getGlobalTraceAttributes().addAll(log.getGlobalTraceAttributes());
		filteredLog.getGlobalEventAttributes().addAll(log.getGlobalEventAttributes());
		AttributeFilterParametersDropdown pars = (AttributeFilterParametersDropdown) parameters.getParameters();
		for (XTrace trace : log) {
			XTrace filteredTrace = factory.createTrace(trace.getAttributes());
			for (XEvent event : trace) {
				boolean add = true;
				if(event.getAttributes().keySet().containsAll(pars.getGlobalAttributes())) {
					for (String key: event.getAttributes().keySet()) {
						String value = event.getAttributes().get(key).toString();
						if (!pars.getLogMap().get(key).contains(value)) {
							add = false;
							continue;
						}
					}
					if (add) {
						filteredTrace.add(event);
					}
				}
				context.getProgress().inc();
			}
			if (!pars.isRemoveEmptyTraces()||!filteredTrace.isEmpty()) {
				filteredLog.add(filteredTrace);
			}
		}
		XConceptExtension.instance().assignName(filteredLog, pars.getName());
		context.getFutureResult(0).setLabel(pars.getName());
		return filteredLog;
	}

}
