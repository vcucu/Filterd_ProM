package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterYesNo;

public class FilterdTraceStartEventCategoricalConfig extends FilterdAbstractConfig {

	public FilterdTraceStartEventCategoricalConfig(XLog log, Filter filterType, String attribute) {
		super(log, filterType);
		parameters = new ArrayList<Parameter>();
		
		// Create list of all values for the selected attribute
		List<String> allValues = new ArrayList<>();
		for (XTrace trace : log) {
			for (XEvent event : trace) {
				String value = event.getAttributes().get(attribute).toString();
				if (!allValues.contains(value)) {
					allValues.add(value);
				}
			}
		}
				
		// Create desiredEvents parameter	
		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet("desiredEvents",
				"Select values", allValues, allValues);
		
		//Create nullHandling parameter
		ParameterYesNo nullHandling = new ParameterYesNo("nullHandling", 
				"Remove if no value provided", true);
		
		parameters.add(desiredEvents);
		parameters.add(nullHandling);
	}

	public FilterdAbstractConfig populate(JComponent component) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean canPopulate(JComponent component) {
		// TODO Auto-generated method stub
		return false;
	}

	public JComponent getConfigPanel() {
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
