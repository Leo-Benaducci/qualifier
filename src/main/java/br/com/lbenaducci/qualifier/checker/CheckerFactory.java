package br.com.lbenaducci.qualifier.checker;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

final class CheckerFactory {

	private CheckerFactory() {
		throw new IllegalStateException("Factory class");
	}

	@SuppressWarnings("unchecked")
	static <I, T> Checker<I, T> create(Class<I> inputType, Class<T> attributeType, List<Checker<I, ?>> checkerList, Function<I, T> getAttribute) {
		if(String.class.isAssignableFrom(attributeType)) {
			return (Checker<I, T>) new StringChecker<>(inputType, checkerList, (Function<I, String>) getAttribute);
		}
		if(Number.class.isAssignableFrom(attributeType)) {
			return (Checker<I, T>) new NumberChecker<>(inputType, checkerList, (Function<I, Number>) getAttribute);
		}
		return new Checker<>(inputType, checkerList, getAttribute);
	}

	static <I, T> Checker<I, Collection<T>> createCollection(Class<I> inputType, Class<T> genericType, List<Checker<I, ?>> checkerList,
	                                                      Function<I, Collection<T>> getAttribute) {
		return new CollectionChecker<>(inputType, checkerList, getAttribute);
	}

}
