package org.processmining.filterd.plugins;

import java.util.Collection;

import org.deckfour.xes.info.XAttributeInfo;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.filterd.parameters.ActionsParameters;
import org.processmining.filterd.parameters.ConcreteParameters;
import org.processmining.filterd.wizard.FilterdWizard;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.framework.util.ui.wizard.ProMWizardDisplay;

@Plugin(name = "Filterd plug-in", returnLabels = { "Output log" }, returnTypes = { XLog.class }, parameterLabels = {
		"Log", "Parameters" }, userAccessible = true)
public class Filterd {

//	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "Filterd team", email = "t.klimovic@student.tue.nl")
	@PluginVariant(variantLabel = "Filterd plug-in, default configuration", requiredParameterLabels = { 0 })
	public XLog mineDefault(PluginContext context, XLog log) {
		return mineParameters(context, log, new ActionsParameters());
	}

	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "Filterd team", email = "t.klimovic@student.tue.nl")
	@PluginVariant(variantLabel = "Filterd plug-in, parameterized", requiredParameterLabels = { 0, 1 })
	public XLog mineParameters(PluginContext context, XLog log, ActionsParameters parameters) {
		return mine(context, log, parameters);
	}

	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "Filterd team", email = "t.klimovic@student.tue.nl")
	@PluginVariant(variantLabel = "Filterd plug-in, setup wizard", requiredParameterLabels = { 0 })
	public XLog mineDefault(UIPluginContext context, XLog log) {
		
		XLogInfo logInfo = XLogInfoFactory.createLogInfo(log);
		XAttributeInfo attrInfo = logInfo.getEventAttributeInfo();
		Collection<String> attributes = attrInfo.getAttributeKeys();
		System.out.println(attributes);
		
		return mineParameters(context, log, populate(context, new ActionsParameters()));
	}

	private ActionsParameters populate(UIPluginContext context, ActionsParameters parameters) {
		FilterdWizard<ActionsParameters> wizard = new FilterdWizard<ActionsParameters>();
		parameters = ProMWizardDisplay.show(context, wizard, parameters);
		if(parameters == null) {
			context.getFutureResult(0).cancel(true);
			return null;
		}
		
		ConcreteParameters act = (ConcreteParameters) parameters.getParameters();
		System.out.println("some int: " + act.getSomeInt());
		System.out.println("some double: " + act.getSomeDouble());
		System.out.println("some bool: " + act.isSomeBool());
		
		return parameters;
	}

	private XLog mine(PluginContext context, XLog log, ActionsParameters parameters) {
		return log;
	}

}
