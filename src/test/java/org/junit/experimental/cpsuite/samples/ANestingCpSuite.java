package org.junit.experimental.cpsuite.samples;

import static org.junit.experimental.cpsuite.SuiteType.*;

import org.junit.experimental.cpsuite.ClasspathSuite;
import org.junit.experimental.cpsuite.ClasspathSuite.*;
import org.junit.runner.RunWith;

/**
 * Run all test suites in this package except itself (to prevent JUnit's recursion exception)
 */

@RunWith(ClasspathSuite.class)
@SuiteTypes( { RUN_WITH_CLASSES })
@ClassnameFilters( { "samples.*", "!samples\\.ANestingCpSuite" })
public class ANestingCpSuite {

}
