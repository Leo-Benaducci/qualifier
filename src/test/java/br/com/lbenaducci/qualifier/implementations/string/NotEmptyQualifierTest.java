package br.com.lbenaducci.qualifier.implementations.string;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotEmptyQualifierTest {

	@Test
	void shouldReturnFalseWhenCallIsSatisfiedByWithValueNull() {
		NotEmptyQualifier notEmptyQualifier = new NotEmptyQualifier();
		assertFalse(notEmptyQualifier.isSatisfiedBy(null));
	}

	@Test
	void shouldReturnFalseWhenCallIsSatisfiedByWithValueEmpty() {
		NotEmptyQualifier notEmptyQualifier = new NotEmptyQualifier();
		assertFalse(notEmptyQualifier.isSatisfiedBy(""));
	}

	@Test
	void shouldReturnTrueWhenCallIsSatisfiedByWithValueNotEmpty() {
		NotEmptyQualifier notEmptyQualifier = new NotEmptyQualifier();
		assertTrue(notEmptyQualifier.isSatisfiedBy("not empty"));
		assertTrue(notEmptyQualifier.isSatisfiedBy(" "));
	}
}