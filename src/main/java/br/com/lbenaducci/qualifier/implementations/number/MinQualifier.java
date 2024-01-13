package br.com.lbenaducci.qualifier.implementations.number;

import br.com.lbenaducci.qualifier.Qualifier;

public class MinQualifier implements Qualifier<Number> {
	private Number min = Double.MIN_VALUE;

	@Override
	public boolean isSatisfiedBy(Number number) {
		return number != null && number.doubleValue() >= min.doubleValue();
	}

	public void setMin(Number min) {
		this.min = min;
	}
}
