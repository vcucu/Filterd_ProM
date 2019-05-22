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

	public FilterdTraceAttrCategoricalConfig(XLog log, Filter filterType) {
		super(log, filterType);
		//initialize the configuration's parameters list
		parameters = new ArrayList<>();
				
		ParameterYesNo nullHandling = new ParameterYesNo("nullHandling",
				"Null handling:",
				true);
		
		List<String> selectionTypeOptions = new ArrayList<>();
		selectionTypeOptions.add("mandatory");
		selectionTypeOptions.add("forbidden");
		
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType",
			"Selection type:",
			"mandatory",
			selectionTypeOptions);
		
		
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
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		// TODO Auto-generated method stub
		return false;
	}

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController("Categorical Trace Attribute Configuration", parameters);
	}

	public boolean checkValidity(XLog log) {
		// TODO Auto-generated method stub
		return false;
	}

	public XLog filter() {
		// TODO Auto-generated method stub
		return null;
	}

}
