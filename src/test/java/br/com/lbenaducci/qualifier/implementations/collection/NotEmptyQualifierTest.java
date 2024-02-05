package br.com.lbenaducci.qualifier.implementations.collection;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotEmptyQualifierTest {

	@Test
	void shouldReturnFalseWhenCallIsSatisfiedByWithValueNull() {
		NotEmptyCollectionQualifier notEmptyQualifier = new NotEmptyCollectionQualifier();
		assertFalse(notEmptyQualifier.isSatisfiedBy(null));
	}

	@Test
	void shouldReturnFalseWhenCallIsSatisfiedByWithValueEmpty() {
		NotEmptyCollectionQualifier notEmptyQualifier = new NotEmptyCollectionQualifier();
		assertFalse(notEmptyQualifier.isSatisfiedBy(List.of()));
	}

	@Test
	void shouldReturnTrueWhenCallIsSatisfiedByWithValueNotEmpty() {
		NotEmptyCollectionQualifier notEmptyQualifier = new NotEmptyCollectionQualifier();
		assertTrue(notEmptyQualifier.isSatisfiedBy(List.of("not empty")));
	}
}