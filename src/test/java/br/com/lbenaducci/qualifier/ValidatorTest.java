package br.com.lbenaducci.qualifier;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {
	@Test
	void shouldReturnInstanceWhenCallOf() {
		Validator validator = Validator.of(true, Qualifier.of(QualifierImpl.class), "invalid");
		assertNotNull(validator);
		Validator validator2 = Validator.of(true, QualifierImpl.class, "invalid");
		assertNotNull(validator2);
		Validator validator3 = Validator.of(true, Qualifier.of(QualifierImpl.class));
		assertNotNull(validator3);
		Validator validator4 = Validator.of(true, QualifierImpl.class);
		assertNotNull(validator4);
	}

	@Test
	void shouldReturnInstanceWhenCallForEach() {
		Validator validator = Validator.forEach(new ArrayList<>(), Qualifier.of(QualifierImpl.class), "invalid");
		assertNotNull(validator);
		Validator validator2 = Validator.forEach(new ArrayList<>(), QualifierImpl.class, "invalid");
		assertNotNull(validator2);
		Validator validator3 = Validator.forEach(new ArrayList<>(), Qualifier.of(QualifierImpl.class));
		assertNotNull(validator3);
		Validator validator4 = Validator.forEach(new ArrayList<>(), QualifierImpl.class);
		assertNotNull(validator4);
	}

	@Test
	void shouldQualifyValueWhenCallOf() {
		Validator validator = Validator.of(true, Qualifier.of(QualifierImpl.class), "invalid");
		assertTrue(validator.errors().isEmpty());
		Validator validator2 = Validator.of(false, Qualifier.of(QualifierImpl.class), "invalid");
		assertFalse(validator2.errors().isEmpty());
	}

	@Test
	void shouldQualifyValueWhenCallForEach() {
		Validator validator = Validator.forEach(List.of(true), Qualifier.of(QualifierImpl.class), "invalid");
		assertTrue(validator.errors().isEmpty());
		Validator validator2 = Validator.forEach(List.of(false), QualifierImpl.class, "invalid");
		assertFalse(validator2.errors().isEmpty());
		assertEquals(1, validator2.errors().size());
		Validator validator3 = Validator.forEach(List.of(true, false, false), Qualifier.of(QualifierImpl.class));
		assertFalse(validator3.errors().isEmpty());
		assertEquals(2, validator3.errors().size());
		Validator validator4 = Validator.forEach(List.of(false, true, true), QualifierImpl.class);
		assertFalse(validator4.errors().isEmpty());
		assertEquals(1, validator4.errors().size());
	}

	@Test
	void shouldQualifyValueWhenCallAnd() {
		Validator validator = Validator.of(true, QualifierImpl.class);
		assertTrue(validator.errors().isEmpty());
		validator = validator.and(false, Qualifier.of(QualifierImpl.class), "invalid");
		assertFalse(validator.errors().isEmpty());

		Validator validator2 = Validator.of(true, QualifierImpl.class);
		assertTrue(validator2.errors().isEmpty());
		validator2 = validator2.and(false, QualifierImpl.class, "invalid");
		assertFalse(validator2.errors().isEmpty());

		Validator validator3 = Validator.of(true, QualifierImpl.class);
		assertTrue(validator3.errors().isEmpty());
		validator3 = validator3.and(false, Qualifier.of(QualifierImpl.class));
		assertFalse(validator3.errors().isEmpty());

		Validator validator4 = Validator.of(true, QualifierImpl.class);
		assertTrue(validator4.errors().isEmpty());
		validator4 = validator4.and(false, QualifierImpl.class);
		assertFalse(validator4.errors().isEmpty());
	}

	@Test
	void shouldQualifyValueWhenCallAndForEach() {
		Validator validator = Validator.of(true, QualifierImpl.class);
		assertTrue(validator.errors().isEmpty());
		validator = validator.andForEach(List.of(false), Qualifier.of(QualifierImpl.class), "invalid");
		assertFalse(validator.errors().isEmpty());

		Validator validator2 = Validator.of(true, QualifierImpl.class);
		assertTrue(validator2.errors().isEmpty());
		validator2 = validator2.andForEach(List.of(false), QualifierImpl.class, "invalid");
		assertFalse(validator2.errors().isEmpty());

		Validator validator3 = Validator.of(true, QualifierImpl.class);
		assertTrue(validator3.errors().isEmpty());
		validator3 = validator3.andForEach(List.of(false), Qualifier.of(QualifierImpl.class));
		assertFalse(validator3.errors().isEmpty());

		Validator validator4 = Validator.of(true, QualifierImpl.class);
		assertTrue(validator4.errors().isEmpty());
		validator4 = validator4.andForEach(List.of(false), QualifierImpl.class);
		assertFalse(validator4.errors().isEmpty());
	}

	@Test
	void shouldValidate() {
		Validator validator = Validator.of(true, QualifierImpl.class);
		assertDoesNotThrow(validator::validate);
		Validator and = validator.and(false, QualifierImpl.class);
		assertThrows(IllegalArgumentException.class, and::validate);
	}
}