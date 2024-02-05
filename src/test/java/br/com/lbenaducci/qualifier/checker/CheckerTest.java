package br.com.lbenaducci.qualifier.checker;

import br.com.lbenaducci.qualifier.Qualifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CheckerTest {

	@Test
	void whenOf_thenReturnCheckerInstance() {
		Checker<String, Integer> checker = Checker.of(String.class, Integer.class, Integer::parseInt);

		assertNotNull(checker);
	}

	@Test
	void whenOfNull_thenThrowIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> Checker.of(Object.class, Object.class, null));
	}

	@Test
	void whenAnd_thenReturnNewCheckerInstance() {
		Checker<String, Integer> checker = Checker.of(String.class, Integer.class, Integer::parseInt);
		Checker<String, Integer> result = checker.and(Integer.class, String::length);
		assertNotNull(result);
		assertNotEquals(checker, result);
	}

	@Test
	void whenAndNullAttribute_thenThrowIllegalArgumentException() {
		Checker<String, Integer> checker = Checker.of(String.class, Integer.class, Integer::parseInt);
		assertThrows(IllegalArgumentException.class, () -> checker.and(Integer.class, null));
	}

	@Test
	void whenSuccess_thenReturnCheckerInstance() {
		Checker<String, Integer> checker = Checker.of(String.class, Integer.class, Integer::parseInt);
		Checker<String, Integer> result = checker.success(value -> {});
		assertNotNull(result);
		assertEquals(checker, result);
	}

	@Test
	void whenSuccessNull_thenThrowIllegalArgumentException() {
		Checker<String, Integer> checker = Checker.of(String.class, Integer.class, Integer::parseInt);
		assertThrows(IllegalArgumentException.class, () -> checker.success(null));
	}

	@Test
	void whenFail_thenReturnCheckerInstance() {
		Checker<String, Integer> checker = Checker.of(String.class, Integer.class, Integer::parseInt);
		Checker<String, Integer> result = checker.fail(error -> {});
		assertNotNull(result);
		assertEquals(checker, result);
	}

	@Test
	void whenFailNull_thenThrowIllegalArgumentException() {
		Checker<String, Integer> checker = Checker.of(String.class, Integer.class, Integer::parseInt);
		assertThrows(IllegalArgumentException.class, () -> checker.fail(null));
	}

	@Test
	void whenError_thenReturnCheckerInstance() {
		Checker<String, Integer> checker = Checker.of(String.class, Integer.class, Integer::parseInt);
		Checker<String, Integer> result = checker.error("Invalid input");
		assertNotNull(result);
		assertEquals(checker, result);
	}

	@Test
	void whenQualifier_thenReturnCheckerInstance() {
		Checker<String, Integer> checker = Checker.of(String.class, Integer.class, Integer::parseInt);
		Checker<String, Integer> result = checker.qualifier(value -> value > 0);
		assertNotNull(result);
		assertEquals(checker, result);
	}

	@Test
	void whenQualifierNull_thenThrowIllegalArgumentException() {
		Checker<String, Integer> checker = Checker.of(String.class, Integer.class, Integer::parseInt);
		assertThrows(IllegalArgumentException.class, () -> checker.qualifier((Qualifier<Integer>) null));
	}

	@Test
	void whenQualifierClass_thenReturnCheckerInstance() {
		Checker<String, Integer> checker = Checker.of(String.class, Integer.class, Integer::parseInt);
		Checker<String, Integer> result = checker.qualifier(SampleQualifier.class);
		assertNotNull(result);
		assertEquals(checker, result);
	}

	@Test
	void whenQualifierClassNull_thenThrowIllegalArgumentException() {
		Checker<String, Integer> checker = Checker.of(String.class, Integer.class, Integer::parseInt);
		assertThrows(IllegalArgumentException.class, () -> checker.qualifier((Class<SampleQualifier>) null));
	}

	@Test
	void whenNoFailures_thenNoExceptionThrown() {
		Checker<String, Integer> checker = Checker.of(String.class, Integer.class, Integer::parseInt);
		checker.qualifier(value -> value > 0);

		assertDoesNotThrow(() -> checker.check("42"));
	}

	@Test
	void whenOneQualifierFails_thenThrowIllegalArgumentException() {
		Checker<String, Integer> checker = Checker.of(String.class, Integer.class, Integer::parseInt);
		checker.qualifier(value -> value > 0)
		       .error("Invalid input");

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> checker.check("-42"));
		assertEquals("Invalid input", exception.getMessage());
	}

	@Test
	void whenMultipleQualifiersFail_thenThrowIllegalArgumentExceptionWithMultipleErrors() {
		Checker<String, Integer> checker = Checker.of(String.class, Integer.class, Integer::parseInt);
		checker.qualifier(value -> value > 0)
		       .error("Invalid input")
		       .and(Integer.class, String::length)
		       .qualifier(value -> value % 2 == 0)
		       .error("Value must be even");

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> checker.check("-42"));
		assertEquals("Invalid input; Value must be even", exception.getMessage());
	}

	@Test
	@Order(0)
	void whenErrorOnCheck_thenLogWarning() {
		try(MockedStatic<LogManager> integerMock = mockStatic(LogManager.class)) {
			final Logger logger = mock(Logger.class);
			integerMock.when(() -> LogManager.getLogger(any(Class.class))).thenReturn(logger);

			Checker<String, Integer> checker = Checker.of(String.class, Integer.class, Integer::parseInt);
			checker.qualifier(value -> {throw new RuntimeException("Exception expected");});

			assertDoesNotThrow(() -> checker.check("42"));

			verify(logger).warn(eq("Error on check"), any(Throwable.class));
		}
	}

	@Test
	void whenSuccessAction_thenActionExecuted() {
		List<Integer> successValues = new ArrayList<>();
		Checker<String, Integer> checker = Checker.of(String.class, Integer.class, Integer::parseInt);
		checker.qualifier(value -> value > 0)
		       .success(successValues::add);

		checker.check("42");

		assertEquals(1, successValues.size());
		assertEquals(Integer.valueOf(42), successValues.get(0));
	}

	@Test
	void whenFailAction_thenActionExecuted() {
		List<Integer> failValues = new ArrayList<>();
		Checker<String, Integer> checker = Checker.of(String.class, Integer.class, Integer::parseInt);
		checker.qualifier(value -> value > 0)
		       .fail(failValues::add);

		checker.check("-42");

		assertEquals(1, failValues.size());
		assertEquals(Integer.valueOf(-42), failValues.get(0));
	}

	@Test
	void whenNotNull_thenReturnCheckerInstance() {
		Checker<String, String> checker = Checker.of(String.class, String.class, Function.identity());
		Checker<String, String> result = checker.notNull().error("Value cannot be null");
		assertNotNull(result);
		assertEquals(checker, result);

		assertDoesNotThrow(() -> result.check("42"));
		assertThrows(IllegalArgumentException.class, () -> result.check(null));
	}

	@Test
	void whenNotEmpty_thenReturnCheckerInstance() {
		Checker<Object, Object> checker = spy(Checker.of(Object.class, Object.class, Function.identity()));
		Checker<Object, Object> result = checker.notEmpty().error("Value cannot be empty");
		assertNotNull(result);
		assertEquals(checker, result);

		verify(checker).notNull();
	}

	@Test
	void whenNotBlank_thenReturnCheckerInstance() {
		Checker<Object, Object> checker = spy(Checker.of(Object.class, Object.class, Function.identity()));
		Checker<Object, Object> result = checker.notBlank().error("Value cannot be empty");
		assertNotNull(result);
		assertEquals(checker, result);

		verify(checker).notNull();
	}

	@Test
	void whenMin_thenReturnCheckerInstance() {
		Checker<Object, Object> checker = spy(Checker.of(Object.class, Object.class, Function.identity()));
		Checker<Object, Object> result = checker.min(3);
		assertNotNull(result);
		assertEquals(checker, result);

		verify(checker).notNull();
	}

	@Test
	void whenMinNull_thenThrowIllegalArgumentException() {
		Checker<Object, Object> checker = spy(Checker.of(Object.class, Object.class, Function.identity()));
		assertThrows(IllegalArgumentException.class, () -> checker.min(null));
	}

	@Test
	void whenMax_thenReturnCheckerInstance() {
		Checker<Object, Object> checker = spy(Checker.of(Object.class, Object.class, Function.identity()));
		Checker<Object, Object> result = checker.max(3);
		assertNotNull(result);
		assertEquals(checker, result);

		verify(checker).notNull();
	}

	@Test
	void whenMaxNull_thenThrowIllegalArgumentException() {
		Checker<Object, Object> checker = spy(Checker.of(Object.class, Object.class, Function.identity()));
		assertThrows(IllegalArgumentException.class, () -> checker.max(null));
	}

	@Test
	void whenMatch_thenReturnCheckerInstance() {
		Checker<Object, Object> checker = spy(Checker.of(Object.class, Object.class, Function.identity()));
		Checker<Object, Object> result = checker.match(".*");
		assertNotNull(result);
		assertEquals(checker, result);

		verify(checker).notNull();
	}

	@Test
	void whenMatchNull_thenThrowIllegalArgumentException() {
		Checker<Object, Object> checker = spy(Checker.of(Object.class, Object.class, Function.identity()));
		assertThrows(IllegalArgumentException.class, () -> checker.match(null));
	}

	public static class SampleQualifier implements Qualifier<Integer> {
		@Override
		public boolean isSatisfiedBy(Integer value) {
			return value > 0;
		}
	}
}