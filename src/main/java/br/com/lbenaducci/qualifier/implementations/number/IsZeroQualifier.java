package br.com.lbenaducci.qualifier.implementations.number;

import br.com.lbenaducci.qualifier.Qualifier;

public class IsZeroQualifier implements Qualifier<Number> {

	@Override
	public boolean isSatisfiedBy(Number t) {
		return t != null && t.intValue() == 0;
	}
}
