package org.processmining.filterd.configurations;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;

public class FilterdTraceAttrNumericalConfig extends FilterdAbstractConfig {

	public FilterdTraceAttrNumericalConfig(XLog log, Filter filterType,
			String attribute) {
		super(log, filterType);
		parameters = new ArrayList<>();
		List<Double> listRange = new ArrayList<Double>();
		double minValue = -Double.MAX_VALUE;
		double maxValue = Double.MAX_VALUE;
		
		for (XTrace trace : log) {
			for (XEvent event : trace) {
				String attrValueString = 
						event.getAttributes().get(attribute).toString();
				if (attrValueString != null) {
					double attrValue = Double.parseDouble(attrValueString);
					if (attrValue < minValue) {
						minValue = attrValue;
					}
					if (maxValue < attrValue) {
						maxValue = attrValue;					}
				}
			}
		}
		
		listRange.add(minValue);
		listRange.add(maxValue);
		ParameterRangeFromRange<Double> range = 
				new ParameterRangeFromRange<Double>("range", 
						"Range", 
						listRange, 
						listRange); 
		
		List<String> selectionOptions = new ArrayList<>();
		
		selectionOptions.add("Mandatory");
		selectionOptions.add("Forbidden");
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType",
				"Selection type:",
				"Mandatory",
				selectionOptions);
		
		parameters.add(range);
		parameters.add(selectionType);
				
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		// TODO Auto-generated method stub
		return false;
	}

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController("Numerical Trace Attribute Configuration", parameters);
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
