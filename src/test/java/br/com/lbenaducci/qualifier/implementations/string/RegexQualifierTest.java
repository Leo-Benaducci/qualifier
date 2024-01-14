package br.com.lbenaducci.qualifier.implementations.string;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegexQualifierTest {

	@Test
	void shouldReturnFalseWhenValueIsNull() {
		RegexQualifier qualifier = new RegexQualifier();
		assertFalse(qualifier.isSatisfiedBy(null));
	}

	@Test
	void shouldReturnFalseWhenValueNotMatches() {
		RegexQualifier qualifier = new RegexQualifier();
		qualifier.setProperty("a");
		assertFalse(qualifier.isSatisfiedBy("b"));

		qualifier.setProperty("\\d");
		assertFalse(qualifier.isSatisfiedBy("a"));
	}

	@Test
	void shouldReturnTrueWhenValueMatches() {
		RegexQualifier qualifier = new RegexQualifier();
		qualifier.setProperty("a");
		assertTrue(qualifier.isSatisfiedBy("a"));

		qualifier.setProperty("\\d");
		assertTrue(qualifier.isSatisfiedBy("1"));
	}

}