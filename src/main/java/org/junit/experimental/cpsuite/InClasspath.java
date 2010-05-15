package org.junit.experimental.cpsuite;

import static org.junit.experimental.cpsuite.SuiteType.TEST_CLASSES;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.experimental.cpsuite.internal.ClassnameComparator;
import org.junit.experimental.cpsuite.internal.ClassesFinder;
import org.junit.experimental.cpsuite.internal.ClassesFinderFactory;
import org.junit.experimental.runners.SuiteBuilder;

public class InClasspath implements SuiteBuilder.Classes.Value {

	private final ClassesFinderFactory fFactory;

	private boolean fIncludeJars= false;

	private String[] fFilterPatterns= {};

	private SuiteType[] fSuiteTypes= { TEST_CLASSES };

	private Class<?>[] fBaseTypes= { Object.class };

	private Class<?>[] fExcludedBaseTypes= {};

	private String fClasspathProperty= "java.class.path";

	private Comparator<Class<?>> fComparator;

	public InClasspath() {
		this(new ClassesFinderFactory());
	}

	/**
	 * Used internally
	 */
	public InClasspath(ClassesFinderFactory factory) {
		fFactory= factory;
	}

	public Collection<? extends Class<?>> get() {
		return find();
	}

	public List<Class<?>> find() {
		ClassesFinder finder= fFactory
				.create(fIncludeJars, fFilterPatterns, fSuiteTypes, fBaseTypes,
						fExcludedBaseTypes, fClasspathProperty);
		return sort(finder.find());
	}

	private List<Class<?>> sort(List<Class<?>> classes) {
		if (fComparator != null) {
			Collections.sort(classes, fComparator);
		}
		return classes;
	}

	public InClasspath includeJars(boolean include) {
		fIncludeJars= include;
		return this;
	}

	public InClasspath includingJars() {
		return includeJars(true);
	}

	public InClasspath excludingJars() {
		return includeJars(false);
	}

	public InClasspath filteredBy(String... patterns) {
		fFilterPatterns= patterns;
		return this;
	}

	public InClasspath ofSuiteType(SuiteType... types) {
		fSuiteTypes= types;
		return this;
	}

	public InClasspath includingSubclassesOf(Class<?>... types) {
		fBaseTypes= types;
		return this;
	}

	public InClasspath excludingSubclassesOf(Class<?>... types) {
		fExcludedBaseTypes= types;
		return this;
	}

	public InClasspath forProperty(String classpathProperty) {
		fClasspathProperty= classpathProperty;
		return this;
	}

	public InClasspath sortedByName() {
		return sortedBy(new ClassnameComparator());
	}

	public InClasspath sortedBy(Comparator<Class<?>> comparator) {
		fComparator= comparator;
		return this;
	}

}
