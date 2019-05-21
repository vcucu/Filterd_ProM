package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.widgets.*;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.framework.plugin.PluginContext;
public abstract class FilterdAbstractConfig {
	
	protected Filter filterType;
	protected XLog log;
	protected List<Parameter> parameters;
	protected boolean isValid;
	protected XEventClassifier classifier;

	
	public FilterdAbstractConfig(XLog log, Filter filterType ) {
		this.filterType = filterType;
		this.setLog(log);
		
	}
	
	public XEventClassifier getClassifier() {
		return classifier;
	}

	public void setClassifier(XEventClassifier classifier) {
		this.classifier = classifier;
	}

	public Filter getFilterType() {
		return filterType;
	}

	public void setFilterType(Filter filterType) {
		this.filterType = filterType;
	}

	public XLog getLog() {
		return log;
	}
    /**
     * Setter for the {@log} attribute.
     * Invokes {@checkValidity(log)}. If it returns true, it sets the log to the 
     * corresponding value. If it returns false, it sets log to null and throws exception.
     * 
     * @param log the log to be set
     * @throws InputMismatchException
     */
	public void setLog(XLog candidateLog) {
		
		if (this.checkValidity(candidateLog)) {
			this.log = candidateLog;
			isValid = true;
		} else {
			// raise error
			isValid = false;
		}
		
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public boolean isValid() {
		return isValid;
	}
	/**
	 * Get the parameter object according to its name.
	 * 
	 * @param whichParameter the name of the parameter whose type is to be returned
	 * @return the parameter object or null if no parameter 
	 * with {@whichParameter} name does not exist.
	 */
	public Parameter getParameter(String whichParameter) {
		
		for(Parameter parameter :parameters ) {
			if (parameter.getName().equals(whichParameter)) {	
				return parameter;
			}
		}
		return null;
	}
	/**
	 * Checks whether the imported log complies with the filter configuration(s)
	 * @param log the imported log in the cell
	 * @return true if the log is valid, false otherwise
	 */
	public abstract boolean checkValidity(XLog candidateLog);
	
	/**
	 * Populates the parameters with information from the configuration panel.
	 * @return concrete configuration of the configuration panel 
	 */
	public FilterdAbstractConfig populate(AbstractFilterConfigPanelController abstractComponent) {
	
	FilterConfigPanelController component = (FilterConfigPanelController) abstractComponent;
	List<ParameterController> controllers = component.getControllers();
	for(ParameterController controller : controllers) {

		if(controller instanceof ParameterOneFromSetExtendedController) {
			ParameterOneFromSetExtendedController casted = (ParameterOneFromSetExtendedController) controller;
			//concreteReference.populate(casted.getNestedConfigPanel());
			//this menthod needs to be in every referencable class
		} else if(controller instanceof ParameterYesNoController) {
			ParameterYesNoController casted = (ParameterYesNoController) controller;
			//getParameter(controller.getName())).setChosen(casted.getValue());		
		} else if(controller instanceof ParameterOneFromSetController) {
			ParameterOneFromSetController casted = (ParameterOneFromSetController) controller;
			
		} else if(controller instanceof ParameterMultipleFromSetController) {
			ParameterMultipleFromSetController casted = (ParameterMultipleFromSetController) controller;

		} else if(controller instanceof ParameterValueFromRangeController) {
			ParameterValueFromRangeController casted = (ParameterValueFromRangeController) controller;
			
		} else if(controller instanceof ParameterTextController) {
			ParameterTextController casted = (ParameterTextController) controller;

		} else if(controller instanceof ParameterRangeFromRangeController) {
			ParameterRangeFromRangeController casted = (ParameterRangeFromRangeController) controller;

		} else {
			throw new IllegalArgumentException("Unsupporrted controller type.");
		}	
		//assumes that the controller has a name corresponding to the parameter name
		
		
		}
		return this;
	}

	/**
	 * Checks whether all components from the configuration panel
	 * have a mapping to all parameters of the concrete configuration.
	 */
	public abstract boolean canPopulate(FilterConfigPanelController component);
	/**
	 * Returns the configuration panel which is used by
	 * the {@populate(component)} and {@canPopulate(component)}.
	 * 
	 * @return the concrete configuration panel
	 */
	public abstract FilterConfigPanelController getConfigPanel();
	
	/**
	 * Invokes the {@filter(PluginContext context, XLog log, List<Parameter> parameters)} 
	 * method of the concrete {@filterType}
	 * @param context the PluginContext
	 * @return the filtered log
	 */
	public XLog filter(PluginContext context) {
		return filterType.filter(context, log, parameters);
	};
}
