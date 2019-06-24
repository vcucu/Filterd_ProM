package org.processmining.filterd.gui.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.processmining.filterd.gui.FilterButtonModel;

/**
 * Class used to marshal (export) and unmarshal (import) the FilterButtonModel
 * class. Lower-level classes, like filter configurations, are marshaled and
 * unmarshaled automatically by JAXB.
 */
public class FilterButtonAdapter extends XmlAdapter<FilterButtonAdapted, FilterButtonModel> {

	/**
	 * Method used by JAXB to import and serialize a filter button model.
	 * 
	 * @param adaptedModel
	 *            adapted model that is to be serialized into a filter button
	 *            model
	 * @return serialized filter button model
	 */
	public FilterButtonModel unmarshal(FilterButtonAdapted adaptedModel) throws Exception {
		FilterButtonModel model = new FilterButtonModel(adaptedModel.getIndex());
		model.setName(adaptedModel.getName()); // set the name of the filter button model
		model.setFilterConfig(adaptedModel.getFilterConfig()); // set the associated filter configuration of the filter button model

		return model;
	}

	/**
	 * Method used by JAXB to deserialize a filter button model into an adapted
	 * filter button model.
	 * 
	 * @param model
	 *            cell model that is to be deserialized
	 * @return deserialized filter button model i.e. adapted filter button
	 */
	public FilterButtonAdapted marshal(FilterButtonModel model) throws Exception {
		FilterButtonAdapted adaptedModel = new FilterButtonAdapted();
		adaptedModel.setIndex(model.getIndex());
		adaptedModel.setName(model.getName());
		adaptedModel.setFilterConfig(model.getFilterConfig());
		return adaptedModel;
	}

}
