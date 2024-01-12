package br.com.lbenaducci.qualifier.implementations.number;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NegativeQualifierTest {

	@Test
	void shouldReturnFalseWhenCallIsSatisfiedByWithValueNull() {
		NegativeQualifier qualifier = new NegativeQualifier();
		assertFalse(qualifier.isSatisfiedBy(null));
	}

	@Test
	void shouldReturnFalseWhenCallIsSatisfiedByWithValueNotNegative() {
		NegativeQualifier qualifier = new NegativeQualifier();
		assertFalse(qualifier.isSatisfiedBy(0));
		assertFalse(qualifier.isSatisfiedBy(1));
	}

	@Test
	void shouldReturnTrueWhenCallIsSatisfiedByWithValueNegative() {
		NegativeQualifier qualifier = new NegativeQualifier();
		assertTrue(qualifier.isSatisfiedBy(-1));
	}
}