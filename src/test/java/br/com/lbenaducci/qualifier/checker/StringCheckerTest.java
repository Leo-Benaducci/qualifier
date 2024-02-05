package br.com.lbenaducci.qualifier.checker;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class StringCheckerTest {
	@Test
	void whenOf_thenReturnStringCheckerInstance() {
		Checker<String, String> checker = Checker.of(String.class, String.class, Function.identity());
		assertNotNull(checker);
		assertInstanceOf(StringChecker.class, checker);
	}

	@Test
	void whenNotEmpty_thenReturnStringCheckerInstance() {
		Checker<String, String> checker = Checker.of(String.class, String.class, Function.identity());
		Checker<String, String> result = checker.error("String cannot be empty").notEmpty();
		assertNotNull(checker);
		assertInstanceOf(StringChecker.class, checker);
		assertEquals(checker, result);

		assertDoesNotThrow(() -> result.check(" "));
		assertThrows(IllegalArgumentException.class, () -> result.check(null));
		assertThrows(IllegalArgumentException.class, () -> result.check(""));
	}

	@Test
	void whenNotBlank_thenReturnStringCheckerInstance() {
		Checker<String, String> checker = Checker.of(String.class, String.class, Function.identity());
		Checker<String, String> result = checker.error("String cannot be blank").notBlank();
		assertNotNull(checker);
		assertInstanceOf(StringChecker.class, checker);
		assertEquals(checker, result);

		assertDoesNotThrow(() -> result.check("a"));
		assertThrows(IllegalArgumentException.class, () -> result.check(null));
		assertThrows(IllegalArgumentException.class, () -> result.check(""));
		assertThrows(IllegalArgumentException.class, () -> result.check(" "));
	}

	@Test
	void whenMinNull_thenThrowIllegalArgumentException() {
		Checker<String, String> checker = Checker.of(String.class, String.class, Function.identity());
		assertThrows(IllegalArgumentException.class, () -> checker.min(null));
	}

	@Test
	void whenMin_thenReturnStringCheckerInstance() {
		Checker<String, String> checker = Checker.of(String.class, String.class, Function.identity());
		Checker<String, String> result = checker.error("String cannot be less than 3").min(3);
		assertNotNull(checker);
		assertInstanceOf(StringChecker.class, checker);
		assertEquals(checker, result);

		assertDoesNotThrow(() -> result.check("aaa"));
		assertThrows(IllegalArgumentException.class, () -> result.check(null));
		assertThrows(IllegalArgumentException.class, () -> result.check(""));
		assertThrows(IllegalArgumentException.class, () -> result.check("a"));
	}

	@Test
	void whenMaxNull_thenThrowIllegalArgumentException() {
		Checker<String, String> checker = Checker.of(String.class, String.class, Function.identity());
		assertThrows(IllegalArgumentException.class, () -> checker.max(null));
	}

	@Test
	void whenMax_thenReturnStringCheckerInstance() {
		Checker<String, String> checker = Checker.of(String.class, String.class, Function.identity());
		Checker<String, String> result = checker.error("String cannot be greater than 3").max(3);
		assertNotNull(checker);
		assertInstanceOf(StringChecker.class, checker);
		assertEquals(checker, result);

		assertDoesNotThrow(() -> result.check("aaa"));
		assertThrows(IllegalArgumentException.class, () -> result.check(null));
		assertThrows(IllegalArgumentException.class, () -> result.check("aaaa"));
	}

	@Test
	void whenMatchNull_thenThrowIllegalArgumentException() {
		Checker<String, String> checker = Checker.of(String.class, String.class, Function.identity());
		assertThrows(IllegalArgumentException.class, () -> checker.match(null));
	}

	@Test
	void whenMatch_thenReturnStringCheckerInstance() {
		Checker<String, String> checker = Checker.of(String.class, String.class, Function.identity());
		Checker<String, String> result = checker.error("String does not match regex").match("[a-z]*");
		assertNotNull(checker);
		assertInstanceOf(StringChecker.class, checker);
		assertEquals(checker, result);

		assertDoesNotThrow(() -> result.check("aaa"));
		assertThrows(IllegalArgumentException.class, () -> result.check(null));
		assertThrows(IllegalArgumentException.class, () -> result.check("999"));
	}
}