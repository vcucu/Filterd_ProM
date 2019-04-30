package org.processmining.filterd.parameters;

public class SomeParameters {
	
	private boolean firstParameter;
	private boolean secondParameter;
	
	public SomeParameters() {
		firstParameter = true;
		secondParameter = true;
	}
	
	SomeParameters(boolean firstParameter, boolean secondParameter) {
		this.firstParameter = firstParameter;
		this.secondParameter = secondParameter;
	}

	public boolean isFirstParameter() {
		return firstParameter;
	}

	public void setFirstParameter(boolean firstParameter) {
		this.firstParameter = firstParameter;
	}

	public boolean isSecondParameter() {
		return secondParameter;
	}

	public void setSecondParameter(boolean secondParameter) {
		this.secondParameter = secondParameter;
	}
	
	@Override
	public boolean equals(Object object) {
		if(object instanceof SomeParameters) {
			SomeParameters parameters = (SomeParameters) object;
			return (parameters.isFirstParameter() == firstParameter && parameters.isSecondParameter() == secondParameter);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int firstVal = (firstParameter) ? 1 : 0;
		int secondVal = (firstParameter) ? 1 : 0;
		return (firstVal * 10 + secondVal);
	}
}
