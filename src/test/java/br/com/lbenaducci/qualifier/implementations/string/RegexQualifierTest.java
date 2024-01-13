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
		qualifier.setRegex("a");
		assertFalse(qualifier.isSatisfiedBy("b"));

		qualifier.setRegex("\\d");
		assertFalse(qualifier.isSatisfiedBy("a"));
	}

	@Test
	void shouldReturnTrueWhenValueMatches() {
		RegexQualifier qualifier = new RegexQualifier();
		qualifier.setRegex("a");
		assertTrue(qualifier.isSatisfiedBy("a"));

		qualifier.setRegex("\\d");
		assertTrue(qualifier.isSatisfiedBy("1"));
	}

}