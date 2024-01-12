package br.com.lbenaducci.qualifier.implementations.collection;

import br.com.lbenaducci.qualifier.Qualifier;

public class NotEmptyQualifier implements Qualifier<Iterable<?>> {
	@Override
	public boolean isSatisfiedBy(Iterable<?> t) {
		return t != null && t.iterator().hasNext();
	}
}
