package org.processmining.filterd.gui.adapters;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.configurations.FilterdAbstractReferenceableConfig;
import org.processmining.filterd.configurations.FilterdAbstractReferencingConfig;
import org.processmining.filterd.filters.Filter;

public class FilterdAbstractConfigAdapter extends XmlAdapter<FilterdAbstractConfigAdapted, FilterdAbstractConfig> {

	private static XLog initialInput;

	/**
	 * Unmarshals an FilterdAbstractConfigAdapted into a corresponding FilterdAbstractConfig.
	 * @param adaptedConfig The FilterdAbstractConfigAdapted transform into a FilterdAbstractConfig.
	 * @throws IllegalStateException if static initialInput was not set before calling unmarshal.
	 */
	public FilterdAbstractConfig unmarshal(FilterdAbstractConfigAdapted adaptedConfig) throws IllegalStateException {
		if (initialInput == null) {
			throw new IllegalStateException("org.processmining.filterd.gui.adapters.FilterdAbstractConfigAdapter.unmarshal: static variable initialInput was not set.");
		}
		
		try {
			// create the new object of the right type based on the class name.
			FilterdAbstractConfig config;
			Class<FilterdAbstractConfig> cls = (Class<FilterdAbstractConfig>) Class.forName(adaptedConfig.getClassName());
			if (FilterdAbstractReferenceableConfig.class.isAssignableFrom(cls.getClass())) {
				//TODO: replace nulls
				config = cls.getDeclaredConstructor(XLog.class, Filter.class, String.class, List.class).newInstance(initialInput ,null,null,null);
			} else {
				//TODO: replace nulls
				config = cls.getDeclaredConstructor(XLog.class, Filter.class).newInstance(initialInput,null);
			}
			
			config.setParameters(config.getParameters());
			
			if (FilterdAbstractReferencingConfig.class.isAssignableFrom(config.getClass())) {
				// set the concrete reference if the config is referencing.
				((FilterdAbstractReferencingConfig) config).setConcreteReference(
						((FilterdAbstractConfigReferencingAdapted) adaptedConfig).getConcreteReference());
			}

			return config;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException e) {
			System.out.println("##################### class name: " + adaptedConfig.getClassName());
			e.printStackTrace();
			return null;
		}
	}

	public FilterdAbstractConfigAdapted marshal(FilterdAbstractConfig config) {
		FilterdAbstractConfigAdapted adaptedConfig;

		if (FilterdAbstractReferencingConfig.class.isAssignableFrom(config.getClass())) {
			// if the config is referencing save the concrete reference.
			adaptedConfig = new FilterdAbstractConfigReferencingAdapted();
			((FilterdAbstractConfigReferencingAdapted) adaptedConfig)
					.setConcreteReference(((FilterdAbstractReferencingConfig) config).getConcreteReference());
		} else {
			// if the config is not referencing can create a general one.
			adaptedConfig = new FilterdAbstractConfigAdapted();
		}

		adaptedConfig.setClassName(config.getClass().getName());
		adaptedConfig.setParameters(config.getParameters());
		return adaptedConfig;
	}

	public static XLog getInitialInput(XLog log) {
		return initialInput;
	}

	public static void setInitialInput(XLog log) {
		initialInput = log;
	}

}
