package br.com.lbenaducci.qualifier.implementations.number;

import br.com.lbenaducci.qualifier.Qualifier;

public class PositiveQualifier implements Qualifier<Number> {

	@Override
	public boolean isSatisfiedBy(Number t) {
		return t != null && t.longValue() > 0;
	}
}
