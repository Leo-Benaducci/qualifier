package br.com.lbenaducci.qualifier;

import br.com.lbenaducci.qualifier.implementations.string.NotBlankQualifier;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class CheckerTest {
	@Test
	void shouldInstanceWhenCallOf() {
		Checker<String> checker = assertDoesNotThrow(() -> Checker.of("value"));
		assertNotNull(checker);

		Checker<Integer> checker2 = assertDoesNotThrow(() -> Checker.of(1));
		assertNotNull(checker2);

		Checker<Object> checker3 = assertDoesNotThrow(() -> Checker.of(null));
		assertNotNull(checker3);
	}

	@Test
	void shouldCheck() {
		Checker<String> checker = Checker.of("value");
		boolean result = assertDoesNotThrow(checker::check);
		assertTrue(result);

		Checker<Object> checker2 = Checker.of(null).qualifier(it -> {
			throw new IllegalArgumentException("Error");
		});
		result = assertDoesNotThrow(checker2::check);
		assertFalse(result);
	}

	@Test
	void shouldAddQualifierWhenCallQualifier() {
		Checker<String> checker = Checker.of("value");
		boolean result = checker.check();
		assertTrue(result);
		result = checker.qualifier(it -> !it.equals("value")).check();
		assertFalse(result);
	}

	@Test
	void shouldThrowExceptionWhenCallQualifierWithNull() {
		Checker<String> checker = Checker.of("value");
		assertThrows(IllegalArgumentException.class, () -> checker.qualifier((Qualifier<String>) null));
	}

	@Test
	void shouldAddQualifierWhenCallQualifierWithClass() {
		Checker<String> checker = Checker.of(" ");
		boolean result = checker.check();
		assertTrue(result);
		result = checker.qualifier(NotBlankQualifier.class).check();
		assertFalse(result);
	}

	@Test
	void shouldThrowExceptionWhenCallQualifierWithClassNull() {
		Checker<String> checker = Checker.of(" ");
		assertThrows(IllegalArgumentException.class, () -> checker.qualifier((Class<? extends Qualifier<String>>) null));
	}

	@Test
	void shouldAddNotNullQualifierWhenCallNotNull() {
		Checker<String> checker = Checker.of(null);
		boolean result = checker.check();
		assertTrue(result);
		result = checker.notNull().check();
		assertFalse(result);
	}

	@Test
	void shouldAddNotEmptyQualifierWhenCallNotEmpty() {
		Checker<String> checker = Checker.of("");
		boolean result = checker.check();
		assertTrue(result);
		result = checker.notEmpty().check();
		assertFalse(result);

		Checker<String> checker2 = Checker.of(" ");
		result = checker2.check();
		assertTrue(result);
		result = checker2.notEmpty().check();
		assertTrue(result);

		Checker<Object> checker3 = Checker.of(null);
		result = checker3.check();
		assertTrue(result);
		result = checker3.notEmpty().check();
		assertFalse(result);

		Checker<Object> checker4 = Checker.of(new Object());
		result = checker4.check();
		assertTrue(result);
		result = checker4.notEmpty().check();
		assertTrue(result);

		Checker<List<String>> checker5 = Checker.of(List.of());
		result = checker5.check();
		assertTrue(result);
		result = checker5.notEmpty().check();
		assertFalse(result);

		Checker<List<String>> checker6 = Checker.of(List.of(""));
		result = checker6.check();
		assertTrue(result);
		result = checker6.notEmpty().check();
		assertTrue(result);
	}

	@Test
	void shouldAddNotBlankQualifierWhenCallNotBlank() {
		Checker<String> checker = Checker.of(" ");
		boolean result = checker.check();
		assertTrue(result);
		result = checker.notBlank().check();
		assertFalse(result);

		Checker<String> checker1 = Checker.of("");
		result = checker1.check();
		assertTrue(result);
		result = checker1.notBlank().check();
		assertFalse(result);

		Checker<String> checker2 = Checker.of("a");
		result = checker2.check();
		assertTrue(result);
		result = checker2.notBlank().check();
		assertTrue(result);

		Checker<Object> checker3 = Checker.of(null);
		result = checker3.check();
		assertTrue(result);
		result = checker3.notBlank().check();
		assertFalse(result);

		Checker<Object> checker4 = Checker.of(new Object());
		result = checker4.check();
		assertTrue(result);
		result = checker4.notBlank().check();
		assertTrue(result);
	}

	@Test
	void shouldAddMinQualifierWhenCallMin() {
		Checker<Integer> checker = Checker.of(0);
		boolean result = checker.check();
		assertTrue(result);
		result = checker.min(0).check();
		assertTrue(result);
		result = checker.min(1).check();
		assertFalse(result);

		Checker<String> checker2 = Checker.of("0");
		result = checker2.check();
		assertTrue(result);
		result = checker2.min(1).check();
		assertTrue(result);
		result = checker2.min(2).check();
		assertFalse(result);

		Checker<Object> checker3 = Checker.of(null);
		result = checker3.check();
		assertTrue(result);
		result = checker3.min(0).check();
		assertTrue(result);
	}

	@Test
	void shouldThrowExceptionWhenCallMinWithNull() {
		Checker<Integer> checker = Checker.of(0);
		assertThrows(IllegalArgumentException.class, () -> checker.min(null));
	}

	@Test
	void shouldAddMaxQualifierWhenCallMax() {
		Checker<Integer> checker = Checker.of(0);
		boolean result = checker.check();
		assertTrue(result);
		result = checker.max(0).check();
		assertTrue(result);
		result = checker.max(-1).check();
		assertFalse(result);

		Checker<String> checker2 = Checker.of("0");
		result = checker2.check();
		assertTrue(result);
		result = checker2.max(1).check();
		assertTrue(result);
		result = checker2.max(0).check();
		assertFalse(result);

		Checker<Object> checker3 = Checker.of(null);
		result = checker3.check();
		assertTrue(result);
		result = checker3.max(0).check();
		assertTrue(result);
	}

	@Test
	void shouldThrowExceptionWhenCallMaxWithNull() {
		Checker<Integer> checker = Checker.of(0);
		assertThrows(IllegalArgumentException.class, () -> checker.max(null));
	}

	@Test
	void shouldAddRegexQualifierWhenCallMatch() {
		Checker<String> checker = Checker.of("0");
		boolean result = checker.check();
		assertTrue(result);
		result = checker.match("0").check();
		assertTrue(result);
		result = checker.match("1").check();
		assertFalse(result);

		Checker<Object> checker2 = Checker.of(null);
		result = checker2.check();
		assertTrue(result);
		result = checker2.match("0").check();
		assertTrue(result);
	}

	@Test
	void shouldThrowExceptionWhenCallMatchWithNull() {
		Checker<String> checker = Checker.of("0");
		assertThrows(IllegalArgumentException.class, () -> checker.match(null));
	}

	@Test
	void shouldThrowExceptionWhenCallSuccessWithNull() {
		Checker<String> checker = Checker.of("0");
		assertThrows(IllegalArgumentException.class, () -> checker.success(null));
	}

	@Test
	void shouldExecuteSuccessWhenCallCheckSuccess() {
		Checker<String> checker = Checker.of("0");
		boolean result = checker.check();
		assertTrue(result);
		AtomicInteger counter = new AtomicInteger();
		assertEquals(0, counter.get());
		result = checker.success(it -> counter.incrementAndGet()).check();
		assertTrue(result);
		assertEquals(1, counter.get());

		checker = Checker.of("").notEmpty();
		result = checker.check();
		assertFalse(result);
		counter.set(0);
		assertEquals(0, counter.get());
		result = checker.success(it -> counter.incrementAndGet()).check();
		assertFalse(result);
		assertEquals(0, counter.get());
	}

	@Test
	void shouldThrowExceptionWhenCallFailWithNull() {
		Checker<String> checker = Checker.of("0");
		assertThrows(IllegalArgumentException.class, () -> checker.fail(null));
	}

	@Test
	void shouldExecuteFailWhenCallCheckFail() {
		Checker<String> checker = Checker.of("").notEmpty();
		boolean result = checker.check();
		assertFalse(result);
		AtomicInteger counter = new AtomicInteger();
		assertEquals(0, counter.get());
		result = checker.fail(it -> counter.incrementAndGet()).check();
		assertFalse(result);
		assertEquals(1, counter.get());

		checker = Checker.of("0");
		result = checker.check();
		assertTrue(result);
		counter.set(0);
		assertEquals(0, counter.get());
		result = checker.fail(it -> counter.incrementAndGet()).check();
		assertTrue(result);
		assertEquals(0, counter.get());
	}
}