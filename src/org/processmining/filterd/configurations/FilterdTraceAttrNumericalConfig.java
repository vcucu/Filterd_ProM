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
		
		//initialize the parameters list
		parameters = new ArrayList<>();
		
		//initialize the range as a list
		List<Double> listRange = new ArrayList<Double>();
		
		//initialize the minimum and maximum values
		double minValue = -Double.MAX_VALUE;
		double maxValue = Double.MAX_VALUE;
		
		//get the minimum and maximum values of the desired attribute
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
		
		//make the range span from the min value to the max value
		
		listRange.add(minValue);
		listRange.add(maxValue);
		
		//create the range parameter
		ParameterRangeFromRange<Double> range = 
				new ParameterRangeFromRange<Double>("range", 
						"Range", 
						listRange, 
						listRange); 
		
		//create the selection options parameter 
		List<String> selectionOptions = new ArrayList<>();
		
		selectionOptions.add("Mandatory");
		selectionOptions.add("Forbidden");
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType",
				"Selection type:",
				"Mandatory",
				selectionOptions);
		
		//add the parameters
		parameters.add(range);
		parameters.add(selectionType);
				
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		// TODO Auto-generated method stub
		return true;
	}

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController("Numerical Trace Attribute Configuration", parameters, this);
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
