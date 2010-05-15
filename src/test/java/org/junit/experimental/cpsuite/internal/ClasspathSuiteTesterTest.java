package org.junit.experimental.cpsuite.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.cpsuite.SuiteType;

public class ClasspathSuiteTesterTest {

	public static class Anything {
	}

	private ClasspathSuiteTester fTester;

	private SuiteType[] fSuiteTypes= { SuiteType.TEST_CLASSES };

	private Class<?>[] fBaseTypes= { Object.class };

	private Class<?>[] fExcludedBaseTypes= {};

	@Before
	public void initTester() {
		fTester= new ClasspathSuiteTester(true, new String[0], fSuiteTypes,
				fBaseTypes, fExcludedBaseTypes);
	}

	@Test
	public void acceptInnerClasses() {
		assertTrue(fTester.acceptInnerClass());
	}

	@Test
	public void dontAcceptNonTestClass() {
		assertFalse(fTester
				.acceptClass(org.junit.experimental.cpsuite.tests.p1.P1NoTest.class));
		assertFalse(fTester
				.acceptClass(org.junit.experimental.cpsuite.suitetest.TestSuite.class));
	}

	@Test
	public void acceptTestClass() {
		assertTrue(fTester
				.acceptClass(org.junit.experimental.cpsuite.tests.p1.P1Test.class));
	}

	@Test
	public void dontAcceptNonRunWithClassWhenSuiteTypeIsRunWith() {
		fSuiteTypes= new SuiteType[] { SuiteType.RUN_WITH_CLASSES };
		fTester= new ClasspathSuiteTester(true, new String[0], fSuiteTypes,
				fBaseTypes, fExcludedBaseTypes);
		assertFalse(fTester
				.acceptClass(org.junit.experimental.cpsuite.tests.p1.P1Test.class));
		assertFalse(fTester
				.acceptClass(org.junit.experimental.cpsuite.tests.p1.P1NoTest.class));
	}

	@Test
	public void acceptRunWithClassesWhenSuiteTypeIsRunWith() {
		fSuiteTypes= new SuiteType[] { SuiteType.RUN_WITH_CLASSES };
		fTester= new ClasspathSuiteTester(true, new String[0], fSuiteTypes,
				fBaseTypes, fExcludedBaseTypes);
		assertTrue(fTester
				.acceptClass(org.junit.experimental.cpsuite.suitetest.TestSuite.class));
	}

	/**
	 * This is now (from JUnit45 on) possible since recursiion in suites will be
	 * detected
	 */
	@Test
	public void alsoAcceptRunWithClassesThatUseClasspathSuiteThemselves() {
		fSuiteTypes= new SuiteType[] { SuiteType.RUN_WITH_CLASSES };
		fTester= new ClasspathSuiteTester(true, new String[0], fSuiteTypes,
				fBaseTypes, fExcludedBaseTypes);
		assertTrue(fTester
				.acceptClass(org.junit.experimental.cpsuite.suitetest.ACPTestSuite.class));
	}

	@Test
	public void acceptRunWithAndTestClasses() {
		fSuiteTypes= new SuiteType[] { SuiteType.RUN_WITH_CLASSES,
				SuiteType.TEST_CLASSES };
		fTester= new ClasspathSuiteTester(true, new String[0], fSuiteTypes,
				fBaseTypes, fExcludedBaseTypes);
		assertTrue(fTester
				.acceptClass(org.junit.experimental.cpsuite.suitetest.TestSuite.class));
		assertTrue(fTester
				.acceptClass(org.junit.experimental.cpsuite.tests.p1.P1Test.class));
		assertFalse(fTester
				.acceptClass(org.junit.experimental.cpsuite.tests.p1.P1NoTest.class));
	}

