package org.processmining.filterd.wizard;

import javax.swing.JList;
import javax.swing.ListModel;

import org.processmining.framework.util.ui.widgets.ProMList;

public class FilterdList extends ProMList {
	
	private static final long serialVersionUID = 3001797916791865535L;
	
	private JList<String> jList;

	public FilterdList(String title) {
		super(title);
		jList = new JList<String>();
	}
	
	public FilterdList(String title, ListModel<String> model) {
		super(title, model);
		jList = new JList<String>();
	}
	
	public void setOptions(ListModel<String> model) {
		jList = new JList<String>(model);
	}

}
