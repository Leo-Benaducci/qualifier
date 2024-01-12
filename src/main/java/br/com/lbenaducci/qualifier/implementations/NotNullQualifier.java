package br.com.lbenaducci.qualifier.implementations;

import br.com.lbenaducci.qualifier.Qualifier;

public class NotNullQualifier<T> implements Qualifier<T> {
	@Override
	public boolean isSatisfiedBy(T t) {
		return t != null;
	}
}
