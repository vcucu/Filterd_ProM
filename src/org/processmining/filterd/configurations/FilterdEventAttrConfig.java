package org.processmining.filterd.configurations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

public class FilterdEventAttrConfig extends FilterdAbstractConfig {

	FilterdAbstractConfig concreteReference;
	
	public FilterdEventAttrConfig(XLog log, Filter filterType) {
		super(log, filterType);
		parameters = new ArrayList<Parameter>();
		
		// Get global attributes that are passed to the parameter 
		List<String> globalAttrAndClassifiers = computeGlobalAttributes(log);
		//add the complex classifiers to the list of global attributes 
		globalAttrAndClassifiers.addAll(computeComplexClassifiers(log));
		
		// Create attribute parameter, creates reference is true
		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", 
			"Filter by", globalAttrAndClassifiers.get(0), globalAttrAndClassifiers, true);

		
		parameters.add(attribute);
		//parameters.addAll(concreteReference.getParameters());
		
		
	}

	@Override
	public FilterdAbstractConfig populate(AbstractFilterConfigPanelController abstractComponent) {
		
		FilterConfigPanelController component = (FilterConfigPanelController) abstractComponent;
		List<ParameterController> controllers = component.getControllers();
		for(ParameterController controller : controllers) {
			//all cases assume that the controller has a name corresponding to the parameter name
			if(controller instanceof ParameterOneFromSetExtendedController) {
				ParameterOneFromSetExtendedController casted = (ParameterOneFromSetExtendedController) controller;
				concreteReference.populate(casted.getNestedConfigPanel());
				
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
		// TODO Auto-generated method stub
		return false;
	}

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController("Event Attribute Configuration", parameters);
	}
	
	public FilterdAbstractConfig changeReference(ParameterOneFromSetController chosen) {
		
		concreteReference = new FilterdEventAttrDateConfig(log, filterType);
		
		return null;
	}
   
	public boolean checkValidity(XLog log) {
		return concreteReference.checkValidity(log);
	}

	public XLog filter() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Date addTimezone (String time) {
		// Set time format for the time stamp
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd-HH:mm:ss.SSS");
				
				Date date = null;

				// Time is in GMT
				if (time.contains("Z")) {
					time.replace("Z", "");
					
					try {
						date = dateFormat.parse(time);	
					} catch (ParseException e) {
						// Print the trace so we know what went wrong.
						e.printStackTrace();
					}
				}
				// Time is relative to GMT
				else {
					
					// Represents the last 5 characters e.g. "02:00".
					String lastFiveCharacters = time.substring(time.length() - 5, 
							time.length() - 1);
					
					// Set time format for the hours relative to GMT.
					SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm");
					Date hourDate = null;
					
					// Parse the hours into a Date.
					try {
						hourDate = hourFormat.parse(lastFiveCharacters);	
					} catch (ParseException e) {
						// Print the trace so we know what went wrong.
						e.printStackTrace();
					}
					
					// Replace the T-separator with a colon.
					time.replace("T", "-");
					
					// Get whether it was later or earlier relative to GMT.
					char stringSign = time.charAt(time.length() - 3);
					
					// Remove The relative time as we already have it separated.
					time = time.substring(0, time.length() - 7);

					// Parse the time stamp into a Date.
					try {
						date = dateFormat.parse(time);	
					} catch (ParseException e) {
						// Print the trace so we know what went wrong.
						e.printStackTrace();
					}
					
					// Change the relative time to GMT
					if (stringSign == '+') {
						date.setTime(date.getTime() - hourDate.getTime());
					} else {
						date.setTime(date.getTime() + hourDate.getTime());
					}
				}
				
				return date;
		}

}
