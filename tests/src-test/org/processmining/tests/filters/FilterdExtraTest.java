package org.processmining.tests.filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.filterd.filters.FilterdEventAttrFilter;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;

public class FilterdExtraTest extends FilterdPackageTest{

	public FilterdExtraTest() throws Exception {
		super();
	}
	
	@Test
	public void eventAttribFilter() throws Exception {
		
		Boolean result = true;
		XLog expected = parseLog("event-attribute", "test_attribute_types.xes");
		ArrayList<Parameter> parameters = new ArrayList<>();
		
		List empty = Collections.EMPTY_LIST;
		
		ParameterOneFromSet literal = new ParameterOneFromSet("attribute", "", "", empty);
		literal.setChosen("concept:name");
		ParameterMultipleFromSet litValues = new ParameterMultipleFromSet("desiredValues", "", empty, empty);
		ArrayList<String> chosen = new ArrayList<>();
		chosen.add("System");
		litValues.setChosen(chosen);
		parameters.add(litValues);
		
		FilterdEventAttrFilter filter = new FilterdEventAttrFilter();
		XLog computed = filter.filter(originalLog, parameters);
		
		if(!equalLog(expected,computed)) result = false;
		
		ParameterOneFromSet bool = new ParameterOneFromSet("attribute", "", "", empty);
		bool.setChosen("bool");
		
		ParameterOneFromSet cost = new ParameterOneFromSet("attribute", "", "", empty);
		cost.setChosen("cost:total");
		
		ParameterOneFromSet id = new ParameterOneFromSet("attribute", "", "", empty);
		id.setChosen("identity:id");
		
		ParameterOneFromSet time = new ParameterOneFromSet("attribute", "", "", empty);
		id.setChosen("time:timestamp");
				
		assert result = true;
	}

}
