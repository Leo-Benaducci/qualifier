package br.com.lbenaducci.qualifier.checker;

import br.com.lbenaducci.qualifier.implementations.string.*;

import java.util.List;
import java.util.function.Function;

public final class StringChecker<I> extends Checker<I, String> {

	StringChecker(Class<I> inputType, List<Checker<I, ?>> checkerList, Function<I, String> getAttribute) {
		super(inputType, checkerList, getAttribute);
	}

	@Override
	public Checker<I, String> notEmpty() {
		return qualifier(NotEmptyQualifier.class);
	}

	@Override
	public Checker<I, String> notBlank() {
		return qualifier(NotBlankQualifier.class);
	}

	public Checker<I, String> min(Number min) {
		if(min == null) {
			throw new IllegalArgumentException("Min cannot be null");
		}
		MinLengthQualifier minQualifier = new MinLengthQualifier();
		minQualifier.setProperty(min);
		return qualifier(minQualifier);
	}

	public Checker<I, String> max(Number max) {
		if(max == null) {
			throw new IllegalArgumentException("Max cannot be null");
		}
		MaxLengthQualifier maxQualifier = new MaxLengthQualifier();
		maxQualifier.setProperty(max);
		return qualifier(maxQualifier);
	}

	public Checker<I, String> match(String regex) {
		if(regex == null) {
			throw new IllegalArgumentException("Regex cannot be null");
		}
		RegexQualifier regexQualifier = new RegexQualifier();
		regexQualifier.setProperty(regex);
		return qualifier(regexQualifier);
	}
}
