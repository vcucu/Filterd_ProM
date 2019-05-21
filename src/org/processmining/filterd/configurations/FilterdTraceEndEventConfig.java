package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XLog;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.widgets.ParameterOneFromSetController;

public class FilterdTraceEndEventConfig extends FilterdAbstractConfig {
	
	FilterdAbstractConfig concreteReference;

	public FilterdTraceEndEventConfig(XLog log, Filter filterType) {
		super(log, filterType);
		parameters = new ArrayList<Parameter>();
		complexClassifiers = new ArrayList<>();
		
		 // Get global attributes that are passed to the parameter 
		List<String> globalAttrAndClassifiers = this.computeGlobalAttributes(log);
		//add the complex classifiers to the list of global attributes 
		globalAttrAndClassifiers.addAll(computeComplexClassifiers(log));
		
		// Create attribute parameter 
		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", 
				"Filter by", globalAttrAndClassifiers.get(0), globalAttrAndClassifiers, true);

		
		
		//Create selectionType parameter
		List<String> selectionTypeOptions = new ArrayList<>(Arrays.asList("Filter in", "Filter out"));
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType",
				"Selection type", "Filter in", selectionTypeOptions);	
		
		// Create the default concrete reference
		concreteReference = new FilterdTraceEndEventCategoricalConfig(log, filterType, 
				globalAttrAndClassifiers.get(0), complexClassifiers);
		
		// Add all parameters to the list of parameters
		
		parameters.add(attribute);
		parameters.add(selectionType);
		parameters.addAll(concreteReference.getParameters());
	}
	
	public List<String> computeGlobalAttributes(XLog log) {
		List<String> globalAttr = new ArrayList<>();
		for (XAttribute attribute : log.getGlobalEventAttributes()) {
			globalAttr.add(attribute.getKey());
		}
		return globalAttr;
	}

	public FilterdAbstractConfig populate(FilterConfigPanelController component) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		// TODO Auto-generated method stub
		return false;
	}

	public FilterConfigPanelController getConfigPanel() {
		return new FilterConfigPanelController("Trace End Event Configuration", parameters);
	}
	
	public FilterdAbstractConfig changeReference(ParameterOneFromSetController chosen) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean checkValidity(XLog candidateLog) {
		List<String> globalAttrCandidateLog = new ArrayList<>();
		for (XAttribute attribute : candidateLog.getGlobalEventAttributes()) {
			globalAttrCandidateLog.add(attribute.getKey());
		}
		List<String> attrs = computeGlobalAttributes(this.getLog());
		String attr = attrs.get(0);
		if (!globalAttrCandidateLog.contains(attr)) {
			return false;
		}	
		return true;
	}

}
