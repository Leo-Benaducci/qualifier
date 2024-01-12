package br.com.lbenaducci.qualifier;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Predicate;

public interface Qualifier<T> {
	boolean isSatisfiedBy(T t);

	default boolean isNotSatisfiedBy(T t) {
		return !isSatisfiedBy(t);
	}

	static <Q extends Qualifier<T>, T> Q of(Class<Q> clazz) {
		try {
			return clazz.getDeclaredConstructor().newInstance();
		} catch(NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
			throw new IllegalArgumentException("Cannot instantiate qualifier", e);
		}
	}

	static <T> Qualifier<T> of(Predicate<T> qualifier) {
		return qualifier::test;
	}

	static <T> Qualifier<T> and(Qualifier<T> first, Qualifier<T> second) {
		return t -> first.isSatisfiedBy(t) && second.isSatisfiedBy(t);
	}

	static <T> Qualifier<T> and(Class<? extends Qualifier<T>> first, Class<? extends Qualifier<T>> second) {
		return and(of(first), of(second));
	}

	static <T> Qualifier<T> and(Qualifier<T> first, Class<? extends Qualifier<T>> second) {
		return and(first, of(second));
	}

	static <T> Qualifier<T> and(Class<? extends Qualifier<T>> first, Qualifier<T> second) {
		return and(of(first), second);
	}

	default Qualifier<T> and(Qualifier<T> qualifier) {
		return and(this, qualifier);
	}

	default Qualifier<T> and(Class<? extends Qualifier<T>> qualifier) {
		return and(this, qualifier);
	}

	static <T> Qualifier<T> or(Qualifier<T> first, Qualifier<T> second) {
		return t -> first.isSatisfiedBy(t) || second.isSatisfiedBy(t);
	}

	static <T> Qualifier<T> or(Class<? extends Qualifier<T>> first, Class<? extends Qualifier<T>> second) {
		return or(of(first), of(second));
	}

	static <T> Qualifier<T> or(Qualifier<T> first, Class<? extends Qualifier<T>> second) {
		return or(first, of(second));
	}

	static <T> Qualifier<T> or(Class<? extends Qualifier<T>> first, Qualifier<T> second) {
		return or(of(first), second);
	}

	default Qualifier<T> or(Qualifier<T> qualifier) {
		return or(this, qualifier);
	}

	default Qualifier<T> or(Class<? extends Qualifier<T>> qualifier) {
		return or(this, qualifier);
	}

	static <T> Qualifier<T> not(Qualifier<T> qualifier) {
		return t -> !qualifier.isSatisfiedBy(t);
	}

	static <T> Qualifier<T> not(Class<? extends Qualifier<T>> qualifier) {
		return not(of(qualifier));
	}

	default Qualifier<T> not() {
		return not(this);
	}

	static <T> Qualifier<T> xor(Qualifier<T> first, Qualifier<T> second) {
		return t -> first.isSatisfiedBy(t) ^ second.isSatisfiedBy(t);
	}

	static <T> Qualifier<T> xor(Class<? extends Qualifier<T>> first, Class<? extends Qualifier<T>> second) {
		return xor(of(first), of(second));
	}

	static <T> Qualifier<T> xor(Qualifier<T> first, Class<? extends Qualifier<T>> second) {
		return xor(first, of(second));
	}

	static <T> Qualifier<T> xor(Class<? extends Qualifier<T>> first, Qualifier<T> second) {
		return xor(of(first), second);
	}

	default Qualifier<T> xor(Qualifier<T> qualifier) {
		return xor(this, qualifier);
	}

	default Qualifier<T> xor(Class<? extends Qualifier<T>> qualifier) {
		return xor(this, qualifier);
	}
}