	@Test
	public void abstractTestClasses() {
		assertFalse(fTester
				.acceptClass(org.junit.experimental.cpsuite.tests.p2.AbstractP2Test.class));
		assertTrue(fTester
				.acceptClass(org.junit.experimental.cpsuite.tests.p2.ConcreteP2Test.class));
	}

	@Test
	public void dontAcceptJUnit38TestClassByDefault() {
		assertFalse(fTester
				.acceptClass(org.junit.experimental.cpsuite.tests.ju38.JU38ConcreteTest.class));
	}

	@Test
	public void acceptJUnit38TestClassIfConfigured() {
		fSuiteTypes= new SuiteType[] { SuiteType.JUNIT38_TEST_CLASSES };
		fTester= new ClasspathSuiteTester(false, new String[0], fSuiteTypes,
				fBaseTypes, fExcludedBaseTypes);
		assertTrue(fTester
				.acceptClass(org.junit.experimental.cpsuite.tests.ju38.JU38ConcreteTest.class));
		assertFalse(fTester
				.acceptClass(org.junit.experimental.cpsuite.tests.ju38.JU38AbstractTest.class));
	}

	@Test
	public void filterPatternsNull() {
		assertTrue(fTester.acceptClassName("oops.MyClass"));
		assertTrue(fTester.acceptClassName("TopLevel"));
	}

	@Test
	public void oneFilterPattern() {
		String[] patterns= new String[] { "oops.*" };
		fTester= new ClasspathSuiteTester(true, patterns, fSuiteTypes,
				fBaseTypes, fExcludedBaseTypes);
		assertTrue(fTester.acceptClassName("oops.MyClass"));
		assertFalse(fTester.acceptClassName("TopLevel"));
	}

	@Test
	public void twoFilterPatterns() {
		String[] patterns= new String[] { "oops.*", ".*Test" };
		fTester= new ClasspathSuiteTester(true, patterns, fSuiteTypes,
				fBaseTypes, fExcludedBaseTypes);
		assertTrue(fTester.acceptClassName("oops.MyClass"));
		assertFalse(fTester.acceptClassName("TopLevel"));
		assertTrue(fTester.acceptClassName("ppp.MyTest"));
	}

	@Test
	public void negationFilter() {
		// Accept all tests except those matching "oops.*"
		String[] patterns= new String[] { "!oops.*" };
		fTester= new ClasspathSuiteTester(true, patterns, fSuiteTypes,
				fBaseTypes, fExcludedBaseTypes);
		assertTrue(fTester.acceptClassName("TopLevel"));
		assertFalse(fTester.acceptClassName("oops.MyTest"));
	}

	@Test
	public void filtersPlusNegationFilters() {
		// Accept all tests that match any positive filter AND do not match any
		// negation filter
		String[] patterns= new String[] { "oops.*", "!.*Test", ".allo.*",
				"!h.*" };
		fTester= new ClasspathSuiteTester(true, patterns, fSuiteTypes,
				fBaseTypes, fExcludedBaseTypes);
		assertFalse(fTester.acceptClassName("TopLevel"));
		assertFalse(fTester.acceptClassName("oops.MyTest"));
		assertTrue(fTester.acceptClassName("oops.MyOops"));
		assertFalse(fTester.acceptClassName("hallo.AnOops"));
		assertTrue(fTester.acceptClassName("dallo.AnOops"));
	}

	@Test
	public void baseTypeFilterIsAppliedOnTestClasses() {
		fSuiteTypes= new SuiteType[] { SuiteType.TEST_CLASSES };
		fBaseTypes= new Class<?>[] { Anything.class,
				org.junit.experimental.cpsuite.tests.p2.AbstractP2Test.class };
		fTester= new ClasspathSuiteTester(true, new String[0], fSuiteTypes,
				fBaseTypes, fExcludedBaseTypes);
		assertTrue(fTester
				.acceptClass(org.junit.experimental.cpsuite.tests.p2.ConcreteP2Test.class));
		assertFalse(fTester
				.acceptClass(org.junit.experimental.cpsuite.tests.p1.P1Test.class));
	}

