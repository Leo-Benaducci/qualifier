package br.com.lbenaducci.qualifier.checker;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CollectionCheckerTest {

	@Test
	void whenOf_thenReturnCollectionCheckerInstance() {
		Checker<Integer, Collection<Integer>> checker = Checker.ofCollection(Integer.class, Integer.class, List::of);
		assertNotNull(checker);
		assertInstanceOf(CollectionChecker.class, checker);
	}

	@Test
	void whenNotEmpty_thenReturnCollectionCheckerInstance() {
		Checker<Integer, Collection<Integer>> checker = Checker.ofCollection(Integer.class, Integer.class, it -> {
			ArrayList<Integer> list = new ArrayList<>();
			for(int i = 0; i < it; i++) {
				list.add(i);
			}
			return list;
		});
		Checker<Integer, Collection<Integer>> result = checker.error("String cannot be empty").notEmpty();
		assertNotNull(checker);
		assertInstanceOf(CollectionChecker.class, checker);
		assertEquals(checker, result);

		assertDoesNotThrow(() -> result.check(1));
		assertThrows(IllegalArgumentException.class, () -> result.check(0));
	}

	@Test
	void whenMinNull_thenThrowIllegalArgumentException() {
		Checker<Integer, Collection<Integer>> checker = Checker.ofCollection(Integer.class, Integer.class, List::of);
		assertThrows(IllegalArgumentException.class, () -> checker.min(null));
	}

	@Test
	void whenMin_thenReturnCollectionCheckerInstance() {
		Checker<Integer, Collection<Integer>> checker = Checker.ofCollection(Integer.class, Integer.class, it -> {
			ArrayList<Integer> list = new ArrayList<>();
			for(int i = 0; i < it; i++) {
				list.add(i);
			}
			return list;
		});
		Checker<Integer, Collection<Integer>> result = checker.error("String cannot be less than 3").min(3);
		assertNotNull(checker);
		assertInstanceOf(CollectionChecker.class, checker);
		assertEquals(checker, result);

		assertDoesNotThrow(() -> result.check(3));
		assertThrows(IllegalArgumentException.class, () -> result.check(0));
	}

	@Test
	void whenMaxNull_thenThrowIllegalArgumentException() {
		Checker<Integer, Collection<Integer>> checker = Checker.ofCollection(Integer.class, Integer.class, List::of);
		assertThrows(IllegalArgumentException.class, () -> checker.max(null));
	}

	@Test
	void whenMax_thenReturnCollectionCheckerInstance() {
		Checker<Integer, Collection<Integer>> checker = Checker.ofCollection(Integer.class, Integer.class, it -> {
			ArrayList<Integer> list = new ArrayList<>();
			for(int i = 0; i < it; i++) {
				list.add(i);
			}
			return list;
		});
		Checker<Integer, Collection<Integer>> result = checker.error("String cannot be less than 3").max(3);
		assertNotNull(checker);
		assertInstanceOf(CollectionChecker.class, checker);
		assertEquals(checker, result);

		assertDoesNotThrow(() -> result.check(3));
		assertThrows(IllegalArgumentException.class, () -> result.check(4));
	}
}