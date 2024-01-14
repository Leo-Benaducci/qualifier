package br.com.lbenaducci.qualifier;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ActionTest {
	@Test
	void shouldThrowExceptionWhenActionIsNull() {
		assertThrows(IllegalArgumentException.class, () -> new Action<>(null));
	}

	@Test
	void shouldCreateAction() {
		assertDoesNotThrow(() -> new Action<>(value -> {}));
	}

	@Test
	void shouldExecuteAction() {
		Action<AtomicInteger> action = new Action<>(AtomicInteger::incrementAndGet);
		AtomicInteger value = new AtomicInteger(0);
		assertDoesNotThrow(() -> action.execute(value));
		assertEquals(1, value.get());
	}

	@Test
	void shouldNotExecuteActionTwice() {
		Action<AtomicInteger> action = new Action<>(AtomicInteger::incrementAndGet);
		AtomicInteger value = new AtomicInteger(0);
		assertDoesNotThrow(() -> action.execute(value));
		assertEquals(1, value.get());
		assertDoesNotThrow(() -> action.execute(value));
		assertEquals(1, value.get());
	}

	@Test
	void shouldNotThrowExceptionWhenActionIsExecute() {
		Action<AtomicInteger> action = new Action<>(value -> {
			throw new IllegalArgumentException("");
		});
		AtomicInteger value = new AtomicInteger(0);
		assertDoesNotThrow(() -> action.execute(value));
	}
}