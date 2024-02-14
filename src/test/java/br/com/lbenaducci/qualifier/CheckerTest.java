package br.com.lbenaducci.qualifier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CheckerTest {

	@Test
	void whenOf_thenReturnCheckerInstance() {
		Checker<Integer> checker = Checker.of(5);
		assertNotNull(checker);
	}

	@Test
	void whenOfForEach_thenReturnCheckerInstance() {
		Checker<List<Integer>> checker = Checker.ofForEach(List.of(1, 2, 3), it -> it.min(0).max(100));
		assertNotNull(checker);
	}

	@Test
	void whenOfForEachNull_thenThrowIllegalArgumentException() {
		List<Integer> values = List.of(1, 2, 3);
		assertThrows(IllegalArgumentException.class, () -> Checker.ofForEach(values, null));
	}

	@Test
	void whenAnd_thenReturnNewCheckerInstance() {
		Checker<Integer> checker = Checker.of(5);
		Checker<Integer> result = checker.and(10);
		assertNotNull(result);
		assertNotEquals(checker, result);
	}

	@Test
	void whenAndForEach_thenReturnNewCheckerInstance() {
		Checker<List<Integer>> checker = Checker.ofForEach(List.of(1, 2, 3), it -> it.min(0).max(100));
		Checker<List<Integer>> result = checker.andForEach(List.of(10, 20, 30), it -> it.min(0).max(100));
		assertNotNull(result);
		assertNotEquals(checker, result);
	}

	@Test
	void whenAndNull_thenThrowIllegalArgumentException() {
		Checker<Integer> checker = Checker.of(5);
		List<Integer> values = List.of(1, 2, 3);
		assertThrows(IllegalArgumentException.class, () -> checker.andForEach(values, null));
	}

	@Test
	void whenSuccess_thenReturnCheckerInstance() {
		Checker<Integer> checker = Checker.of(5);
		Checker<Integer> result = checker.success(value -> {});
		assertNotNull(result);
		assertEquals(checker, result);
	}

	@Test
	void whenSuccessNull_thenThrowIllegalArgumentException() {
		Checker<Integer> checker = Checker.of(5);
		assertThrows(IllegalArgumentException.class, () -> checker.success(null));
	}

	@Test
	void whenFail_thenReturnCheckerInstance() {
		Checker<Integer> checker = Checker.of(5);
		Checker<Integer> result = checker.fail(value -> {});
		assertNotNull(result);
		assertEquals(checker, result);
	}

	@Test
	void whenFailNull_thenThrowIllegalArgumentException() {
		Checker<Integer> checker = Checker.of(5);
		assertThrows(IllegalArgumentException.class, () -> checker.fail(null));
	}

	@Test
	void whenError_thenReturnCheckerInstance() {
		Checker<Integer> checker = Checker.of(5);
		Checker<Integer> result = checker.error("Invalid input");
		assertNotNull(result);
		assertEquals(checker, result);
	}

	@Test
	void whenQualifier_thenReturnCheckerInstance() {
		Checker<Integer> checker = Checker.of(5);
		Checker<Integer> result = checker.qualifier(value -> value > 0);
		assertNotNull(result);
		assertEquals(checker, result);
	}

	@Test
	void whenQualifierNull_thenThrowIllegalArgumentException() {
		Checker<Integer> checker = Checker.of(5);
		assertThrows(IllegalArgumentException.class, () -> checker.qualifier((Qualifier<Integer>) null));
	}

	@Test
	void whenQualifierClass_thenReturnCheckerInstance() {
		Checker<Integer> checker = Checker.of(5);
		Checker<Integer> result = checker.qualifier(SampleQualifier.class);
		assertNotNull(result);
		assertEquals(checker, result);
	}

	@Test
	void whenQualifierClassNull_thenThrowIllegalArgumentException() {
		Checker<Integer> checker = Checker.of(5);
		assertThrows(IllegalArgumentException.class, () -> checker.qualifier((Class<? extends Qualifier<Integer>>) null));
	}

	@Test
	void whenNoFailures_thenNoExceptionThrown() {
		Checker<Integer> checker = Checker.of(5)
		                                  .qualifier(value -> value > 0);

		Boolean response = assertDoesNotThrow(checker::check);
		assertTrue(response);

		Checker<Integer> checker2 = Checker.of(-5)
		                                   .qualifier(value -> value > 0);

		Boolean response2 = assertDoesNotThrow(checker2::check);
		assertFalse(response2);
	}

	@Test
	void whenOneQualifierFails_thenThrowIllegalArgumentException() {
		Checker<Integer> checker = Checker.of(-5)
		                                  .qualifier(value -> value > 0)
		                                  .error("Invalid input");

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, checker::check);
		assertEquals("Invalid input", exception.getMessage());
	}

	@Test
	void whenMultipleQualifiersFail_thenThrowIllegalArgumentExceptionWithMultipleErrors() {
		Checker<Integer> checker = Checker.of(-5)
		                                  .qualifier(value -> value > 0)
		                                  .error("Invalid input")
		                                  .and(3)
		                                  .qualifier(value -> value % 2 == 0)
		                                  .error("Value '{}' must be even");

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, checker::check);
		assertEquals("Invalid input; Value '3' must be even", exception.getMessage());
	}

	@Test
	@Order(0)
	void whenErrorOnCheck_thenLogWarning() {
		try(MockedStatic<LogManager> integerMock = mockStatic(LogManager.class)) {
			final Logger logger = mock(Logger.class);
			integerMock.when(() -> LogManager.getLogger(any(Class.class))).thenReturn(logger);

			Checker<Integer> checker = Checker.of(5)
			                                  .qualifier(value -> {throw new RuntimeException("Exception expected");});

			assertDoesNotThrow(checker::check);

			verify(logger).warn(eq("Error on check"), any(Throwable.class));
		}
	}

	@Test
	void whenSuccessAction_thenActionExecuted() {
		List<Integer> successValues = new ArrayList<>();
		Checker.of(5)
		       .qualifier(value -> value > 0)
		       .success(successValues::add)
		       .check();

		assertEquals(1, successValues.size());
		assertEquals(5, successValues.get(0));
	}

	@Test
	void whenFailAction_thenActionExecuted() {
		List<Integer> failValues = new ArrayList<>();
		Checker<Integer> checker = Checker.of(-5);
		checker.qualifier(value -> value > 0)
		       .fail(failValues::add)
		       .check();

		assertEquals(1, failValues.size());
		assertEquals(Integer.valueOf(-5), failValues.get(0));
	}

	@Test
	void whenNotNull_thenReturnCheckerInstance() {
		Checker<Integer> checker = Checker.of(5);
		Checker<Integer> result = checker.notNull().error("Value cannot be null");
		assertNotNull(result);
		assertEquals(checker, result);

		Boolean response = assertDoesNotThrow(result::check);
		assertTrue(response);

		Checker<Integer> checker2 = Checker.of(null);
		Checker<Integer> result2 = checker2.notNull().error("Value cannot be null");
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, result2::check);
		assertEquals("Value cannot be null", exception.getMessage());
	}

	@Test
	void whenNotEmpty_thenReturnCheckerInstance() {
		Checker<Integer> checker = spy(Checker.of(null));
		Checker<Integer> result = checker.notEmpty();
		assertNotNull(result);
		assertEquals(checker, result);

		verify(checker).notNull();

		Checker<String> checker2 = Checker.of("");
		Checker<String> result2 = checker2.error("Value cannot be empty").notEmpty();
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, result2::check);
		assertEquals("Value cannot be empty", exception.getMessage());

		Checker<String> checker3 = Checker.of(null);
		Checker<String> result3 = checker3.error("Value cannot be empty").notEmpty();
		exception = assertThrows(IllegalArgumentException.class, result3::check);
		assertEquals("Value cannot be empty", exception.getMessage());

		Checker<List<Integer>> checker4 = Checker.of(List.of());
		Checker<List<Integer>> result4 = checker4.error("Value cannot be empty").notEmpty();
		exception = assertThrows(IllegalArgumentException.class, result4::check);
		assertEquals("Value cannot be empty", exception.getMessage());
	}

	@Test
	void whenNotBlank_thenReturnCheckerInstance() {
		Checker<Integer> checker = spy(Checker.of(5));
		Checker<Integer> result = checker.notBlank();
		assertNotNull(result);
		assertEquals(checker, result);

		verify(checker).notNull();

		Checker<String> checker1 = Checker.of(null);
		Checker<String> result1 = checker1.error("Value cannot be blank").notBlank();
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, result1::check);
		assertEquals("Value cannot be blank", exception.getMessage());

		Checker<String> checker2 = Checker.of("");
		Checker<String> result2 = checker2.error("Value cannot be blank").notBlank();
		exception = assertThrows(IllegalArgumentException.class, result2::check);
		assertEquals("Value cannot be blank", exception.getMessage());

		Checker<String> checker3 = Checker.of(" ");
		Checker<String> result3 = checker3.error("Value cannot be blank").notBlank();
		exception = assertThrows(IllegalArgumentException.class, result3::check);
		assertEquals("Value cannot be blank", exception.getMessage());
	}

	@Test
	void whenMin_thenReturnCheckerInstance() {
		Checker<Boolean> checker = Checker.of(true);
		Checker<Boolean> result = checker.min(3);
		assertNotNull(result);
		assertEquals(checker, result);

		Checker<Integer> checker1 = Checker.of(5).min(3);
		assertTrue(checker1.check());

		Checker<Integer> checker2 = Checker.of(5).min(6);
		assertFalse(checker2.check());

		Checker<String> checker3 = Checker.of("aa").min(2);
		assertTrue(checker3.check());

		Checker<String> checker4 = Checker.of("aa").min(3);
		assertFalse(checker4.check());

		Checker<List<Integer>> checker5 = Checker.of(List.of(1)).min(1);
		assertTrue(checker5.check());

		Checker<List<Integer>> checker6 = Checker.of(List.of(1)).min(2);
		assertFalse(checker6.check());
	}

	@Test
	void whenMinNull_thenThrowIllegalArgumentException() {
		Checker<Integer> checker = Checker.of(5);
		assertThrows(IllegalArgumentException.class, () -> checker.min(null));
	}

	@Test
	void whenMax_thenReturnCheckerInstance() {
		Checker<Boolean> checker = Checker.of(true);
		Checker<Boolean> result = checker.max(3);
		assertNotNull(result);
		assertEquals(checker, result);

		Checker<Integer> checker1 = Checker.of(2).max(3);
		assertTrue(checker1.check());

		Checker<Integer> checker2 = Checker.of(5).max(3);
		assertFalse(checker2.check());

		Checker<String> checker3 = Checker.of("aa").max(2);
		assertTrue(checker3.check());

		Checker<String> checker4 = Checker.of("aa").max(1);
		assertFalse(checker4.check());

		Checker<List<Integer>> checker5 = Checker.of(List.of(1)).max(1);
		assertTrue(checker5.check());

		Checker<List<Integer>> checker6 = Checker.of(List.of(1, 2)).max(1);
		assertFalse(checker6.check());
	}

	@Test
	void whenMaxNull_thenThrowIllegalArgumentException() {
		Checker<Integer> checker = spy(Checker.of(5));
		assertThrows(IllegalArgumentException.class, () -> checker.max(null));
	}

	@Test
	void whenMatch_thenReturnCheckerInstance() {
		Checker<Integer> checker = spy(Checker.of(5));
		Checker<Integer> result = checker.match(".*");
		assertNotNull(result);
		assertEquals(checker, result);

		Checker<String> checker1 = Checker.of("a").match("[a-z]*");
		assertTrue(checker1.check());
		Checker<String> checker2 = Checker.of("aaa").match("[a-z]*");
		assertTrue(checker2.check());
		Checker<String> checker3 = Checker.of("9").match("[a-z]*");
		assertFalse(checker3.check());
	}

	@Test
	void whenMatchNull_thenThrowIllegalArgumentException() {
		Checker<Integer> checker = spy(Checker.of(5));
		assertThrows(IllegalArgumentException.class, () -> checker.match(null));
	}

	public static class SampleQualifier implements Qualifier<Integer> {
		@Override
		public boolean isSatisfiedBy(Integer value) {
			return value > 0;
		}
	}
}