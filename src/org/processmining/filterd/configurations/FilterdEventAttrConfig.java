package org.processmining.filterd.configurations;

import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.widgets.ParameterOneFromSetController;
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

		
		//create reference to the dateConfig
		concreteReference = new FilterdEventAttrDateConfig(log, filterType);
		
		//not needed for date, kept for generality
		parameters.add(attribute);
		parameters.addAll(concreteReference.getParameters());
		
		
	}

	public FilterdAbstractConfig populate(AbstractFilterConfigPanelController component) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean canPopulate(FilterConfigPanelController component) {
		// TODO Auto-generated method stub
		return false;
	}

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController("Event Attribute Configuration", parameters);
	}
	
	public FilterdAbstractConfig changeReference(ParameterOneFromSetController chosen) {
		// TODO Auto-generated method stub
		return null;
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
