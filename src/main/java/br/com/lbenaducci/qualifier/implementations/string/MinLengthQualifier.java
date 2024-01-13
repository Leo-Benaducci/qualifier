package br.com.lbenaducci.qualifier.implementations.string;

import br.com.lbenaducci.qualifier.Qualifier;

public class MinLengthQualifier implements Qualifier<String> {
	private Number min = 0;

	@Override
	public boolean isSatisfiedBy(String s) {
		return s != null && s.length() >= min.longValue();
	}

	public void setMin(Number min) {
		this.min = min;
	}
}
