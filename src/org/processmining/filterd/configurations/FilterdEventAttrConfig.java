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
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.filterd.widgets.ParameterOneFromSetExtendedController;

public class FilterdEventAttrConfig extends FilterdAbstractReferencingConfig {

	// Collection to hold the attributes of the events.
	Collection<XAttribute> eventAttributes;

	public FilterdEventAttrConfig(XLog log, Filter filterType) {
		super(log, filterType);
		// Create list for holding the parameters.
		parameters = new ArrayList<Parameter>();

		// Create list for holding the keys of the event attributes.
		List<String> attributes = new ArrayList<String>();

		// Get XLog info to get all the event attributes.
		XLogInfo logInfo = XLogInfoImpl.create(log);
		eventAttributes = logInfo.getEventAttributeInfo().getAttributes();

		// Loop over all the event attribute keys.
		for (String key : logInfo.getEventAttributeInfo().getAttributeKeys()) {
			// Add to attributes list.
			attributes.add(key);
		}

		// Create attribute parameter, creates reference is true
		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", 
				"Filter by", attributes.get(0), attributes, true);

		// Add to parameter list.
		parameters.add(attribute);
		// Switch reference based on the chosen attribute.
		switchReference(attributes.get(0));
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};

	@Override
	public AbstractFilterConfigPanelController getConfigPanel() {
		// If the configuration panel is not initialized yet.
		if (this.configPanel == null) {
			// Create it.
			this.configPanel = new FilterConfigPanelController("Event Attribute Configuration", parameters, this);
		}
		
		return configPanel;
	}
	
	/**
	 * Changes the reference this configuration is holding.
	 * 
	 * @param chosen The controller on which the reference depends.
	 * 
	 * @return The correct reference.
	 */
	public FilterdAbstractConfig changeReference(ParameterOneFromSetExtendedController chosen) {
		// Remove all parameters that belong to the concrete reference.
		for (Parameter param : concreteReference.getParameters()) {
			parameters.remove(param);
		}
		// Switch based on the chosen value.
		switchReference(chosen.getValue());
		// Populate the parameters list with the parameters of the new 
		// reference.
		for (Parameter param : concreteReference.getParameters()) {
			parameters.add(param);
		}
		return concreteReference;
	}

	/**
	 * Switches the concrete reference based on the key.
	 * 
	 * @param key To switch on.
	 */
	public void switchReference(String key) {
		// Loop over all event attributes.
		for (XAttribute a : eventAttributes) {
			// Get the string representation of the key.
			String k = a.getKey();
			// If this equals the key.
			if (k.equals(key)) {
				// Switch based on the type.
				switch(Toolbox.getType(a)) {
					case "Literal":
						// Literal, thus we do categorical.
						concreteReference = new FilterdEventAttrCategoricalConfig(log, filterType, k);
						break;
					case "Boolean":
						// Boolean, thus we do categorical.
						concreteReference = new FilterdEventAttrCategoricalConfig(log, filterType, k);
						break;
					case "Continuous":
						// Continuous, thus we do numerical.
						concreteReference = new FilterdEventAttrNumericalConfig(log, filterType, k);
						break;
					case "Discrete":
						// Discrete, thus we do numerical.
						concreteReference = new FilterdEventAttrNumericalConfig(log, filterType, k);
						break;
					case "ID":
						// ID, thus we do categorical.
						concreteReference = new FilterdEventAttrCategoricalConfig(log, filterType, k);
						break;
					case "Timestamp":
						// Timestamp, thus we do date.
						concreteReference = new FilterdEventAttrDateConfig(log, filterType);
						break;
					default: concreteReference = new FilterdEventAttrCategoricalConfig(log, filterType, k);
					break;
				}
			}
		}
	}

	/**
	 * Check if the parameters are still valid on the candidate log.
	 * 
	 * @param log the log to check.
	 */
	public boolean checkValidity(XLog log) {
		if (concreteReference == null) return true;
		return concreteReference.checkValidity(log);
	}
}
