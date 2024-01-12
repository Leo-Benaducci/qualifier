package br.com.lbenaducci.qualifier.implementations;

import br.com.lbenaducci.qualifier.Qualifier;

public class BooleanQualifier implements Qualifier<Boolean> {
	@Override
	public boolean isSatisfiedBy(Boolean t) {
		return t != null && t;
	}
}
