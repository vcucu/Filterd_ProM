package org.processmining.filterd.configurations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.filterd.widgets.ParameterOneFromSetExtendedController;

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
		switchReference(attributes.get(0));
	}

	
	
	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController("Event Attribute Configuration", parameters, this);
	}
	
	public FilterdAbstractConfig changeReference(ParameterOneFromSetExtendedController chosen) {
		switchReference(chosen.getValue());
		return concreteReference;
	}
	
	public void switchReference(String key) {
		for (XAttribute a : eventAttributes) {
			String k = a.getKey();
			if (k.equals(key)) {
				switch(Toolbox.getType(a)) {
					case "Literal":
						concreteReference = new FilterdEventAttrCategoricalConfig(log, filterType, k);
						break;
					case "Boolean":
						concreteReference = new FilterdEventAttrCategoricalConfig(log, filterType, k);
						break;
					case "Continuous":
						concreteReference = new FilterdEventAttrNumericalConfig(log, filterType, k);
						break;
					case "Discrete":
						concreteReference = new FilterdEventAttrNumericalConfig(log, filterType, k);
						break;
					case "ID":
						concreteReference = new FilterdEventAttrCategoricalConfig(log, filterType, k);
						break;
					case "Timestamp":
						concreteReference = new FilterdEventAttrDateConfig(log, filterType);
						break;
					default: concreteReference = new FilterdEventAttrCategoricalConfig(log, filterType, k);
						break;
				}
			}
		}
	}
   
	public boolean checkValidity(XLog log) {
		if (concreteReference == null) return true;
		return concreteReference.checkValidity(log);
	}
}
