package org.processmining.filterd.configurations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XAttribute;
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
import org.processmining.filterd.tools.Toolbox;
import org.processmining.filterd.widgets.ParameterController;
import org.processmining.filterd.widgets.ParameterMultipleFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetExtendedController;
import org.processmining.filterd.widgets.ParameterRangeFromRangeController;
import org.processmining.filterd.widgets.ParameterTextController;
import org.processmining.filterd.widgets.ParameterValueFromRangeController;
import org.processmining.filterd.widgets.ParameterYesNoController;

public class FilterdEventAttrConfig extends FilterdAbstractReferencingConfig {

	Collection<XAttribute> eventAttributes;
	
	public FilterdEventAttrConfig(XLog log, Filter filterType) {
		super(log, filterType);
		parameters = new ArrayList<Parameter>();
		
		List<String> attributes = new ArrayList<String>();
		
		XLogInfo logInfo = XLogInfoImpl.create(log);
		eventAttributes = logInfo.getEventAttributeInfo().getAttributes();
		
		for (String key : logInfo.getEventAttributeInfo().getAttributeKeys()) {
			attributes.add(key);
		}
		
		// Create attribute parameter, creates reference is true
		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", 
			"Filter by", attributes.get(0), attributes, true);

		parameters.add(attribute);
		
		for (XAttribute a : eventAttributes) {
			if (a.getKey().equals(attributes.get(0))) {
				switch(Toolbox.getType(a)) {
					case "Literal":
						concreteReference = new FilterdEventAttrCategoricalConfig(log, filterType, a);
						break;
					case "Boolean":
						concreteReference = new FilterdEventAttrCategoricalConfig(log, filterType, a);
						break;
					case "Continuous":
						concreteReference = new FilterdEventAttrNumericalConfig(log, filterType, a);
						break;
					case "Discrete":
						concreteReference = new FilterdEventAttrNumericalConfig(log, filterType, a);
						break;
					case "ID":
						concreteReference = new FilterdEventAttrCategoricalConfig(log, filterType, a);
						break;
					case "Timestamp":
						concreteReference = new FilterdEventAttrDateConfig(log, filterType);
						break;
					default: concreteReference = new FilterdEventAttrCategoricalConfig(log, filterType, a);
						break;
				}
			}
		}
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
		//check whether no params are empty if you populate with the component
		return true;
	};

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController("Event Attribute Configuration", parameters, this);
	}
	
	public FilterdAbstractConfig changeReference(ParameterOneFromSetExtendedController chosen) {
		String key = chosen.getValue();
		
		for (XAttribute a : eventAttributes) {
			if (a.getKey().equals(key)) {
				switch(Toolbox.getType(a)) {
					case "Literal":
						concreteReference = new FilterdEventAttrCategoricalConfig(log, filterType, a);
						break;
					case "Boolean":
						concreteReference = new FilterdEventAttrCategoricalConfig(log, filterType, a);
						break;
					case "Continuous":
						concreteReference = new FilterdEventAttrNumericalConfig(log, filterType, a);
						break;
					case "Discrete":
						concreteReference = new FilterdEventAttrNumericalConfig(log, filterType, a);
						break;
					case "ID":
						concreteReference = new FilterdEventAttrCategoricalConfig(log, filterType, a);
						break;
					case "Timestamp":
						concreteReference = new FilterdEventAttrDateConfig(log, filterType);
						break;
					default: concreteReference = new FilterdEventAttrCategoricalConfig(log, filterType, a);
						break;
				}
			}
		}
		
		return concreteReference;
	}
   
	public boolean checkValidity(XLog log) {
		if (concreteReference == null) return true;
		return concreteReference.checkValidity(log);
	}

	public XLog filter() {
		// TODO Auto-generated method stub
		
		return null;
	}
}
