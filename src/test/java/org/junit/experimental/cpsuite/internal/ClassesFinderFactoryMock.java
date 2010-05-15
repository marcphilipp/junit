package org.junit.experimental.cpsuite.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.experimental.cpsuite.SuiteType;

public final class ClassesFinderFactoryMock extends ClassesFinderFactory {

	private final ClassesFinder fFinder;

	private boolean fSearchInJars;

	private String[] fFilterPatterns;

	private SuiteType[] fSuiteTypes;

	private Class<?>[] fBaseTypes;

	private Class<?>[] fExcludedBaseTypes;

	private String fClasspathProperty;

	public ClassesFinderFactoryMock() {
		this(new ClassesFinderStub());
	}

	public ClassesFinderFactoryMock(ClassesFinder finder) {
		fFinder= finder;
	}

	@Override
	public ClassesFinder create(boolean searchInJars, String[] filterPatterns,
			SuiteType[] suiteTypes, Class<?>[] baseTypes,
			Class<?>[] excludedBaseTypes, String classpathProperty) {
		fSearchInJars= searchInJars;
		fFilterPatterns= filterPatterns;
		fSuiteTypes= suiteTypes;
		fBaseTypes= baseTypes;
		fExcludedBaseTypes= excludedBaseTypes;
		fClasspathProperty= classpathProperty;
		return fFinder;
	}

	public void verify(boolean searchInJars, String[] filterPatterns,
			SuiteType[] suiteTypes, Class<?>[] baseTypes,
			Class<?>[] excludedBaseTypes, String classpathProperty) {
		verifyIncludeJars(searchInJars);
		verifyFilterPatterns(filterPatterns);
		verifySuiteTypes(suiteTypes);
		verifyBaseTypes(baseTypes);
		verifyExcludedTypes(excludedBaseTypes);
		verifyClasspathProperty(classpathProperty);
	}

	public void verifyClasspathProperty(String classpathProperty) {
		assertEquals(fClasspathProperty, classpathProperty);
	}

	public void verifyExcludedTypes(Class<?>... excludedBaseTypes) {
		assertArrayEquals(fExcludedBaseTypes, excludedBaseTypes);
	}

	public void verifyBaseTypes(Class<?>... baseTypes) {
		assertArrayEquals(fBaseTypes, baseTypes);
	}

	public void verifySuiteTypes(SuiteType... suiteTypes) {
		assertArrayEquals(fSuiteTypes, suiteTypes);
	}

	public void verifyFilterPatterns(String... filterPatterns) {
		assertArrayEquals(fFilterPatterns, filterPatterns);
	}

	public void verifyIncludeJars(boolean searchInJars) {
		assertEquals(fSearchInJars, searchInJars);
	}

}