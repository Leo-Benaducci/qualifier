package br.com.lbenaducci.qualifier;

import br.com.lbenaducci.qualifier.implementations.NotNullQualifier;
import br.com.lbenaducci.qualifier.implementations.collection.NotEmptyIteratorQualifier;
import br.com.lbenaducci.qualifier.implementations.number.MaxQualifier;
import br.com.lbenaducci.qualifier.implementations.number.MinQualifier;
import br.com.lbenaducci.qualifier.implementations.string.*;
import lombok.extern.log4j.Log4j2;
import net.jodah.typetools.TypeResolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.LoggerContextFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Checker<I, T> {
	private static final Logger log = LogManager.getLogger(Checker.class);
	private final List<Checker<I, ?>> checkerList;
	private final Function<I, T> getAttribute;
	private final Class<T> type;
	private Qualifier<T> qualifier;
	private String errorMessage;
	private Consumer<T> success;
	private Consumer<T> fail;

	@SuppressWarnings("unchecked")
	private Checker(List<Checker<I, ?>> checkerList, Function<I, T> getAttribute) {
		this.checkerList = checkerList;
		this.getAttribute = getAttribute;
		Class<?>[] classes = TypeResolver.resolveRawArguments(Function.class, getAttribute.getClass());
		for(Class<?> t: classes) {
			log.info(t.getSimpleName());
		}
		this.type = (Class<T>) classes[1];
		this.qualifier = it -> true;
		checkerList.add(this);
	}

	public static <I, T> Checker<I, T> of(Function<I, T> getAttribute) {
		if(getAttribute == null) {
			throw new IllegalArgumentException("getAttribute cannot be null");
		}
		return new Checker<>(new ArrayList<>(), getAttribute);
	}

	public Checker<I, T> success(Consumer<T> success) {
		if(success == null) {
			throw new IllegalArgumentException("Then cannot be null");
		}
		this.success = success;
		return this;
	}

	public Checker<I, T> fail(Consumer<T> fail) {
		if(fail == null) {
			throw new IllegalArgumentException("onError cannot be null");
		}
		this.fail = fail;
		return this;
	}

	public Checker<I, T> error(String errorMessage) {
		this.errorMessage = errorMessage;
		return this;
	}

	public Checker<I, T> qualifier(Qualifier<T> qualifier) {
		if(qualifier == null) {
			throw new IllegalArgumentException("Qualifier cannot be null");
		}
		this.qualifier = this.qualifier.and(qualifier);
		return this;
	}

	public Checker<I, T> qualifier(Class<? extends Qualifier<T>> qualifier) {
		if(qualifier == null) {
			throw new IllegalArgumentException("Qualifier cannot be null");
		}
		return qualifier(Qualifier.of(qualifier));
	}

	public <A> Checker<I, A> and(Function<I, A> getAttribute) {
		if(getAttribute == null) {
			throw new IllegalArgumentException("getAttribute cannot be null");
		}
		return new Checker<>(checkerList, getAttribute);
	}

	public void check(I input) {
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

	public Checker<I, T> notNull() {
		return qualifier(it -> new NotNullQualifier().isSatisfiedBy(it));
	}

	public Checker<I, T> notEmpty() {
		if(String.class.isAssignableFrom(type)) {
			return qualifier(it -> new NotEmptyQualifier().isSatisfiedBy((String) it));
		}
		if(Iterable.class.isAssignableFrom(type)) {
			return qualifier(it -> new NotEmptyIteratorQualifier().isSatisfiedBy((Iterable<?>) it));
		}
		log.info(type.getSimpleName());
		return notNull();
	}

	public Checker<I, T> notBlank() {
		if(String.class.isAssignableFrom(type)) {
			return qualifier(it -> new NotBlankQualifier().isSatisfiedBy((String) it));
		}
		return notEmpty();
	}

	public Checker<I, T> min(Number min) {
		if(min == null) {
			throw new IllegalArgumentException("Min cannot be null");
		}
		if(Number.class.isAssignableFrom(type)) {
			MinQualifier minQualifier = new MinQualifier();
			minQualifier.setProperty(min);
			return qualifier(it -> minQualifier.isSatisfiedBy((Number) it));
		} else if(String.class.isAssignableFrom(type)) {
			MinLengthQualifier minLengthQualifier = new MinLengthQualifier();
			minLengthQualifier.setProperty(min);
			return qualifier(it -> minLengthQualifier.isSatisfiedBy((String) it));
		} else {
			return this;
		}
	}

	public Checker<I, T> max(Number max) {
		if(max == null) {
			throw new IllegalArgumentException("Max cannot be null");
		}
		if(Number.class.isAssignableFrom(type)) {
			MaxQualifier maxQualifier = new MaxQualifier();
			maxQualifier.setProperty(max);
			return qualifier(it -> maxQualifier.isSatisfiedBy((Number) it));
		} else if(String.class.isAssignableFrom(type)) {
			MaxLengthQualifier maxLengthQualifier = new MaxLengthQualifier();
			maxLengthQualifier.setProperty(max);
			return qualifier(it -> maxLengthQualifier.isSatisfiedBy((String) it));
		} else {
			return this;
		}
	}

	public Checker<I, T> match(String regex) {
		if(regex == null) {
			throw new IllegalArgumentException("Regex cannot be null");
		}
		if(String.class.isAssignableFrom(type)) {
			RegexQualifier regexQualifier = new RegexQualifier();
			regexQualifier.setProperty(regex);
			return qualifier(it -> regexQualifier.isSatisfiedBy((String) it));
		}
		return this;
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
