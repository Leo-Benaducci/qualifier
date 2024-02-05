package br.com.lbenaducci.qualifier.checker;

import br.com.lbenaducci.qualifier.Qualifier;
import br.com.lbenaducci.qualifier.implementations.NotNullQualifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public sealed class Checker<I, T> permits CollectionChecker, NumberChecker, StringChecker {
	private static final Logger log = LogManager.getLogger(Checker.class);
	private final Class<I> inputType;
	private final List<Checker<I, ?>> checkerList;
	private final Function<I, T> getAttribute;
	private Qualifier<T> qualifier;
	private String errorMessage;
	private Consumer<T> success;
	private Consumer<T> fail;

	protected Checker(Class<I> inputType, List<Checker<I, ?>> checkerList, Function<I, T> getAttribute) {
		this.inputType = inputType;
		this.checkerList = checkerList;
		this.getAttribute = getAttribute;
		this.qualifier = it -> true;
		checkerList.add(this);
	}

	public static <I, T> Checker<I, T> of(Class<I> inputType, Class<T> attributeType, Function<I, T> getAttribute) {
		if(getAttribute == null) {
			throw new IllegalArgumentException("getAttribute cannot be null");
		}
		return CheckerFactory.create(inputType, attributeType, new ArrayList<>(), getAttribute);
	}

	public static <I, T> Checker<I, Collection<T>> ofCollection(Class<I> inputType, Class<T> genericType, Function<I, Collection<T>> getAttribute) {
		if(getAttribute == null) {
			throw new IllegalArgumentException("getAttribute cannot be null");
		}
		return CheckerFactory.createCollection(inputType, genericType, new ArrayList<>(), getAttribute);
	}

	public final <A> Checker<I, A> and(Class<A> attributeType, Function<I, A> getAttribute) {
		if(getAttribute == null) {
			throw new IllegalArgumentException("getAttribute cannot be null");
		}
		return CheckerFactory.create(inputType, attributeType, checkerList, getAttribute);
	}

	public <A> Checker<I, Collection<A>> and(Class<I> inputType, Class<A> genericType, Function<I, Collection<A>> getAttribute) {
		if(getAttribute == null) {
			throw new IllegalArgumentException("getAttribute cannot be null");
		}
		return CheckerFactory.createCollection(inputType, genericType, checkerList, getAttribute);
	}

	public final Checker<I, T> success(Consumer<T> success) {
		if(success == null) {
			throw new IllegalArgumentException("Then cannot be null");
		}
		this.success = success;
		return this;
	}

	public final Checker<I, T> fail(Consumer<T> fail) {
		if(fail == null) {
			throw new IllegalArgumentException("onError cannot be null");
		}
		this.fail = fail;
		return this;
	}

	public final Checker<I, T> error(String errorMessage) {
		this.errorMessage = errorMessage;
		return this;
	}

	public final Checker<I, T> qualifier(Qualifier<T> qualifier) {
		if(qualifier == null) {
			throw new IllegalArgumentException("Qualifier cannot be null");
		}
		this.qualifier = this.qualifier.and(qualifier);
		return this;
	}

	public final Checker<I, T> qualifier(Class<? extends Qualifier<T>> qualifier) {
		if(qualifier == null) {
			throw new IllegalArgumentException("Qualifier cannot be null");
		}
		return qualifier(Qualifier.of(qualifier));
	}

	public final void check(I input) {
		List<String> errors = new ArrayList<>();
		for(Checker<I, ?> checker: checkerList) {
			if(!checker.isSatisfiedBy(input) && checker.errorMessage != null) {
				errors.add(checker.errorMessage);
			}
		}
		if(!errors.isEmpty()) {
			throw new IllegalArgumentException(String.join("; ", errors));
		}
	}

	public final Checker<I, T> notNull() {
		return qualifier(it -> new NotNullQualifier().isSatisfiedBy(it));
	}

	public Checker<I, T> notEmpty() {
		return notNull();
	}

	public Checker<I, T> notBlank() {
		return notNull();
	}

	public Checker<I, T> min(Number min) {
		if(min == null) {
			throw new IllegalArgumentException("Min cannot be null");
		}
		return notNull();
	}

	public Checker<I, T> max(Number max) {
		if(max == null) {
			throw new IllegalArgumentException("Max cannot be null");
		}
		return notNull();
	}

	public Checker<I, T> match(String regex) {
		if(regex == null) {
			throw new IllegalArgumentException("Regex cannot be null");
		}
		return notNull();
	}

	private boolean isSatisfiedBy(I input) {
		boolean result = false;
		T value = getAttribute.apply(input);
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
