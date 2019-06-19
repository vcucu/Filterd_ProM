package org.processmining.filterd.configurations;

import java.util.List;

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
import org.processmining.filterd.widgets.ParameterController;
import org.processmining.filterd.widgets.ParameterMultipleFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetExtendedController;
import org.processmining.filterd.widgets.ParameterRangeFromRangeController;
import org.processmining.filterd.widgets.ParameterTextController;
import org.processmining.filterd.widgets.ParameterValueFromRangeController;
import org.processmining.filterd.widgets.ParameterYesNoController;

public abstract class FilterdAbstractReferencingConfig extends FilterdAbstractConfig {

	
	FilterdAbstractReferenceableConfig concreteReference;
	
	public FilterdAbstractReferencingConfig(XLog log, Filter filterType) {
		super(log, filterType);
	}

	/**
	 *  @return true if the candidate log matches the given parametres
	 */
	@Override
	public abstract boolean checkValidity(XLog candidateLog);
	
	/**
	 *  @return true if the parameters from the component match the current log
	 */
	@Override
	public abstract boolean canPopulate(FilterConfigPanelController component) ;

	/**
	 * Method responsible for assigning the new correct reference based on the 
	 * OneFromSetExtended controller that it receives into the concreteReference
	 * Before that, it removes all of the parameters from the Referenceable config
	 * from the parameters array of this config, and after it updates the reference
	 * it also adds the new parameters of the reference to its own parameters list
	 */
	public abstract FilterdAbstractConfig changeReference(ParameterOneFromSetExtendedController controller);
	
	/**
	 * get reference without changing it
	 */
	public FilterdAbstractReferenceableConfig getConcreteReference() {
		return concreteReference;
	}
	
	/**
	 * setter for concreteReference
	 */
	public void setConcreteReference(FilterdAbstractReferenceableConfig concreteReference) {
		this.concreteReference = concreteReference;
	}
	
	/**
	 * retrieves the parameters from the ui components and updates in the values
	 * of the parameters in the parameters array based on what the user has chosen
	 * it also calls the populate method of its concrete reference
	 */
	@Override
	public FilterdAbstractConfig populate(AbstractFilterConfigPanelController abstractComponent) {
		
		FilterConfigPanelController component = (FilterConfigPanelController) abstractComponent;
		List<ParameterController> controllers = component.getControllers();
		for(ParameterController controller : controllers) {
			//all cases assume that the controller has a name corresponding to the parameter name
			
			
			//OneFromSetController refers to the ui component of the concreteReference
			//so the referenceable config. This calls the populate method of the concrete reference and also 
			// updates the parameters in the parameters list of this(referencing) configuration
			//to match the newly updated values 
			if(controller instanceof ParameterOneFromSetExtendedController) {
				ParameterOneFromSetExtendedController casted = (ParameterOneFromSetExtendedController) controller;
				concreteReference.populate(casted.getNestedConfigPanel());
				for (Parameter param : concreteReference.getParameters()) {
					parameters.remove(param);
					parameters.add(param);
				}
				ParameterOneFromSet param = (ParameterOneFromSet) getParameter(casted.getName());
				param.setChosen(casted.getValue());
	
				
				//retrieve the Controller of type YesNo and update the  chosen value
				//first ParameterYesNo with the same name as the name of the controller name
			} else if(controller instanceof ParameterYesNoController) {
				ParameterYesNoController casted = (ParameterYesNoController) controller;
				ParameterYesNo param = (ParameterYesNo) getParameter(controller.getName());
				param.setChosen(casted.getValue());	
			
				
				//retrieve the Controller of type OneFromSet and update the  chosen value
				//first ParameterOneFromSet with the same name as the name of the controller name
			} else if(controller instanceof ParameterOneFromSetController) {
				ParameterOneFromSetController casted = (ParameterOneFromSetController) controller;
				ParameterOneFromSet param = (ParameterOneFromSet) getParameter(controller.getName());
				param.setChosen(casted.getValue());	
				
				
				//retrieve the Controller of type MultipleFromSet and update the  chosen value
				//first ParameterMultipleFromSet with the same name as the name of the controller name
			} else if(controller instanceof ParameterMultipleFromSetController) {
				ParameterMultipleFromSetController casted = (ParameterMultipleFromSetController) controller;
				ParameterMultipleFromSet param = (ParameterMultipleFromSet) getParameter(controller.getName());
				param.setChosen(casted.getValue());				
				
				
				//retrieve the Controller of type ValueFromRange and update the  chosen value
				//first ParameterValueFromRange with the same name as the name of the controller name
			} else if(controller instanceof ParameterValueFromRangeController) {
				ParameterValueFromRangeController casted = (ParameterValueFromRangeController) controller;
				ParameterValueFromRange param = (ParameterValueFromRange) getParameter(controller.getName());
				param.setChosen(casted.getValue());	
				
				
				//retrieve the Controller of type Text and update the  chosen value
				//first ParameterText with the same name as the name of the controller name
			} else if(controller instanceof ParameterTextController) {
				ParameterTextController casted = (ParameterTextController) controller;
				ParameterText param = (ParameterText) getParameter(controller.getName());
				param.setChosen(casted.getValue());	
							
				//retrieve the Controller of type RangeFromRange and update the  chosen value
				//first ParameterRangeFromRange with the same name as the name of the controller name
			} else if(controller instanceof ParameterRangeFromRangeController) {
				ParameterRangeFromRangeController casted = (ParameterRangeFromRangeController) controller;
				ParameterRangeFromRange param = (ParameterRangeFromRange) getParameter(controller.getName());
				param.setChosenPair(casted.getValue());	
			
				//If a different controller than one of the expected values, throw and exception
			} else {
				throw new IllegalArgumentException("Unsupporrted controller type.");
			}	
			
		}
		return this;
	}
}

