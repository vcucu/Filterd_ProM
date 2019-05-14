package org.processmining.filterd.wizard;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.filterd.configurations.ActionsParameters;
import org.processmining.filterd.configurations.OLD_FilterdEventAttributesParameters;
import org.processmining.framework.plugin.Progress;
import org.processmining.framework.plugin.impl.ProgressBarImpl;
import org.processmining.framework.util.ui.wizard.ProMWizard;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;

import timeframe.TimeframeParameters;

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
		return wizardModel.getConfiguration();
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
			switch(model.getConfiguration().getFilter()) {
				case "Event Attributes (dropdown)":
					model.getConfiguration().setParameters(new OLD_FilterdEventAttributesParameters(context, log));
					break;
				case "Timeframe Filter":
					model.getConfiguration().setParameters(new TimeframeParameters(context, log));
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
