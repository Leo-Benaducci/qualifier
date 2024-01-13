package br.com.lbenaducci.qualifier.implementations.string;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaxLengthQualifierTest {
	@Test
	void shouldReturnFalseWhenValueIsNull() {
		MaxLengthQualifier maxLengthQualifier = new MaxLengthQualifier();
		assertFalse(maxLengthQualifier.isSatisfiedBy(null));
	}

	@Test
	void shouldReturnFalseWhenValueLengthIsGreaterThanMax() {
		MaxLengthQualifier maxLengthQualifier = new MaxLengthQualifier();
		maxLengthQualifier.setMax(3);
		assertFalse(maxLengthQualifier.isSatisfiedBy("1234"));
	}

	@Test
	void shouldReturnTrueWhenValueLengthIsLessOrEqualToMax() {
		MaxLengthQualifier maxLengthQualifier = new MaxLengthQualifier();
		maxLengthQualifier.setMax(3);
		assertTrue(maxLengthQualifier.isSatisfiedBy("12"));
		assertTrue(maxLengthQualifier.isSatisfiedBy("123"));
	}
}