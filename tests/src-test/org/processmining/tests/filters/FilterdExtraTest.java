package org.processmining.tests.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.filters.FilterdEventAttrFilter;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;

public class FilterdExtraTest extends FilterdPackageTest{

	public FilterdExtraTest() throws Exception {
		super();
	}
	
	@Test
	public void testEventAttribFilter() throws Exception {
		
		Boolean result = true;
		XLog expected = parseLog("event-attribute", "test_attribute_types.xes");
		ArrayList<Parameter> parameters = new ArrayList<>();
		FilterdEventAttrFilter filter;
		
		List empty = Collections.EMPTY_LIST;
		ParameterOneFromSet selectionType = new ParameterOneFromSet("selectionType", "", "", empty); 
		selectionType.setChosen("Filter out");
		parameters.add(selectionType);
		
		/* remove empty traces */ 
		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", "", true);
		traceHandling.setChosen(false);
		parameters.add(traceHandling);
		
		ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", "", true);
		eventHandling.setChosen(false);
		parameters.add(eventHandling);
		
		ParameterOneFromSet literal = new ParameterOneFromSet("attribute", "", "", empty);
		literal.setChosen("concept:name");
		parameters.add(literal);
		ParameterMultipleFromSet litValues = new ParameterMultipleFromSet("desiredValues", "", empty, empty);
		ArrayList<String> chosen = new ArrayList<>();
		chosen.add("System");
		litValues.setChosen(chosen);
		parameters.add(litValues);
		
		filter = new FilterdEventAttrFilter();
		XLog computed = filter.filter(expected, parameters);
		
		if(!equalLog(expected,computed)) result = false;
		parameters.remove(litValues);
		parameters.remove(literal);

		
		
		ParameterOneFromSet bool = new ParameterOneFromSet("attribute", "", "", empty);
		bool.setChosen("bool");
		parameters.add(bool);
		ParameterMultipleFromSet boolValues = new ParameterMultipleFromSet("desiredValues", "", empty, empty);
		ArrayList<String> chosenBool = new ArrayList<>();
		chosenBool.add("true");
		boolValues.setChosen(chosenBool);
		parameters.add(boolValues);
		
		filter = new FilterdEventAttrFilter();
		computed= filter.filter(expected, parameters);
				
		if(!equalLog(expected,computed)) result = false;
		parameters.remove(bool);
		parameters.remove(boolValues);
		
		ParameterOneFromSet cost = new ParameterOneFromSet("attribute", "", "", empty);
		cost.setChosen("cost:total");
		parameters.add(cost);
		ParameterMultipleFromSet costValues = new ParameterMultipleFromSet("desiredValues", "", empty, empty);
		ArrayList<String> chosenCost = new ArrayList<>();
		chosenCost.add("3");
		costValues.setChosen(chosenCost);
		parameters.add(costValues);
		ParameterOneFromSet parameterType = new ParameterOneFromSet ("parameterType","","",empty);
		parameterType.setChosen("");
		parameters.add(parameterType);
		ParameterRangeFromRange<Double> rangeCost = new ParameterRangeFromRange<Double>("range","", Arrays.asList(3.), Arrays.asList(3.));
		rangeCost.setChosenPair(Arrays.asList(3.,3.));
		parameters.add(rangeCost);
		filter = new FilterdEventAttrFilter();
		computed= filter.filter(expected, parameters);
				
		if(!equalLog(expected,computed)) result = false;
		
		parameters.remove(rangeCost);
		parameters.remove(costValues);
		parameters.remove(cost);
		parameters.remove(parameterType);
		
		
		ParameterOneFromSet id = new ParameterOneFromSet("attribute", "", "", empty);
		id.setChosen("identity:id");
		ParameterMultipleFromSet idValues = new ParameterMultipleFromSet("desiredValues", "", empty, empty);
		ArrayList<String> chosenId = new ArrayList<>();
		chosenId.add("309b758b-68f2-4844-a202-c5a9f364b70a");
		idValues.setChosen(chosenId);
		parameters.add(id);
		parameters.add(idValues);
		filter = new FilterdEventAttrFilter();
		computed= filter.filter(expected, parameters);
				
		if(!equalLog(expected,computed)) result = false;
		parameters.remove(id);
		parameters.remove(idValues);
		
		ParameterOneFromSet time = new ParameterOneFromSet("attribute", "", "", empty);
		time.setChosen("time:timestamp");
		ParameterRangeFromRange<Integer> rangeTime = new ParameterRangeFromRange<Integer>(
				"time-range","", Arrays.asList(0),  Arrays.asList(0));
		rangeTime.setChosenPair(Arrays.asList(0,0));
		parameters.add(time);
		parameters.add(rangeTime);
		filter = new FilterdEventAttrFilter();
		computed= filter.filter(expected, parameters);
				
		if(!equalLog(expected,computed)) result = false;
		parameters.remove(time);
		parameters.remove(rangeTime);
		
		ParameterOneFromSet discrete = new ParameterOneFromSet("attribute", "", "", empty);
		discrete.setChosen("discrete");
		ParameterMultipleFromSet discreteValues = new ParameterMultipleFromSet("desiredValues", "", empty, empty);
		discreteValues.setChosen(Arrays.asList("3"));
		parameters.add(discrete);
		parameters.add(discreteValues);
		ParameterOneFromSet parameterTypeDiscrete = new ParameterOneFromSet ("parameterType","","",empty);
		parameterTypeDiscrete.setChosen("");
		parameters.add(parameterTypeDiscrete);
		filter = new FilterdEventAttrFilter();
		ParameterRangeFromRange<Double> rangeDiscrete = new ParameterRangeFromRange<Double>("range","", Arrays.asList(3.), Arrays.asList(3.));
		rangeDiscrete.setChosenPair(Arrays.asList(3.,3.));
		parameters.add(rangeDiscrete);
		computed= filter.filter(expected, parameters);
				
		if(!equalLog(expected,computed)) result = false;
		
		assert result = true;
	}

}
