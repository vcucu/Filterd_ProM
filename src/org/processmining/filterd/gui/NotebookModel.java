package org.processmining.filterd.gui;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIContext;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.hub.ProMResourceManager;
import org.processmining.contexts.uitopia.hub.ProMViewManager;

public class NotebookModel {
	
	private UIPluginContext context;
	private XLog log;
	
	private ProMViewManager vm; // Current view manager	
    private ProMResourceManager rm; // Current resource manager
    
    
    public NotebookModel(UIPluginContext context, XLog log) {
    	this.context = context;
    	this.log = log;

		// Get current view manager and resource manager
		UIContext globalContext = context.getGlobalContext();
    	vm = ProMViewManager.initialize(globalContext); 	
        rm = ProMResourceManager.initialize(globalContext);
    }
    
	public UIPluginContext getContext() {
		return context;
	}
	
	public XLog getLog() {
		return log;
	}
	
	public ProMViewManager getVm() {
		return vm;
	}
	
	public ProMResourceManager getRm() {
		return rm;
	}

}
