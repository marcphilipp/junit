package org.junit.experimental.cpsuite.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.experimental.cpsuite.SuiteType;

public class ClasspathFinderFactoryTest {

	@Test
	public void creationParametersAreHandedToTester() {
		ClassesFinder finder= new ClassesFinderFactory().create(true,
				new String[] { "pos", "!neg" },
				new SuiteType[] { SuiteType.TEST_CLASSES }, new Class<?>[] {
						Object.class, getClass() },
				new Class<?>[] { String.class }, "my.class.path");
		assertEquals("my.class.path", finder.getClasspathProperty());
		ClasspathSuiteTester tester= (ClasspathSuiteTester) finder.getTester();
		assertTrue(tester.searchInJars());
		assertArrayEquals(new String[] { "pos" }, tester
				.getPositiveClassnameFilters().toArray());
		assertArrayEquals(new String[] { "neg" }, tester
				.getNegationClassnameFilters().toArray());
		assertArrayEquals(new SuiteType[] { SuiteType.TEST_CLASSES }, tester
				.getSuiteTypes());
		assertArrayEquals(new Class<?>[] { Object.class, getClass() }, tester
				.getBaseTypes());
		assertArrayEquals(new Class<?>[] { String.class }, tester
				.getExcludedBaseTypes());
	}
}
