package org.junit.experimental.cpsuite;

import static org.junit.experimental.cpsuite.SuiteType.TEST_CLASSES;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.experimental.cpsuite.internal.ClassesFinder;
import org.junit.experimental.cpsuite.internal.ClassesFinderFactory;
import org.junit.experimental.cpsuite.internal.ClassnameComparator;
import org.junit.experimental.runners.SuiteBuilder;
import org.junit.runner.RunWith;

/**
 * Instead of specifying test classes explicitly this class scans the classpath.
 * <p>
 * It is to be used by annotating a field {@link SuiteBuilder.Classes}
 * annotation of {@link SuiteBuilder}, for example:
 * 
 * <pre>
 * &#064;RunWith(SuiteBuilder.class)
 * public class OnlyYes {
 * 
 * 	&#064;Classes
 * 	public InClasspath classes= new InClasspath().includingJars().filteredBy(
 * 			&quot;.*Test&quot;, &quot;!.*AllTests&quot;).includingSubclassesOf(MyBaseTest.class);
 * 
 * 	&#064;RunnerFilter
 * 	public CategoryFilter filter= CategoryFilter.include(Yes.class);
 * 
 * }
 * </pre>
 */
public class InClasspath implements SuiteBuilder.Classes.Value {

	private final ClassesFinderFactory fFactory;

	private boolean fIncludeJars= false;

	private String[] fFilterPatterns= {};

	private SuiteType[] fSuiteTypes= { TEST_CLASSES };

	private Class<?>[] fBaseTypes= { Object.class };

	private Class<?>[] fExcludedBaseTypes= {};

	private String fClasspathProperty= "java.class.path";

	private Comparator<Class<?>> fComparator;

	/**
	 * By default this will find all JUnit 4 test classes on the classpath
	 * excluding classes inside JAR files.
	 */
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

	/**
	 * Scans the classpath for test classes.
	 * 
	 * @return a possibly sorted list of test classes on the classpath
	 */
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

	/**
	 * Specify whether to include test classes inside JAR files. By default they
	 * are excluded.
	 * 
	 * @param include
	 *            Should JAR files be searched for test classes?
	 * @return this
	 */
	public InClasspath includeJars(boolean include) {
		fIncludeJars= include;
		return this;
	}

	/**
	 * Specify that test classes inside JAR files are to be included. By default
	 * they are excluded.
	 * 
	 * @return this
	 */
	public InClasspath includingJars() {
		return includeJars(true);
	}

	/**
	 * Specify that test classes inside JAR files are to be excluded. This is
	 * the default.
	 * 
	 * @return this
	 */
	public InClasspath excludingJars() {
		return includeJars(false);
	}

	/**
	 * Restrict the test classes to be found by regular expressions. No filters
	 * are applied by default.
	 * <p>
	 * The filter patterns work disjunctively; if any one filter matches, the
	 * class will be added to the suite of tests.
	 * <p>
	 * In addition, to normal regular expressions, negation filters are also
	 * supported. Negation filters are preceded by a <code>"!"</code>, e.g.
	 * <code>"!.*AllTests"</code>.
	 * <p>
	 * While the positive filters work disjunctively the negated filters will
	 * subtract all matching tests after the maximum set of tests to run has
	 * been determined.
	 * 
	 * @param patterns
	 *            Regular expression filters
	 * @return this
	 * @see java.util.regex.Pattern
	 */
	public InClasspath filteredBy(String... patterns) {
		fFilterPatterns= patterns;
		return this;
	}

	/**
	 * Specify what types of test classes are to be found.
	 * <p>
	 * By default the search result will only include normal JUnit 4 test
	 * classes. Both JUnit 3.8 style tests and test classes that make use of the
	 * {@link RunWith} annotation are excluded.
	 * <p>
	 * In order to run only JUnit 3.8 style tests, specify
	 * {@link SuiteType#JUNIT38_TEST_CLASSES}. For test with the {@link RunWith}
	 * annotation use {@link SuiteType#RUN_WITH_CLASSES}.
	 * <p>
	 * Of course you can combine different styles of test classes by specifying
	 * more than one suite type.
	 * 
	 * @param types
	 * @return this
	 * @see SuiteType
	 */
	public InClasspath ofSuiteType(SuiteType... types) {
		fSuiteTypes= types;
		return this;
	}

	/**
	 * Restrict the test classes to be found to subclasses of certain base
	 * types. There is no restriction by default.
	 * 
	 * @param types
	 *            Base types to include
	 * @return this
	 */
	public InClasspath includingSubclassesOf(Class<?>... types) {
		fBaseTypes= types;
		return this;
	}

	/**
	 * Exclude subclasses of certain base types from the classes to be found. By
	 * default no classes are excluded based on their type.
	 * 
	 * @param types
	 *            Base types to exclude
	 * @return this
	 */
	public InClasspath excludingSubclassesOf(Class<?>... types) {
		fExcludedBaseTypes= types;
		return this;
	}

	/**
	 * Specify the system property key the classpath is read from. By default
	 * "java.class.path" is used. In case the given property is not set, the
	 * default property key is used as fallback value.
	 * 
	 * @param classpathProperty
	 *            System property key
	 * @return this
	 */
	public InClasspath forProperty(String classpathProperty) {
		fClasspathProperty= classpathProperty;
		return this;
	}

	/**
	 * Specify that the test classes are to be sorted by their qualified name.
	 * By default, the order is derived from the classpath.
	 * 
	 * @return this
	 */
	public InClasspath sortedByName() {
		return sortedBy(new ClassnameComparator());
	}

	/**
	 * Specify a Comparator to use for sorting the test classes. By default, the
	 * order is derived from the classpath.
	 * 
	 * @param comparator
	 *            to be used for sorting
	 * @return this
	 */
	public InClasspath sortedBy(Comparator<Class<?>> comparator) {
		fComparator= comparator;
		return this;
	}

}
