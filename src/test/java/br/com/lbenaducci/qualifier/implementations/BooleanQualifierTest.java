package br.com.lbenaducci.qualifier.implementations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BooleanQualifierTest {

	@Test
	void shouldReturnFalseWhenCallIsSatisfiedByWithValueNull() {
		BooleanQualifier booleanQualifier = new BooleanQualifier();
		assertFalse(booleanQualifier.isSatisfiedBy(null));
	}

	@Test
	void shouldReturnFalseWhenCallIsSatisfiedByWithValueFalse() {
		BooleanQualifier booleanQualifier = new BooleanQualifier();
		assertFalse(booleanQualifier.isSatisfiedBy(false));
	}

	@Test
	void shouldReturnTrueWhenCallIsSatisfiedByWithValueTrue() {
		BooleanQualifier booleanQualifier = new BooleanQualifier();
		assertTrue(booleanQualifier.isSatisfiedBy(true));
	}

}