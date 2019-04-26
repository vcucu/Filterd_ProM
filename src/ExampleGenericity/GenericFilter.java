package ExampleGenericity;

public abstract class GenericFilter {
	
	abstract XLog filter(XLog log, GenericFilterConfiguration genericFilterConfiguration);
	abstract XTrace filter(XTrace trace, GenericFilterConfiguration genericFilterConfiguration);
	abstract XEvent filter(XEvent event, GenericFilterConfiguration genericFilterConfiguration);
	
	abstract GenericFilterConfiguration createFilterConfiguration(XLog log);
}
