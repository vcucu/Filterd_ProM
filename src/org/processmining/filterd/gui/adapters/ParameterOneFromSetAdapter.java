package org.processmining.filterd.gui.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.processmining.filterd.parameters.ParameterOneFromSet;

public class ParameterOneFromSetAdapter extends XmlAdapter<ParameterOneFromSetAdapted, ParameterOneFromSet> {

	public ParameterOneFromSet unmarshal(ParameterOneFromSetAdapted v) throws Exception {
		ParameterOneFromSet parameter = new ParameterOneFromSet(v.getName(), v.getNameDisplayed(),
				v.getDefaultChoice(), v.getOptions(), v.isCreatesReference());
		parameter.setChosen(v.getChosen());
		return parameter;
	}

	public ParameterOneFromSetAdapted marshal(ParameterOneFromSet v) throws Exception {
		ParameterOneFromSetAdapted adapted = new ParameterOneFromSetAdapted();
		// parameter adapted
		adapted.setName(v.getName());
		adapted.setNameDisplayed(v.getNameDisplayed());
		adapted.setExplanation(v.getExplanation());
		// parameter one from set adapted
		adapted.setDefaultChoice(v.getDefaultChoice());
		adapted.setChosen(v.getChosen());
		adapted.setOptions(v.getOptions());
		adapted.setCreatesReference(v.getCreatesReference());

		return adapted;
	}

}
