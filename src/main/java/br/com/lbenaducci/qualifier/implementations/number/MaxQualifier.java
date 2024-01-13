package br.com.lbenaducci.qualifier.implementations.number;

import br.com.lbenaducci.qualifier.Qualifier;

public class MaxQualifier implements Qualifier<Number> {
	private Number max = Double.MAX_VALUE;

	@Override
	public boolean isSatisfiedBy(Number number) {
		return number != null && number.doubleValue() <= max.doubleValue();
	}

	public void setMax(Number max) {
		this.max = max;
	}
}
