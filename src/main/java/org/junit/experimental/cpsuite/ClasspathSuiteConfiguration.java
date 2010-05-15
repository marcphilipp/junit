package org.junit.experimental.cpsuite;

import java.lang.annotation.Annotation;

import org.junit.experimental.cpsuite.ClasspathSuite.BaseTypeFilter;
import org.junit.experimental.cpsuite.ClasspathSuite.ClassnameFilters;
import org.junit.experimental.cpsuite.ClasspathSuite.ClasspathProperty;
import org.junit.experimental.cpsuite.ClasspathSuite.ExcludeBaseTypeFilter;
import org.junit.experimental.cpsuite.ClasspathSuite.IncludeJars;
import org.junit.experimental.cpsuite.ClasspathSuite.SuiteTypes;


/**
 * Utility class that reads the configuration of a ClasspathSuite from its
 * annotations.
 */
public class ClasspathSuiteConfiguration {

	private final InClasspath fClasspath;

	private final Class<?> fSuiteClass;

	public ClasspathSuiteConfiguration(Class<?> suiteClass,
			InClasspath classpath) {
		fSuiteClass= suiteClass;
		fClasspath= classpath;
	}

	public InClasspath read() {
		readIncludeJars();
		readFilters();
		readSuiteTypes();
		readBaseTypes();
		readExcludedBaseTypes();
		readClasspathProperty();
		return fClasspath.sortedByName();
	}

	private void readFilters() {
		ClassnameFilters annotation= getAnnotation(ClassnameFilters.class);
		if (annotation != null) {
			fClasspath.filteredBy(annotation.value());
		}
	}

	private void readIncludeJars() {
		IncludeJars annotation= getAnnotation(IncludeJars.class);
		if (annotation != null) {
			fClasspath.includeJars(annotation.value());
		}
	}

	private void readSuiteTypes() {
		SuiteTypes annotation= getAnnotation(SuiteTypes.class);
		if (annotation != null) {
			fClasspath.ofSuiteType(annotation.value());
		}
	}

	private void readBaseTypes() {
		BaseTypeFilter annotation= getAnnotation(BaseTypeFilter.class);
		if (annotation != null) {
			fClasspath.includingSubclassesOf(annotation.value());
		}
	}

	private void readExcludedBaseTypes() {
		ExcludeBaseTypeFilter annotation= getAnnotation(ExcludeBaseTypeFilter.class);
		if (annotation != null) {
			fClasspath.excludingSubclassesOf(annotation.value());
		}
	}

	private void readClasspathProperty() {
		ClasspathProperty annotation= getAnnotation(ClasspathProperty.class);
		if (annotation != null) {
			fClasspath.forProperty(annotation.value());
		}
	}

	private <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return fSuiteClass.getAnnotation(annotationClass);
	}

}
