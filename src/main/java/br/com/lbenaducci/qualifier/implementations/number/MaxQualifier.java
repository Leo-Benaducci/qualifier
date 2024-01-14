package br.com.lbenaducci.qualifier.implementations.number;

import br.com.lbenaducci.qualifier.ComparatorQualifier;

public class MaxQualifier implements ComparatorQualifier<Number, Number> {
	private Number max = Double.MAX_VALUE;

	@Override
	public boolean isSatisfiedBy(Number number) {
		return number != null && number.doubleValue() <= max.doubleValue();
	}

	@Override
	public void setProperty(Number max) {
		this.max = max;
	}
}
