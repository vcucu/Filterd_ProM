package ExampleGenericity;

public interface GenericFilterConfiguration {

	Attributes retrieveAttributes(XLog log);
	void showUI(Attributes attributes);
	
	Object retrieveParameters();
}
