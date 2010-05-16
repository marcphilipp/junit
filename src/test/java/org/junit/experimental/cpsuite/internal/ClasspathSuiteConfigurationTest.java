package org.junit.experimental.cpsuite.internal;

import static org.junit.experimental.cpsuite.SuiteType.JUNIT38_TEST_CLASSES;
import static org.junit.experimental.cpsuite.SuiteType.RUN_WITH_CLASSES;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.cpsuite.ClasspathSuite;
import org.junit.experimental.cpsuite.ClasspathSuiteTest;
import org.junit.experimental.cpsuite.InClasspath;
import org.junit.experimental.cpsuite.SuiteType;
import org.junit.experimental.cpsuite.ClasspathSuite.BaseTypeFilter;
import org.junit.experimental.cpsuite.ClasspathSuite.ClassnameFilters;
import org.junit.experimental.cpsuite.ClasspathSuite.ClasspathProperty;
import org.junit.experimental.cpsuite.ClasspathSuite.ExcludeBaseTypeFilter;
import org.junit.experimental.cpsuite.ClasspathSuite.IncludeJars;
import org.junit.experimental.cpsuite.ClasspathSuite.SuiteTypes;
import org.junit.runner.RunWith;

public class ClasspathSuiteConfigurationTest {

	private ClassesFinderFactoryMock fFactory= new ClassesFinderFactoryMock();

	@Ignore
	@RunWith(ClasspathSuite.class)
	@ClassnameFilters( { "filter1", "!filter2" })
	@IncludeJars(true)
	@SuiteTypes( { RUN_WITH_CLASSES, JUNIT38_TEST_CLASSES })
	@BaseTypeFilter( { ClasspathSuiteTest.class })
	@ExcludeBaseTypeFilter( { ClasspathSuite.class })
	@ClasspathProperty("my.class.path")
	private static class ComplexSuite {
	}

	@Test
	public void findWithComplexValues() {
		ClasspathSuiteConfiguration config= new ClasspathSuiteConfiguration(ComplexSuite.class, new InClasspath(fFactory));
		InClasspath classpath= config.read();
		classpath.get();
		fFactory.verify(true, new String[] { "filter1", "!filter2" },
				new SuiteType[] { RUN_WITH_CLASSES, JUNIT38_TEST_CLASSES },
				new Class<?>[] { ClasspathSuiteTest.class },
				new Class<?>[] { ClasspathSuite.class }, "my.class.path");
	}
}
