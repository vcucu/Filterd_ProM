package org.processmining.filterd.gui.adapters;

import java.lang.reflect.InvocationTargetException;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.configurations.FilterdAbstractReferencingConfig;
import org.processmining.filterd.filters.Filter;

public class FilterdAbstractConfigAdapter extends XmlAdapter<FilterdAbstractConfigAdapted, FilterdAbstractConfig> {

	private static XLog initialInput;

	/**
	 * Unmarshals an FilterdAbstractConfigAdapted into a corresponding FilterdAbstractConfig.
	 * @param adaptedConfig The FilterdAbstractConfigAdapted transform into a FilterdAbstractConfig.
	 * @throws IllegalStateException if static initialInput was not set before calling unmarshal.
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	public FilterdAbstractConfig unmarshal(FilterdAbstractConfigAdapted adaptedConfig) throws IllegalStateException, InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if (initialInput == null) {
			throw new IllegalStateException("org.processmining.filterd.gui.adapters.FilterdAbstractConfigAdapter.unmarshal: static variable initialInput was not set.");
		}
		
		try {
			// create the new object of the right type based on the class name.
			FilterdAbstractConfig config;
			Class<FilterdAbstractConfig> configClass = (Class<FilterdAbstractConfig>) Class.forName(adaptedConfig.getClassName());
			Filter filterType =  (Filter) Class.forName(adaptedConfig.getfilterTypeName()).newInstance();
			config = configClass.getDeclaredConstructor(XLog.class, Filter.class).newInstance(initialInput, filterType);
			
			if (FilterdAbstractReferencingConfig.class.isAssignableFrom(config.getClass())) {
				// set the concrete reference if the config is referencing.
				((FilterdAbstractReferencingConfig) config).setConcreteReference(
						((FilterdAbstractReferencingConfigAdapted) adaptedConfig).getConcreteReference());
			}
			
			config.setParameters(adaptedConfig.getParameters());
			return config;
		//} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException e) {
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public FilterdAbstractConfigAdapted marshal(FilterdAbstractConfig config) {
		FilterdAbstractConfigAdapted adaptedConfig;

		if (FilterdAbstractReferencingConfig.class.isAssignableFrom(config.getClass())) {
			// if the config is referencing save the concrete reference.
			adaptedConfig = new FilterdAbstractReferencingConfigAdapted();
			((FilterdAbstractReferencingConfigAdapted) adaptedConfig)
					.setConcreteReference(((FilterdAbstractReferencingConfig) config).getConcreteReference());
		} else {
			// if the config is not referencing can create a general one.
			adaptedConfig = new FilterdAbstractConfigAdapted();
		}

		adaptedConfig.setClassName(config.getClass().getName());
		adaptedConfig.setfilterTypeName(config.getFilterType().getClass().getName());
		adaptedConfig.setParameters(config.getParameters());
		return adaptedConfig;
	}

	public static XLog getInitialInput() {
		return initialInput;
	}

	public static void setInitialInput(XLog log) {
		initialInput = log;
	}

}
