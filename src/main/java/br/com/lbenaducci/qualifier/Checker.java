package br.com.lbenaducci.qualifier;

import br.com.lbenaducci.qualifier.implementations.NotNullQualifier;
import br.com.lbenaducci.qualifier.implementations.collection.NotEmptyIteratorQualifier;
import br.com.lbenaducci.qualifier.implementations.number.MaxQualifier;
import br.com.lbenaducci.qualifier.implementations.number.MinQualifier;
import br.com.lbenaducci.qualifier.implementations.string.*;
import lombok.extern.log4j.Log4j2;

import java.util.function.Consumer;

@Log4j2
public final class Checker<T> {
	private final T value;
	private Qualifier<T> qualifier;
	private Action<T> success;
	private Action<T> fail;

	private Checker(T value) {
		this.value = value;
		this.qualifier = it -> true;
	}

	public static <T> Checker<T> of(T value) {
		return new Checker<>(value);
	}

	public boolean check() {
		boolean result = false;
		try {
			result = qualifier.isSatisfiedBy(value);
		} catch(Exception e) {
			log.warn("Error on check", e);
		}
		Action<T> action = result ? success : fail;
		if(action != null) {
			action.execute(value);
		}
		return result;
	}

	public Checker<T> qualifier(Qualifier<T> qualifier) {
		if(qualifier == null) {
			throw new IllegalArgumentException("Qualifier cannot be null");
		}
		this.qualifier = this.qualifier.and(qualifier);
		return this;
	}

	public Checker<T> qualifier(Class<? extends Qualifier<T>> qualifier) {
		if(qualifier == null) {
			throw new IllegalArgumentException("Qualifier cannot be null");
		}
		return qualifier(Qualifier.of(qualifier));
	}

	public Checker<T> notNull() {
		return qualifier(it -> new NotNullQualifier().isSatisfiedBy(it));
	}

	public Checker<T> notEmpty() {
		if(value instanceof String) {
			return qualifier(it -> new NotEmptyQualifier().isSatisfiedBy((String) it));
		}
		if(value instanceof Iterable<?>) {
			return qualifier(it -> new NotEmptyIteratorQualifier().isSatisfiedBy((Iterable<?>) it));
		}
		return notNull();
	}

	public Checker<T> notBlank() {
		if(value instanceof String) {
			return qualifier(it -> new NotBlankQualifier().isSatisfiedBy((String) it));
		}
		return notEmpty();
	}

	public Checker<T> min(Number min) {
		if(min == null) {
			throw new IllegalArgumentException("Min cannot be null");
		}
		if(value instanceof Number) {
			MinQualifier minQualifier = new MinQualifier();
			minQualifier.setProperty(min);
			return qualifier(it -> minQualifier.isSatisfiedBy((Number) it));
		} else if(value instanceof String) {
			MinLengthQualifier minLengthQualifier = new MinLengthQualifier();
			minLengthQualifier.setProperty(min);
			return qualifier(it -> minLengthQualifier.isSatisfiedBy((String) it));
		} else {
			return this;
		}
	}

	public Checker<T> max(Number max) {
		if(max == null) {
			throw new IllegalArgumentException("Max cannot be null");
		}
		if(value instanceof Number) {
			MaxQualifier maxQualifier = new MaxQualifier();
			maxQualifier.setProperty(max);
			return qualifier(it -> maxQualifier.isSatisfiedBy((Number) it));
		} else if(value instanceof String) {
			MaxLengthQualifier maxLengthQualifier = new MaxLengthQualifier();
			maxLengthQualifier.setProperty(max);
			return qualifier(it -> maxLengthQualifier.isSatisfiedBy((String) it));
		} else {
			return this;
		}
	}

	public Checker<T> match(String regex) {
		if(regex == null) {
			throw new IllegalArgumentException("Regex cannot be null");
		}
		if(value instanceof String) {
			RegexQualifier regexQualifier = new RegexQualifier();
			regexQualifier.setProperty(regex);
			return qualifier(it -> regexQualifier.isSatisfiedBy((String) it));
		}
		return this;
	}

	public Checker<T> success(Consumer<T> success) {
		if(success == null) {
			throw new IllegalArgumentException("Then cannot be null");
		}
		this.success = new Action<>(success);
		return this;
	}

	public Checker<T> fail(Consumer<T> fail) {
		if(fail == null) {
			throw new IllegalArgumentException("onError cannot be null");
		}
		this.fail = new Action<>(fail);
		return this;
	}
}
