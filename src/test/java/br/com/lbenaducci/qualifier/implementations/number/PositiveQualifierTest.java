package br.com.lbenaducci.qualifier.implementations.number;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositiveQualifierTest {

	@Test
	void shouldReturnFalseWhenCallIsSatisfiedByWithValueNull() {
		PositiveQualifier qualifier = new PositiveQualifier();
		assertFalse(qualifier.isSatisfiedBy(null));
	}

	@Test
	void shouldReturnFalseWhenCallIsSatisfiedByWithValueNotPositive() {
		PositiveQualifier qualifier = new PositiveQualifier();
		assertFalse(qualifier.isSatisfiedBy(0));
		assertFalse(qualifier.isSatisfiedBy(-1));
	}

	@Test
	void shouldReturnTrueWhenCallIsSatisfiedByWithValuePositive() {
		PositiveQualifier qualifier = new PositiveQualifier();
		assertTrue(qualifier.isSatisfiedBy(1));
	}
}