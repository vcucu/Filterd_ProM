package org.processmining.filterd.gui.adapters;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.processmining.filterd.configurations.FilterdAbstractReferenceableConfig;

@XmlRootElement
public class FilterdAbstractReferencingConfigAdapted extends FilterdAbstractConfigAdapted {

	FilterdAbstractReferenceableConfig concreteReference;
	
	@XmlJavaTypeAdapter(FilterdAbstractConfigAdapter.class)
	public FilterdAbstractReferenceableConfig getConcreteReference() {
		return concreteReference;
	}
	
	public void setConcreteReference(FilterdAbstractReferenceableConfig concreteReference) {
		this.concreteReference = concreteReference;
	}
}
