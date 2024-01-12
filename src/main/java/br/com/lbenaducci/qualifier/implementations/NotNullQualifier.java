package br.com.lbenaducci.qualifier.implementations;

import br.com.lbenaducci.qualifier.Qualifier;

public class NotNullQualifier implements Qualifier<Object> {
	@Override
	public boolean isSatisfiedBy(Object t) {
		return t != null;
	}
}