	@Test
	public void baseTypeFilterIsAppliedOnJUnit38TestClasses() {
		fSuiteTypes= new SuiteType[] { SuiteType.JUNIT38_TEST_CLASSES };
		fBaseTypes= new Class<?>[] {
				Anything.class,
				org.junit.experimental.cpsuite.tests.ju38.JU38AbstractTest.class };
		fTester= new ClasspathSuiteTester(true, new String[0], fSuiteTypes,
				fBaseTypes, fExcludedBaseTypes);
		assertTrue(fTester
				.acceptClass(org.junit.experimental.cpsuite.tests.ju38.JU38ConcreteTest.class));
		assertFalse(fTester
				.acceptClass(org.junit.experimental.cpsuite.tests.ju38.JU38TestWithoutBase.class));
	}

	@Test
	public void baseTypeFilterIsNotAppliedOnRunWithClasses() {
		fSuiteTypes= new SuiteType[] { SuiteType.RUN_WITH_CLASSES };
		fBaseTypes= new Class<?>[] { Anything.class };
		fTester= new ClasspathSuiteTester(true, new String[0], fSuiteTypes,
				fBaseTypes, fExcludedBaseTypes);
		assertTrue(fTester
				.acceptClass(org.junit.experimental.cpsuite.suitetest.TestSuite.class));
	}

	@Test
	public void excludedBaseTypeFilterIsAppliedOnTestClasses() {
		fSuiteTypes= new SuiteType[] { SuiteType.TEST_CLASSES };
		fExcludedBaseTypes= new Class<?>[] { Anything.class,
				org.junit.experimental.cpsuite.tests.p2.AbstractP2Test.class };
		fTester= new ClasspathSuiteTester(true, new String[0], fSuiteTypes,
				fBaseTypes, fExcludedBaseTypes);
		assertTrue(fTester
				.acceptClass(org.junit.experimental.cpsuite.tests.p1.P1Test.class));
		assertFalse(fTester
				.acceptClass(org.junit.experimental.cpsuite.tests.p2.ConcreteP2Test.class));
	}

	@Test
	public void excludedBaseTypeFilterIsAppliedOnJUnit38TestClasses() {
		fSuiteTypes= new SuiteType[] { SuiteType.JUNIT38_TEST_CLASSES };
		fExcludedBaseTypes= new Class<?>[] {
				Anything.class,
				org.junit.experimental.cpsuite.tests.ju38.JU38AbstractTest.class };
		fTester= new ClasspathSuiteTester(true, new String[0], fSuiteTypes,
				fBaseTypes, fExcludedBaseTypes);
		assertTrue(fTester
				.acceptClass(org.junit.experimental.cpsuite.tests.ju38.JU38TestWithoutBase.class));
		assertFalse(fTester
				.acceptClass(org.junit.experimental.cpsuite.tests.ju38.JU38ConcreteTest.class));
	}

	@Test
	public void excludedBaseTypeFilterIsNotAppliedOnRunWithClasses() {
		fSuiteTypes= new SuiteType[] { SuiteType.RUN_WITH_CLASSES };
		fExcludedBaseTypes= new Class<?>[] { Object.class };
		fTester= new ClasspathSuiteTester(true, new String[0], fSuiteTypes,
				fBaseTypes, fExcludedBaseTypes);
		assertTrue(fTester
				.acceptClass(org.junit.experimental.cpsuite.suitetest.TestSuite.class));
	}

	@Test
	public void searchInJars() {
		assertTrue(new ClasspathSuiteTester(true, new String[0], fSuiteTypes,
				fBaseTypes, fExcludedBaseTypes).searchInJars());
		assertFalse(new ClasspathSuiteTester(false, new String[0], fSuiteTypes,
				fBaseTypes, fExcludedBaseTypes).searchInJars());
	}

}
