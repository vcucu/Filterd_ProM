package org.processmining.filterd.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterText;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.framework.plugin.PluginContext;

public class FilterdTraceSampleFilter extends Filter {

	public XLog filter(PluginContext context, XLog log, List<Parameter> parameters) {
	
		ParameterValueFromRange nrSamples = (ParameterValueFromRange) this.getParameter(parameters, "threshold");
		Random rand = new Random();
		
		XFactory factory = XFactoryRegistry.instance().currentDefault();
		XLog filteredLog = factory.createLog((XAttributeMap) log.getAttributes().clone());
		filteredLog.getClassifiers().addAll(log.getClassifiers());
		filteredLog.getExtensions().addAll(log.getExtensions());
		filteredLog.getGlobalTraceAttributes().addAll(log.getGlobalTraceAttributes());
		filteredLog.getGlobalEventAttributes().addAll(log.getGlobalEventAttributes());
		
		List<Integer> tracesAdded = new ArrayList<>(); 
		
		while(tracesAdded.size() < (int) nrSamples.getChosen()) {
			int random = rand.nextInt(log.size());
			if (!tracesAdded.contains(random)) {
				tracesAdded.add(random);
				XTrace sampleTrace = factory.createTrace(log.get(random).getAttributes());
				filteredLog.add(sampleTrace);			
			}
		}
		return filteredLog;
	}

}
