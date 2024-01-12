package br.com.lbenaducci.qualifier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QualifierTest {
	@Test
	void shouldReturnInverseValueIsSatisfiedByWhenCallIsNotSatisfiedBy() {
		Qualifier<Boolean> qualifier = Qualifier.of(QualifierImpl.class);
		assertFalse(qualifier.isNotSatisfiedBy(true));
		assertTrue(qualifier.isNotSatisfiedBy(false));
		assertTrue(qualifier.isSatisfiedBy(true));
		assertFalse(qualifier.isSatisfiedBy(false));
	}

	@Test
	@SuppressWarnings("all")
	void shouldReturnInstanceWhenCallOf() {
		Qualifier<?> qualifier = Qualifier.of(QualifierImpl.class);
		assertNotNull(qualifier);
		assertTrue(qualifier instanceof QualifierImpl);
	}

	@Test
	void shouldReturnInstanceWhenCallOfFunction() {
		Qualifier<Boolean> qualifier = Qualifier.of(value -> true);
		assertNotNull(qualifier);
	}

	@Test
	void shouldThrowExceptionWhenInstanceInvalidQualifier() {
		assertThrows(IllegalArgumentException.class, () -> Qualifier.of(IllegalQualifierImpl.class));
	}

	@Test
	void shouldExecuteAndOperatorWhenCallAnd() {
		Qualifier<Boolean> and1 = Qualifier.and(value -> false, value -> false);
		assertFalse(and1.isSatisfiedBy(true));
		Qualifier<Boolean> and2 = Qualifier.and(value -> true, value -> false);
		assertFalse(and2.isSatisfiedBy(true));
		Qualifier<Boolean> and3 = Qualifier.and(value -> false, value -> true);
		assertFalse(and3.isSatisfiedBy(true));
		Qualifier<Boolean> and4 = Qualifier.and(value -> true, value -> true);
		assertTrue(and4.isSatisfiedBy(true));
	}

	@Test
	void shouldExecuteAndOperatorWhenCallAndClass() {
		Qualifier<Boolean> and1 = Qualifier.and(QualifierImpl.class, QualifierImpl.class);
		assertTrue(and1.isSatisfiedBy(true));
		Qualifier<Boolean> and2 = Qualifier.and(QualifierImpl.class, value -> false);
		assertFalse(and2.isSatisfiedBy(true));
		Qualifier<Boolean> and3 = Qualifier.and(value -> false, QualifierImpl.class);
		assertFalse(and3.isSatisfiedBy(true));
		Qualifier<Boolean> and4 = Qualifier.of(QualifierImpl.class).and(QualifierImpl.class);
		assertTrue(and4.isSatisfiedBy(true));
		Qualifier<Boolean> and5 = Qualifier.of(QualifierImpl.class).and(value -> false);
		assertFalse(and5.isSatisfiedBy(true));
	}

	@Test
	void shouldExecuteOrOperatorWhenCallOr() {
		Qualifier<Boolean> or1 = Qualifier.or(value -> false, value -> false);
		assertFalse(or1.isSatisfiedBy(true));
		Qualifier<Boolean> or2 = Qualifier.or(value -> true, value -> false);
		assertTrue(or2.isSatisfiedBy(true));
		Qualifier<Boolean> or3 = Qualifier.or(value -> false, value -> true);
		assertTrue(or3.isSatisfiedBy(true));
		Qualifier<Boolean> or4 = Qualifier.or(value -> true, value -> true);
		assertTrue(or4.isSatisfiedBy(true));
	}

	@Test
	void shouldExecuteOrOperatorWhenCallOrClass() {
		Qualifier<Boolean> or1 = Qualifier.or(QualifierImpl.class, QualifierImpl.class);
		assertTrue(or1.isSatisfiedBy(true));
		Qualifier<Boolean> or2 = Qualifier.or(QualifierImpl.class, value -> false);
		assertTrue(or2.isSatisfiedBy(true));
		Qualifier<Boolean> or3 = Qualifier.or(value -> false, QualifierImpl.class);
		assertTrue(or3.isSatisfiedBy(true));
		Qualifier<Boolean> or4 = Qualifier.of(QualifierImpl.class).or(QualifierImpl.class);
		assertTrue(or4.isSatisfiedBy(true));
		Qualifier<Boolean> or5 = Qualifier.of(QualifierImpl.class).or(value -> false);
		assertTrue(or5.isSatisfiedBy(true));
	}

	@Test
	void shouldExecuteNotOperatorWhenCallNot() {
		Qualifier<Boolean> not1 = Qualifier.not(value -> false);
		assertTrue(not1.isSatisfiedBy(true));
		Qualifier<Boolean> not2 = Qualifier.not(value -> true);
		assertFalse(not2.isSatisfiedBy(true));
	}

	@Test
	void shouldExecuteNotOperatorWhenCallNotClass() {
		Qualifier<Boolean> not1 = Qualifier.not(QualifierImpl.class);
		assertFalse(not1.isSatisfiedBy(true));
		Qualifier<Boolean> not2 = Qualifier.of(QualifierImpl.class).not();
		assertFalse(not2.isSatisfiedBy(true));
	}

	@Test
	void shouldExecuteXorOperatorWhenCallXor() {
		Qualifier<Boolean> xor1 = Qualifier.xor(value -> false, value -> false);
		assertFalse(xor1.isSatisfiedBy(true));
		Qualifier<Boolean> xor2 = Qualifier.xor(value -> true, value -> false);
		assertTrue(xor2.isSatisfiedBy(true));
		Qualifier<Boolean> xor3 = Qualifier.xor(value -> false, value -> true);
		assertTrue(xor3.isSatisfiedBy(true));
		Qualifier<Boolean> xor4 = Qualifier.xor(value -> true, value -> true);
		assertFalse(xor4.isSatisfiedBy(true));
	}

	@Test
	void shouldExecuteXorOperatorWhenCallXorClass() {
		Qualifier<Boolean> xor1 = Qualifier.xor(QualifierImpl.class, QualifierImpl.class);
		assertFalse(xor1.isSatisfiedBy(true));
		Qualifier<Boolean> xor2 = Qualifier.xor(QualifierImpl.class, value -> false);
		assertTrue(xor2.isSatisfiedBy(true));
		Qualifier<Boolean> xor3 = Qualifier.xor(value -> false, QualifierImpl.class);
		assertTrue(xor3.isSatisfiedBy(true));
		Qualifier<Boolean> xor4 = Qualifier.of(QualifierImpl.class).xor(QualifierImpl.class);
		assertFalse(xor4.isSatisfiedBy(true));
		Qualifier<Boolean> xor5 = Qualifier.of(QualifierImpl.class).xor(value -> false);
		assertTrue(xor5.isSatisfiedBy(true));
	}
}

class QualifierImpl implements Qualifier<Boolean> {
	@Override
	public boolean isSatisfiedBy(Boolean t) {
		return t;
	}
}

class IllegalQualifierImpl implements Qualifier<Boolean> {
	private IllegalQualifierImpl() {
	}

	@Override
	public boolean isSatisfiedBy(Boolean t) {
		return t;
	}
}