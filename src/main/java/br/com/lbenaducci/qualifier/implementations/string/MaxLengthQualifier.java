package br.com.lbenaducci.qualifier.implementations.string;

import br.com.lbenaducci.qualifier.Qualifier;

public class MaxLengthQualifier implements Qualifier<String> {
	private Number max = Long.MAX_VALUE;

	@Override
	public boolean isSatisfiedBy(String s) {
		return s != null && s.length() <= max.longValue();
	}

	public void setMax(Number max) {
		this.max = max;
	}
}
