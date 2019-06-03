package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;

public class FilterdTraceAttrConfig extends FilterdAbstractReferencingConfig {

	
	Set<String> traceAttributes;
	
	public FilterdTraceAttrConfig(XLog log, Filter filterType) {
		super(log, filterType);
		
		parameters = new ArrayList<Parameter>();
		
		traceAttributes = new HashSet<>();
		
		for (XTrace trace : log) {
			
				
				traceAttributes.addAll(trace.getAttributes().keySet());
				
			
			
		}
		
		// Create attribute parameter, creates reference is true
		ParameterOneFromSet attribute = new ParameterOneFromSet(
				"attribute", 
				"Filter by", 
				traceAttributes.iterator().next(), 
				new ArrayList<String>(traceAttributes), 
				true);
		
		ParameterOneFromSet filterInOut = new ParameterOneFromSet(
				"filterInOut",
				"Filter mode",
				"in",
				Arrays.asList("in", "out")
				);
		

		Set<String> attributeValues = new HashSet<>();
		
		List traceAttributesList = new ArrayList<String>();
		for (XTrace trace : log) {
			
				
				XAttributeMap traceAttrs = trace.getAttributes();
				if (traceAttrs.containsKey(traceAttributesList.get(0))) 
					attributeValues.add(traceAttrs.get(traceAttributesList.get(0)).toString());
			
		}
		List<String> attributeValuesList = new ArrayList<String>(attributeValues);
		ParameterMultipleFromSet attrValues = new ParameterMultipleFromSet(
				"attrValues",
				"Desired values:",
				Arrays.asList(attributeValuesList.get(0)),
				attributeValuesList
				);
		parameters.add(attribute);
		parameters.add(filterInOut);
		parameters.add(attrValues);
	}

	public FilterdAbstractConfig populate(AbstractFilterConfigPanelController abstractComponent) {
		FilterConfigPanelController component = (FilterConfigPanelController) abstractComponent;
		List<ParameterController> controllers = component.getControllers();
		for(ParameterController controller : controllers) {
			//all cases assume that the controller has a name corresponding to the parameter name
			if(controller instanceof ParameterOneFromSetExtendedController) {
				ParameterOneFromSetExtendedController casted = (ParameterOneFromSetExtendedController) controller;
				concreteReference.populate(casted.getNestedConfigPanel());
				//this method needs to be in every referencing class
				
			} else if(controller instanceof ParameterYesNoController) {
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

	public boolean canPopulate(FilterConfigPanelController component) {
		// Impossible to check so we have to rely on the user himself to 
		// populate it with the correct parameters.
		return true;
	}

	public FilterConfigPanelController getConfigPanel() {
		FilterConfigPanelController filterConfigPanel = 
				new FilterConfigPanelController(
				"Trace Attribute Configuration", 
				parameters, this
				);
		for(ParameterController parameter : filterConfigPanel.getControllers()) {
			if (parameter.getName().equals("attribute")) {
				ParameterOneFromSetController casted = (ParameterOneFromSetController) parameter;
				ComboBox<String> comboBox = casted.getComboBox();
				comboBox.valueProperty().addListener(new ChangeListener<String>() {
					@Override 
					public void changed(ObservableValue ov, String oldValue, String newValue) {
						final XLog Llog = log;
						List<Parameter> params = parameters;
						if (Llog != null) {
						for (ParameterController changingParameter : filterConfigPanel.getControllers()) {
							
							if (changingParameter.getName().equals("attrValues")) {
								
								ParameterMultipleFromSetController castedChanging = 
										(ParameterMultipleFromSetController) changingParameter;
								Set<String> attributeValues = new HashSet<>();
								
								for (XTrace trace : Llog) {
									
										
										XAttributeMap traceAttrs = trace.getAttributes();
										if (traceAttrs.containsKey(newValue))
											attributeValues.add(traceAttrs.get(newValue).toString());
									
								}
								List<String> attributeValuesList = new ArrayList<String>(attributeValues);
								((ParameterMultipleFromSet) params.get(2))
								.setOptions(attributeValuesList);
								((ParameterMultipleFromSet) params.get(2))
								.setChosen(attributeValuesList);
								((ParameterMultipleFromSet) params.get(2))
								.setDefaultChoice(attributeValuesList);
								castedChanging.changeOptions(attributeValuesList);
							
							}
						}
						
					}
					}
			});
				
		}
		}
		return filterConfigPanel;
	}
	
	@Override
	public FilterdAbstractConfig changeReference(
			ParameterOneFromSetExtendedController chosen) {
		
		
		// Return this reference.
		return this;
	}

	public boolean checkValidity(XLog log) {
		// Impossible since we can not figure out the type.
		return false;
	}



}
