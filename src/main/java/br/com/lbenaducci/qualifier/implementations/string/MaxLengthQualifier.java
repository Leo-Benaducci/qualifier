package br.com.lbenaducci.qualifier.implementations.string;

import br.com.lbenaducci.qualifier.ComparatorQualifier;

public class MaxLengthQualifier implements ComparatorQualifier<String, Number> {
	private Number max = Long.MAX_VALUE;

	@Override
	public boolean isSatisfiedBy(String s) {
		return s != null && s.length() <= max.longValue();
	}

	@Override
	public void setProperty(Number max) {
		this.max = max;
	}
}
