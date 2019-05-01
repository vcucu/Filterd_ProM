package ExampleGenericity;

public class Driver {
	
	public static void main(String[] args) {
		
		XLog log = new XLog();		
		
		System.out.println(log);
		
		GenericFilter lengthFilter = new LengthFilter();
		GenericFilterConfiguration lengthFIlterConfiguration = new LengthFilterConfiguration(5);
		
		lengthFilter.filter(log, lengthFIlterConfiguration);
		
		
	}

}
