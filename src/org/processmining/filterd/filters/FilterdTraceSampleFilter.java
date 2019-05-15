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
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.framework.plugin.PluginContext;

public class FilterdTraceSampleFilter extends Filter {

	public XLog filter(PluginContext context, XLog log, List<Parameter> parameters) {
	
		//get value of threshold parameter (i. e. the desired number of samples)
		ParameterValueFromRange nrSamples = (ParameterValueFromRange) this.getParameter(parameters, "threshold");
		
		Random rand = new Random();
		
		//initialize the log that will be output
		XFactory factory = XFactoryRegistry.instance().currentDefault();
		XLog filteredLog = factory.createLog((XAttributeMap) log.getAttributes().clone());
		filteredLog.getClassifiers().addAll(log.getClassifiers());
		filteredLog.getExtensions().addAll(log.getExtensions());
		filteredLog.getGlobalTraceAttributes().addAll(log.getGlobalTraceAttributes());
		filteredLog.getGlobalEventAttributes().addAll(log.getGlobalEventAttributes());
		
		//list of the randomly generated unique trace keys 
		List<Integer> tracesAdded = new ArrayList<>(); 
		
		//while we haven't sampled enough traces
		while(tracesAdded.size() < (int) nrSamples.getChosen()) {
			//generate random trace key
			int random = rand.nextInt(log.size());
			
			//if the trace hasn't been added before, add it to the output log and 
			//add its key to the list of generated keys 	
			if (!tracesAdded.contains(random)) {
				tracesAdded.add(random);
				XTrace sampleTrace = factory.createTrace(log.get(random).getAttributes());
				filteredLog.add(sampleTrace);			
			}
		}
		return filteredLog;
	}

}
