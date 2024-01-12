package br.com.lbenaducci.qualifier;

import java.util.ArrayList;
import java.util.List;

public class Validator {
	private final List<Error> errors;

	private Validator() {
		this.errors = new ArrayList<>();
	}

	public static <T> Validator of(T value, Qualifier<T> qualifier, String invalidMessage) {
		Validator validator = new Validator();
		validator.qualify(value, qualifier, invalidMessage);
		return validator;
	}

	public static <T> Validator of(T value, Class<? extends Qualifier<T>> qualifier, String invalidMessage) {
		return of(value, Qualifier.of(qualifier), invalidMessage);
	}

	public static <T> Validator of(T value, Qualifier<T> qualifier) {
		return of(value, qualifier, "Invalid value '%s'");
	}

	public static <T> Validator of(T value, Class<? extends Qualifier<T>> qualifier) {
		return of(value, Qualifier.of(qualifier));
	}

	public static <T> Validator forEach(Iterable<T> values, Qualifier<T> qualifier, String invalidMessage) {
		Validator validator = new Validator();
		validator.qualify(values, qualifier, invalidMessage);
		return validator;
	}

	public static <T> Validator forEach(Iterable<T> values, Class<? extends Qualifier<T>> qualifier, String invalidMessage) {
		return forEach(values, Qualifier.of(qualifier), invalidMessage);
	}

	public static <T> Validator forEach(Iterable<T> values, Qualifier<T> qualifier) {
		return forEach(values, qualifier, "Invalid value[i] '%s'");
	}

	public static <T> Validator forEach(Iterable<T> values, Class<? extends Qualifier<T>> qualifier) {
		return forEach(values, Qualifier.of(qualifier));
	}

	public List<Error> errors() {
		return errors;
	}

	public <T> Validator and(T value, Qualifier<T> qualifier, String invalidMessage) {
		qualify(value, qualifier, invalidMessage);
		return this;
	}

	public <T> Validator and(T value, Class<? extends Qualifier<T>> qualifier, String invalidMessage) {
		return and(value, Qualifier.of(qualifier), invalidMessage);
	}

	public <T> Validator and(T value, Qualifier<T> qualifier) {
		return and(value, qualifier, "Invalid value '%s'");
	}

	public <T> Validator and(T value, Class<? extends Qualifier<T>> qualifier) {
		return and(value, Qualifier.of(qualifier));
	}

	public <T> Validator andForEach(Iterable<T> values, Qualifier<T> qualifier, String invalidMessage) {
		qualify(values, qualifier, invalidMessage);
		return this;
	}

	public <T> Validator andForEach(Iterable<T> values, Class<? extends Qualifier<T>> qualifier, String invalidMessage) {
		return andForEach(values, Qualifier.of(qualifier), invalidMessage);
	}

	public <T> Validator andForEach(Iterable<T> values, Qualifier<T> qualifier) {
		return andForEach(values, qualifier, "Invalid value[i] '%s'");
	}

	public <T> Validator andForEach(Iterable<T> values, Class<? extends Qualifier<T>> qualifier) {
		return andForEach(values, Qualifier.of(qualifier));
	}

	public void validate() {
		if(errors.isEmpty()) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		for(Error error: errors) {
			sb.append(error.message()).append("; ");
		}
		sb.deleteCharAt(sb.length() - 1);
		throw new IllegalArgumentException(sb.toString());
	}

	private <T> void qualify(T value, Qualifier<T> qualifier, String invalidMessage) {
		if(qualifier.isNotSatisfiedBy(value)) {
			if(invalidMessage.contains("%s")) {
				invalidMessage = String.format(invalidMessage, value);
			}
			errors.add(new Error(value, invalidMessage));
		}
	}

	private <T> void qualify(Iterable<T> values, Qualifier<T> qualifier, String invalidMessage) {
		int i = 0;
		for(T value: values) {
			qualify(value, qualifier, invalidMessage.replace("[i]", "[" + i + "]"));
			i++;
		}
	}
}
