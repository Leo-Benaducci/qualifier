package br.com.lbenaducci.qualifier.checker;

import br.com.lbenaducci.qualifier.implementations.collection.MaxSizeQualifier;
import br.com.lbenaducci.qualifier.implementations.collection.MinSizeQualifier;
import br.com.lbenaducci.qualifier.implementations.collection.NotEmptyCollectionQualifier;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public final class CollectionChecker<I, T> extends Checker<I, Collection<T>> {

	CollectionChecker(Class<I> inputType, List<Checker<I, ?>> checkerList, Function<I, Collection<T>> getAttribute) {
		super(inputType, checkerList, getAttribute);
	}

	@Override
	public Checker<I, Collection<T>> notEmpty() {
		NotEmptyCollectionQualifier notEmptyQualifier = new NotEmptyCollectionQualifier();
		return qualifier(notEmptyQualifier::isSatisfiedBy);
	}

	@Override
	public Checker<I, Collection<T>> min(Number min) {
		if(min == null) {
			throw new IllegalArgumentException("Min cannot be null");
		}
		MinSizeQualifier minQualifier = new MinSizeQualifier();
		minQualifier.setProperty(min);
		return qualifier(minQualifier::isSatisfiedBy);
	}

	@Override
	public Checker<I, Collection<T>> max(Number max) {
		if(max == null) {
			throw new IllegalArgumentException("Max cannot be null");
		}
		MaxSizeQualifier maxQualifier = new MaxSizeQualifier();
		maxQualifier.setProperty(max);
		return qualifier(maxQualifier::isSatisfiedBy);
	}
}
