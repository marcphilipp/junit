package org.junit.experimental.cpsuite.samples;

import org.junit.experimental.cpsuite.ClasspathSuite;
import org.junit.experimental.cpsuite.ClasspathSuite.*;
import org.junit.runner.RunWith;

import static org.junit.experimental.cpsuite.SuiteType.*;

@RunWith(ClasspathSuite.class)
@ClassnameFilters({"suitetest.*TestSuite"})
@SuiteTypes(RUN_WITH_CLASSES)
public class AllRunWithSuites {

}
