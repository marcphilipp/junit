package org.junit.experimental.cpsuite.samples;

import static org.junit.experimental.cpsuite.SuiteType.JUNIT38_TEST_CLASSES;

import org.junit.experimental.cpsuite.ClasspathSuite;
import org.junit.experimental.cpsuite.ClasspathSuite.*;
import org.junit.runner.RunWith;

@RunWith(ClasspathSuite.class)
@ClassnameFilters( { "tests.*" })
@SuiteTypes(JUNIT38_TEST_CLASSES)
public class AllJUnit38Tests {

}
