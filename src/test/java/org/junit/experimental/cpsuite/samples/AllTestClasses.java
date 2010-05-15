package org.junit.experimental.cpsuite.samples;

import static org.junit.experimental.cpsuite.SuiteType.*;
import junit.framework.JUnit4TestAdapter;

import org.junit.experimental.cpsuite.ClasspathSuite;
import org.junit.experimental.cpsuite.ClasspathSuite.*;
import org.junit.runner.RunWith;

@RunWith(ClasspathSuite.class)
@ClassnameFilters( { "injar.*", "tests.*" })
@SuiteTypes( { JUNIT38_TEST_CLASSES, TEST_CLASSES })
@IncludeJars(true)
public class AllTestClasses {

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(AllTestClasses.class);
	}

	@BeforeSuite
	public static void init1() {
		System.out.println("This is the init1 method");
	}

	@BeforeSuite
	public static void init2() {
		System.out.println("This is the init2 method");
	}
}
