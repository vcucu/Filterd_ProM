package org.processmining.filterd.gui;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.framework.plugin.ProMCanceller;

/**
 * This abstract adapter allows for setting static variables that are needed by
 * the constructors of the models in the notebook. These variables should be set
 * before loading a notebook.
 * 
 * @param <BoundType>
 *            The type that JAXB doesn't know how to handle. An adapter is
 *            written to allow this type to be used as an in-memory
 *            representation through the <tt>ValueType</tt>.
 * @param <ValueType>
 *            The type that JAXB knows how to handle out of the box.
 */
public abstract class AbstractJAXBAdapter<ValueType, BoundType> extends XmlAdapter<ValueType, BoundType> {

	// The context that should be passed to the created classes.
	public static UIPluginContext staticPromContext;
	// The canceller that should be passed to the created classes.
	public static ProMCanceller staticPromCanceller;
	// The initial log that should be passed to the created classes.
	public static XLog staticInitialInput;

	/**
	 * Returns the static context.
	 * 
	 * @return the static prom context.
	 */
	public static UIPluginContext getContext() {
		return staticPromContext;
	}

	/**
	 * Sets the static context.
	 * 
	 * @param context
	 *            the context to set.
	 */
	public static void setContext(UIPluginContext context) {
		staticPromContext = context;
	}

	/**
	 * Returns the static ProMCanceller.
	 * 
	 * @return the canceller.
	 */
	public static ProMCanceller getPromCanceller() {
		return staticPromCanceller;
	}

	/**
	 * Sets the static canceller.
	 * 
	 * @param canceller
	 *            the canceller to set.
	 */
	public static void setCanceller(ProMCanceller canceller) {
		staticPromCanceller = canceller;
	}

	/**
	 * Returns the initial input.
	 * 
	 * @return The initial input XLog.
	 */
	public static XLog getInitialInput() {
		return staticInitialInput;
	}

	/**
	 * Sets the static initial input.
	 * 
	 * @param log
	 *            the initial input log.
	 */
	public static void setInitialInput(XLog log) {
		staticInitialInput = log;
	}

}
