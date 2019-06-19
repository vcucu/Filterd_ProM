package org.processmining.filterd.gui.adapters;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.processmining.filterd.configurations.FilterdAbstractReferenceableConfig;

@XmlRootElement
public class FilterdAbstractReferencingConfigAdapted extends FilterdAbstractConfigAdapted {

	// Reference to the referenceable configuration.
	FilterdAbstractReferenceableConfig concreteReference;
	
	@XmlJavaTypeAdapter(FilterdAbstractConfigReferenceableAdapter.class)
	/**
	 * Getter to return the concrete reference.
	 * 
	 * @return concreteReference.
	 */
	public FilterdAbstractReferenceableConfig getConcreteReference() {
		return concreteReference;
	}
	
	/**
	 * Setter for the concrete reference.
	 * 
	 * @param concreteReference the concrete reference to set it to.
	 */
	public void setConcreteReference(FilterdAbstractReferenceableConfig concreteReference) {
		this.concreteReference = concreteReference;
	}
}
