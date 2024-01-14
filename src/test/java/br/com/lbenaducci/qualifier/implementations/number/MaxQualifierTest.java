package br.com.lbenaducci.qualifier.implementations.number;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MaxQualifierTest {

	@Test
	void shouldReturnFalseWhenValueIsNull() {
		MaxQualifier maxQualifier = new MaxQualifier();
		assertFalse(maxQualifier.isSatisfiedBy(null));
	}

	@Test
	void shouldReturnFalseWhenValueIsGreaterThanMax() {
		MaxQualifier maxQualifier = new MaxQualifier();
		maxQualifier.setProperty(10.0);
		assertFalse(maxQualifier.isSatisfiedBy(20.0));

		maxQualifier.setProperty(20);
		assertFalse(maxQualifier.isSatisfiedBy(21));

		maxQualifier.setProperty(21L);
		assertFalse(maxQualifier.isSatisfiedBy(22L));

		maxQualifier.setProperty(BigDecimal.TEN);
		assertFalse(maxQualifier.isSatisfiedBy(BigDecimal.valueOf(20)));
	}

	@Test
	void shouldReturnTrueWhenValueIsLessOrEqualThanMax() {
		MaxQualifier maxQualifier = new MaxQualifier();
		maxQualifier.setProperty(10.0);
		assertTrue(maxQualifier.isSatisfiedBy(5.0));

		maxQualifier.setProperty(20);
		assertTrue(maxQualifier.isSatisfiedBy(20));

		maxQualifier.setProperty(21L);
		assertTrue(maxQualifier.isSatisfiedBy(20L));

		maxQualifier.setProperty(BigDecimal.TEN);
		assertTrue(maxQualifier.isSatisfiedBy(BigDecimal.TEN));
	}
}