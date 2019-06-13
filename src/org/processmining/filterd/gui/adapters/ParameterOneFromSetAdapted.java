package org.processmining.filterd.gui.adapters;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParameterOneFromSetAdapted extends ParameterAdapted {

	private String defaultChoice;
	private String chosen;
	private List<String> options;
	private boolean createsReference;

	@XmlElement
	public String getDefaultChoice() {
		return defaultChoice;
	}

	public void setDefaultChoice(String defaultChoice) {
		this.defaultChoice = defaultChoice;
	}

	public String getChosen() {
		return chosen;
	}

	public void setChosen(String chosen) {
		this.chosen = chosen;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public boolean isCreatesReference() {
		return createsReference;
	}

	public void setCreatesReference(boolean createsReference) {
		this.createsReference = createsReference;
	}

}
