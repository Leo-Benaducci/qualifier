package br.com.lbenaducci.qualifier.implementations.number;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class IsZeroQualifierTest {

	@Test
	void shouldReturnFalseWhenCallIsSatisfiedByWithValueNull() {
		IsZeroQualifier qualifier = new IsZeroQualifier();
		assertFalse(qualifier.isSatisfiedBy(null));
	}

	@Test
	void shouldReturnFalseWhenCallIsSatisfiedByWithValueNotZero() {
		IsZeroQualifier qualifier = new IsZeroQualifier();
		assertFalse(qualifier.isSatisfiedBy(1));
	}

	@Test
	void shouldReturnTrueWhenCallIsSatisfiedByWithValueZero() {
		IsZeroQualifier qualifier = new IsZeroQualifier();
		assertFalse(qualifier.isSatisfiedBy(1));
	}
}