package br.com.lbenaducci.qualifier.implementations.number;

import br.com.lbenaducci.qualifier.ComparatorQualifier;

public class MinQualifier implements ComparatorQualifier<Number, Number> {
	private Number min = Double.MIN_VALUE;

	@Override
	public boolean isSatisfiedBy(Number number) {
		return number != null && number.doubleValue() >= min.doubleValue();
	}

	@Override
	public void setProperty(Number min) {
		this.min = min;
	}
}
