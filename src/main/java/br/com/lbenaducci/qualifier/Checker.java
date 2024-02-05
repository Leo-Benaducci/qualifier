package br.com.lbenaducci.qualifier;

import br.com.lbenaducci.qualifier.implementations.NotNullQualifier;
import br.com.lbenaducci.qualifier.implementations.collection.MaxSizeQualifier;
import br.com.lbenaducci.qualifier.implementations.collection.MinSizeQualifier;
import br.com.lbenaducci.qualifier.implementations.collection.NotEmptyCollectionQualifier;
import br.com.lbenaducci.qualifier.implementations.number.MaxQualifier;
import br.com.lbenaducci.qualifier.implementations.number.MinQualifier;
import br.com.lbenaducci.qualifier.implementations.string.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public final class Checker<T> {
	private static final Logger log = LogManager.getLogger(Checker.class);
	private final List<Checker<?>> checkerList;
	private final T value;
	private Qualifier<T> qualifier;
	private Consumer<T> success;
	private Consumer<T> fail;
	private String errorMessage;

	private Checker(List<Checker<?>> checkerList, T value) {
		this.checkerList = checkerList;
		this.value = value;
		this.qualifier = it -> true;
		checkerList.add(this);
	}

	public static <T> Checker<T> of(T value) {
		return new Checker<>(new ArrayList<>(), value);
	}

	public static <I extends Iterable<T>, T> Checker<I> ofForEach(I values, Consumer<Checker<T>> checker) {
		if(checker == null) {
			throw new IllegalArgumentException("Checker cannot be null");
		}
		Checker<I> checkerInstance = new Checker<>(new ArrayList<>(), values);
		if(values != null) {
			values.forEach(it -> checker.accept(checkerInstance.and(it)));
		}
		return checkerInstance;
	}

	public <N> Checker<N> and(N value) {
		return new Checker<>(checkerList, value);
	}

	public <I extends Iterable<N>, N> Checker<I> andForEach(I values, Consumer<Checker<N>> checker) {
		if(checker == null) {
			throw new IllegalArgumentException("Checker cannot be null");
		}
		Checker<I> checkerInstance = new Checker<>(checkerList, values);
		values.forEach(it -> checker.accept(checkerInstance.and(it)));
		return checkerInstance;
	}

	public Checker<T> success(Consumer<T> success) {
		if(success == null) {
			throw new IllegalArgumentException("Success cannot be null");
		}
		this.success = success;
		return this;
	}

	public Checker<T> fail(Consumer<T> fail) {
		if(fail == null) {
			throw new IllegalArgumentException("Fail cannot be null");
		}
		this.fail = fail;
		return this;
	}

	public Checker<T> error(String errorMessage) {
		this.errorMessage = errorMessage;
		return this;
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

	public boolean check() {
		List<String> errors = new ArrayList<>();
		boolean result = true;
		for(Checker<?> checker: checkerList) {
			if(!checker.isSatisfied()) {
				result = false;
				if(checker.errorMessage != null) {
					errors.add(checker.errorMessage);
				}
			}
		}
		if(!errors.isEmpty()) {
			throw new IllegalArgumentException(String.join("; ", errors));
		}
		return result;
	}

	public Checker<T> notNull() {
		return qualifier(it -> new NotNullQualifier().isSatisfiedBy(it));
	}

	public Checker<T> notEmpty() {
		if(value instanceof String) {
			return qualifier(it -> new NotEmptyQualifier().isSatisfiedBy((String) it));
		}
		if(value instanceof Collection) {
			return qualifier(it -> new NotEmptyCollectionQualifier().isSatisfiedBy((Collection<?>) it));
		}
		return notNull();
	}

	public Checker<T> notBlank() {
		if(value instanceof String) {
			return qualifier(it -> new NotBlankQualifier().isSatisfiedBy((String) it));
		}
		return notNull();
	}

	public Checker<T> min(Number min) {
		if(min == null) {
			throw new IllegalArgumentException("Min cannot be null");
		}

		if(value instanceof String) {
			return qualifier(it -> {
				MinLengthQualifier minLengthQualifier = new MinLengthQualifier();
				minLengthQualifier.setProperty(min);
				return minLengthQualifier.isSatisfiedBy((String) it);
			});
		}
		if(value instanceof Collection) {
			return qualifier(it -> {
				MinSizeQualifier minSizeQualifier = new MinSizeQualifier();
				minSizeQualifier.setProperty(min);
				return minSizeQualifier.isSatisfiedBy((Collection<?>) it);
			});
		}
		if(value instanceof Number) {
			return qualifier(it -> {
				MinQualifier minQualifier = new MinQualifier();
				minQualifier.setProperty(min);
				return minQualifier.isSatisfiedBy((Number) it);
			});
		}
		return this;
	}

	public Checker<T> max(Number max) {
		if(max == null) {
			throw new IllegalArgumentException("Max cannot be null");
		}

		if(value instanceof String) {
			return qualifier(it -> {
				MaxLengthQualifier maxLengthQualifier = new MaxLengthQualifier();
				maxLengthQualifier.setProperty(max);
				return maxLengthQualifier.isSatisfiedBy((String) it);
			});
		}
		if(value instanceof Collection) {
			return qualifier(it -> {
				MaxSizeQualifier maxSizeQualifier = new MaxSizeQualifier();
				maxSizeQualifier.setProperty(max);
				return maxSizeQualifier.isSatisfiedBy((Collection<?>) it);
			});
		}
		if(value instanceof Number) {
			return qualifier(it -> {
				MaxQualifier maxQualifier = new MaxQualifier();
				maxQualifier.setProperty(max);
				return maxQualifier.isSatisfiedBy((Number) it);
			});
		}
		return this;
	}

	public Checker<T> match(String regex) {
		if(regex == null) {
			throw new IllegalArgumentException("Regex cannot be null");
		}
		if(value instanceof String) {
			return qualifier(it -> {
				RegexQualifier regexQualifier = new RegexQualifier();
				regexQualifier.setProperty(regex);
				return regexQualifier.isSatisfiedBy((String) it);
			});
		}
		return this;
	}

	private boolean isSatisfied() {
		boolean result = false;
		try {
			result = qualifier.isSatisfiedBy(value);
		} catch(Exception e) {
			log.warn("Error on check", e);
		}
		Consumer<T> action = result ? success : fail;
		if(action != null) {
			action.accept(value);
		}
		return result;
	}
}
