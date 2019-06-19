package org.processmining.filterd.gui.adapters;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.configurations.FilterdAbstractReferenceableConfig;
import org.processmining.filterd.configurations.FilterdAbstractReferencingConfig;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.parameters.Parameter;

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
			// if the static initialInput is not set, throw an exception.
			throw new IllegalStateException("org.processmining.filterd.gui.adapters.FilterdAbstractConfigAdapter.unmarshal: static variable initialInput was not set.");
		}
		FilterdAbstractConfig config; // initialize the config.
		List<Parameter> parameters = new ArrayList<Parameter>(); // initialize the parameter list.
		parameters.addAll(adaptedConfig.getParameters()); // add the parameters from the adapted config to the parameters for the new config.
		Class<FilterdAbstractConfig> configClass = (Class<FilterdAbstractConfig>) Class.forName(adaptedConfig.getClassName()); // get the config class based on the class name stored in the adapted config.
		Filter filterType =  (Filter) Class.forName(adaptedConfig.getfilterTypeName()).newInstance(); // create an instance of a Filter of a type based on the filter type name stored in the adapted config.
		config = configClass.getDeclaredConstructor(XLog.class, Filter.class).newInstance(initialInput, filterType); // get the constructor from the config class that takes an XLog and Filter as input and use it to instantiate the config.
		if (config instanceof FilterdAbstractReferencingConfig) {
			// if the new config is referencing, create and set the concrete reference.
			FilterdAbstractReferenceableConfig concreteReference = ((FilterdAbstractReferencingConfigAdapted) adaptedConfig).getConcreteReference(); // get the concrete reference from the adapted config.
			((FilterdAbstractReferencingConfig) config).setConcreteReference(concreteReference); // set the concrete reference in the new config.
			parameters.addAll(concreteReference.getParameters()); // add all parameters from the concrete reference to the parameters for the referencing config. (This is required for the filters to work.)
			// Note: we add remove the concrete reference parameters from the referencing config during saving and add them back during loading because they have to be references to the SAME OBJECTS.
			// If we would not do this identical parameters will be created in the concrete reference and referencing config but they will not be the same objects.
		}
		config.setParameters(parameters); // set the parameters for the new config.
		return config;
	}

	public FilterdAbstractConfigAdapted marshal(FilterdAbstractConfig config) {
		FilterdAbstractConfigAdapted adaptedConfig; // initialize the adapted config.
		List<Parameter> parameters = new ArrayList<Parameter>(); // initialize the parameters list. This needs to be a new ArrayList since we do not want to modify the original.
		parameters.addAll(config.getParameters()); // add all the parameters from the config to the new list.
		
		if (config instanceof FilterdAbstractReferencingConfig) {
			// if the config is referencing
			adaptedConfig = new FilterdAbstractReferencingConfigAdapted(); // create the new adapted config.
			FilterdAbstractReferenceableConfig concreteReference = ((FilterdAbstractReferencingConfig) config).getConcreteReference(); // get the concrete reference from the referencing config.
			((FilterdAbstractReferencingConfigAdapted) adaptedConfig).setConcreteReference(concreteReference); // set the concrete reference in the adapted config.
			parameters.removeAll(concreteReference.getParameters()); // remove the parameters from the referencing config that are duplicates from the concrete reference parameters. This is required for reconstruction
			// Note: we add remove the concrete reference parameters from the referencing config during saving and add them back during loading because they have to be references to the SAME OBJECTS.
			// If we would not do this identical parameters will be created in the concrete reference and referencing config but they will not be the same objects.
		} else {
			// if the config is not referencing can create a general one.
			adaptedConfig = new FilterdAbstractConfigAdapted(); // create a new adapted config.
		}

		adaptedConfig.setClassName(config.getClass().getName()); // set the name of the config class name in the adapted config.
		adaptedConfig.setfilterTypeName(config.getFilterType().getClass().getName()); // set the name of the filter type in the adapted config.
		adaptedConfig.setParameters(parameters); // set the parameters in the adapted config.
		return adaptedConfig;
	}

	public static XLog getInitialInput() {
		return initialInput;
	}

	public static void setInitialInput(XLog log) {
		initialInput = log;
	}

}
