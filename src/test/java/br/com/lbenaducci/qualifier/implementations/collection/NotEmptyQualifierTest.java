package br.com.lbenaducci.qualifier.implementations.collection;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotEmptyQualifierTest {

	@Test
	void shouldReturnFalseWhenCallIsSatisfiedByWithValueNull() {
		NotEmptyIteratorQualifier notEmptyQualifier = new NotEmptyIteratorQualifier();
		assertFalse(notEmptyQualifier.isSatisfiedBy(null));
	}

	@Test
	void shouldReturnFalseWhenCallIsSatisfiedByWithValueEmpty() {
		NotEmptyIteratorQualifier notEmptyQualifier = new NotEmptyIteratorQualifier();
		assertFalse(notEmptyQualifier.isSatisfiedBy(List.of()));
	}

	@Test
	void shouldReturnTrueWhenCallIsSatisfiedByWithValueNotEmpty() {
		NotEmptyIteratorQualifier notEmptyQualifier = new NotEmptyIteratorQualifier();
		assertTrue(notEmptyQualifier.isSatisfiedBy(List.of("not empty")));
	}
}