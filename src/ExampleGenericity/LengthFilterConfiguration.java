package ExampleGenericity;

public class LengthFilterConfiguration implements GenericFilterConfiguration {
	
	int maxLength;
	
	public LengthFilterConfiguration(int maxLength) {
		this.maxLength = maxLength;
	}

	@Override
	public Attributes retrieveAttributes(XLog log) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showUI(Attributes attributes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object retrieveParameters() {
		// TODO Auto-generated method stub
		return maxLength;
	}

}
