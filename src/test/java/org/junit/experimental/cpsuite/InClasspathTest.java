package org.junit.experimental.cpsuite;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.experimental.cpsuite.SuiteType.TEST_CLASSES;

import java.util.List;

import org.junit.Test;
import org.junit.experimental.cpsuite.internal.ClassesFinderFactoryMock;
import org.junit.experimental.cpsuite.internal.ClassesFinderStub;

public class InClasspathTest {

	private static class A {
	}

	private static class B {
	}

	private static class C {
	}

	private ClassesFinderStub fFinder= new ClassesFinderStub();

	private ClassesFinderFactoryMock fFactory= new ClassesFinderFactoryMock(
			fFinder);

	private InClasspath fInClasspath= new InClasspath(fFactory);

	@Test
	public void defaultValues() {
		fInClasspath.get();
		fFactory.verify(false, new String[0], new SuiteType[] { TEST_CLASSES },
				new Class<?>[] { Object.class }, new Class<?>[0],
				"java.class.path");
		assertTrue(fFinder.wasCalled());
	}

	@Test
	public void includingJars() {
		fInClasspath.excludingJars().includingJars().get();
		fFactory.verifyIncludeJars(true);
	}

	@Test
	public void excludingJars() {
		fInClasspath.includingJars().excludingJars().get();
		fFactory.verifyIncludeJars(false);
	}

	@Test
	public void filteredBy() {
		fInClasspath.filteredBy("x").filteredBy("y", "z").get();
		fFactory.verifyFilterPatterns("y", "z");
	}

	@Test
	public void ofSuiteType() {
		fInClasspath.ofSuiteType(SuiteType.JUNIT38_TEST_CLASSES).ofSuiteType(
				SuiteType.RUN_WITH_CLASSES, SuiteType.TEST_CLASSES).get();
		fFactory.verifySuiteTypes(SuiteType.RUN_WITH_CLASSES,
				SuiteType.TEST_CLASSES);
	}

	@Test
	public void includingSubclassesOf() {
		fInClasspath.includingSubclassesOf(A.class).includingSubclassesOf(
				B.class, C.class).get();
		fFactory.verifyBaseTypes(B.class, C.class);
	}

	@Test
	public void excludingSubclassesOf() {
		fInClasspath.excludingSubclassesOf(A.class).excludingSubclassesOf(
				B.class, C.class).get();
		fFactory.verifyExcludedTypes(B.class, C.class);
	}

	@Test
	public void forProperty() {
		fInClasspath.forProperty("aaa").forProperty("xxx").get();
		fFactory.verifyClasspathProperty("xxx");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void sortedByName() {
		fFinder.setResult(asList(B.class, A.class));
		List<Class<?>> result= fInClasspath.sortedByName().find();
		assertSame(A.class, result.get(0));
		assertSame(B.class, result.get(1));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void unsorted() {
		fFinder.setResult(asList(B.class, A.class));
		List<Class<?>> result= fInClasspath.find();
		assertSame(B.class, result.get(0));
		assertSame(A.class, result.get(1));
	}
}
