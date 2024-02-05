package br.com.lbenaducci.qualifier.checker;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class NumberCheckerTest {
	@Test
	void whenOf_thenReturnNumberCheckerInstance() {
		Checker<Integer, Integer> checker = Checker.of(Integer.class, Integer.class, Function.identity());
		assertNotNull(checker);
		assertInstanceOf(NumberChecker.class, checker);
	}

	@Test
	void whenMinNull_thenThrowIllegalArgumentException() {
		Checker<Integer, Integer> checker = Checker.of(Integer.class, Integer.class, Function.identity());
		assertThrows(IllegalArgumentException.class, () -> checker.min(null));
	}

	@Test
	void whenMin_thenReturnNumberCheckerInstance() {
		Checker<Integer, Integer> checker = Checker.of(Integer.class, Integer.class, Function.identity());
		Checker<Integer, Integer> result = checker.error("Integer cannot be less than 3").min(3);
		assertNotNull(checker);
		assertInstanceOf(NumberChecker.class, checker);
		assertEquals(checker, result);

		assertDoesNotThrow(() -> result.check(3));
		assertThrows(IllegalArgumentException.class, () -> result.check(null));
		assertThrows(IllegalArgumentException.class, () -> result.check(0));
	}

	@Test
	void whenMaxNull_thenThrowIllegalArgumentException() {
		Checker<Integer, Integer> checker = Checker.of(Integer.class, Integer.class, Function.identity());
		assertThrows(IllegalArgumentException.class, () -> checker.max(null));
	}

	@Test
	void whenMax_thenReturnNumberCheckerInstance() {
		Checker<Integer, Integer> checker = Checker.of(Integer.class, Integer.class, Function.identity());
		Checker<Integer, Integer> result = checker.error("Integer cannot be greater than 3").max(3);
		assertNotNull(checker);
		assertInstanceOf(NumberChecker.class, checker);
		assertEquals(checker, result);

		assertDoesNotThrow(() -> result.check(3));
		assertThrows(IllegalArgumentException.class, () -> result.check(null));
		assertThrows(IllegalArgumentException.class, () -> result.check(4));
	}
}