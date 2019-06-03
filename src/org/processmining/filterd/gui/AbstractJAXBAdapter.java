package org.processmining.filterd.gui;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.framework.plugin.ProMCanceller;

public abstract class AbstractJAXBAdapter<ValueType,BoundType> extends XmlAdapter<ValueType,BoundType>{

	public static UIPluginContext promContext;
	public static ProMCanceller promCanceller;
	public static XLog initialInput;
	
	public static void setContext(UIPluginContext context) {
		promContext = context;
	}
	
	public static void setCanceller(ProMCanceller canceller) {
		promCanceller = canceller;
	}
	
	public static void setInitialInput(XLog log) {
		initialInput = log;
	}
	
}
