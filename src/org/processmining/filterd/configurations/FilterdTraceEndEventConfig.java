package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.filterd.widgets.ParameterOneFromSetExtendedController;

public class FilterdTraceEndEventConfig extends FilterdAbstractReferencingConfig {

	public FilterdTraceEndEventConfig(XLog log, Filter filterType) {
		super(log, filterType);
		parameters = new ArrayList<Parameter>();
		List<XEventClassifier>complexClassifiers = Toolbox.computeComplexClassifiers(log);
		XLog endEventsLog = endEventsOnly();
		// Get all the events attributes that are passed to the parameter 
		List<String> attrAndClassifiers = Toolbox.computeAttributes(endEventsLog);
		//add the complex classifiers to the list of global attributes 
		attrAndClassifiers.addAll(Toolbox.getClassifiersName(complexClassifiers));

		// Create attribute parameter 
		ParameterOneFromSet attribute = new ParameterOneFromSet("attribute", 
				"Filter by", attrAndClassifiers.get(0), attrAndClassifiers, true);


		//Create selectionType parameter
		List<String> selectionTypeOptions = new ArrayList<>(Arrays.asList("Filter in", "Filter out"));
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType",
				"Selection type", "Filter in", selectionTypeOptions);	

		// Create the default concrete reference
		concreteReference = new FilterdTraceEndEventCategoricalConfig(log, filterType, 
				attrAndClassifiers.get(0), complexClassifiers);

		// Add all parameters to the list of parameters

		parameters.add(attribute);
		parameters.add(selectionType);
		//parameters.addAll(concreteReference.getParameters());


	}

	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};

	@Override
	public AbstractFilterConfigPanelController getConfigPanel() {
		this.configPanel = new FilterConfigPanelController("Trace End Event Configuration", parameters, this);

		return configPanel;
	}

	
	/*
	 * The candidateLog is invalid if the event attributes list does not 
	 * contain the selected attribute 
	 * The candidateLog is invalid if the complex classifiers list does not
	 * contain the selected complex classifier
	 */
	@Override
	public boolean checkValidity(XLog candidateLog) {
		return ConfigurationToolbox.traceStartAndEndEventValidity(parameters, this, candidateLog);
	}

	/**
	 * Changes the content of the configuration according to the 
	 * selected attributes
	 */
	@Override
	public FilterdAbstractConfig changeReference(ParameterOneFromSetExtendedController controller) {
		for (Parameter param : concreteReference.getParameters()) {
			parameters.remove(param);
		}
		concreteReference = new FilterdTraceEndEventCategoricalConfig(
				log, filterType, controller.getValue(),Toolbox.computeComplexClassifiers(log));
		for (Parameter param : concreteReference.getParameters()) {
			parameters.add(param);
		}		
		return concreteReference;
	}
	
	/**
	 * Method which modifies the log such that it only contains 
	 * end events
	 * @return the log containing all initial traces, but with
	 * only one event.
	 */
	private XLog endEventsOnly() {
		XLog filteredLog = Toolbox.initializeLog(log);
		XFactory factory = XFactoryRegistry.instance().currentDefault();
		for (XTrace trace: this.log) {
			XTrace filteredTrace = factory.createTrace(trace.getAttributes());
			filteredTrace.add(trace.get(trace.size()-1));
			filteredLog.add(filteredTrace);
		}
		return filteredLog;
	}

}
