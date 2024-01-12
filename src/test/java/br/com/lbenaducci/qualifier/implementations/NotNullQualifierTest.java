package br.com.lbenaducci.qualifier.implementations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotNullQualifierTest {

	@Test
	void shouldReturnFalseWhenCallIsSatisfiedByWithValueNull() {
		NotNullQualifier notNullQualifier = new NotNullQualifier();
		assertFalse(notNullQualifier.isSatisfiedBy(null));
	}

	@Test
	void shouldReturnTrueWhenCallIsSatisfiedByWithValueNotNull() {
		NotNullQualifier notNullQualifier = new NotNullQualifier();
		assertTrue(notNullQualifier.isSatisfiedBy("not null"));
		assertTrue(notNullQualifier.isSatisfiedBy(1));
		assertTrue(notNullQualifier.isSatisfiedBy(1.0));
		assertTrue(notNullQualifier.isSatisfiedBy(true));
	}
}