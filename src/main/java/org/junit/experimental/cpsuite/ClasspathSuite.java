/*
 * @author Johannes Link (business@johanneslink.net)
 * 
 * Published under GNU General Public License 2.0 (http://www.gnu.org/licenses/gpl.html)
 */
package org.junit.experimental.cpsuite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import org.junit.experimental.cpsuite.internal.ClassesFinderFactory;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

public class ClasspathSuite extends Suite {

	private final Class<?> fSuiteClass;

	/**
	 * The <code>ClassnameFilters</code> annotation specifies a set of regex
	 * expressions for all test classes (ie. their qualified names) to include
	 * in the test run. When the annotation is missing, all test classes in all
	 * packages will be run.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface ClassnameFilters {
		public String[] value();
	}

	/**
	 * The <code>IncludeJars</code> annotation specifies if Jars should be
	 * searched in or not. If the annotation is missing Jars are not being
	 * searched.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface IncludeJars {
		public boolean value();
	}

	/**
	 * The <code>SuiteTypes</code> annotation specifies which types of tests
	 * will be included in the test run. You can choose one or more from
	 * TEST_CLASSES, RUN_WITH_CLASSES, JUNIT38_TEST_CLASSES. If none is
	 * specified only JUnit4-style TEST_CLASSES will be run.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface SuiteTypes {
		public SuiteType[] value();
	}

	/**
	 * The <code>BaseTypeFilter</code> annotation filters all test classes to be
	 * run by one or several given base types, i.e. only those classes will be
	 * run which extend one of the base types. Default is
	 * <code>Object.class</code>.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface BaseTypeFilter {
		public Class<?>[] value();
	}

	/**
	 * The <code>ExcludeBaseTypeFilter</code> annotation filters all test
	 * classes to be run by one or several given base types, i.e. only those
	 * classes will be run which <em>do not extend</em> any of the base types.
	 * Default is <code>Object.class</code>.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface ExcludeBaseTypeFilter {
		public Class<?>[] value();
	}

	/**
	 * The <code>ClasspathProperty</code> specifies the System property name
	 * used to retrieve the java classpath which is searched for Test classes
	 * and suites. Default is "java.class.path".
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface ClasspathProperty {
		String value();
	}

	/**
	 * The <code>BeforeSuite</code> marks a method that will be run before the
	 * suite is run.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface BeforeSuite {
	}

	/**
	 * Used by JUnit
	 */
	public ClasspathSuite(Class<?> suiteClass, RunnerBuilder builder)
			throws InitializationError {
		this(suiteClass, builder, new ClassesFinderFactory());
	}

	/**
	 * For testing purposes only
	 */
	protected ClasspathSuite(Class<?> suiteClass, RunnerBuilder builder,
			ClassesFinderFactory factory) throws InitializationError {
		super(builder, suiteClass, classesInClasspath(suiteClass, factory));
		this.fSuiteClass= suiteClass;
	}

	private static Class<?>[] classesInClasspath(Class<?> suiteClass,
			ClassesFinderFactory factory) {
		InClasspath classpath= new ClasspathSuiteConfiguration(suiteClass, new InClasspath(factory)).read();
		List<? extends Class<?>> classes= classpath.find();
		return classes.toArray(new Class[classes.size()]);
	}

	@Override
	public void run(RunNotifier notifier) {
		try {
			runBeforeMethods();
		} catch (Exception e) {
			notifier.fireTestFailure(new Failure(getDescription(), e));
			return;
		}
		super.run(notifier);
	}

	private void runBeforeMethods() throws Exception {
		for (Method each : fSuiteClass.getMethods()) {
			if (each.isAnnotationPresent(BeforeSuite.class)) {
				if (isPublicStaticVoid(each)) {
					each.invoke(null, new Object[0]);
				}
			}
		}
	}

	private boolean isPublicStaticVoid(Method method) {
		return method.getReturnType() == void.class
				&& method.getParameterTypes().length == 0
				&& (method.getModifiers() & Modifier.STATIC) != 0;
	}
}
