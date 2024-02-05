package br.com.lbenaducci.qualifier.implementations.collection;

import br.com.lbenaducci.qualifier.Qualifier;

import java.util.Collection;

public class NotEmptyCollectionQualifier implements Qualifier<Collection<?>> {
	@Override
	public boolean isSatisfiedBy(Collection<?> t) {
		return t != null && !t.isEmpty();
	}
}
