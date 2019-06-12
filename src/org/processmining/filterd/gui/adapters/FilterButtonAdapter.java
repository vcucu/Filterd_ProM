package org.processmining.filterd.gui.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.processmining.filterd.gui.FilterButtonModel;

public class FilterButtonAdapter extends XmlAdapter<FilterButtonAdapted, FilterButtonModel> {

	public FilterButtonModel unmarshal(FilterButtonAdapted adaptedModel) throws Exception {
		FilterButtonModel model = new FilterButtonModel(adaptedModel.getIndex());
		model.setName(adaptedModel.getName());
		// model.setFilterConfig(adaptedModel.getFilterConfig()); TODO: uncomment when an adapted exists for filterd abstract config
		return model;
	}

	public FilterButtonAdapted marshal(FilterButtonModel model) throws Exception {
		FilterButtonAdapted adaptedModel = new FilterButtonAdapted();
		adaptedModel.setIndex(model.getIndex());
		adaptedModel.setName(model.getName());
		adaptedModel.setFilterAbstractConfig(model.getFilterConfig());
		return adaptedModel;
	}
	
}
