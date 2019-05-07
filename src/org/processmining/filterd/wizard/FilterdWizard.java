package org.processmining.filterd.wizard;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.filterd.parameters.ActionsParameters;
import org.processmining.filterd.parameters.AttributeFilterParameters;
import org.processmining.filterd.parameters.AttributeFilterParametersDropdown;
import org.processmining.filterd.parameters.ConcreteParameters;
import org.processmining.framework.plugin.Progress;
import org.processmining.framework.plugin.impl.ProgressBarImpl;
import org.processmining.framework.util.ui.wizard.ProMWizard;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;

public class FilterdWizard<T extends ActionsParameters> implements ProMWizard<T, FilterdWizardModel<T>> {
	
	private int step;
	private XLog log;
	private UIPluginContext context;
	
	public FilterdWizard(UIPluginContext context, XLog log) {
		super();
		step = 0;
		this.log = log;
		this.context = context;
		
		context.getProgress().setMaximum(3 * log.size());
		Progress progress = new ProgressBarImpl(context);
		progress.setMaximum(3 * log.size());
	}

	public ProMWizardStep<T> getFirst(FilterdWizardModel<T> model) {
//		return TextStep.create("Welcome to the Filterd plug-in", "On the following screen, select which filter you would like to use. Finally, configure the filter.");
		return getNext(model, null);
	}

	public T getModel(FilterdWizardModel<T> wizardModel) {
		return wizardModel.getParameters();
	}

	public ProMWizardStep<T> getNext(FilterdWizardModel<T> model, ProMWizardStep<T> current) {
//		if(model.getParameters().getFilter().equals("")) {
		if(step == 0) {
			// filter not set yet i.e. choose filter step
			step++;
			return new FilterdFilterWizardStep<T>();
		} else if(step == 1) {
			// filter is set i.e. configuration for a specific filter
			step++;
			
			// create configuration for selected filter
			switch(model.getParameters().getFilter()) {
				case "Event Attributes":
					model.getParameters().setParameters(new AttributeFilterParameters(context, log));
					break;
				case "Concrete Filter":
					model.getParameters().setParameters(new ConcreteParameters());
					break;
				case "Event Attributes (dropdown)":
					model.getParameters().setParameters(new AttributeFilterParametersDropdown(context, log));
					break;
			}
			
			return new FilterdConfigurationWizardStep<T>();
		} else {
			step++;
			return null;
		}
	}

	public FilterdWizardModel<T> getWizardModel(T model, FilterdWizardModel<T> currentWizardModel) {
		return new FilterdWizardModel<T>(model);
	}

	public boolean isFinished(FilterdWizardModel<T> model) {
		return step == 3;
	}

	public boolean isLastStep(FilterdWizardModel<T> model) {
		return step == 2;
	}
}
