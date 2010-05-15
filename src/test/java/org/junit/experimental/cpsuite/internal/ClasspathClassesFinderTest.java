package org.junit.experimental.cpsuite.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.cpsuite.ClasspathSuite;

public class ClasspathClassesFinderTest {

	class AcceptAllClassesInTestDirTester extends AcceptAllTester {
		@Override
		public boolean acceptClassName(String className) {
			return className.startsWith(ClasspathSuite.class.getPackage()
					.getName()
					+ ".tests.");
		}

		@Override
		public boolean searchInJars() {
			return false;
		}
	}

	private static final String DEFAULT_CLASSPATH_PROPERTY= "java.class.path";

	@Test
	public void allClasses() {
		ClassTester tester= new AcceptAllClassesInTestDirTester();
		Collection<Class<?>> classes= new ClassesFinder(tester,
				DEFAULT_CLASSPATH_PROPERTY).find();
		assertEquals(8, classes.size());
		assertTrue(classes
				.contains(org.junit.experimental.cpsuite.tests.ju38.JU38AbstractTest.class));
		assertTrue(classes
				.contains(org.junit.experimental.cpsuite.tests.ju38.JU38ConcreteTest.class));
		assertTrue(classes
				.contains(org.junit.experimental.cpsuite.tests.ju38.JU38TestWithoutBase.class));
		assertTrue(classes
				.contains(org.junit.experimental.cpsuite.tests.p1.P1Test.class));
		assertTrue(classes
				.contains(org.junit.experimental.cpsuite.tests.p1.P1NoTest.class));
		assertTrue(classes
				.contains(org.junit.experimental.cpsuite.tests.p1.P1NoTest.InnerTest.class));
		assertTrue(classes
				.contains(org.junit.experimental.cpsuite.tests.p2.AbstractP2Test.class));
		assertTrue(classes
				.contains(org.junit.experimental.cpsuite.tests.p2.ConcreteP2Test.class));
	}

	@Test
	public void allClassNames() {
		final Set<String> allClasses= new HashSet<String>();
		allClasses.add("tests.ju38.JU38AbstractTest");
		allClasses.add("tests.ju38.JU38ConcreteTest");
		allClasses.add("tests.ju38.JU38TestWithoutBase");
		allClasses.add("tests.p1.P1Test");
		allClasses.add("tests.p1.P1NoTest");
		allClasses.add("tests.p1.P1NoTest$InnerTest");
		allClasses.add("tests.p2.AbstractP2Test");
		allClasses.add("tests.p2.ConcreteP2Test");
		ClassTester tester= new AcceptAllClassesInTestDirTester() {
			@Override
			public boolean acceptClassName(String className) {
				if (className.startsWith("tests.")) {
					Assert.assertTrue(allClasses.contains(className));
				}
				return super.acceptClassName(className);
			}
		};
		Collection<Class<?>> classes= new ClassesFinder(tester,
				DEFAULT_CLASSPATH_PROPERTY).find();
		Assert.assertEquals(allClasses.size(), classes.size());
	}

	@Test
	public void allClassesExceptInner() {
		ClassTester tester= new AcceptAllClassesInTestDirTester() {
			@Override
			public boolean acceptInnerClass() {
				return false;
			}
		};
		Collection<Class<?>> classes= new ClassesFinder(tester,
				DEFAULT_CLASSPATH_PROPERTY).find();
		Assert.assertEquals(7, classes.size());
	}

	@Test
	public void selectClassByAcceptClass() {
		ClassTester tester= new AcceptAllClassesInTestDirTester() {
			@Override
			public boolean acceptClass(Class<?> clazz) {
				return clazz.getName().endsWith("NoTest");
			}
		};
		Collection<Class<?>> classes= new ClassesFinder(tester,
				DEFAULT_CLASSPATH_PROPERTY).find();
		Assert.assertEquals(1, classes.size());
	}

	@Test
	public void dontSearchJarsIfSpecified() {
		ClassTester tester= new AcceptAllTester() {
			@Override
			public boolean searchInJars() {
				return false;
			}

			@Override
			public boolean acceptClassName(String className) {
				return className.startsWith("injar.");
			}
		};
		Collection<Class<?>> classes= new ClassesFinder(tester,
				DEFAULT_CLASSPATH_PROPERTY).find();
		Assert.assertEquals(0, classes.size());
	}

	@Test
	public void allClassesIncludingJarFiles() {
		ClassTester tester= new AcceptAllTester() {
			@Override
			public boolean searchInJars() {
				return true;
			}

			@Override
			public boolean acceptClassName(String className) {
				return className.startsWith("injar.");
			}
		};
		Collection<Class<?>> classes= new ClassesFinder(tester,
				DEFAULT_CLASSPATH_PROPERTY).find();
		Assert.assertEquals(4, classes.size());
	}

	@Test
	public void nonDefaultClasspathProperty() {
		System.setProperty("my.class.path", "lib/mytests.jar");
		ClassTester tester= new AcceptAllTester();
		Collection<Class<?>> classes= new ClassesFinder(tester,
				"my.class.path").find();
		Assert.assertEquals(4, classes.size());
	}

	@Test
	public void useFallbackValueIfClasspathPropertyIsSpecifiedButNotSetInSystem() {
		System.clearProperty("my.class.path");
		ClassTester tester= new AcceptAllClassesInTestDirTester();
		Collection<Class<?>> classes= new ClassesFinder(tester,
				"my.class.path").find();
		Assert.assertEquals(8, classes.size());
	}
}
