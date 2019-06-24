package org.processmining.filterd.gui.adapters;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.configurations.FilterdAbstractReferenceableConfig;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.tools.Toolbox;

public class FilterdAbstractConfigReferenceableAdapter extends FilterdAbstractConfigAdapter {

	/**
	 * These lists of classes are used to identify different types of constructors in the FilterdAbstractConfigReferenceable classes.
	 */
	Class<?>[] typesKey = { XLog.class, Filter.class, String.class }; // constructor types for the key parameter
	Class<?>[] typesAttribute = { XLog.class, Filter.class, String.class, List.class }; // constructor types for the attribute parameter
	Class<?>[] typesRegular = { XLog.class, Filter.class }; // constructor types for normal referenceables

	/**
	 * Converts an adaptedConfig into a FilterdAbstractConfig.
	 */
	@Override
	public FilterdAbstractConfig unmarshal(FilterdAbstractConfigAdapted adaptedConfig)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		try {
			FilterdAbstractConfig config;
			// get the input log and filter.
			XLog initialInput = getInitialInput(); // get the initial input that is statically set in the FIlterdAbstractConfigAdapter.
			Filter filterType = (Filter) Class.forName(adaptedConfig.getfilterTypeName()).newInstance(); // get the config class based on the class name stored in the adapted config.
			Class<FilterdAbstractReferenceableConfig> configClass = (Class<FilterdAbstractReferenceableConfig>) Class
					.forName(adaptedConfig.getClassName()); // create an instance of a Filter of a type based on the filter type name stored in the adapted config.
			Constructor<FilterdAbstractReferenceableConfig> constructor; // declare the constructor variable for the instructor.
			// construct the config
			if (constructorPresent(configClass.getConstructors(), typesAttribute)) {
				// if the constructor takes an XLog, Filter, string (key) and a List (classifiers).
				constructor = configClass.getDeclaredConstructor(XLog.class, Filter.class, String.class, List.class); // get the constructor with the right parameters from the config class.
				config = constructor.newInstance( // instantiate the config using the constructor.
						initialInput,
						filterType,
						((FilterdAbstractConfigAdapted) adaptedConfig).getKey(),
						Toolbox.computeComplexClassifiers(initialInput));
			} else if (constructorPresent(configClass.getConstructors(), typesKey)) {
				// if the constructor takes an XLog, Filter, string (key).
				constructor = configClass.getDeclaredConstructor(XLog.class, Filter.class, String.class); // get the constructor with the right parameters from the config class.
				config = constructor.newInstance( // instantiate the config using the constructor.
						initialInput,
						filterType,
						((FilterdAbstractConfigAdapted) adaptedConfig).getKey());
			} else if (constructorPresent(configClass.getConstructors(), typesRegular)) {
				// if the constructor takes an XLog and Filter.
				constructor = configClass.getDeclaredConstructor(XLog.class, Filter.class); // get the constructor with the right parameters from the config class.
				config = constructor.newInstance(initialInput, filterType); // instantiate the config using the constructor.
			} else {
				// adapter has not been configured for this class.
				throw new IllegalStateException(
						"org.processmining.filterd.gui.adapters.FilterdAbstractConfigReferenceableAdapter.unmarshal(): Constructor parameters not supported for: "
								+ configClass.getCanonicalName());
			}

			// set the parameters.
			config.setParameters(adaptedConfig.getParameters());
			return config;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Converts an FilterdAbstractConfig into a adaptedConfig.
	 */
	@Override
	public FilterdAbstractConfigAdapted marshal(FilterdAbstractConfig uncastConfig) {
		FilterdAbstractReferenceableConfig config = (FilterdAbstractReferenceableConfig) uncastConfig;
		FilterdAbstractConfigAdapted adaptedConfig;

		if (constructorPresent(config.getClass().getConstructors(), typesAttribute)) {
			// configs that take an attribute as a constructor input.
			adaptedConfig = new FilterdAbstractConfigAdapted();
			((FilterdAbstractConfigAdapted) adaptedConfig).setKey(config.getKey());
		} else if (constructorPresent(config.getClass().getConstructors(), typesKey)) {
			// configs that take a key as a constructor input.
			adaptedConfig = new FilterdAbstractConfigAdapted();
			((FilterdAbstractConfigAdapted) adaptedConfig).setKey(config.getKey());
		} else if (constructorPresent(config.getClass().getConstructors(), typesRegular)) {
			adaptedConfig = super.marshal(uncastConfig);
		} else {
			// adapter has not been configured for this class.
			throw new IllegalStateException(
					"org.processmining.filterd.gui.adapters.FilterdAbstractConfigReferenceableAdapter.marshal(): Constructor parameters not supported for: "
							+ config.getClass().getCanonicalName());
		}

		// set generic variables.
		adaptedConfig.setClassName(config.getClass().getName());
		adaptedConfig.setfilterTypeName(config.getFilterType().getClass().getName());
		adaptedConfig.setParameters(config.getParameters());

		return adaptedConfig;
	}

	/**
	 * Checks if the list of constructors contains a constructor that takes the
	 * parameter types specified in types as input.
	 * 
	 * @param constructors
	 *            the array of constructors to check
	 * @param types
	 *            the array of types to check the constructors for
	 * @return true if a constructor exists in the construtors array with the
	 *         same parameter types as in the types array
	 */
	private boolean constructorPresent(Constructor<?>[] constructors, Class<?>[] types) {
		for (Constructor<?> constructor : constructors) {
			// for all constructors
			if (constructor.getParameterTypes().length == types.length) {
				//get the parameter types of this constructor
				boolean equal = true;
				for (int i = 0; i < types.length; i++) {
					//for each parameter type
					if (constructor.getParameterTypes()[i] != types[i]) {
						// check if the parameter type is equal to the corresponding type in types, if not return false
						System.out.println(constructor.getParameterTypes()[i].getCanonicalName() + " != "
								+ types[i].getCanonicalName());
						equal = false;
					}
				}
				if (equal) {
					// if a constructor exists that has the same parameter types in the same order as the types array, return true.
					return true;
				}
			}
		}
		return false;
	}
}
