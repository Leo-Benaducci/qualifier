package br.com.lbenaducci.qualifier.implementations.string;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotBlankQualifierTest {

	@Test
	void shouldReturnFalseWhenCallIsSatisfiedByWithValueNull() {
		NotBlankQualifier notBlankQualifier = new NotBlankQualifier();
		assertFalse(notBlankQualifier.isSatisfiedBy(null));
	}

	@Test
	void shouldReturnFalseWhenCallIsSatisfiedByWithValueBlank() {
		NotBlankQualifier notBlankQualifier = new NotBlankQualifier();
		assertFalse(notBlankQualifier.isSatisfiedBy(""));
		assertFalse(notBlankQualifier.isSatisfiedBy(" "));
	}

	@Test
	void shouldReturnTrueWhenCallIsSatisfiedByWithValueNotBlank() {
		NotBlankQualifier notBlankQualifier = new NotBlankQualifier();
		assertTrue(notBlankQualifier.isSatisfiedBy("not blank"));
	}
}