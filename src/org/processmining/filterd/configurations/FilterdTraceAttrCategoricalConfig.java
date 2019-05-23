package org.processmining.filterd.configurations;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterYesNo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;

public class FilterdTraceAttrCategoricalConfig extends FilterdAbstractConfig {

	public FilterdTraceAttrCategoricalConfig(XLog log, Filter filterType, String
			attribute) {
		super(log, filterType);
		
		// Initialize the configuration's parameters list.
		parameters = new ArrayList<>();
				
		ParameterYesNo nullHandling = new ParameterYesNo("nullHandling",
				"Null handling:",
				true);
		
		//initialize selection type parameter
		List<String> selectionTypeOptions = new ArrayList<>();
		selectionTypeOptions.add("Mandatory");
		selectionTypeOptions.add("Forbidden");
		
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType",
			"Selection type:",
			"Mandatory",
			selectionTypeOptions);
		
		
		//initialize the values parameter for the chosen attribute
		Set<String> valuesSet = new HashSet<String>();
		
		for (XTrace trace : log) {
			for (XEvent event : trace) {
				if (event.getAttributes().get(attribute) != null) {
					valuesSet.add(event.getAttributes().get(attribute).toString());
				}
			}
		}
		List<String> valuesList = new ArrayList<>(valuesSet);
		List<String> defaultChoice = new ArrayList<>();
		defaultChoice.add(valuesList.get(0));
		ParameterMultipleFromSet desiredValues = new ParameterMultipleFromSet(
				"desiredValues",
				"Desired values:",
				defaultChoice,
				valuesList
			);
		
		//add the created parameters
		parameters.add(nullHandling);
		parameters.add(selectionType);
		parameters.add(desiredValues);
		
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController("Categorical Trace Attribute Configuration", parameters, this);
	}

	public boolean checkValidity(XLog log) {
		return false;
	}

	public XLog filter() {
		// TODO Auto-generated method stub
		return null;
	}

}
