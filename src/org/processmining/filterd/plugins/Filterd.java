package org.processmining.filterd.plugins;

import javax.swing.JComponent;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.filterd.parameters.ActionsParameters;
import org.processmining.filterd.parameters.AttributeFilterParameters;
import org.processmining.filterd.parameters.AttributeFilterParametersDropdown;
import org.processmining.filterd.parameters.ConcreteParameters;
import org.processmining.filterd.parameters.FilterdParameters;
import org.processmining.filterd.wizard.FilterdFilterStep;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

@Plugin(name = "Filterd plug-in", returnLabels = { "Output log" }, returnTypes = { XLog.class }, parameterLabels = {
		"Log", "Parameters" }, userAccessible = true)
public class Filterd {

	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "T. Klimovic & F. Davidovic", email = "t.klimovic@student.tue.nl & f.davidovic@student.tue.nl")
	@PluginVariant(variantLabel = "Filterd plug-in, setup wizard", requiredParameterLabels = { 0 })
	public XLog mineInterface(UIPluginContext context, XLog log) {
		return mine(context, log, populate(context, log, new ActionsParameters()));
	}

	private ActionsParameters populate(UIPluginContext context, XLog log, ActionsParameters parameters) {
		InteractionResult res;
		
		// show step 1 (pick which filter you want to use)
		FilterdFilterStep step1 = new FilterdFilterStep();
		res = context.showWizard("Filterd plug-in configuration", true, true, step1);
		if(res == InteractionResult.CANCEL) {
			context.getFutureResult(0).cancel(true);
			return null;
		}
		step1.apply(parameters); // apply changes from the gui
		
		// resolve filter to filter parameters, and show progress bar if necessary
		FilterdParameters filterParameters = mapFilterToParameters(parameters, context, log);
		parameters.setParameters(filterParameters);
		
		// show step 2 (configuration of a specific filter)
		JComponent propertiesPanel = filterParameters.getPropertiesPanel();
		res = context.showWizard("Filterd plug-in configuration", true, true, propertiesPanel);
		if(res != InteractionResult.FINISHED) {
			context.getFutureResult(0).cancel(true);
			return null;
		}
		filterParameters.apply(propertiesPanel); // apply changes from the gui
		
		return parameters;
	}
	
	private FilterdParameters mapFilterToParameters(ActionsParameters parameters, UIPluginContext context, XLog log) {
		switch(parameters.getFilter()) {
			case "Event Attributes":
				context.getProgress().setMaximum(3 * log.size());
				return new AttributeFilterParameters(context, log);
			case "Concrete Filter":
				// no progress needed
				return new ConcreteParameters();
			case "Event Attributes (dropdown)":
				context.getProgress().setMaximum(3 * log.size());
				return new AttributeFilterParametersDropdown(context, log);
			default: 
				return null;
		}
	}

	private XLog mine(PluginContext context, XLog log, ActionsParameters parameters) {
		return log;
	}

}
