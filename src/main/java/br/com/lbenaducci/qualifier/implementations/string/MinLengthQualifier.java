package br.com.lbenaducci.qualifier.implementations.string;

import br.com.lbenaducci.qualifier.ComparatorQualifier;

public class MinLengthQualifier implements ComparatorQualifier<String, Number> {
	private Number min = 0;

	@Override
	public boolean isSatisfiedBy(String s) {
		return s != null && s.length() >= min.longValue();
	}

	@Override
	public void setProperty(Number min) {
		this.min = min;
	}
}
