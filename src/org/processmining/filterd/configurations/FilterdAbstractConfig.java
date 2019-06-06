package org.processmining.filterd.configurations;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.extension.XExtension;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.parameters.ParameterText;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.filterd.tools.EmptyLogException;
import org.processmining.filterd.widgets.ParameterController;
import org.processmining.filterd.widgets.ParameterMultipleFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetController;
import org.processmining.filterd.widgets.ParameterRangeFromRangeController;
import org.processmining.filterd.widgets.ParameterTextController;
import org.processmining.filterd.widgets.ParameterValueFromRangeController;
import org.processmining.filterd.widgets.ParameterYesNoController;

@XmlAccessorType(XmlAccessType.NONE) // Makes sure only explicitly named elements get added to the XML.
@XmlRootElement(name = "FilterButtonModel") // Needed by JAXB to generate an XML.
public abstract class FilterdAbstractConfig {
	
	protected Filter filterType;
	protected XLog log;
	protected List<Parameter> parameters;
	protected boolean isValid;
	protected XEventClassifier classifier;
	protected List<XExtension> standardExtensions;
	protected boolean isAttribute; // checks whether selected string is attribute or complex classifier
	protected FilterConfigPanelController configPanel;

	public FilterdAbstractConfig(XLog log, Filter filterType ) throws EmptyLogException{
		
		this.filterType = filterType;
		this.checkEmptyLog(log);
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
	
	public void checkEmptyLog(XLog candidateLog) throws EmptyLogException {
		try {
			candidateLog.get(0);		
		} catch (IndexOutOfBoundsException e) {
			throw new EmptyLogException("The used log cannot be empty");
		}
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
			//all cases assume that the controller has a name corresponding to the parameter name
			if(controller instanceof ParameterYesNoController) {
				ParameterYesNoController casted = (ParameterYesNoController) controller;
				ParameterYesNo param = (ParameterYesNo) getParameter(controller.getName());
				param.setChosen(casted.getValue());	
				
			} else if(controller instanceof ParameterOneFromSetController) {
				ParameterOneFromSetController casted = (ParameterOneFromSetController) controller;
				ParameterOneFromSet param = (ParameterOneFromSet) getParameter(controller.getName());
				param.setChosen(casted.getValue());	
				
			} else if(controller instanceof ParameterMultipleFromSetController) {
				ParameterMultipleFromSetController casted = (ParameterMultipleFromSetController) controller;
				ParameterMultipleFromSet param = (ParameterMultipleFromSet) getParameter(controller.getName());
				param.setChosen(casted.getValue());				
				
			} else if(controller instanceof ParameterValueFromRangeController) {
				ParameterValueFromRangeController casted = (ParameterValueFromRangeController) controller;
				ParameterValueFromRange param = (ParameterValueFromRange) getParameter(controller.getName());
				param.setChosen(casted.getValue());	
				
			} else if(controller instanceof ParameterTextController) {
				ParameterTextController casted = (ParameterTextController) controller;
				ParameterText param = (ParameterText) getParameter(controller.getName());
				param.setChosen(casted.getValue());	
				
			} else if(controller instanceof ParameterRangeFromRangeController) {
				ParameterRangeFromRangeController casted = (ParameterRangeFromRangeController) controller;
				ParameterRangeFromRange param = (ParameterRangeFromRange) getParameter(controller.getName());
				param.setChosenPair(casted.getValue());	
				
			} else {
				throw new IllegalArgumentException("Unsupporrted controller type.");
			}	
				
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
	public AbstractFilterConfigPanelController getConfigPanel() {
			return configPanel;
		}
	
	/**
	 * Invokes the {@filter(PluginContext context, XLog log, List<Parameter> parameters)} 
	 * method of the concrete {@filterType}
	 * @param context the PluginContext
	 * @return the filtered log
	 */
	public XLog filter() {
		return filterType.filter(log, parameters);
	}
}
