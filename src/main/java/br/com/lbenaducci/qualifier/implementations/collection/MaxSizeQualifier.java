package br.com.lbenaducci.qualifier.implementations.collection;

import br.com.lbenaducci.qualifier.ComparatorQualifier;

import java.util.Collection;

public class MaxSizeQualifier implements ComparatorQualifier<Collection<?>, Number> {
	private Number value;

	@Override
	public boolean isSatisfiedBy(Collection<?> t) {
		return t != null && t.size() <= value.intValue();
	}

	@Override
	public void setProperty(Number value) {
		this.value = value;
	}
}
