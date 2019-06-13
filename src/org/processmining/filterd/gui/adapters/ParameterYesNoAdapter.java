package org.processmining.filterd.gui.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.processmining.filterd.parameters.ParameterYesNo;

public class ParameterYesNoAdapter extends XmlAdapter<ParameterYesNoAdapted, ParameterYesNo> {

	public ParameterYesNo unmarshal(ParameterYesNoAdapted v) throws Exception {
		ParameterYesNo parameter = new ParameterYesNo(v.getName(), v.getNameDisplayed(),
				v.getDefaultChoice());
		parameter.setChosen(v.getChosen());
		return parameter;
	}

	public ParameterYesNoAdapted marshal(ParameterYesNo v) throws Exception {
		ParameterYesNoAdapted adapted = new ParameterYesNoAdapted();
		// parameter adapted
		adapted.setName(v.getName());
		adapted.setNameDisplayed(v.getNameDisplayed());
		adapted.setExplanation(v.getExplanation());
		// parameter yes no
		adapted.setChosen(v.getChosen());
		adapted.setDefaultChoice(v.getDefaultChoice());

		return adapted;
	}

}
