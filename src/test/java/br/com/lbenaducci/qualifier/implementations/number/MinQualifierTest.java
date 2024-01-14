package br.com.lbenaducci.qualifier.implementations.number;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MinQualifierTest {
	@Test
	void shouldReturnFalseWhenValueIsNull() {
		MinQualifier minQualifier = new MinQualifier();
		assertFalse(minQualifier.isSatisfiedBy(null));
	}

	@Test
	void shouldReturnFalseWhenValueIsLessThanMin() {
		MinQualifier minQualifier = new MinQualifier();
		minQualifier.setProperty(10.0);
		assertFalse(minQualifier.isSatisfiedBy(5.0));

		minQualifier.setProperty(20);
		assertFalse(minQualifier.isSatisfiedBy(19));

		minQualifier.setProperty(21L);
		assertFalse(minQualifier.isSatisfiedBy(20L));

		minQualifier.setProperty(BigDecimal.valueOf(20));
		assertFalse(minQualifier.isSatisfiedBy(BigDecimal.TEN));
	}

	@Test
	void shouldReturnTrueWhenValueIsGreaterOrEqualThanMin() {
		MinQualifier minQualifier = new MinQualifier();
		minQualifier.setProperty(10.0);
		assertTrue(minQualifier.isSatisfiedBy(20.0));

		minQualifier.setProperty(20);
		assertTrue(minQualifier.isSatisfiedBy(20));

		minQualifier.setProperty(21L);
		assertTrue(minQualifier.isSatisfiedBy(22L));

		minQualifier.setProperty(BigDecimal.valueOf(20));
		assertTrue(minQualifier.isSatisfiedBy(BigDecimal.valueOf(21)));
	}
}