package br.com.lbenaducci.qualifier.implementations.string;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinLengthQualifierTest {

	@Test
	void shouldReturnFalseWhenValueIsNull() {
		MinLengthQualifier minLengthQualifier = new MinLengthQualifier();
		assertFalse(minLengthQualifier.isSatisfiedBy(null));
	}

	@Test
	void shouldReturnFalseWhenValueLengthIsLessThanMin() {
		MinLengthQualifier minLengthQualifier = new MinLengthQualifier();
		minLengthQualifier.setMin(3);
		assertFalse(minLengthQualifier.isSatisfiedBy("12"));
	}

	@Test
	void shouldReturnTrueWhenValueLengthIsGreaterOrEqualToMin() {
		MinLengthQualifier minLengthQualifier = new MinLengthQualifier();
		minLengthQualifier.setMin(3);
		assertTrue(minLengthQualifier.isSatisfiedBy("123"));
		assertTrue(minLengthQualifier.isSatisfiedBy("1234"));
	}
}