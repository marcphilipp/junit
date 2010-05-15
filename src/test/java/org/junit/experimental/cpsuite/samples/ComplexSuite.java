package org.junit.experimental.cpsuite.samples;

import static org.junit.experimental.cpsuite.SuiteType.*;

import org.junit.experimental.cpsuite.ClasspathSuite;
import org.junit.experimental.cpsuite.ClasspathSuite.*;
import org.junit.runner.RunWith;

@RunWith(ClasspathSuite.class)
@ClassnameFilters({"suitetest.*", "tests\\.p[12].*"})
@SuiteTypes({RUN_WITH_CLASSES, TEST_CLASSES})
public class ComplexSuite {

}
