package br.com.lbenaducci.qualifier.checker;

import br.com.lbenaducci.qualifier.implementations.number.MaxQualifier;
import br.com.lbenaducci.qualifier.implementations.number.MinQualifier;

import java.util.List;
import java.util.function.Function;

public final class NumberChecker<I> extends Checker<I, Number> {

	NumberChecker(Class<I> inputType, List<Checker<I, ?>> checkerList, Function<I, Number> getAttribute) {
		super(inputType, checkerList, getAttribute);
	}

	@Override
	public Checker<I, Number> min(Number min) {
		if(min == null) {
			throw new IllegalArgumentException("Min cannot be null");
		}
		MinQualifier minQualifier = new MinQualifier();
		minQualifier.setProperty(min);
		return qualifier(minQualifier);
	}

	@Override
	public Checker<I, Number> max(Number max) {
		if(max == null) {
			throw new IllegalArgumentException("Max cannot be null");
		}
		MaxQualifier maxQualifier = new MaxQualifier();
		maxQualifier.setProperty(max);
		return qualifier(maxQualifier);
	}
}
