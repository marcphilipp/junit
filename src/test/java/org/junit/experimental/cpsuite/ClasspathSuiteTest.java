package org.junit.experimental.cpsuite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.experimental.cpsuite.ClasspathSuite.BeforeSuite;
import org.junit.experimental.cpsuite.internal.ClassesFinderFactoryMock;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

public class ClasspathSuiteTest {

	private static boolean beforeWasRun;
	private static boolean testFailed;

	private ClassesFinderFactoryMock fFactory= new ClassesFinderFactoryMock();

	private RunnerBuilder newRunnerBuilder() {
		return new RunnerBuilder() {
			@Override
			public Runner runnerForClass(Class<?> testClass) throws Throwable {
				return null;
			}
		};
	}

	@RunWith(ClasspathSuite.class)
	protected static class SuiteWithBefore {
		@BeforeSuite
		public static void before() {
			beforeWasRun= true;
		}
	}

	@Test
	public void beforeSuite() throws InitializationError {
		beforeWasRun= false;
		ClasspathSuite cpsuite= new ClasspathSuite(SuiteWithBefore.class,
				newRunnerBuilder(), fFactory);
		cpsuite.run(new RunNotifier());
		assertTrue(beforeWasRun);
	}

	@RunWith(ClasspathSuite.class)
	protected static class ErroneousSuiteWithBefore {
		@BeforeSuite
		public static void before() {
			throw new RuntimeException("Something bad happened!");
		}
	}

	@Test
	public void beforeSuiteWithException() throws InitializationError {
		testFailed= false;
		ClasspathSuite cpsuite= new ClasspathSuite(ErroneousSuiteWithBefore.class,
				newRunnerBuilder(), fFactory);
		cpsuite.run(new RunNotifier() {
			@Override
			public void fireTestFailure(Failure failure) {
				testFailed= true;
				Throwable exception= failure.getException().getCause();
				assertEquals(RuntimeException.class, exception.getClass());
				assertEquals("Something bad happened!", exception.getMessage());
			}
		});
		assertTrue(testFailed);
	}
}
