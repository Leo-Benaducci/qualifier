package br.com.lbenaducci.qualifier.implementations.collection;

import org.junit.jupiter.api.Test;

import java.util.List;

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
		assertFalse(notEmptyQualifier.isSatisfiedBy(List.of()));
	}

	@Test
	void shouldReturnTrueWhenCallIsSatisfiedByWithValueNotEmpty() {
		NotEmptyQualifier notEmptyQualifier = new NotEmptyQualifier();
		assertTrue(notEmptyQualifier.isSatisfiedBy(List.of("not empty")));
	}
}